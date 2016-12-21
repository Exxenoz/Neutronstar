package at.autrage.projects.zeta.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.GameActivity;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.model.GameLoop;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Player;
import at.autrage.projects.zeta.model.Weapon;
import at.autrage.projects.zeta.model.WeaponUpgrades;
import at.autrage.projects.zeta.model.Weapons;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.module.Util;

/**
 * It is responsible for {@link GameLoop} management and drawing of the whole game view.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    /** Reference to our {@link GameActivity} object. */
    private GameActivity m_GameActivity;
    /** Reference to our {@link GameLoop} object. */
    private GameLoop m_Loop;
    /** Reference to our {@link GameLoop} thread. */
    private Thread m_LoopThread;
    /** Reference to our {@link GameViewUI} object. */
    private GameViewUI m_UI;
    /** Reference to (@link Player) object. */
    private Player m_Player;
    /** Reference to our drawn {@link GameObject}s. */
    private List<GameObject> m_GameObjects;
    /** Reference to the game objects which will be inserted into {@link GameView#m_GameObjects}. */
    private ConcurrentLinkedQueue<GameObject> m_GameObjectsToInsert;
    /** Reference to the game objects which will be deleted from {@link GameView#m_GameObjects}. */
    private ConcurrentLinkedQueue<GameObject> m_GameObjectsToDelete;
    /** Contains active colliders - most of the time, at least. (Cache) */
    private List<Collider> m_ColliderList;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Initialize game activity variable
        m_GameActivity = (GameActivity)context;
        // Add callback for events
        getHolder().addCallback(this);
        // Ensure that events are generated
        setFocusable(true);

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
    }

    private void initializeGameView() {
        new GameObject(this, 960, 540, AssetManager.getInstance().getAnimationSet(AnimationSets.BackgroundGame));

        // Initialize player
        m_Player = new Player(this, 960, 540, AssetManager.getInstance().getAnimationSet(AnimationSets.Planet));
        m_Player.setScaleFactor(2.56f);
        m_Player.setCollider(new CircleCollider(m_Player, 128f));

        GameObject clouds = new GameObject(this, 960, 540, AssetManager.getInstance().getAnimationSet(AnimationSets.Clouds));
        clouds.setScaleFactor(2.56f);
        clouds.setAnimationReversed(true);
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

        SoundManager.getInstance().StartBGM(R.raw.cantina_band, true);

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

    @Override
    public boolean onTouchEvent(MotionEvent e) {
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

                if (m_UI.TxtViewPopulation != null) {
                    m_UI.TxtViewPopulation.setText(String.format("%s Mrd.", Util.addLeadingZeros(m_Player.getPopulation(), 5, true)));
                }

                if (m_UI.TxtViewMoney != null) {
                    m_UI.TxtViewMoney.setText(String.format("$ %s Mrd.", Util.addLeadingZeros(m_Player.getMoney(), 6, true)));
                }

                if (m_UI.TxtViewLevel != null) {
                    m_UI.TxtViewLevel.setText(String.format("Lvl. %d", m_Player.getLevel()));
                }

                if (m_UI.TxtViewScore != null) {
                    m_UI.TxtViewScore.setText(String.format("%s",Util.addLeadingZeros(m_Player.getScore(), 9, true)));
                }

                if (m_UI.TxtViewTime != null) {
                    m_UI.TxtViewTime.setText(String.format("%ds", (int) m_Player.getRemainingTime()));
                }

                if (m_UI.TxtViewFPS != null) {
                    m_UI.TxtViewFPS.setText("" + Time.getFPS());
                }

                if (m_UI.TxtViewBigRocketCount != null) {
                    m_UI.TxtViewBigRocketCount.setText("" + m_Player.getWeaponCount(Weapons.BigRocket));
                }

                if (m_UI.TxtViewSmallNukeCount != null && m_Player.getWeaponUpgrade(WeaponUpgrades.ResearchNuke) == 1) {
                    m_UI.TxtViewSmallNukeCount.setText("" + m_Player.getWeaponCount(Weapons.SmallNuke));
                }

                if (m_UI.TxtViewBigNukeCount != null && m_Player.getWeaponUpgrade(WeaponUpgrades.ResearchNuke) == 1) {
                    m_UI.TxtViewBigNukeCount.setText("" + m_Player.getWeaponCount(Weapons.BigNuke));
                }

                if (m_UI.TxtViewSmallLaserCount != null && m_Player.getWeaponUpgrade(WeaponUpgrades.ResearchLaser) == 1) {
                    m_UI.TxtViewSmallLaserCount.setText(Math.min((int)(m_Player.getMoney() / Pustafin.SmallLaserCostPerSecond), 99) + "s");
                }

                if (m_UI.TxtViewBigLaserCount != null && m_Player.getWeaponUpgrade(WeaponUpgrades.ResearchLaser) == 1) {
                    m_UI.TxtViewBigLaserCount.setText(Math.min((int)(m_Player.getMoney() / Pustafin.BigLaserCostPerSecond), 99) + "s");
                }

                if (m_UI.TxtViewSmallContactBombCount != null && m_Player.getWeaponUpgrade(WeaponUpgrades.ResearchContactBomb) == 1) {
                    m_UI.TxtViewSmallContactBombCount.setText("" + m_Player.getWeaponCount(Weapons.SmallContactBomb));
                }

                if (m_UI.TxtViewBigContactBombCount != null && m_Player.getWeaponUpgrade(WeaponUpgrades.ResearchContactBomb) == 1) {
                    m_UI.TxtViewBigContactBombCount.setText("" + m_Player.getWeaponCount(Weapons.BigContactBomb));
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

    public void changeSelectedWeapon(Weapons weapon) {
        if (m_Player != null) {
            m_Player.setSelectedWeapon(weapon);
        }
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
}
