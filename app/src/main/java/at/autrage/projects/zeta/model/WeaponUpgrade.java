package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.GameManager;

public abstract class WeaponUpgrade {
    protected WeaponUpgrades m_Upgrade;
    protected int m_Level;
    protected int m_MaxLevel;

    public WeaponUpgrade(WeaponUpgrades upgrade, int level, int maxLevel) {
        this.m_Upgrade = upgrade;
        this.m_Level = level;
        this.m_MaxLevel = maxLevel;
    }

    public boolean upgrade() {
        int nextLevel = m_Level + 1;

        if (nextLevel > m_MaxLevel) {
            return false;
        }

        int price = getUpgradeCostsForNextLevel();

        if (GameManager.getInstance().getMoney() < price) {
            return false;
        }

        GameManager.getInstance().setMoney(GameManager.getInstance().getMoney() - price);
        setLevel(nextLevel);

        return true;
    }

    public boolean downgrade() {
        int lastLevel = m_Level - 1;

        if (lastLevel < 0) {
            return false;
        }

        setLevel(lastLevel);
        GameManager.getInstance().setMoney(GameManager.getInstance().getMoney() + getUpgradeCostsForNextLevel());

        return true;
    }

    public abstract int getUpgradeCostsForNextLevel();

    public WeaponUpgrades getUpgrade() {
        return m_Upgrade;
    }

    private void setUpgrade(WeaponUpgrades upgrade) {
        this.m_Upgrade = upgrade;
    }

    public boolean isResearchable() { return false; }

    public boolean isResearched() {
        return m_Level > 0;
    }

    public int getLevel() {
        return m_Level;
    }

    private void setLevel(int level) {
        this.m_Level = level;
    }

    public int getMaxLevel() {
        return m_MaxLevel;
    }

    private void setMaxLevel(int maxLevel) {
        this.m_MaxLevel = maxLevel;
    }
}
