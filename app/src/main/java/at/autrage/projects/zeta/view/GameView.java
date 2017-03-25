package at.autrage.projects.zeta.view;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.GameActivity;
import at.autrage.projects.zeta.activity.HighscoreActivity;
import at.autrage.projects.zeta.activity.ShopActivity;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.ColliderManager;
import at.autrage.projects.zeta.framework.Synchronitron;
import at.autrage.projects.zeta.model.EnemySpawner;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Player;
import at.autrage.projects.zeta.model.Sprite;
import at.autrage.projects.zeta.model.WeaponUpgrades;
import at.autrage.projects.zeta.model.Weapons;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.TutorialManager;
import at.autrage.projects.zeta.module.UpdateFlags;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.module.Util;
import at.autrage.projects.zeta.opengl.MeshRenderer;

/**
 * It is responsible for {@link GameViewUpdater} management and drawing of the whole game view.
 */
public class GameView extends GLSurfaceView {
    /**
     * Reference to the {@link GameActivity} object.
     */
    private GameActivity m_GameActivity;
    /**
     * Reference to the {@link GameViewRenderer} object.
     */
    private GameViewRenderer m_Renderer;
    /**
     * Reference to the {@link GameViewUpdater} object.
     */
    private GameViewUpdater m_Updater;
    /**
     * Reference to the {@link GameViewUpdater} thread.
     */
    private Thread m_UpdaterThread;
    /**
     * Reference to the {@link GameViewUI} object, which contains UI references.
     */
    private GameViewUI m_UI;

    /**
     * Cached reference to the {@link GameManager} module.
     */
    private GameManager m_GameManager;

    /**
     * Reference to all updated {@link GameObject} objects.
     */
    private Synchronitron<GameObject> m_GameObjects;

    /**
     * Reference to all enabled {@link MeshRenderer} objects.
     */
    private Synchronitron<MeshRenderer> m_MeshRenderers;

    /**
     * Reference to (@link ColliderManager) object.
     */
    public final ColliderManager ColliderManager;

    /**
     * Reference to (@link EnemySpawner) object.
     */
    private EnemySpawner m_EnemySpawner;
    /**
     * Reference to (@link Player) object.
     */
    private Player m_Player;

    /**
     * Indicates whether the alarm is enabled or not.
     */
    private boolean m_AlarmEnabled;
    /**
     * True if the alarm should be stopped automatically, otherwise false.
     */
    private boolean m_AlarmAutoStop;
    /**
     * Timer for smooth alarm foreground blinking.
     */
    private float m_AlarmTimer;

    /**
     * True if a click event is in progress, otherwise false.
     */
    private boolean m_ClickEventActive;
    /**
     * Timer to delay redirection to the next activity.
     */
    private Timer m_RedirectionDelayTimer;
    /**
     * True if the current level is finished, otherwise false.
     */
    private boolean m_LevelFinished;

    public GameView(GameActivity gameActivity) {
        super(gameActivity.getApplicationContext());

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Initialize game activity variable
        m_GameActivity = gameActivity;

        // Initialize asset manager module
        AssetManager.getInstance().initialize();

        // Add callback for events
        //getHolder().addCallback(this);
        // Ensure that events are generated
        setFocusable(true);
        // Set pixel format
        //getHolder().setFormat(PixelFormat.RGBA_8888);

        // Create game view renderer
        m_Renderer = new GameViewRenderer(this);
        // Set the game view renderer for drawing on the GLSurfaceView
        setRenderer(m_Renderer);
        // Render the view also when there is no change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        // Cache game manager module reference
        m_GameManager = GameManager.getInstance();

        m_GameObjects = new Synchronitron<>(GameObject.class, 256);

        m_MeshRenderers = new Synchronitron<>(MeshRenderer.class, 256);

        ColliderManager = new ColliderManager();

        GameObject enemySpawnerGameObject = new GameObject(this, 0f, 0f);
        new Sprite(enemySpawnerGameObject, AssetManager.getInstance().getAnimationSet(AnimationSets.BackgroundGame));

        m_EnemySpawner = new EnemySpawner(enemySpawnerGameObject);

        GameObject playerGameObject = new GameObject(this, 0f, 0f);

        m_Player = new Player(playerGameObject);

        m_AlarmEnabled = false;
        m_AlarmAutoStop = true;
        m_AlarmTimer = 0f;

        m_ClickEventActive = false;

        m_RedirectionDelayTimer = new Timer();

        m_LevelFinished = false;
    }

    public void addGameObject(GameObject gameObject) {
        m_GameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        m_GameObjects.remove(gameObject);
    }

    public void addMeshRenderer(MeshRenderer meshRenderer) {
        m_MeshRenderers.add(meshRenderer);
    }

    public void removeMeshRenderer(MeshRenderer meshRenderer) {
        m_MeshRenderers.remove(meshRenderer);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        Logger.D("GameView::surfaceCreated()");

        AssetManager.getInstance().onSurfaceCreated(getResources());

        // ToDo: Start BGM if there is any

        // Initialize game view updater and start thread
        m_Updater = new GameViewUpdater(this);
        m_UpdaterThread = new Thread(m_Updater);
        m_UpdaterThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        super.surfaceChanged(holder, format, width, height);
        Logger.D("GameView::surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        Logger.D("GameView::surfaceDestroyed()");

        m_Updater.setRunning(false);

        try {
            m_UpdaterThread.join();
        } catch (InterruptedException e) {
            Logger.E(e.getMessage());
        }

        SoundManager.getInstance().StopBGM();

        AssetManager.getInstance().onSurfaceDestroyed();
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

        if (m_Player != null) {
            m_Player.onGlobalTouch(e);
        }

        // Always handle touch events
        return true;
    }

    /**
     * Function which updates the game models
     */
    public void update() {
        m_GameObjects.synchronize();

        // Update game objects
        for (GameObject go : m_GameObjects) {
            go.onUpdate();
        }

        // Update colliders
        ColliderManager.update();

        // Update user interface states
        m_GameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (m_UI == null) {
                    return;
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.Population) && m_UI.TxtViewPopulation != null) {
                    m_UI.TxtViewPopulation.setText(String.format(m_GameActivity.getString(R.string.gv_population_display), Util.addLeadingZeros((int) m_GameManager.getPopulation(), 5, true, false)));
                    m_GameManager.delUpdateFlag(UpdateFlags.Population);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.Money) && m_UI.TxtViewMoney != null) {
                    m_UI.TxtViewMoney.setText(String.format(m_GameActivity.getString(R.string.gv_money_display), Util.addLeadingZeros(m_GameManager.getMoney(), 6, true, false)));
                    m_GameManager.delUpdateFlag(UpdateFlags.Money);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.Level) && m_UI.TxtViewLevel != null) {
                    m_UI.TxtViewLevel.setText(String.format("Lvl. %d", m_GameManager.getLevel()));
                    m_GameManager.delUpdateFlag(UpdateFlags.Level);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.Score) && m_UI.TxtViewScore != null) {
                    m_UI.TxtViewScore.setText(String.format("%s", Util.addLeadingZeros(m_GameManager.getScore(), 6, true, true)));
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

                if (m_GameManager.hasUpdateFlag(UpdateFlags.SmallNukeCount) && m_UI.TxtViewSmallNukeCount != null && m_GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchNuke)) {
                    m_UI.TxtViewSmallNukeCount.setText("" + m_GameManager.getWeaponCount(Weapons.SmallNuke));
                    m_GameManager.delUpdateFlag(UpdateFlags.SmallNukeCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.BigNukeCount) && m_UI.TxtViewBigNukeCount != null && m_GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchNuke)) {
                    m_UI.TxtViewBigNukeCount.setText("" + m_GameManager.getWeaponCount(Weapons.BigNuke));
                    m_GameManager.delUpdateFlag(UpdateFlags.BigNukeCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.SmallLaserCount) && m_UI.TxtViewSmallLaserCount != null && m_GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchLaser)) {
                    m_UI.TxtViewSmallLaserCount.setText(Math.min((int) (m_GameManager.getMoney() / Pustafin.SmallLaserCostPerSecond), 99) + "s");
                    m_GameManager.delUpdateFlag(UpdateFlags.SmallLaserCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.BigLaserCount) && m_UI.TxtViewBigLaserCount != null && m_GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchLaser)) {
                    m_UI.TxtViewBigLaserCount.setText(Math.min((int) (m_GameManager.getMoney() / Pustafin.BigLaserCostPerSecond), 99) + "s");
                    m_GameManager.delUpdateFlag(UpdateFlags.BigLaserCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.SmallContactBombCount) && m_UI.TxtViewSmallContactBombCount != null && m_GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchContactBomb)) {
                    m_UI.TxtViewSmallContactBombCount.setText("" + m_GameManager.getWeaponCount(Weapons.SmallContactBomb));
                    m_GameManager.delUpdateFlag(UpdateFlags.SmallContactBombCount);
                }

                if (m_GameManager.hasUpdateFlag(UpdateFlags.BigContactBombCount) && m_UI.TxtViewBigContactBombCount != null && m_GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchContactBomb)) {
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

                    m_UI.ImgViewAlarm.setAlpha((float) Math.sin(Math.PI * m_AlarmTimer / Pustafin.AlarmForegroundBlinkDuration));
                }
            }
        });

        synchronized (m_MeshRenderers) {
            m_MeshRenderers.synchronize();

            for (MeshRenderer renderer : m_MeshRenderers) {
                renderer.shift();
            }
        }
    }

    /**
     * This method draws the game view to the surface view.
     */
    public void draw(float[] vpMatrix) {
        synchronized (m_MeshRenderers) {
            for (MeshRenderer renderer : m_MeshRenderers) {
                renderer.draw(vpMatrix);
            }
        }
    }

    public void win() {
        if (m_LevelFinished) {
            return;
        }

        if (GameManager.getInstance().isTutorialMode()) {
            return;
        }

        GameManager.getInstance().onWin(this, m_Player);

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
                        //m_GameActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    }
                });
            }
        }, Pustafin.GameActivityRedirectionDelayOnWin);

        // Current level is finished
        m_LevelFinished = true;
    }

    public void lose() {
        if (m_LevelFinished) {
            return;
        }

        if (GameManager.getInstance().isTutorialMode()) {
            return;
        }

        GameManager.getInstance().onLose(this, m_Player);

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
                        //m_GameActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
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

    public EnemySpawner getEnemySpawner() {
        return m_EnemySpawner;
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
