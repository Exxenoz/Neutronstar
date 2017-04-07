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
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.module.UpdateFlags;
import at.autrage.projects.zeta.opengl.Color;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.opengl.SphereMesh;
import at.autrage.projects.zeta.opengl.SpriteMaterial;
import at.autrage.projects.zeta.ui.TouchDown;
import at.autrage.projects.zeta.ui.TouchEvent;
import at.autrage.projects.zeta.ui.TouchUp;

public class Player extends Component {
    private float m_RemainingTime;
    private float m_OnUpdateEverySecondTimer;
    private Weapons m_SelectedWeapon;
    private float m_PopulationIncreaseTimer;

    private SphereMesh m_SphereMesh;
    private SpriteMaterial m_Material;
    private MeshRenderer meshRenderer;

    public Player(GameObject gameObject) {
        super(gameObject);

        m_RemainingTime = Pustafin.LevelDuration;
        m_OnUpdateEverySecondTimer = 1f;
        m_SelectedWeapon = Weapons.SmallRocket;
        m_PopulationIncreaseTimer = 0f;

        m_SphereMesh = null;
        m_Material = null;
        meshRenderer = null;
    }

    @Override
    protected void onStart() {
        gameObject.setScale(Pustafin.PlanetScale, Pustafin.PlanetScale, Pustafin.PlanetScale);

        m_SphereMesh = AssetManager.getInstance().getSphereMesh();
        m_Material = new SpriteMaterial();
        m_Material.setTexture(AssetManager.getInstance().getTexture(R.drawable.gv_planet));
        m_Material.setTextureCoordinates(m_SphereMesh.getTextureCoordBuffer());

        meshRenderer = gameObject.addComponent(MeshRenderer.class);
        meshRenderer.setMaterial(m_Material);
        meshRenderer.setMesh(m_SphereMesh);

        gameObject.addComponent(CircleCollider.class).setRadius(gameObject.getHalfScaleX());
    }

    public void onUpdate() {
        gameObject.setRotationY(gameObject.getRotationY() + Pustafin.PlanetTurnSpeed * Time.getDeltaTime());

        super.onUpdate();

        if (gameObject.getGameView().isLevelFinished()) {
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
            gameObject.getGameView().win();
        }
    }

    public void receiveDamage(float damage) {
        if (gameObject.getGameView().isLevelFinished()) {
            return;
        }

        if (GameManager.getInstance().getPopulation() > 0) {
            double remainingPopulation = GameManager.getInstance().getPopulation() - damage;
            if ((int) remainingPopulation <= 0f) {
                remainingPopulation = 0f;
            }

            GameManager.getInstance().setPopulation(remainingPopulation);

            if (remainingPopulation <= 0f) {
                gameObject.getGameView().lose();
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
