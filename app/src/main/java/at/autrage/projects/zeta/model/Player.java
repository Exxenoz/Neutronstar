package at.autrage.projects.zeta.model;


import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;

public class Player {
    private int m_Level;
    private float m_RemainingTime;

    private int m_Score;
    private int m_Money;

    private int m_Population;
    private float m_PopulationReg;

    private Map<Weapons, Integer> m_Weapons;
    private Map<WeaponUpgrades, Integer> m_WeaponUpgrades;
    private Weapons m_SelectedWeapon;

    public Player() {
        m_Level = 1;
        m_RemainingTime = Pustafin.LevelDuration;

        m_Score = 0;
        m_Money = Pustafin.StartBudget;

        m_Population = Pustafin.StartPopulation;
        m_PopulationReg = Pustafin.BasePopulationRegeneration;

        m_Weapons = new HashMap<Weapons, Integer>();
        m_WeaponUpgrades = new HashMap<WeaponUpgrades, Integer>();
        m_SelectedWeapon = Weapons.SmallRocket;

        m_Weapons.put(Weapons.SmallRocket, -1);
    }

    public void onUpdate() {


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

    public void setSelectedWeapon(Weapons selectedWeapon) {
        this.m_SelectedWeapon = selectedWeapon;
    }
}
