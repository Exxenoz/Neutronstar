package at.autrage.projects.zeta.model;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.module.UpdateFlags;
import at.autrage.projects.zeta.view.GameView;

public class Player extends Sprite {
    private float m_RemainingTime;
    private float m_OnUpdateEverySecondTimer;
    private Weapons m_SelectedWeapon;
    private float m_PopulationIncreaseTimer;

    private Map<Integer, Vector2D> m_TouchEventStartPositions;
    private Paint m_PlanetTouchColliderPaint;

    private Sprite m_Clouds;
    private AlarmArea m_AlarmArea;

    public Player(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_RemainingTime = Pustafin.LevelDuration;
        m_OnUpdateEverySecondTimer = 1f;
        m_SelectedWeapon = Weapons.SmallRocket;

        m_TouchEventStartPositions = new HashMap<Integer, Vector2D>();
        m_PlanetTouchColliderPaint = new Paint();
        m_PlanetTouchColliderPaint.setColor(Color.BLUE);
        m_PlanetTouchColliderPaint.setStyle(Paint.Style.STROKE);
        m_PlanetTouchColliderPaint.setStrokeWidth(2f);

        m_Clouds = new Sprite(gameView, positionX, positionY, AssetManager.getInstance().getAnimationSet(AnimationSets.Clouds));
        m_Clouds.setAnimationReversed(true);
        m_Clouds.setAnimationRepeatable(true);

        m_AlarmArea = new AlarmArea(gameView, positionX, positionY);
        m_AlarmArea.setCollider(new CircleCollider(m_AlarmArea, Pustafin.AlarmAreaRadius));
    }

    public void onUpdate() {
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

        if (!isVisible()) {
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
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
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

        float directionX = (float)(deltaPositionX / distance);
        float directionY = (float)(deltaPositionY / distance) * (-1); // Flip y direction

        float radius = m_Transform.getHalfScaleX();

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

        if (weapon != null) {
            Logger.D("Spawned selected weapon %s at (%f, %f) with direction (%f, %f), hit damage %f and speed %f",
                    m_SelectedWeapon.toString(), spawnPositionX, spawnPositionY, directionX, directionY, weapon.getHitDamage(), weapon.getSpeed());
        }

        if (weaponCount > 0) {
            weaponStockpile.setCount(--weaponCount);
            if (weaponCount == 0) {
                getGameView().getGameActivity().setHighlightedHotbarBoxToSmallRocketArea();
            }
        }
    }

    @Override
    public void onCollide(Collider collider) {
        super.onCollide(collider);

        if (getGameView().isLevelFinished()) {
            return;
        }

        if (collider.getOwner() instanceof Enemy && GameManager.getInstance().getPopulation() > 0) {
            Enemy enemy = (Enemy)collider.getOwner();

            SoundManager.getInstance().PlaySFX(R.raw.sfx_hit_planet, 0.5f + (float)Math.random());

            double remainingPopulation = GameManager.getInstance().getPopulation() - enemy.getHitDamage();
            if ((int)remainingPopulation <= 0f) {
                remainingPopulation = 0f;
            }

            GameManager.getInstance().setPopulation(remainingPopulation);

            if (remainingPopulation <= 0f) {
                getGameView().lose();
            }
        }
    }

    /*@Override
    public void onRender(Canvas canvas) {
        super.onRender(canvas);

        if (Pustafin.DebugMode && isVisible()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                float touchRadius = Pustafin.PlanetTouchRadius * SuperActivity.getScaleFactor();
                canvas.drawOval
                (
                    SuperActivity.getCurrentResolutionX() / 2f - touchRadius,
                    SuperActivity.getCurrentResolutionY() / 2f - touchRadius,
                    SuperActivity.getCurrentResolutionX() / 2f + touchRadius,
                    SuperActivity.getCurrentResolutionY() / 2f + touchRadius,
                    m_PlanetTouchColliderPaint
                );
            }
        }
    }*/

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (m_Clouds != null) {
            m_Clouds.setVisible(visible);
        }

        if (m_AlarmArea != null) {
            m_AlarmArea.setVisible(visible);
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
