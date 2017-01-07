package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.Pustafin;

public class WeaponUpgradeIncreaseRadius extends WeaponUpgrade {
    public WeaponUpgradeIncreaseRadius(String name, int level) {
        super(name, WeaponUpgrades.IncreaseRadius, level, Pustafin.RadiusUpgradeMaxLevel);
    }

    @Override
    public int getUpgradeCostsForNextLevel() {
        return (int)(Pustafin.StartRadiusUpgradeCost * Math.pow(Pustafin.RadiusUpgradeCostIncreaseFactor, m_Level));
    }
}
