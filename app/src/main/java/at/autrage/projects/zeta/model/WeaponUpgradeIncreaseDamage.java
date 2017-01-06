package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.Pustafin;

public class WeaponUpgradeIncreaseDamage extends WeaponUpgrade {
    public WeaponUpgradeIncreaseDamage(int level) {
        super(WeaponUpgrades.IncreaseDamage, level, Pustafin.DamageUpgradeMaxLevel);
    }

    @Override
    public int getUpgradeCostsForNextLevel() {
        return (int)(Pustafin.StartDamageUpgradeCost * Math.pow(Pustafin.DamageUpgradeCostIncreaseFactor, m_Level));
    }
}
