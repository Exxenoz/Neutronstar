package at.autrage.projects.zeta.module;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.model.Weapon;
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

    /**
     * A flagged state means that the UI must be updated for the corresponding view.
     */
    private int m_UpdateFlags;

    private int m_Level;

    private int m_Score;
    private int m_Money;

    private double m_Population;

    private Map<Weapons, Integer> m_Weapons;
    private Map<WeaponUpgrades, Integer> m_WeaponUpgrades;
    private Map<Weapons, Float> m_WeaponBaseHitDamages;
    private Map<Weapons, Float> m_WeaponHitDamages;
    private Map<Weapons, Float> m_WeaponBaseSpeeds;
    private Map<Weapons, Float> m_WeaponSpeeds;
    private Map<Weapons, Float> m_WeaponBaseRadii;
    private Map<Weapons, Float> m_WeaponRadii;

    private GameManager() {
        m_Weapons = new HashMap<Weapons, Integer>();
        m_WeaponUpgrades = new HashMap<WeaponUpgrades, Integer>();
        m_WeaponBaseHitDamages = new HashMap<Weapons, Float>();
        m_WeaponHitDamages = new HashMap<Weapons, Float>();
        m_WeaponBaseSpeeds = new HashMap<Weapons, Float>();
        m_WeaponSpeeds = new HashMap<Weapons, Float>();
        m_WeaponBaseRadii = new HashMap<Weapons, Float>();
        m_WeaponRadii = new HashMap<Weapons, Float>();

        reset();
    }

    public void reset() {
        m_UpdateFlags = UpdateFlags.All;

        m_Level = 1;

        m_Score = 0;
        m_Money = Pustafin.StartBudget;

        m_Population = Pustafin.StartPopulation;

        m_Weapons.clear();
        m_WeaponUpgrades.clear();
        m_WeaponBaseHitDamages.clear();
        m_WeaponHitDamages.clear();
        m_WeaponBaseSpeeds.clear();
        m_WeaponSpeeds.clear();
        m_WeaponBaseRadii.clear();
        m_WeaponRadii.clear();

        m_Weapons.put(Weapons.SmallRocket, -1);
        m_Weapons.put(Weapons.BigRocket, 5);

        setWeaponBaseHitDamage(Weapons.SmallRocket, Pustafin.SmallRocketHitDamageBase);
        setWeaponBaseHitDamage(Weapons.BigRocket, Pustafin.BigRocketHitDamageBase);
        setWeaponBaseHitDamage(Weapons.SmallNuke, Pustafin.SmallNukeHitDamageBase);
        setWeaponBaseHitDamage(Weapons.BigNuke, Pustafin.BigNukeHitDamageBase);
        setWeaponBaseHitDamage(Weapons.SmallLaser, Pustafin.SmallLaserHitDamageBase);
        setWeaponBaseHitDamage(Weapons.BigLaser, Pustafin.BigLaserHitDamageBase);
        setWeaponBaseHitDamage(Weapons.SmallContactBomb, Pustafin.SmallContactBombHitDamageBase);
        setWeaponBaseHitDamage(Weapons.BigContactBomb, Pustafin.BigContactBombHitDamageBase);

        setWeaponBaseSpeed(Weapons.SmallRocket, Pustafin.SmallRocketSpeedBase);
        setWeaponBaseSpeed(Weapons.BigRocket, Pustafin.BigRocketSpeedBase);
        setWeaponBaseSpeed(Weapons.SmallNuke, Pustafin.SmallNukeSpeedBase);
        setWeaponBaseSpeed(Weapons.BigNuke, Pustafin.BigNukeSpeedBase);
        setWeaponBaseSpeed(Weapons.SmallContactBomb, Pustafin.SmallContactBombSpeedBase);
        setWeaponBaseSpeed(Weapons.BigContactBomb, Pustafin.BigContactBombSpeedBase);

        setWeaponBaseRadius(Weapons.SmallNuke, Pustafin.SmallNukeRadiusBase);
        setWeaponBaseRadius(Weapons.BigNuke, Pustafin.BigNukeRadiusBase);
        setWeaponBaseRadius(Weapons.SmallContactBomb, Pustafin.SmallContactBombRadiusBase);
        setWeaponBaseRadius(Weapons.BigContactBomb, Pustafin.BigContactBombRadiusBase);

        for (Map.Entry<Weapons, Float> pair : m_WeaponBaseHitDamages.entrySet()) {
            setWeaponHitDamage(pair.getKey(), pair.getValue());
        }

        for (Map.Entry<Weapons, Float> pair : m_WeaponBaseSpeeds.entrySet()) {
            setWeaponSpeed(pair.getKey(), pair.getValue());
        }

        for (Map.Entry<Weapons, Float> pair : m_WeaponBaseRadii.entrySet()) {
            setWeaponRadius(pair.getKey(), pair.getValue());
        }
    }

    public void setUpdateFlag(int updateFlag) {
        m_UpdateFlags |= updateFlag;
    }

    public boolean hasUpdateFlag(int updateFlag) {
        return (m_UpdateFlags & updateFlag) == updateFlag;
    }

    public void delUpdateFlag(int updateFlag) {
        m_UpdateFlags &= ~updateFlag;
    }

    public int getLevel() {
        return m_Level;
    }

    public void setLevel(int level) {
        m_Level = level;
        setUpdateFlag(UpdateFlags.Level);
    }

    public int getMoney() {
        return m_Money;
    }

    public void setMoney(int money) {
        m_Money = money;
        setUpdateFlag(UpdateFlags.Money);
    }

    public double getPopulation() {
        return m_Population;
    }

    public void setPopulation(double population) {
        m_Population = population;
        setUpdateFlag(UpdateFlags.Population);
    }

    public int getScore() {
        return m_Score;
    }

    public void setScore(int score) {
        m_Score = score;
        setUpdateFlag(UpdateFlags.Score);
    }

    public int getWeaponCount(Weapons weapon) {
        Integer count = m_Weapons.get(weapon);
        return (count != null) ? count : 0;
    }

    public void setWeaponCount(Weapons weapon, int count) {
        m_Weapons.put(weapon, count);

        switch (weapon) {
            case SmallRocket:
                setUpdateFlag(UpdateFlags.SmallRocketCount);
                break;
            case BigRocket:
                setUpdateFlag(UpdateFlags.BigRocketCount);
                break;
            case SmallNuke:
                setUpdateFlag(UpdateFlags.SmallNukeCount);
                break;
            case BigNuke:
                setUpdateFlag(UpdateFlags.BigNukeCount);
                break;
            case SmallLaser:
                setUpdateFlag(UpdateFlags.SmallLaserCount);
                break;
            case BigLaser:
                setUpdateFlag(UpdateFlags.BigLaserCount);
                break;
            case SmallContactBomb:
                setUpdateFlag(UpdateFlags.SmallContactBombCount);
                break;
            case BigContactBomb:
                setUpdateFlag(UpdateFlags.BigContactBombCount);
                break;
            default:
                Logger.E("Could not set update flag for weapon " + weapon + ", because it is not defined!");
                break;
        }
    }

    public int getWeaponUpgrade(WeaponUpgrades weaponUpgrade) {
        Integer upgrade = m_WeaponUpgrades.get(weaponUpgrade);
        return (upgrade != null) ? upgrade : 0;
    }

    public void setWeaponUpgradeLevel(WeaponUpgrades weaponUpgrade, int level) {
        m_WeaponUpgrades.put(weaponUpgrade, level);
        if (weaponUpgrade == WeaponUpgrades.IncreaseDamage) {
            for (Weapons weapon : m_WeaponBaseHitDamages.keySet()) {
                setWeaponHitDamage(weapon, (float) (getWeaponBaseHitDamage(weapon) * Math.pow(Pustafin.DamageUpgradeIncreaseFactor, level)));
            }
        }
        else if (weaponUpgrade == WeaponUpgrades.IncreaseSpeed) {
            for (Weapons weapon : m_WeaponBaseSpeeds.keySet()) {
                setWeaponSpeed(weapon, (float) (getWeaponBaseSpeed(weapon) * Math.pow(Pustafin.SpeedUpgradeIncreaseFactor, level)));
            }
        }
        else if (weaponUpgrade == WeaponUpgrades.IncreaseRadius) {
            for (Weapons weapon : m_WeaponBaseRadii.keySet()) {
                setWeaponRadius(weapon, (float) (getWeaponBaseRadius(weapon) * Math.pow(Pustafin.RadiusUpgradeIncreaseFactor, level)));
            }
        }
    }

    public float getWeaponBaseHitDamage(Weapons weapon) {
        Float hitDamage = m_WeaponBaseHitDamages.get(weapon);
        return (hitDamage != null) ? hitDamage : 0;
    }

    public void setWeaponBaseHitDamage(Weapons weapon, float hitDamage) {
        m_WeaponBaseHitDamages.put(weapon, hitDamage);
    }

    public float getWeaponHitDamage(Weapons weapon) {
        Float hitDamage = m_WeaponHitDamages.get(weapon);
        return (hitDamage != null) ? hitDamage : 0;
    }

    public void setWeaponHitDamage(Weapons weapon, float hitDamage) {
        m_WeaponHitDamages.put(weapon, hitDamage);
    }

    public float getWeaponBaseSpeed(Weapons weapon) {
        Float speed = m_WeaponBaseSpeeds.get(weapon);
        return (speed != null) ? speed : 0;
    }

    public void setWeaponBaseSpeed(Weapons weapon, float speed) {
        m_WeaponBaseSpeeds.put(weapon, speed);
    }

    public float getWeaponSpeed(Weapons weapon) {
        Float speed = m_WeaponSpeeds.get(weapon);
        return (speed != null) ? speed : 0;
    }

    public void setWeaponSpeed(Weapons weapon, float speed) {
        m_WeaponSpeeds.put(weapon, speed);
    }

    public float getWeaponBaseRadius(Weapons weapon) {
        Float radius = m_WeaponBaseRadii.get(weapon);
        return (radius != null) ? radius : 0;
    }

    public void setWeaponBaseRadius(Weapons weapon, float radius) {
        m_WeaponBaseRadii.put(weapon, radius);
    }

    public float getWeaponRadius(Weapons weapon) {
        Float radius = m_WeaponRadii.get(weapon);
        return (radius != null) ? radius : 0;
    }

    public void setWeaponRadius(Weapons weapon, float radius) {
        m_WeaponRadii.put(weapon, radius);
    }
}
