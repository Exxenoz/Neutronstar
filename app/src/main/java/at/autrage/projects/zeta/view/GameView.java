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
import java.util.concurrent.ConcurrentSkipListMap;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.GameActivity;
import at.autrage.projects.zeta.activity.HighscoreActivity;
import at.autrage.projects.zeta.activity.ShopActivity;
import at.autrage.projects.zeta.collision.ColliderManager;
import at.autrage.projects.zeta.exception.ArgumentNullException;
import at.autrage.projects.zeta.model.EnemySpawner;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Player;
import at.autrage.projects.zeta.model.Sprite;
import at.autrage.projects.zeta.model.WeaponUpgrades;
import at.autrage.projects.zeta.model.Weapons;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.opengl.Renderer;
import at.autrage.projects.zeta.tutorial.TutorialManager;
import at.autrage.projects.zeta.module.UpdateFlags;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.util.Util;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.prefab.GameViewUserInterfacePrefab;
import at.autrage.projects.zeta.ui.TouchEvent;

/**
 * It is responsible for {@link GameViewUpdater} management and drawing of the whole game view.
 */
public class GameView extends GLSurfaceView {
    public final GameActivity GameActivity;

    private GameViewRenderer m_Renderer;
    private GameViewUpdater m_Updater;

    public final GameViewUI UI;
    public final GameViewUserInterfacePrefab UserInterfacePrefab;

    private List<GameObject> m_GameObjects;
    private int currGameObjectIdx;

    private ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<at.autrage.projects.zeta.opengl.Renderer>> renderers;

    public final GameManager GameManager;
    public final ColliderManager ColliderManager;
    public final TutorialManager TutorialManager;

    private EnemySpawner m_EnemySpawner;
    private Player m_Player;

    private ConcurrentLinkedQueue<TouchEvent> touchEvents;

    private Timer m_RedirectionDelayTimer;
    private boolean m_LevelFinished;

    public GameView(GameActivity gameActivity, GameViewUI ui) {
        super(gameActivity.getApplicationContext());

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Initialize game activity variable
        GameActivity = gameActivity;

        UI = ui;

        // Initialize asset manager module
        AssetManager.getInstance().initialize(getContext());

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

        m_GameObjects = new ArrayList<>(256);
        currGameObjectIdx = -1;

        renderers = new ConcurrentSkipListMap<>();

        GameManager = at.autrage.projects.zeta.module.GameManager.getInstance(); // ToDo: Replace singleton with object
        ColliderManager = new ColliderManager();

        GameObject enemySpawnerGameObject = new GameObject(this, 0f, 0f, "EnemySpawner");

        Sprite enemySpawnerSprite = enemySpawnerGameObject.addComponent(Sprite.class);
        enemySpawnerSprite.setAnimationSet(AnimationSets.BackgroundGame);
        enemySpawnerSprite.playDefaultAnimationFromSet();

        m_EnemySpawner = enemySpawnerGameObject.addComponent(EnemySpawner.class);

        GameObject playerGameObject = new GameObject(this, 0f, 0f, "Player");
        m_Player = playerGameObject.addComponent(Player.class);

        UserInterfacePrefab = new GameViewUserInterfacePrefab(this);

        touchEvents = new ConcurrentLinkedQueue<>();

        m_RedirectionDelayTimer = new Timer();
        m_LevelFinished = false;

        if (GameManager.isTutorialMode()) {
            TutorialManager = new TutorialManager(this);
        }
        else {
            TutorialManager = null;
        }
    }

    public void addGameObject(GameObject gameObject) {
        if (gameObject == null) {
            throw new ArgumentNullException();
        }

        m_GameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        if (gameObject == null) {
            throw new ArgumentNullException();
        }

        if (currGameObjectIdx > -1) {
            int gameObjectIndex = m_GameObjects.indexOf(gameObject);
            if (gameObjectIndex > -1) {
                if (gameObjectIndex <= currGameObjectIdx) {
                    currGameObjectIdx--;
                }

                m_GameObjects.remove(gameObjectIndex);
            }
        }
        else {
            m_GameObjects.remove(gameObject);
        }
    }

    public void addRenderer(at.autrage.projects.zeta.opengl.Renderer renderer, int drawOrderID) {
        if (renderer == null) {
            throw new ArgumentNullException();
        }

        if (renderer.Holder != null) {
            throw new IllegalStateException();
        }

        renderer.Holder = renderers.get(drawOrderID);
        if (renderer.Holder == null) {
            renderers.put(drawOrderID, renderer.Holder = new ConcurrentLinkedQueue<>());
        }

        renderer.Holder.add(renderer);
    }

    public void removeRenderer(at.autrage.projects.zeta.opengl.Renderer renderer) {
        if (renderer == null) {
            throw new ArgumentNullException();
        }

        if (renderer.Holder == null) {
            throw new IllegalStateException();
        }

        renderer.Holder.remove(renderer);
        renderer.Holder = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        Logger.D("GameView::surfaceCreated()");

        AssetManager.getInstance().onSurfaceCreated(getResources());

        // ToDo: Start BGM if there is any

        // Initialize game view updater and start thread
        m_Updater = new GameViewUpdater(this);
        m_Updater.start();
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

        m_Updater.stop();

        try {
            m_Updater.join();
        } catch (InterruptedException e) {
            Logger.E(e.getMessage());
        }

        SoundManager.getInstance().StopBGM();

        AssetManager.getInstance().onSurfaceDestroyed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        touchEvents.add(new TouchEvent(e));

        // Always handle touch events
        return true;
    }

    /**
     * Function which updates the game models
     */
    public void update() {
        // Update game objects
        for (currGameObjectIdx = 0; currGameObjectIdx < m_GameObjects.size(); currGameObjectIdx++) {
            m_GameObjects.get(currGameObjectIdx).onUpdate();
        }

        currGameObjectIdx = -1;

        // Update touch events
        for (TouchEvent touchEvent = touchEvents.poll(); touchEvent != null; touchEvent = touchEvents.poll()) {
            ColliderManager.touch(touchEvent);

            if (TutorialManager != null) {
                TutorialManager.touch(touchEvent);
            }
        }

        // Update colliders
        ColliderManager.update();

        // Update user interface states
        GameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (UI == null) {
                    return;
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.Population) && UI.TxtViewPopulation != null) {
                    UI.TxtViewPopulation.setText(String.format(GameActivity.getString(R.string.gv_population_display), Util.addLeadingZeros((int) GameManager.getPopulation(), 5, true, false)));
                    GameManager.delUpdateFlag(UpdateFlags.Population);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.Money) && UI.TxtViewMoney != null) {
                    UI.TxtViewMoney.setText(String.format(GameActivity.getString(R.string.gv_money_display), Util.addLeadingZeros(GameManager.getMoney(), 6, true, false)));
                    GameManager.delUpdateFlag(UpdateFlags.Money);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.Level) && UI.TxtViewLevel != null) {
                    UI.TxtViewLevel.setText(String.format("Lvl. %d", GameManager.getLevel()));
                    GameManager.delUpdateFlag(UpdateFlags.Level);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.Score) && UI.TxtViewScore != null) {
                    UI.TxtViewScore.setText(String.format("%s", Util.addLeadingZeros(GameManager.getScore(), 6, true, true)));
                    GameManager.delUpdateFlag(UpdateFlags.Score);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.Time) && UI.TxtViewTime != null) {
                    UI.TxtViewTime.setText(String.format("%ds", (int) m_Player.getRemainingTime()));
                    GameManager.delUpdateFlag(UpdateFlags.Time);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.FPS) && UI.TxtViewFPS != null) {
                    UI.TxtViewFPS.setText(Time.getFPS() + "/" + Time.getFPSGL());
                    GameManager.delUpdateFlag(UpdateFlags.FPS);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.BigRocketCount) && UI.TxtViewBigRocketCount != null) {
                    UI.TxtViewBigRocketCount.setText("" + GameManager.getWeaponCount(Weapons.BigRocket));
                    GameManager.delUpdateFlag(UpdateFlags.BigRocketCount);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.SmallNukeCount) && UI.TxtViewSmallNukeCount != null && GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchNuke)) {
                    UI.TxtViewSmallNukeCount.setText("" + GameManager.getWeaponCount(Weapons.SmallNuke));
                    GameManager.delUpdateFlag(UpdateFlags.SmallNukeCount);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.BigNukeCount) && UI.TxtViewBigNukeCount != null && GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchNuke)) {
                    UI.TxtViewBigNukeCount.setText("" + GameManager.getWeaponCount(Weapons.BigNuke));
                    GameManager.delUpdateFlag(UpdateFlags.BigNukeCount);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.SmallLaserCount) && UI.TxtViewSmallLaserCount != null && GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchLaser)) {
                    UI.TxtViewSmallLaserCount.setText(Math.min((int) (GameManager.getMoney() / Pustafin.SmallLaserCostPerSecond), 99) + "s");
                    GameManager.delUpdateFlag(UpdateFlags.SmallLaserCount);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.BigLaserCount) && UI.TxtViewBigLaserCount != null && GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchLaser)) {
                    UI.TxtViewBigLaserCount.setText(Math.min((int) (GameManager.getMoney() / Pustafin.BigLaserCostPerSecond), 99) + "s");
                    GameManager.delUpdateFlag(UpdateFlags.BigLaserCount);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.SmallContactBombCount) && UI.TxtViewSmallContactBombCount != null && GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchContactBomb)) {
                    UI.TxtViewSmallContactBombCount.setText("" + GameManager.getWeaponCount(Weapons.SmallContactBomb));
                    GameManager.delUpdateFlag(UpdateFlags.SmallContactBombCount);
                }

                if (GameManager.hasUpdateFlag(UpdateFlags.BigContactBombCount) && UI.TxtViewBigContactBombCount != null && GameManager.isWeaponUpgradeResearched(WeaponUpgrades.ResearchContactBomb)) {
                    UI.TxtViewBigContactBombCount.setText("" + GameManager.getWeaponCount(Weapons.BigContactBomb));
                    GameManager.delUpdateFlag(UpdateFlags.BigContactBombCount);
                }
            }
        });

        for (ConcurrentLinkedQueue<at.autrage.projects.zeta.opengl.Renderer> holder : renderers.values()) {
            for (at.autrage.projects.zeta.opengl.Renderer renderer : holder) {
                renderer.lateUpdate();
            }
        }
    }

    /**
     * This method draws the game view to the surface view.
     */
    public void draw(float[] vpMatrix) {
        for (ConcurrentLinkedQueue<at.autrage.projects.zeta.opengl.Renderer> holder : renderers.values()) {
            for (at.autrage.projects.zeta.opengl.Renderer renderer : holder) {
                renderer.draw(vpMatrix);
            }
        }
    }

    public void win() {
        if (m_LevelFinished) {
            return;
        }

        if (GameManager.isTutorialMode()) {
            return;
        }

        GameManager.onWin(this, m_Player);

        m_RedirectionDelayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Open shop activity
                        Intent redirectIntent = new Intent(GameActivity, ShopActivity.class);
                        GameActivity.startActivity(redirectIntent);

                        // Close game activity
                        GameActivity.finish();

                        // Start slide animation
                        //GameActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
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

        if (GameManager.isTutorialMode()) {
            return;
        }

        GameManager.onLose(this, m_Player);

        m_RedirectionDelayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Open highscore activity
                        Intent redirectIntent = new Intent(GameActivity, HighscoreActivity.class);
                        GameActivity.startActivity(redirectIntent);

                        // Close game activity
                        GameActivity.finish();

                        // Start slide animation
                        //GameActivity.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
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

    public Player getPlayer() {
        return m_Player;
    }

    public EnemySpawner getEnemySpawner() {
        return m_EnemySpawner;
    }

    public boolean isLevelFinished() {
        return m_LevelFinished;
    }
}
