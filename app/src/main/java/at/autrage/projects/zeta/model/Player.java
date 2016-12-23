package at.autrage.projects.zeta.model;


import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.module.UpdateFlags;
import at.autrage.projects.zeta.view.GameView;

public class Player extends GameObject implements View.OnTouchListener {
    private float m_RemainingTime;
    private float m_LastRemainingTime;
    private Weapons m_SelectedWeapon;

    private class Position {
        public float X;
        public float Y;
    }

    private Map<Integer, Position> m_TouchEventStartPositions;

    public Player(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_RemainingTime = Pustafin.LevelDuration;
        m_LastRemainingTime = m_RemainingTime;
        m_SelectedWeapon = Weapons.SmallRocket;

        m_TouchEventStartPositions = new HashMap<Integer, Position>();
    }

    public void onUpdate() {
        super.onUpdate();

        m_LastRemainingTime = m_RemainingTime;
        m_RemainingTime -= Time.getScaledDeltaTime();
        if (m_RemainingTime <= 0f) {
            m_RemainingTime = 0f;
        }

        if ((int)m_LastRemainingTime != (int)m_RemainingTime) {
            GameManager.getInstance().setUpdateFlag(UpdateFlags.Time);
            GameManager.getInstance().setUpdateFlag(UpdateFlags.FPS);
        }

        if (m_RemainingTime <= 0f) {
            win();
        }
    }

    /**
     * This method is called when the player swipes over the planet to launch a missile.
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Position pos = new Position();
                pos.X = event.getX();
                pos.Y = event.getY();
                m_TouchEventStartPositions.put(event.getActionIndex(), pos);
                return true; // if you want to handle the touch event
            }
            case MotionEvent.ACTION_UP: {
                Position pos = m_TouchEventStartPositions.get(event.getActionIndex());
                if (pos != null) {
                    onTouchRelease(event, pos);
                    m_TouchEventStartPositions.remove(event.getActionIndex());
                }
                return true; // if you want to handle the touch event
            }
        }
        return false;
    }

    public boolean onGlobalTouch(MotionEvent event) {
        // TODO: Implement LAZOR!
        return false;
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

        if (collider.getOwner() instanceof Enemy && GameManager.getInstance().getPopulation() > 0) {
            Enemy enemy = (Enemy)collider.getOwner();

            SoundManager.getInstance().PlaySFX(R.raw.sfx_hit_planet);

            int remainingPopulation = GameManager.getInstance().getPopulation() - (int)enemy.getHitDamage();
            if (remainingPopulation <= 0) {
                remainingPopulation = 0;
            }

            GameManager.getInstance().setPopulation(remainingPopulation);

            if (remainingPopulation <= 0) {
                loose();
            }
        }
    }

    public void win() {

    }

    public void loose() {
        GameManager.getInstance().reset();
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
