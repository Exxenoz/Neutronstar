package at.autrage.projects.zeta.module;

import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.model.Player;
import at.autrage.projects.zeta.model.WeaponStockpile;
import at.autrage.projects.zeta.model.WeaponUpgrade;
import at.autrage.projects.zeta.model.WeaponUpgradeIncreaseDamage;
import at.autrage.projects.zeta.model.WeaponUpgradeIncreaseRadius;
import at.autrage.projects.zeta.model.WeaponUpgradeIncreaseSpeed;
import at.autrage.projects.zeta.model.WeaponUpgradeResearchContactBomb;
import at.autrage.projects.zeta.model.WeaponUpgradeResearchLaser;
import at.autrage.projects.zeta.model.WeaponUpgradeResearchNuke;
import at.autrage.projects.zeta.model.WeaponUpgrades;
import at.autrage.projects.zeta.model.Weapons;
import at.autrage.projects.zeta.persistence.HighscoreTable;
import at.autrage.projects.zeta.persistence.HighscoreTableEntry;
import at.autrage.projects.zeta.util.Util;
import at.autrage.projects.zeta.view.GameView;

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

    private Map<Weapons, WeaponStockpile> m_WeaponStockpiles;
    private Map<WeaponUpgrades, WeaponUpgrade> m_WeaponUpgrades;
    private Map<Weapons, Float> m_WeaponBaseHitDamages;
    private Map<Weapons, Float> m_WeaponHitDamages;
    private Map<Weapons, Float> m_WeaponBaseSpeeds;
    private Map<Weapons, Float> m_WeaponSpeeds;
    private Map<Weapons, Float> m_WeaponBaseRadii;
    private Map<Weapons, Float> m_WeaponRadii;

    private boolean m_TutorialMode;

    private GameManager() {
        m_WeaponStockpiles = new HashMap<Weapons, WeaponStockpile>();
        m_WeaponUpgrades = new HashMap<WeaponUpgrades, WeaponUpgrade>();
        m_WeaponBaseHitDamages = new HashMap<Weapons, Float>();
        m_WeaponHitDamages = new HashMap<Weapons, Float>();
        m_WeaponBaseSpeeds = new HashMap<Weapons, Float>();
        m_WeaponSpeeds = new HashMap<Weapons, Float>();
        m_WeaponBaseRadii = new HashMap<Weapons, Float>();
        m_WeaponRadii = new HashMap<Weapons, Float>();
    }

    public void reset(SuperActivity superActivity) {
        m_UpdateFlags = UpdateFlags.All;

        m_Level = 1;

        m_Score = 0;
        m_Money = Pustafin.StartBudget;

        m_Population = Pustafin.StartPopulation;

        m_WeaponStockpiles.clear();
        m_WeaponUpgrades.clear();
        m_WeaponBaseHitDamages.clear();
        m_WeaponHitDamages.clear();
        m_WeaponBaseSpeeds.clear();
        m_WeaponSpeeds.clear();
        m_WeaponBaseRadii.clear();
        m_WeaponRadii.clear();

        m_WeaponStockpiles.put(Weapons.SmallRocket, new WeaponStockpile(String.format(superActivity.getString(R.string.sv_desc_small_rocket), Pustafin.SmallRocketPacketSize), Weapons.SmallRocket, Pustafin.SmallRocketStartCount, Pustafin.SmallRocketPacketCost, Pustafin.SmallRocketPacketSize, WeaponUpgrades.None, UpdateFlags.SmallRocketCount));
        m_WeaponStockpiles.put(Weapons.BigRocket, new WeaponStockpile(String.format(superActivity.getString(R.string.sv_desc_big_rocket), Pustafin.BigRocketPacketSize), Weapons.BigRocket, Pustafin.BigRocketStartCount, Pustafin.BigRocketPacketCost, Pustafin.BigRocketPacketSize, WeaponUpgrades.None, UpdateFlags.BigRocketCount));
        m_WeaponStockpiles.put(Weapons.SmallNuke, new WeaponStockpile(String.format(superActivity.getString(R.string.sv_desc_small_nuke), Pustafin.SmallNukePacketSize), Weapons.SmallNuke, Pustafin.SmallNukeStartCount, Pustafin.SmallNukePacketCost, Pustafin.SmallNukePacketSize, WeaponUpgrades.ResearchNuke, UpdateFlags.SmallNukeCount));
        m_WeaponStockpiles.put(Weapons.BigNuke, new WeaponStockpile(String.format(superActivity.getString(R.string.sv_desc_big_nuke), Pustafin.BigNukePacketSize), Weapons.BigNuke, Pustafin.BigNukeStartCount, Pustafin.BigNukePacketCost, Pustafin.BigNukePacketSize, WeaponUpgrades.ResearchNuke, UpdateFlags.BigNukeCount));
        // ToDo: Implement Laser and Contact Bomb
        //m_WeaponStockpiles.put(Weapons.SmallContactBomb, new WeaponStockpile(String.format(superActivity.getString(R.string.sv_desc_small_contact_bomb), Pustafin.SmallContactBombPacketSize), Weapons.SmallContactBomb, Pustafin.SmallContactBombStartCount, Pustafin.SmallContactBombPacketCost, Pustafin.SmallContactBombPacketSize, WeaponUpgrades.ResearchContactBomb, UpdateFlags.SmallContactBombCount));
        //m_WeaponStockpiles.put(Weapons.BigContactBomb, new WeaponStockpile(String.format(superActivity.getString(R.string.sv_desc_big_contact_bomb), Pustafin.BigContactBombPacketSize), Weapons.BigContactBomb, Pustafin.BigContactBombStartCount, Pustafin.BigContactBombPacketCost, Pustafin.BigContactBombPacketSize, WeaponUpgrades.ResearchContactBomb, UpdateFlags.BigContactBombCount));
        m_WeaponStockpiles.put(Weapons.ProBabyPill, new WeaponStockpile(String.format(superActivity.getString(R.string.sv_desc_pro_babypill), Pustafin.ProBabypillPacketSize), Weapons.ProBabyPill, Pustafin.ProBabypillStartCount, Pustafin.ProBabypillPacketCost, Pustafin.ProBabypillPacketSize, WeaponUpgrades.None, UpdateFlags.None));

        m_WeaponUpgrades.put(WeaponUpgrades.IncreaseDamage, new WeaponUpgradeIncreaseDamage(superActivity.getString(R.string.sv_desc_increase_damage), Pustafin.DamageUpgradeStartLevel));
        m_WeaponUpgrades.put(WeaponUpgrades.IncreaseSpeed, new WeaponUpgradeIncreaseSpeed(superActivity.getString(R.string.sv_desc_increase_speed), Pustafin.SpeedUpgradeStartLevel));
        m_WeaponUpgrades.put(WeaponUpgrades.IncreaseRadius, new WeaponUpgradeIncreaseRadius(superActivity.getString(R.string.sv_desc_increase_radius), Pustafin.RadiusUpgradeStartLevel));
        m_WeaponUpgrades.put(WeaponUpgrades.ResearchNuke, new WeaponUpgradeResearchNuke(superActivity.getString(R.string.sv_desc_research_nuke), Pustafin.ResearchNukeUpgradeStartLevel));
        m_WeaponUpgrades.put(WeaponUpgrades.ResearchLaser, new WeaponUpgradeResearchLaser(superActivity.getString(R.string.sv_desc_research_laser), Pustafin.ResearchLaserUpgradeStartLevel));
        m_WeaponUpgrades.put(WeaponUpgrades.ResearchContactBomb, new WeaponUpgradeResearchContactBomb(superActivity.getString(R.string.sv_desc_research_contact_bomb), Pustafin.ResearchContactBombUpgradeStartLevel));

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

        m_TutorialMode = false;
    }

    public void onStartGame(SuperActivity activity) {
        SoundManager.getInstance().PlaySFX(R.raw.sfx_drumhits_next_level);

        reset(activity);
    }

    public void onStartTutorialGame(SuperActivity activity) {
        SoundManager.getInstance().PlaySFX(R.raw.sfx_drumhits_next_level);

        reset(activity);
        setTutorialMode(true);
    }

    public void onStartNextLevel() {
        SoundManager.getInstance().PlaySFX(R.raw.sfx_drumhits_next_level);

        setLevel(getLevel() + 1);

        updateWeaponHitDamages();
        updateWeaponSpeeds();
        updateWeaponRadii();
    }

    public void onWin(GameView gameView, Player player) {
        // Calculate remaining population increase
        setPopulation(GameManager.getInstance().getPopulation() * Math.pow(1f + Pustafin.PopulationIncreaseFactor +
                Pustafin.ProBabypillPopulationIncreaseFactor * getWeaponCount(Weapons.ProBabyPill), Util.roof(player.getRemainingTime())));
        setWeaponCount(Weapons.ProBabyPill, 0);

        SoundManager.getInstance().PlaySFX(R.raw.sfx_ending_win);
    }

    public void onLose(GameView gameView, Player player) {
        SoundManager.getInstance().PlaySFX(R.raw.sfx_ending_loose);

        Database.getInstance().open();

        HighscoreTable table = (HighscoreTable)Database.getTable(Database.Tables.HighscoreTable);

        // Add highscore entry
        HighscoreTableEntry highscoreTableEntry = new HighscoreTableEntry(table);
        highscoreTableEntry.Level = getLevel();
        highscoreTableEntry.Score = getScore();
        highscoreTableEntry.Date = (int)(System.currentTimeMillis() / 1000);
        Database.getInstance().insertTableEntry(highscoreTableEntry);

        Database.getInstance().close();
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

    public WeaponStockpile getWeaponStockpile(Weapons weapon) {
        return m_WeaponStockpiles.get(weapon);
    }

    public int getWeaponCount(Weapons weapon) {
        WeaponStockpile weaponStockpile = m_WeaponStockpiles.get(weapon);
        if (weaponStockpile == null) {
            return 0;
        }

        return weaponStockpile.getCount();
    }

    public void setWeaponCount(Weapons weapon, int count) {
        WeaponStockpile weaponStockpile = m_WeaponStockpiles.get(weapon);
        if (weaponStockpile == null) {
            return;
        }

        weaponStockpile.setCount(count);
    }

    public boolean isWeaponUpgradeResearched(WeaponUpgrades weaponUpgrade) {
        if (weaponUpgrade == WeaponUpgrades.None) {
            return true;
        }

        WeaponUpgrade weaponUpgradeObj = m_WeaponUpgrades.get(weaponUpgrade);
        return weaponUpgradeObj != null && weaponUpgradeObj.isResearched();
    }

    public WeaponUpgrade getWeaponUpgrade(WeaponUpgrades weaponUpgrade) {
        return m_WeaponUpgrades.get(weaponUpgrade);
    }

    public void updateWeaponHitDamages() {
        WeaponUpgrade weaponUpgrade = getWeaponUpgrade(WeaponUpgrades.IncreaseDamage);
        int level = (weaponUpgrade != null) ? weaponUpgrade.getLevel() : 0;

        for (Weapons weapon : m_WeaponBaseHitDamages.keySet()) {
            setWeaponHitDamage(weapon, (float) (getWeaponBaseHitDamage(weapon) * Math.pow(Pustafin.DamageUpgradeIncreaseFactor, level)));
        }
    }

    public void updateWeaponSpeeds() {
        WeaponUpgrade weaponUpgrade = getWeaponUpgrade(WeaponUpgrades.IncreaseSpeed);
        int level = (weaponUpgrade != null) ? weaponUpgrade.getLevel() : 0;

        for (Weapons weapon : m_WeaponBaseSpeeds.keySet()) {
            setWeaponSpeed(weapon, (float) (getWeaponBaseSpeed(weapon) * Math.pow(Pustafin.SpeedUpgradeIncreaseFactor, level)));
        }
    }

    public void updateWeaponRadii() {
        WeaponUpgrade weaponUpgrade = getWeaponUpgrade(WeaponUpgrades.IncreaseRadius);
        int level = (weaponUpgrade != null) ? weaponUpgrade.getLevel() : 0;

        for (Weapons weapon : m_WeaponBaseRadii.keySet()) {
            setWeaponRadius(weapon, (float) (getWeaponBaseRadius(weapon) * Math.pow(Pustafin.RadiusUpgradeIncreaseFactor, level)));
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

    public boolean isTutorialMode() {
        return m_TutorialMode;
    }

    public void setTutorialMode(boolean tutorialMode) {
        this.m_TutorialMode = tutorialMode;
    }
}
