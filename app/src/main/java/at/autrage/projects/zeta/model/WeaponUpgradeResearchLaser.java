package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.Pustafin;

public class WeaponUpgradeResearchLaser extends WeaponUpgrade {
    public WeaponUpgradeResearchLaser(int level) {
        super(WeaponUpgrades.ResearchLaser, level, Pustafin.ResearchLaserUpgradeMaxLevel);
    }

    @Override
    public int getUpgradeCostsForNextLevel() {
        return Pustafin.ResearchLaserCost;
    }

    @Override
    public boolean isResearchable() {
        return true;
    }
}
