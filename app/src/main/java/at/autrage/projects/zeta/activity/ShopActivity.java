package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.model.WeaponUpgrades;
import at.autrage.projects.zeta.model.Weapons;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Util;

public class ShopActivity extends SuperActivity {
    private GameManager m_GameManager;
    private TextView m_TxtViewMoneyDisplay;
    private boolean m_Finished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        m_GameManager = GameManager.getInstance();

        initializeShop();

        Button btnAreaNextLevel = (Button)findViewById(R.id.btnAreaShopNextLevel);
        btnAreaNextLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNextLevel();
            }
        });

        Button btnAreaNextLevelIcon = (Button)findViewById(R.id.btnAreaShopNextLevelIcon);
        btnAreaNextLevelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNextLevel();
            }
        });

        m_Finished = false;

        scaleChildViewsToCurrentResolution((ViewGroup)findViewById(R.id.activity_shop));
    }

    @Override
    protected void onResume() {
        super.onResume();

        m_CurrentActivity = Activities.ShopActivity;
    }

    private void initializeShop() {
        m_TxtViewMoneyDisplay = (TextView)findViewById(R.id.txtViewShopMoney);
        m_TxtViewMoneyDisplay.setText(String.format(getString(R.string.sv_money_display), Util.addLeadingZeros(m_GameManager.getMoney(), 5, true)));

        TextView txtViewDescBigRocket = (TextView)findViewById(R.id.txtViewShopDescBigRocket);
        TextView txtViewDescSmallNuke = (TextView)findViewById(R.id.txtViewShopDescSmallNuke);
        TextView txtViewDescBigNuke = (TextView)findViewById(R.id.txtViewShopDescBigNuke);
        TextView txtViewDescSmallContactBomb = (TextView)findViewById(R.id.txtViewShopDescSmallContactBomb);
        TextView txtViewDescBigContactBomb = (TextView)findViewById(R.id.txtViewShopDescBigContactBomb);
        TextView txtViewDescProBabypill = (TextView)findViewById(R.id.txtViewShopDescProBabyPill);

        txtViewDescBigRocket.setText(String.format(getString(R.string.sv_desc_big_rocket), Pustafin.BigRocketPacketSize));
        txtViewDescSmallNuke.setText(String.format(getString(R.string.sv_desc_small_nuke), Pustafin.SmallNukePacketSize));
        txtViewDescBigNuke.setText(String.format(getString(R.string.sv_desc_big_nuke), Pustafin.BigNukePacketSize));
        txtViewDescSmallContactBomb.setText(String.format(getString(R.string.sv_desc_small_contact_bomb), Pustafin.SmallContactBombPacketSize));
        txtViewDescBigContactBomb.setText(String.format(getString(R.string.sv_desc_big_contact_bomb), Pustafin.BigContactBombPacketSize));
        txtViewDescProBabypill.setText(String.format(getString(R.string.sv_desc_pro_babypill), Pustafin.ProBabypillPacketSize));

        TextView txtViewPriceBigRocket = (TextView)findViewById(R.id.txtViewShopPriceBigRocket);
        TextView txtViewPriceSmallNuke = (TextView)findViewById(R.id.txtViewShopPriceSmallNuke);
        TextView txtViewPriceBigNuke = (TextView)findViewById(R.id.txtViewShopPriceBigNuke);
        TextView txtViewPriceSmallContactBomb = (TextView)findViewById(R.id.txtViewShopPriceSmallContactBomb);
        TextView txtViewPriceBigContactBomb = (TextView)findViewById(R.id.txtViewShopPriceBigContactBomb);
        TextView txtViewPriceProBabypill = (TextView)findViewById(R.id.txtViewShopPriceProBabyPill);

        txtViewPriceBigRocket.setText(String.format(getString(R.string.sv_price_count_display), Pustafin.BigRocketPacketCost, m_GameManager.getWeaponCount(Weapons.BigRocket)));
        txtViewPriceSmallNuke.setText(String.format(getString(R.string.sv_price_count_display), Pustafin.SmallNukePacketCost, m_GameManager.getWeaponCount(Weapons.SmallNuke)));
        txtViewPriceBigNuke.setText(String.format(getString(R.string.sv_price_count_display), Pustafin.BigNukePacketCost, m_GameManager.getWeaponCount(Weapons.BigNuke)));
        txtViewPriceSmallContactBomb.setText(String.format(getString(R.string.sv_price_count_display), Pustafin.SmallContactBombPacketCost, m_GameManager.getWeaponCount(Weapons.SmallContactBomb)));
        txtViewPriceBigContactBomb.setText(String.format(getString(R.string.sv_price_count_display), Pustafin.BigContactBombPacketCost, m_GameManager.getWeaponCount(Weapons.BigContactBomb)));
        txtViewPriceProBabypill.setText(String.format(getString(R.string.sv_price_count_display), Pustafin.ProBabypillPacketCost, m_GameManager.getWeaponUpgrade(WeaponUpgrades.ProBabyPill)));

        TextView txtViewPriceIncreaseDamage = (TextView)findViewById(R.id.txtViewShopPriceIncreaseDamage);
        TextView txtViewPriceIncreaseSpeed = (TextView)findViewById(R.id.txtViewShopPriceIncreaseSpeed);
        TextView txtViewPriceIncreaseRadius = (TextView)findViewById(R.id.txtViewShopPriceIncreaseRadius);

        int increaseDamageUpgradeLevel = m_GameManager.getWeaponUpgrade(WeaponUpgrades.IncreaseDamage);
        int increaseSpeedUpgradeLevel = m_GameManager.getWeaponUpgrade(WeaponUpgrades.IncreaseSpeed);
        int increaseRadiusUpgradeLevel = m_GameManager.getWeaponUpgrade(WeaponUpgrades.IncreaseRadius);

        if (increaseDamageUpgradeLevel < Pustafin.DamageUpgradeMaxLevel) {
            txtViewPriceIncreaseDamage.setText(String.format(getString(R.string.sv_price_level_display),
                    calculateWeaponUpgradeCost(WeaponUpgrades.IncreaseDamage, increaseDamageUpgradeLevel + 1), increaseDamageUpgradeLevel));
        }
        else {
            txtViewPriceIncreaseDamage.setText(String.format(getString(R.string.sv_max_level_display), increaseDamageUpgradeLevel));
        }

        if (increaseSpeedUpgradeLevel < Pustafin.SpeedUpgradeMaxLevel) {
            txtViewPriceIncreaseSpeed.setText(String.format(getString(R.string.sv_price_level_display),
                    calculateWeaponUpgradeCost(WeaponUpgrades.IncreaseSpeed, increaseSpeedUpgradeLevel + 1), increaseSpeedUpgradeLevel));
        }
        else {
            txtViewPriceIncreaseSpeed.setText(String.format(getString(R.string.sv_max_level_display), increaseSpeedUpgradeLevel));
        }

        if (increaseRadiusUpgradeLevel < Pustafin.RadiusUpgradeMaxLevel) {
            txtViewPriceIncreaseRadius.setText(String.format(getString(R.string.sv_price_level_display),
                    calculateWeaponUpgradeCost(WeaponUpgrades.IncreaseRadius, increaseRadiusUpgradeLevel + 1), increaseRadiusUpgradeLevel));
        }
        else {
            txtViewPriceIncreaseRadius.setText(String.format(getString(R.string.sv_max_level_display), increaseRadiusUpgradeLevel));
        }

        TextView txtViewPriceResearchNuke = (TextView)findViewById(R.id.txtViewShopPriceResearchNuke);
        TextView txtViewPriceResearchLaser = (TextView)findViewById(R.id.txtViewShopPriceResearchLaser);
        TextView txtViewPriceResearchContactBomb = (TextView)findViewById(R.id.txtViewShopPriceResearchContactBomb);

        boolean researchedNuke = m_GameManager.getWeaponUpgrade(WeaponUpgrades.ResearchNuke) == 1;
        boolean researchedLaser = m_GameManager.getWeaponUpgrade(WeaponUpgrades.ResearchLaser) == 1;
        boolean researchedContactBomb = m_GameManager.getWeaponUpgrade(WeaponUpgrades.ResearchContactBomb) == 1;

        if (researchedNuke) {
            txtViewPriceResearchNuke.setText(getString(R.string.sv_researched_display));
        }
        else {
            txtViewPriceResearchNuke.setText(String.format(getString(R.string.sv_price_display), Pustafin.ResearchNukeCost));
        }

        if (researchedLaser) {
            txtViewPriceResearchLaser.setText(getString(R.string.sv_researched_display));
        }
        else {
            txtViewPriceResearchLaser.setText(String.format(getString(R.string.sv_price_display), Pustafin.ResearchLaserCost));
        }

        if (researchedContactBomb) {
            txtViewPriceResearchContactBomb.setText(getString(R.string.sv_researched_display));
        }
        else {
            txtViewPriceResearchContactBomb.setText(String.format(getString(R.string.sv_price_display), Pustafin.ResearchContactBombCost));
        }
    }

    private void onClickNextLevel() {
        if (m_Finished) {
            return;
        }

        Logger.D("Clicked Next Level Button...");

        SoundManager.getInstance().PlaySFX(R.raw.sfx_drumhits_next_level);

        m_GameManager.setLevel(m_GameManager.getLevel() + 1);

        Intent redirectIntent = new Intent(this, GameActivity.class);
        startActivity(redirectIntent);

        // Close current activity
        finish();

        // Start slide animation
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

        m_Finished = true;
    }

    public static int calculateWeaponUpgradeCost(WeaponUpgrades weaponUpgrade, int level) {
        if (level <= 0) {
            Logger.D("Could not calculate weapon upgrade cost for weapon upgrade " + weaponUpgrade + ", because parameter level " + level + " is invalid!");
            return 0;
        }

        int t = level - 1;

        switch (weaponUpgrade) {
            case IncreaseDamage:
                return (int)(Pustafin.StartDamageUpgradeCost * Math.pow(Pustafin.DamageUpgradeCostIncreaseFactor, t));
            case IncreaseSpeed:
                return (int)(Pustafin.StartSpeedUpgradeCost * Math.pow(Pustafin.SpeedUpgradeCostIncreaseFactor, t));
            case IncreaseRadius:
                return (int)(Pustafin.StartRadiusUpgradeCost * Math.pow(Pustafin.RadiusUpgradeCostIncreaseFactor, t));
        }

        Logger.E("Could not calculate weapon upgrade cost for weapon upgrade " + weaponUpgrade + ", because it is not defined!");

        return 0;
    }
}
