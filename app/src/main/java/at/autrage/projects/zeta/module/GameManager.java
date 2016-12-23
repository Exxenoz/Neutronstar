package at.autrage.projects.zeta.module;

import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.model.WeaponUpgrades;
import at.autrage.projects.zeta.model.Weapons;

public class GameManager {
    private static GameManager m_Instance;

    public static GameManager getInstance() {
        if (m_Instance == null) {
            m_Instance = new GameManager();
        }

        return m_Instance;
    }

    private int m_Level;

    private int m_Score;
    private int m_Money;

    private int m_Population;

    private Map<Weapons, Integer> m_Weapons;
    private Map<WeaponUpgrades, Integer> m_WeaponUpgrades;

    private GameManager() {
        m_Weapons = new HashMap<Weapons, Integer>();
        m_WeaponUpgrades = new HashMap<WeaponUpgrades, Integer>();

        reset();
    }

    public void reset() {
        m_Level = 1;

        m_Score = 0;
        m_Money = Pustafin.StartBudget;

        m_Population = Pustafin.StartPopulation;

        m_Weapons.clear();
        m_WeaponUpgrades.clear();

        m_Weapons.put(Weapons.SmallRocket, -1);
        m_Weapons.put(Weapons.BigRocket, 5);
    }

    public int getLevel() {
        return m_Level;
    }

    public void setLevel(int level) {
        this.m_Level = level;
    }

    public int getMoney() {
        return m_Money;
    }

    public void setMoney(int money) {
        this.m_Money = money;
    }

    public int getPopulation() {
        return m_Population;
    }

    public void setPopulation(int population) {
        this.m_Population = population;
    }

    public int getScore() {
        return m_Score;
    }

    public void setScore(int score) {
        this.m_Score = score;
    }

    public int getWeaponCount(Weapons weapon) {
        Integer count = m_Weapons.get(weapon);
        return (count != null) ? count : 0;
    }

    public void setWeaponCount(Weapons weapon, int count) {
        m_Weapons.put(weapon, count);
    }

    public int getWeaponUpgrade(WeaponUpgrades weaponUpgrade) {
        Integer upgrade = m_WeaponUpgrades.get(weaponUpgrade);
        return (upgrade != null) ? upgrade : 0;
    }
}
