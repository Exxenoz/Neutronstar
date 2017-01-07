package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.GameManager;

public class WeaponStockpile implements IShopItem {
    private String m_Name;
    private Weapons m_Weapon;
    private int m_Count;
    private int m_PacketSize;
    private int m_PacketCosts;
    private WeaponUpgrades m_RequiredWeaponUpgrade;
    private int m_UpdateFlag;

    public WeaponStockpile(String name, Weapons weapon, int count, int packetCosts, int packetSize, WeaponUpgrades requiredWeaponUpgrade, int updateFlag) {
        this.m_Name = name;
        this.m_Weapon = weapon;
        this.m_Count = count;
        this.m_PacketCosts = packetCosts;
        this.m_PacketSize = packetSize;
        this.m_RequiredWeaponUpgrade = requiredWeaponUpgrade;
        this.m_UpdateFlag = updateFlag;
    }

    public boolean buy() {
        if (!GameManager.getInstance().isWeaponUpgradeResearched(m_RequiredWeaponUpgrade)) {
            return false;
        }

        if (GameManager.getInstance().getMoney() < m_PacketCosts) {
            return false;
        }

        GameManager.getInstance().setMoney(GameManager.getInstance().getMoney() - m_PacketCosts);
        setCount(m_Count + m_PacketSize);

        return true;
    }

    public boolean sell() {
        if (m_Count <= 0 ||
            m_Count < m_PacketSize) {
            return false;
        }

        GameManager.getInstance().setMoney(GameManager.getInstance().getMoney() + m_PacketCosts);
        setCount(m_Count - m_PacketSize);

        return true;
    }

    public String getName() {
        return m_Name;
    }

    private void setName(String name) {
        this.m_Name = name;
    }

    public Weapons getWeapon() {
        return m_Weapon;
    }

    private void setWeapon(Weapons weapon) {
        this.m_Weapon = weapon;
    }

    public int getCount() {
        return m_Count;
    }

    public void setCount(int count) {
        if (count < 0) {
            count = 0;
        }

        this.m_Count = count;

        GameManager.getInstance().setUpdateFlag(m_UpdateFlag);
    }

    public int getPacketCosts() {
        return m_PacketCosts;
    }

    private void setPacketCosts(int packetCosts) {
        this.m_PacketCosts = packetCosts;
    }

    public int getPacketSize() {
        return m_PacketSize;
    }

    private void setPacketSize(int packetSize) {
        this.m_PacketSize = packetSize;
    }

    public void setRequiredWeaponUpgrade(WeaponUpgrades requiredWeaponUpgrade) {
        this.m_RequiredWeaponUpgrade = requiredWeaponUpgrade;
    }

    public WeaponUpgrades getRequiredWeaponUpgrade() {
        return m_RequiredWeaponUpgrade;
    }
}
