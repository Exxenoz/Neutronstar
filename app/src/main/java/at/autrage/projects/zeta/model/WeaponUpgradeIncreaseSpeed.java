package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.Pustafin;

public class WeaponUpgradeIncreaseSpeed extends WeaponUpgrade {
    public WeaponUpgradeIncreaseSpeed(String name, int level) {
        super(name, WeaponUpgrades.IncreaseSpeed, level, Pustafin.SpeedUpgradeMaxLevel);
    }

    @Override
    public int getUpgradeCostsForNextLevel() {
        return (int)(Pustafin.StartSpeedUpgradeCost * Math.pow(Pustafin.SpeedUpgradeCostIncreaseFactor, m_Level));
    }
}
