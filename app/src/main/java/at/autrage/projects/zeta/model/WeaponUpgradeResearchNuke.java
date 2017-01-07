package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.Pustafin;

public class WeaponUpgradeResearchNuke extends WeaponUpgrade {
    public WeaponUpgradeResearchNuke(String name, int level) {
        super(name, WeaponUpgrades.ResearchNuke, level, Pustafin.ResearchNukeUpgradeMaxLevel);
    }

    @Override
    public int getUpgradeCostsForNextLevel() {
        return Pustafin.ResearchNukeCost;
    }

    @Override
    public boolean isResearchable() {
        return true;
    }
}
