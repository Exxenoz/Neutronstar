package at.autrage.projects.zeta.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.GameActivity;
import at.autrage.projects.zeta.activity.HighscoreActivity;
import at.autrage.projects.zeta.activity.ShopActivity;
import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.model.AlarmArea;
import at.autrage.projects.zeta.model.EnemySpawner;
import at.autrage.projects.zeta.model.GameLoop;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Player;
import at.autrage.projects.zeta.model.WeaponUpgrades;
import at.autrage.projects.zeta.model.Weapons;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Database;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.TutorialManager;
import at.autrage.projects.zeta.module.UpdateFlags;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.module.Util;
import at.autrage.projects.zeta.persistence.HighscoreTable;
import at.autrage.projects.zeta.persistence.HighscoreTableEntry;

/**
 * It is responsible for {@link GameLoop} management and drawing of the whole game view.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    /** Reference to the {@link GameActivity} object. */
    private GameActivity m_GameActivity;
    /** Cached reference to the {@link GameManager} module.*/
    private GameManager m_GameManager;
    /** Reference to the {@link GameLoop} object. */
    private GameLoop m_Loop;
    /** Reference to the {@link GameLoop} thread. */
    private Thread m_LoopThread;
    /** Reference to the {@link GameViewUI} object, which contains UI references. */
    private GameViewUI m_UI;
    /** Reference to all updated and drawn {@link GameObject} objects. */
    private List<GameObject> m_GameObjects;
    /** Reference to the game objects which will be inserted into {@link GameView#m_GameObjects}. */
    private ConcurrentLinkedQueue<GameObject> m_GameObjectsToInsert;
    /** Reference to the game objects which will be deleted from {@link GameView#m_GameObjects}. */
    private ConcurrentLinkedQueue<GameObject> m_GameObjectsToDelete;
    /** Contains active colliders - most of the time, at least. (Cache) */
    private List<Collider> m_ColliderList;
    /** Reference to (@link EnemySpawner) object. */
    private EnemySpawner m_EnemySpawner;
    /** Reference to (@link Player) object. */
    private Player m_Player;
    /** Indicates whether the alarm is enabled or not. */
    private boolean m_AlarmEnabled;
    /** True if the alarm should be stopped automatically, otherwise false. */
    private boolean m_AlarmAutoStop;
    /** Timer for smooth alarm foreground blinking. */
    private float m_AlarmTimer;
    /** True if a click event is in progress, otherwise false. */
    private boolean m_ClickEventActive;
    /** Timer to delay redirection to the next activity. */
    private Timer m_RedirectionDelayTimer;
    /** True if the current level is finished, otherwise false. */
    private boolean m_LevelFinished;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Initialize game activity variable
        m_GameActivity = (GameActivity)context;
        // Cache game manager module reference
        m_GameManager = GameManager.getInstance();
        // Add callback for events
        getHolder().addCallback(this);
        // Ensure that events are generated
        setFocusable(true);
        // Set canvas format
        getHolder().setFormat(PixelFormat.RGBA_8888);

        // Initialize game object list
        m_GameObjects = new ArrayList<GameObject>();
        // Initialize game objects to insert queue
        m_GameObjectsToInsert = new ConcurrentLinkedQueue<GameObject>();
        // Initialize game objects to delete queue
        m_GameObjectsToDelete = new ConcurrentLinkedQueue<GameObject>();
        // Initialize asset manager module
        AssetManager.getInstance().initialize();
        // Initialize collider list
        m_ColliderList = new ArrayList<Collider>(128);

        initializeGameView();

        m_AlarmEnabled = false;
        m_AlarmAutoStop = true;
        m_AlarmTimer = 0f;

        m_ClickEventActive = false;

        m_RedirectionDelayTimer = new Timer();

        m_LevelFinished = false;
    }

    private void initializeGameView() {
        m_EnemySpawner = new EnemySpawner(this, 0, 0, AssetManager.getInstance().getAnimationSet(AnimationSets.BackgroundGame));

        // Initialize player
        m_Player = new Player(this, SuperActivity.getCurrentResolutionX() / (2f * SuperActivity.getScaleFactor()), SuperActivity.getCurrentResolutionY() / (2f * SuperActivity.getScaleFactor()), AssetManager.getInstance().getAnimationSet(AnimationSets.Planet));
        m_Player.setScaleFactor(2.56f);
        m_Player.setAnimationRepeatable(true);
        m_Player.setCollider(new CircleCollider(m_Player, 128f));

        m_EnemySpawner.initialize();

        GameObject clouds = new GameObject(this, m_Player.getPositionX(), m_Player.getPositionY(), AssetManager.getInstance().getAnimationSet(AnimationSets.Clouds));
        clouds.setScaleFactor(2.56f);
        clouds.setAnimationReversed(true);
        clouds.setAnimationRepeatable(true);

        AlarmArea alarmArea = new AlarmArea(this, m_Player.getPositionX(), m_Player.getPositionY());
        alarmArea.setCollider(new CircleCollider(alarmArea, Pustafin.AlarmAreaRadius));
    }

    public void addGameObjectToInsertQueue(GameObject gameObject) {
        m_GameObjectsToInsert.add(gameObject);
    }

    public void addGameObjectToDeleteQueue(GameObject gameObject) {
        m_GameObjectsToDelete.add(gameObject);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Logger.D("GameView::surfaceCreated()");

        AssetManager.getInstance().load(getResources());

        if (GameManager.getInstance().isTutorialMode()) {
            TutorialManager.getInstance().updateTutorialEntry(this);
        }

        // ToDo: Start BGM if there is any

        // Initialize game loop and start thread
        m_Loop = new GameLoop(holder, this);
        m_LoopThread = new Thread(m_Loop);
        m_LoopThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Logger.D("GameView::surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Logger.D("GameView::surfaceDestroyed()");

        m_Loop.setRunning(false);

        try {
            m_LoopThread.join();
        } catch (InterruptedException e) {
            Logger.E(e.getMessage());
        }

        SoundManager.getInstance().StopBGM();

        AssetManager.getInstance().unLoad();
    }

    public void onClick() {
        if (GameManager.getInstance().isTutorialMode()) {
            TutorialManager.getInstance().onClickEvent(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!m_ClickEventActive) {
                    m_ClickEventActive = true;
                    onClick();
                }
                break;
            case MotionEvent.ACTION_UP:
                m_ClickEventActive = false;
                break;
        }

        if (m_Player == null) {
            return false;
        }

        return m_Player.onGlobalTouch(e);
    }

    /**
     * Function which updates the game models
     */
    public void update() {
        synchronized (m_GameObjectsToInsert) {
            while (!m_GameObjectsToInsert.isEmpty()) {
                m_GameObjects.add(m_GameObjectsToInsert.poll());
            }
        }

        synchronized (m_GameObjectsToDelete) {
            while (!m_GameObjectsToDelete.isEmpty()) {
                m_GameObjects.remove(m_GameObjectsToDelete.poll());
            }
        }

        m_ColliderList.clear();

        // Update game objects
        for (GameObject go : m_GameObjects) {
            go.onUpdate();

            if (go.getCollider() != null) {
                m_ColliderList.add(go.getCollider());
            }
        }

        int i = 0;

        for (Collider co1 : m_ColliderList) {
            i++;

            for (int j = i; j < m_ColliderList.size(); j++) {
                Collider co2 = m_ColliderList.get(j);
                if (co1.intersects(co2)) {
                    co1.getOwner().onCollide(co2);
                    co2.getOwner().onCollide(co1);
                }
            }
        }

        // Update user interface states
        m_GameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (m_UI == null) {
                    return;
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.Population) && m_UI.TxtViewPopulation != null) {
                    m_UI.TxtViewPopulation.setText(String.format("%s Mrd.", Util.addLeadingZeros((int)m_GameManager.getPopulation(), 5, true)));
                    m_GameManager.delUpdateFlag(UpdateFlags.Population);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.Money) && m_UI.TxtViewMoney != null) {
                    m_UI.TxtViewMoney.setText(String.format("$ %s Mrd.", Util.addLeadingZeros(m_GameManager.getMoney(), 6, true)));
                    m_GameManager.delUpdateFlag(UpdateFlags.Money);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.Level) && m_UI.TxtViewLevel != null) {
                    m_UI.TxtViewLevel.setText(String.format("Lvl. %d", m_GameManager.getLevel()));
                    m_GameManager.delUpdateFlag(UpdateFlags.Level);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.Score) && m_UI.TxtViewScore != null) {
                    m_UI.TxtViewScore.setText(String.format("%s",Util.addLeadingZeros(m_GameManager.getScore(), 9, true)));
                    m_GameManager.delUpdateFlag(UpdateFlags.Score);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.Time) && m_UI.TxtViewTime != null) {
                    m_UI.TxtViewTime.setText(String.format("%ds", (int) m_Player.getRemainingTime()));
                    m_GameManager.delUpdateFlag(UpdateFlags.Time);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.FPS) && m_UI.TxtViewFPS != null) {
                    m_UI.TxtViewFPS.setText("" + Time.getFPS());
                    m_GameManager.delUpdateFlag(UpdateFlags.FPS);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.BigRocketCount) && m_UI.TxtViewBigRocketCount != null) {
                    m_UI.TxtViewBigRocketCount.setText("" + m_GameManager.getWeaponCount(Weapons.BigRocket));
                    m_GameManager.delUpdateFlag(UpdateFlags.BigRocketCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.SmallNukeCount) && m_UI.TxtViewSmallNukeCount != null && m_GameManager.getWeaponUpgrade(WeaponUpgrades.ResearchNuke) == 1) {
                    m_UI.TxtViewSmallNukeCount.setText("" + m_GameManager.getWeaponCount(Weapons.SmallNuke));
                    m_GameManager.delUpdateFlag(UpdateFlags.SmallNukeCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.BigNukeCount) && m_UI.TxtViewBigNukeCount != null && m_GameManager.getWeaponUpgrade(WeaponUpgrades.ResearchNuke) == 1) {
                    m_UI.TxtViewBigNukeCount.setText("" + m_GameManager.getWeaponCount(Weapons.BigNuke));
                    m_GameManager.delUpdateFlag(UpdateFlags.BigNukeCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.SmallLaserCount) && m_UI.TxtViewSmallLaserCount != null && m_GameManager.getWeaponUpgrade(WeaponUpgrades.ResearchLaser) == 1) {
                    m_UI.TxtViewSmallLaserCount.setText(Math.min((int)(m_GameManager.getMoney() / Pustafin.SmallLaserCostPerSecond), 99) + "s");
                    m_GameManager.delUpdateFlag(UpdateFlags.SmallLaserCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.BigLaserCount) && m_UI.TxtViewBigLaserCount != null && m_GameManager.getWeaponUpgrade(WeaponUpgrades.ResearchLaser) == 1) {
                    m_UI.TxtViewBigLaserCount.setText(Math.min((int)(m_GameManager.getMoney() / Pustafin.BigLaserCostPerSecond), 99) + "s");
                    m_GameManager.delUpdateFlag(UpdateFlags.BigLaserCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.SmallContactBombCount) && m_UI.TxtViewSmallContactBombCount != null && m_GameManager.getWeaponUpgrade(WeaponUpgrades.ResearchContactBomb) == 1) {
                    m_UI.TxtViewSmallContactBombCount.setText("" + m_GameManager.getWeaponCount(Weapons.SmallContactBomb));
                    m_GameManager.delUpdateFlag(UpdateFlags.SmallContactBombCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.BigContactBombCount) && m_UI.TxtViewBigContactBombCount != null && m_GameManager.getWeaponUpgrade(WeaponUpgrades.ResearchContactBomb) == 1) {
                    m_UI.TxtViewBigContactBombCount.setText("" + m_GameManager.getWeaponCount(Weapons.BigContactBomb));
                    m_GameManager.delUpdateFlag(UpdateFlags.BigContactBombCount);
                }

                if ((m_AlarmEnabled || m_AlarmTimer > 0f) && m_UI.ImgViewAlarm != null) {
                    if (m_AlarmTimer == 0f && !m_LevelFinished) {
                        SoundManager.getInstance().PlaySFX(R.raw.sfx_siren_noise);
                    }

                    if (m_AlarmAutoStop) {
                        m_AlarmEnabled = false;
                    }

                    m_AlarmTimer += Time.getScaledDeltaTime();
                    if (m_AlarmTimer >= Pustafin.AlarmForegroundBlinkDuration) {
                        m_AlarmTimer = 0f;
                    }

                    m_UI.ImgViewAlarm.setAlpha((float)Math.sin(Math.PI * m_AlarmTimer / Pustafin.AlarmForegroundBlinkDuration));
                }
            }
        });
    }

    /**
     * This method draws the game view to the given {@link Canvas} object.
     *
     * @param canvas The canvas where the game view should be drawn.
     */
    public void render(Canvas canvas) {
        // Draw game objects
        for (GameObject go : m_GameObjects) {
            go.onDraw(canvas);
        }
    }

    public void win() {
        if (m_LevelFinished) {
            return;
        }

        // Calculate remaining population increase
        GameManager.getInstance().setPopulation(GameManager.getInstance().getPopulation() * Math.pow(1f + Pustafin.PopulationIncreaseFactor +
                        Pustafin.ProBabypillPopulationIncreaseFactor * GameManager.getInstance().getWeaponCount(Weapons.ProBabyPill), Util.roof(m_Player.getRemainingTime())));
        GameManager.getInstance().setWeaponCount(Weapons.ProBabyPill, 0);

        SoundManager.getInstance().PlaySFX(R.raw.sfx_ending_win);

        m_RedirectionDelayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                m_GameActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Open shop activity
                        Intent redirectIntent = new Intent(m_GameActivity, ShopActivity.class);
                        m_GameActivity.startActivity(redirectIntent);

                        // Close game activity
                        m_GameActivity.finish();

                        // Start slide animation
                        m_GameActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    }
                });
            }
        }, Pustafin.GameActivityRedirectionDelayOnWin);

        // Current level is finished
        m_LevelFinished = true;
    }

    public void loose() {
        if (m_LevelFinished) {
            return;
        }

        SoundManager.getInstance().PlaySFX(R.raw.sfx_ending_loose);

        Database.getInstance().open();

        HighscoreTable table = (HighscoreTable)Database.getTable(Database.Tables.HighscoreTable);

        // Add highscore entry
        HighscoreTableEntry highscoreTableEntry = new HighscoreTableEntry(table);
        highscoreTableEntry.Level = GameManager.getInstance().getLevel();
        highscoreTableEntry.Score = GameManager.getInstance().getScore();
        highscoreTableEntry.Date = (int)(System.currentTimeMillis() / 1000);
        Database.getInstance().insertTableEntry(highscoreTableEntry);

        Database.getInstance().close();

        m_RedirectionDelayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                m_GameActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Open highscore activity
                        Intent redirectIntent = new Intent(m_GameActivity, HighscoreActivity.class);
                        m_GameActivity.startActivity(redirectIntent);

                        // Close game activity
                        m_GameActivity.finish();

                        // Start slide animation
                        m_GameActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                    }
                });
            }
        }, Pustafin.GameActivityRedirectionDelayOnLoose);

        // Current level is finished
        m_LevelFinished = true;
    }

    public void changeSelectedWeapon(Weapons weapon) {
        if (m_Player != null) {
            m_Player.setSelectedWeapon(weapon);
        }

        SoundManager.getInstance().PlaySFX(R.raw.sfx_change_weapon);
    }

    /**
     * Sets the value of {@link GameView#m_UI}
     *
     * @param ui The reference to game view ui object.
     */
    public void setGameViewUI(GameViewUI ui) {
        m_UI = ui;
    }

    public GameActivity getGameActivity() {
        return m_GameActivity;
    }

    public Player getPlayer() {
        return m_Player;
    }

    public boolean isAlarmEnabled() {
        return m_AlarmEnabled;
    }

    public void setAlarmEnabled(boolean alarmEnabled) {
        this.m_AlarmEnabled = alarmEnabled;
    }

    public boolean isAlarmAutoStopped() {
        return m_AlarmAutoStop;
    }

    public void setAlarmAutoStop(boolean alarmAutoStop) {
        this.m_AlarmAutoStop = alarmAutoStop;
    }

    public boolean isLevelFinished() {
        return m_LevelFinished;
    }
}
