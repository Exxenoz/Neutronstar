package at.autrage.projects.zeta.model;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.module.UpdateFlags;
import at.autrage.projects.zeta.view.GameView;

public class Player extends GameObject {
    private float m_RemainingTime;
    private float m_LastRemainingTime;
    private Weapons m_SelectedWeapon;
    private float m_PopulationIncreaseTimer;

    private class Position {
        public float X;
        public float Y;
    }

    private Map<Integer, Position> m_TouchEventStartPositions;
    private Paint m_PlanetTouchColliderPaint;

    public Player(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_RemainingTime = Pustafin.LevelDuration;
        m_LastRemainingTime = m_RemainingTime;
        m_SelectedWeapon = Weapons.SmallRocket;

        m_TouchEventStartPositions = new HashMap<Integer, Position>();
        m_PlanetTouchColliderPaint = new Paint();
        m_PlanetTouchColliderPaint.setColor(Color.BLUE);
        m_PlanetTouchColliderPaint.setStyle(Paint.Style.STROKE);
        m_PlanetTouchColliderPaint.setStrokeWidth(2f);
    }

    public void onUpdate() {
        super.onUpdate();

        if (getGameView().isLevelFinished()) {
            return;
        }

        m_LastRemainingTime = m_RemainingTime;
        m_RemainingTime -= Time.getScaledDeltaTime();
        if (m_RemainingTime <= 0f) {
            m_RemainingTime = 0f;
        }


        m_PopulationIncreaseTimer += Time.getScaledDeltaTime();
        while (m_PopulationIncreaseTimer >= 1f) {
            GameManager.getInstance().setPopulation((1f + Pustafin.PopulationIncreaseFactor +
                    Pustafin.ProBabypillPopulationIncreaseFactor * GameManager.getInstance().getWeaponCount(Weapons.ProBabyPill))
                    * GameManager.getInstance().getPopulation());
            m_PopulationIncreaseTimer--;
        }

        if ((int)m_LastRemainingTime != (int)m_RemainingTime) {
            GameManager.getInstance().setUpdateFlag(UpdateFlags.Time);
            GameManager.getInstance().setUpdateFlag(UpdateFlags.FPS);
        }

        if (m_RemainingTime == 0f) {
            getGameView().win();
        }
    }

    public boolean onGlobalTouch(MotionEvent event) {
        if (getGameView().isLevelFinished()) {
            return false;
        }

        if (Time.getTimeScale() == 0f) {
            m_TouchEventStartPositions.clear();
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchRadius = Pustafin.PlanetTouchRadius * SuperActivity.getScaleFactor();

            float diffX = event.getX() - SuperActivity.getCurrentResolutionX() / 2f;
            float diffY = event.getY() - SuperActivity.getCurrentResolutionY() / 2f;

            float sqrDist = diffX * diffX + diffY * diffY;
            if (sqrDist <= touchRadius * touchRadius) {
                Position pos = new Position();
                pos.X = event.getX();
                pos.Y = event.getY();
                m_TouchEventStartPositions.put(event.getActionIndex(), pos);
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            Position pos = m_TouchEventStartPositions.get(event.getActionIndex());
            if (pos != null) {
                onTouchRelease(event, pos);
                m_TouchEventStartPositions.remove(event.getActionIndex());
            }
        }

        // TODO: Implement LAZOR!
        return true;
    }

    private void onTouchRelease(MotionEvent event, Position startTouchPosition) {
        if (m_SelectedWeapon == Weapons.SmallLaser ||
            m_SelectedWeapon == Weapons.BigLaser) {
            // Laser weapon handled in onGlobalTouch function
            return;
        }

        Integer weaponCount = GameManager.getInstance().getWeaponCount(m_SelectedWeapon);
        if (weaponCount == null || weaponCount == 0) {
            return;
        }

        float deltaPositionX = event.getX() - startTouchPosition.X;
        float deltaPositionY = event.getY() - startTouchPosition.Y;

        double distance = Math.sqrt(deltaPositionX * deltaPositionX + deltaPositionY * deltaPositionY);

        float directionX = (float)(deltaPositionX / distance);
        float directionY = (float)(deltaPositionY / distance);

        float radius = 128f;

        float spawnPositionX = 960 + directionX * radius;
        float spawnPositionY = 540 + directionY * radius;

        Logger.D("Spawn selected weapon %s at (%f, %f) with direction (%f, %f)",
                m_SelectedWeapon.toString(), spawnPositionX, spawnPositionY, directionX, directionY);

        switch (m_SelectedWeapon) {
            case SmallRocket:
                Rocket.createSmallRocket(this, spawnPositionX, spawnPositionY, directionX, directionY);
                break;
            case BigRocket:
                Rocket.createBigRocket(this, spawnPositionX, spawnPositionY, directionX, directionY);
                break;
            case SmallNuke:
                break;
            case BigNuke:
                break;
            case SmallContactBomb:
                break;
            case BigContactBomb:
                break;
        }

        if (weaponCount > 0) {
            GameManager.getInstance().setWeaponCount(m_SelectedWeapon, --weaponCount);
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
                getGameView().loose();
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Pustafin.DebugMode) {
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
    }

    public float getRemainingTime() {
        return m_RemainingTime;
    }

    public Weapons getSelectedWeapon() {
        return m_SelectedWeapon;
    }

    public void setSelectedWeapon(Weapons selectedWeapon) {
        this.m_SelectedWeapon = selectedWeapon;
    }
}
