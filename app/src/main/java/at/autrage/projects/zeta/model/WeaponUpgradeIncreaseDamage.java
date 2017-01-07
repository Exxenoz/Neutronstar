package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.Pustafin;

public class WeaponUpgradeIncreaseDamage extends WeaponUpgrade {
    public WeaponUpgradeIncreaseDamage(String name, int level) {
        super(name, WeaponUpgrades.IncreaseDamage, level, Pustafin.DamageUpgradeMaxLevel);
    }

    @Override
    public int getUpgradeCostsForNextLevel() {
        return (int)(Pustafin.StartDamageUpgradeCost * Math.pow(Pustafin.DamageUpgradeCostIncreaseFactor, m_Level));
    }
}
