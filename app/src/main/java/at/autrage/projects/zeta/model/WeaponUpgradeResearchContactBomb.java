package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.Pustafin;

public class WeaponUpgradeResearchContactBomb extends WeaponUpgrade {
    public WeaponUpgradeResearchContactBomb(String name, int level) {
        super(name, WeaponUpgrades.ResearchContactBomb, level, Pustafin.ResearchContactBombUpgradeMaxLevel);
    }

    @Override
    public int getUpgradeCostsForNextLevel() {
        return Pustafin.ResearchContactBombCost;
    }

    @Override
    public boolean isResearchable() {
        return true;
    }
}
