package at.autrage.projects.zeta.model;


import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.view.GameView;

public class Player extends GameObject implements View.OnTouchListener {
    private int m_Level;
    private float m_RemainingTime;

    private int m_Score;
    private int m_Money;

    private int m_Population;
    private float m_PopulationReg;

    private Map<Weapons, Integer> m_Weapons;
    private Map<WeaponUpgrades, Integer> m_WeaponUpgrades;
    private Weapons m_SelectedWeapon;

    private class Position {
        public float X;
        public float Y;
    }

    private Map<Integer, Position> m_TouchEventStartPositions;

    public Player(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_Level = 1;
        m_RemainingTime = Pustafin.LevelDuration;

        m_Score = 0;
        m_Money = Pustafin.StartBudget;

        m_Population = Pustafin.StartPopulation;
        m_PopulationReg = Pustafin.PopulationIncreaseFactor;

        m_Weapons = new HashMap<Weapons, Integer>();
        m_WeaponUpgrades = new HashMap<WeaponUpgrades, Integer>();
        m_SelectedWeapon = Weapons.SmallRocket;

        m_TouchEventStartPositions = new HashMap<Integer, Position>();

        m_Weapons.put(Weapons.SmallRocket, -1);
        m_Weapons.put(Weapons.BigRocket, 5);
    }

    public void onUpdate() {
        super.onUpdate();

        if (m_Population <= 0) {
            // Level failed!!! :,(
            return;
        }

        m_RemainingTime -= Time.getScaledDeltaTime();
        if (m_RemainingTime <= 0f) {
            m_RemainingTime = 0f;
            // Level finished!!!
            return;
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

        Integer weaponCount = m_Weapons.get(m_SelectedWeapon);
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
            m_Weapons.put(m_SelectedWeapon, --weaponCount);
            if (weaponCount == 0) {
                getGameView().getGameActivity().setHighlightedHotbarBoxToSmallRocketArea();
            }
        }
    }

    public int getLevel() {
        return m_Level;
    }

    public float getRemainingTime() {
        return m_RemainingTime;
    }

    public int getScore() {
        return m_Score;
    }

    public int getMoney() {
        return m_Money;
    }

    public int getPopulation() {
        return m_Population;
    }

    public int getWeaponCount(Weapons weapon) {
        Integer count = m_Weapons.get(weapon);
        if (count == null) {
            count = 0;
        }

        return count;
    }

    public int getWeaponUpgrade(WeaponUpgrades weaponUpgrade) {
        Integer state = m_WeaponUpgrades.get(weaponUpgrade);
        if (state == null) {
            state = 0;
        }

        return state;
    }

    public Weapons getSelectedWeapon() {
        return m_SelectedWeapon;
    }

    public void setScore(int score) {
        this.m_Score = score;
    }

    public void setMoney(int money) {
        this.m_Money = money;
    }

    public void setSelectedWeapon(Weapons selectedWeapon) {
        this.m_SelectedWeapon = selectedWeapon;
    }
}
