package at.autrage.projects.zeta.model;

import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.module.UpdateFlags;
import at.autrage.projects.zeta.opengl.Color;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.opengl.SphereMesh;
import at.autrage.projects.zeta.opengl.SpriteMaterial;
import at.autrage.projects.zeta.view.GameView;

public class Player extends GameObject {
    private float m_RemainingTime;
    private float m_OnUpdateEverySecondTimer;
    private Weapons m_SelectedWeapon;
    private float m_PopulationIncreaseTimer;

    private Map<Integer, Vector2D> m_TouchEventStartPositions;

    private SphereMesh m_SphereMesh;
    private SpriteMaterial m_Material;
    private MeshRenderer meshRenderer;

    private AlarmArea m_AlarmArea;
    private Sprite m_TouchRadiusDebugCircle;

    public Player(GameView gameView, float positionX, float positionY) {
        super(gameView, positionX, positionY);

        setScale(Pustafin.PlanetScale, Pustafin.PlanetScale, Pustafin.PlanetScale);

        m_RemainingTime = Pustafin.LevelDuration;
        m_OnUpdateEverySecondTimer = 1f;
        m_SelectedWeapon = Weapons.SmallRocket;

        m_TouchEventStartPositions = new HashMap<>();

        m_SphereMesh = new SphereMesh(Pustafin.PlanetMeshStacks, Pustafin.PlanetMeshSlices);
        m_Material = new SpriteMaterial();
        m_Material.setTexture(AssetManager.getInstance().getTexture(R.drawable.gv_planet));
        m_Material.setTextureCoordinates(m_SphereMesh.getTextureCoordBuffer());

        meshRenderer = new MeshRenderer(this);
        meshRenderer.setMaterial(m_Material);
        meshRenderer.setMesh(m_SphereMesh);
        meshRenderer.setEnabled(true);
        addComponent(meshRenderer);

        m_AlarmArea = new AlarmArea(gameView, positionX, positionY);
        m_AlarmArea.addComponent(new CircleCollider(m_AlarmArea, Pustafin.AlarmAreaRadius));

        if (Pustafin.DebugMode) {
            m_TouchRadiusDebugCircle = new Sprite(gameView, positionX, positionY, AssetManager.getInstance().getAnimationSet(AnimationSets.DebugCircle));
            m_TouchRadiusDebugCircle.setScaleFactor(2f * Pustafin.PlanetTouchRadius / m_TouchRadiusDebugCircle.getScaleX());
            m_TouchRadiusDebugCircle.getSpriteMaterial().getColor().setColor(Color.Blue);
            m_TouchRadiusDebugCircle.setParent(this);
        }
    }

    public void onUpdate() {
        setRotationY(getRotationY() + Pustafin.PlanetTurnSpeed * Time.getDeltaTime());

        super.onUpdate();

        if (getGameView().isLevelFinished()) {
            return;
        }

        m_PopulationIncreaseTimer += Time.getScaledDeltaTime();
        while (m_PopulationIncreaseTimer >= 1f) {
            GameManager.getInstance().setPopulation((1f + Pustafin.PopulationIncreaseFactor +
                    Pustafin.ProBabypillPopulationIncreaseFactor * GameManager.getInstance().getWeaponCount(Weapons.ProBabyPill))
                    * GameManager.getInstance().getPopulation());
            m_PopulationIncreaseTimer--;
        }

        m_OnUpdateEverySecondTimer += Time.getScaledDeltaTime();
        while (m_OnUpdateEverySecondTimer >= 1f) {
            m_OnUpdateEverySecondTimer -= 1f;
            onUpdateEverySecond();
        }
    }

    public void onUpdateEverySecond() {
        m_RemainingTime -= 1f;
        if (m_RemainingTime <= 0f) {
            m_RemainingTime = 0f;
        }

        GameManager.getInstance().setUpdateFlag(UpdateFlags.Time);
        GameManager.getInstance().setUpdateFlag(UpdateFlags.FPS);

        if (m_RemainingTime == 0f) {
            getGameView().win();
        }
    }

    public void onGlobalTouch(MotionEvent event) {
        if (getGameView().isLevelFinished()) {
            return;
        }

        if (Time.getTimeScale() == 0f) {
            m_TouchEventStartPositions.clear();
            return;
        }

        if (!meshRenderer.isEnabled()) {
            return;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchRadius = Pustafin.PlanetTouchRadius * SuperActivity.getScaleFactor();

            float diffX = event.getX() - SuperActivity.getCurrentResolutionX() / 2f;
            float diffY = event.getY() - SuperActivity.getCurrentResolutionY() / 2f;

            float sqrDist = diffX * diffX + diffY * diffY;
            if (sqrDist <= touchRadius * touchRadius) {
                Vector2D pos = new Vector2D();
                pos.X = event.getX();
                pos.Y = event.getY();
                m_TouchEventStartPositions.put(event.getActionIndex(), pos);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            Vector2D pos = m_TouchEventStartPositions.get(event.getActionIndex());
            if (pos != null) {
                onTouchRelease(event, pos);
                m_TouchEventStartPositions.remove(event.getActionIndex());
            }
        }

        // TODO: Implement LAZOR!
    }

    private void onTouchRelease(MotionEvent event, Vector2D startTouchPosition) {
        if (m_SelectedWeapon == Weapons.SmallLaser ||
                m_SelectedWeapon == Weapons.BigLaser) {
            // Laser weapon handled in onGlobalTouch function
            return;
        }

        WeaponStockpile weaponStockpile = GameManager.getInstance().getWeaponStockpile(m_SelectedWeapon);
        if (weaponStockpile == null) {
            return;
        }

        int weaponCount = weaponStockpile.getCount();
        if (weaponCount == 0) {
            return;
        }

        float deltaPositionX = event.getX() - startTouchPosition.X;
        float deltaPositionY = event.getY() - startTouchPosition.Y;

        double distance = Math.sqrt(deltaPositionX * deltaPositionX + deltaPositionY * deltaPositionY);

        float directionX = (float) (deltaPositionX / distance);
        float directionY = (float) (deltaPositionY / distance) * (-1); // Flip y direction

        float radius = getHalfScaleX();

        float spawnPositionX = directionX * radius;
        float spawnPositionY = directionY * radius;

        Weapon weapon = null;
        switch (m_SelectedWeapon) {
            case SmallRocket:
                weapon = Rocket.createSmallRocket(this, spawnPositionX, spawnPositionY, directionX, directionY);
                break;
            case BigRocket:
                weapon = Rocket.createBigRocket(this, spawnPositionX, spawnPositionY, directionX, directionY);
                break;
            case SmallNuke:
                weapon = Nuke.createSmallNuke(this, spawnPositionX, spawnPositionY, directionX, directionY);
                break;
            case BigNuke:
                weapon = Nuke.createBigNuke(this, spawnPositionX, spawnPositionY, directionX, directionY);
                break;
            case SmallContactBomb:
                break;
            case BigContactBomb:
                break;
        }

        LinearMovement movement = weapon.getComponent(LinearMovement.class);
        if (weapon != null && movement != null) {
            Logger.D("Spawned selected weapon %s at (%f, %f) with direction (%f, %f), hit damage %f and speed %f",
                    m_SelectedWeapon.toString(), spawnPositionX, spawnPositionY, directionX, directionY, weapon.getHitDamage(), movement.getSpeed());
        } else if (weapon != null && movement == null) {
            Logger.D("Spawned selected weapon %s at (%f, %f) with direction (%f, %f), hit damage %f and unknown speed.",
                    m_SelectedWeapon.toString(), spawnPositionX, spawnPositionY, directionX, directionY, weapon.getHitDamage());
        }

        if (weaponCount > 0) {
            weaponStockpile.setCount(--weaponCount);
            if (weaponCount == 0) {
                getGameView().getGameActivity().setHighlightedHotbarBoxToSmallRocketArea();
            }
        }
    }

    @Override
    public void onCollide(Collider other) {
        super.onCollide(other);

        if (getGameView().isLevelFinished()) {
            return;
        }

        if (other.getGameObject() instanceof Enemy && GameManager.getInstance().getPopulation() > 0) {
            Enemy enemy = (Enemy) other.getGameObject();

            SoundManager.getInstance().PlaySFX(R.raw.sfx_hit_planet, 0.5f + (float) Math.random());

            double remainingPopulation = GameManager.getInstance().getPopulation() - enemy.getHitDamage();
            if ((int) remainingPopulation <= 0f) {
                remainingPopulation = 0f;
            }

            GameManager.getInstance().setPopulation(remainingPopulation);

            if (remainingPopulation <= 0f) {
                getGameView().lose();
            }
        }
    }

    public float getRemainingTime() {
        return m_RemainingTime;
    }

    public void setRemainingTime(float remainingTime) {
        this.m_RemainingTime = remainingTime;
    }

    public Weapons getSelectedWeapon() {
        return m_SelectedWeapon;
    }

    public void setSelectedWeapon(Weapons selectedWeapon) {
        this.m_SelectedWeapon = selectedWeapon;
    }
}
