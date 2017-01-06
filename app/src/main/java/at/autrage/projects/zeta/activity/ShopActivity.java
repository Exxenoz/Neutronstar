package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.model.Weapon;
import at.autrage.projects.zeta.model.WeaponUpgrade;
import at.autrage.projects.zeta.model.WeaponUpgrades;
import at.autrage.projects.zeta.model.Weapons;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Util;

/**
 * This activity implements the behaviour of the in-game shop.
 */
public class ShopActivity extends SuperActivity {
    private GameManager m_GameManager;
    private TextView m_TxtViewMoneyDisplay;

    private TextView m_TxtViewPriceBigRocket;
    private TextView m_TxtViewPriceSmallNuke;
    private TextView m_TxtViewPriceBigNuke;
    private TextView m_TxtViewPriceSmallContactBomb;
    private TextView m_TxtViewPriceBigContactBomb;
    private TextView m_TxtViewPriceProBabypill;

    private TextView m_TxtViewPriceIncreaseDamage;
    private TextView m_TxtViewPriceIncreaseSpeed;
    private TextView m_TxtViewPriceIncreaseRadius;

    private TextView m_TxtViewPriceResearchNuke;
    private TextView m_TxtViewPriceResearchLaser;
    private TextView m_TxtViewPriceResearchContactBomb;

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

    private void initializeShopDescriptions() {
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
    }

    private void initializeShopPrices() {
        m_TxtViewPriceBigRocket = (TextView)findViewById(R.id.txtViewShopPriceBigRocket);
        m_TxtViewPriceSmallNuke = (TextView)findViewById(R.id.txtViewShopPriceSmallNuke);
        m_TxtViewPriceBigNuke = (TextView)findViewById(R.id.txtViewShopPriceBigNuke);
        m_TxtViewPriceSmallContactBomb = (TextView)findViewById(R.id.txtViewShopPriceSmallContactBomb);
        m_TxtViewPriceBigContactBomb = (TextView)findViewById(R.id.txtViewShopPriceBigContactBomb);
        m_TxtViewPriceProBabypill = (TextView)findViewById(R.id.txtViewShopPriceProBabyPill);

        m_TxtViewPriceIncreaseDamage = (TextView)findViewById(R.id.txtViewShopPriceIncreaseDamage);
        m_TxtViewPriceIncreaseSpeed = (TextView)findViewById(R.id.txtViewShopPriceIncreaseSpeed);
        m_TxtViewPriceIncreaseRadius = (TextView)findViewById(R.id.txtViewShopPriceIncreaseRadius);

        m_TxtViewPriceResearchNuke = (TextView)findViewById(R.id.txtViewShopPriceResearchNuke);
        m_TxtViewPriceResearchLaser = (TextView)findViewById(R.id.txtViewShopPriceResearchLaser);
        m_TxtViewPriceResearchContactBomb = (TextView)findViewById(R.id.txtViewShopPriceResearchContactBomb);
    }

    private void initializeShopOnClickListener() {
        WeaponBuyOnClickListener bigRocketBuyOnClickListener = new WeaponBuyOnClickListener(m_TxtViewPriceBigRocket, Weapons.BigRocket,
                Pustafin.BigRocketPacketCost, Pustafin.BigRocketPacketSize, WeaponUpgrades.None);
        WeaponBuyOnClickListener smallNukeOnClickListener = new WeaponBuyOnClickListener(m_TxtViewPriceSmallNuke, Weapons.SmallNuke,
                Pustafin.SmallNukePacketCost, Pustafin.SmallNukePacketSize, WeaponUpgrades.ResearchNuke);
        WeaponBuyOnClickListener bigNukeBuyOnClickListener = new WeaponBuyOnClickListener(m_TxtViewPriceBigNuke, Weapons.BigNuke,
                Pustafin.BigNukePacketCost, Pustafin.BigNukePacketSize, WeaponUpgrades.ResearchNuke);
        WeaponBuyOnClickListener smallContactBombBuyOnClickListener = new WeaponBuyOnClickListener(m_TxtViewPriceSmallContactBomb, Weapons.SmallContactBomb,
                Pustafin.SmallContactBombPacketCost, Pustafin.SmallContactBombPacketSize, WeaponUpgrades.ResearchContactBomb);
        WeaponBuyOnClickListener bigContactBombBuyOnClickListener = new WeaponBuyOnClickListener(m_TxtViewPriceBigContactBomb, Weapons.BigContactBomb,
                Pustafin.BigContactBombPacketCost, Pustafin.BigContactBombPacketSize, WeaponUpgrades.ResearchContactBomb);
        WeaponBuyOnClickListener proBabyPillBuyOnClickListener = new WeaponBuyOnClickListener(m_TxtViewPriceProBabypill, Weapons.ProBabyPill,
                Pustafin.ProBabypillPacketCost, Pustafin.ProBabypillPacketSize, WeaponUpgrades.None);

        Button btnAreaBigRocket = (Button) findViewById(R.id.btnAreaShopBuyBigRocket);
        Button btnAreaBigRocketIcon = (Button) findViewById(R.id.btnAreaShopBuyBigRocketIcon);
        btnAreaBigRocket.setOnClickListener(bigRocketBuyOnClickListener);
        btnAreaBigRocketIcon.setOnClickListener(bigRocketBuyOnClickListener);

        Button btnAreaSmallNuke = (Button) findViewById(R.id.btnAreaShopBuySmallNuke);
        Button btnAreaSmallNukeIcon = (Button) findViewById(R.id.btnAreaShopBuySmallNukeIcon);
        btnAreaSmallNuke.setOnClickListener(smallNukeOnClickListener);
        btnAreaSmallNukeIcon.setOnClickListener(smallNukeOnClickListener);

        Button btnAreaBigNuke = (Button) findViewById(R.id.btnAreaShopBuyBigNuke);
        Button btnAreaBigNukeIcon = (Button) findViewById(R.id.btnAreaShopBuyBigNukeIcon);
        btnAreaBigNuke.setOnClickListener(bigNukeBuyOnClickListener);
        btnAreaBigNukeIcon.setOnClickListener(bigNukeBuyOnClickListener);

        Button btnAreaSmallContactBomb = (Button) findViewById(R.id.btnAreaShopBuySmallContactBomb);
        Button btnAreaSmallContactBombIcon = (Button) findViewById(R.id.btnAreaShopBuySmallContactBombIcon);
        btnAreaSmallContactBomb.setOnClickListener(smallContactBombBuyOnClickListener);
        btnAreaSmallContactBombIcon.setOnClickListener(smallContactBombBuyOnClickListener);

        Button btnAreaBigContactBomb = (Button) findViewById(R.id.btnAreaShopBuyBigContactBomb);
        Button btnAreaBigContactBombIcon = (Button) findViewById(R.id.btnAreaShopBuyBigContactBombIcon);
        btnAreaBigContactBomb.setOnClickListener(bigContactBombBuyOnClickListener);
        btnAreaBigContactBombIcon.setOnClickListener(bigContactBombBuyOnClickListener);

        Button btnAreaProBabyPill = (Button) findViewById(R.id.btnAreaShopBuyProBabypill);
        Button btnAreaProBabyPillIcon = (Button) findViewById(R.id.btnAreaShopBuyProBabypillIcon);
        btnAreaProBabyPill.setOnClickListener(proBabyPillBuyOnClickListener);
        btnAreaProBabyPillIcon.setOnClickListener(proBabyPillBuyOnClickListener);

        BuyWeaponUpgradeOnClickListener buyIncreaseDamageWeaponUpgradeOnClickListener = new BuyWeaponUpgradeOnClickListener(m_TxtViewPriceIncreaseDamage, WeaponUpgrades.IncreaseDamage);
        BuyWeaponUpgradeOnClickListener buyIncreaseSpeedWeaponUpgradeOnClickListener = new BuyWeaponUpgradeOnClickListener(m_TxtViewPriceIncreaseSpeed, WeaponUpgrades.IncreaseSpeed);
        BuyWeaponUpgradeOnClickListener buyIncreaseRadiusWeaponUpgradeOnClickListener = new BuyWeaponUpgradeOnClickListener(m_TxtViewPriceIncreaseRadius, WeaponUpgrades.IncreaseRadius);
        BuyWeaponUpgradeOnClickListener buyResearchNukeWeaponUpgradeOnClickListener = new BuyWeaponUpgradeOnClickListener(m_TxtViewPriceResearchNuke, WeaponUpgrades.ResearchNuke);
        BuyWeaponUpgradeOnClickListener buyResearchLaserWeaponUpgradeOnClickListener = new BuyWeaponUpgradeOnClickListener(m_TxtViewPriceResearchLaser, WeaponUpgrades.ResearchLaser);
        BuyWeaponUpgradeOnClickListener buyResearchContactBombWeaponUpgradeOnClickListener = new BuyWeaponUpgradeOnClickListener(m_TxtViewPriceResearchContactBomb, WeaponUpgrades.ResearchContactBomb);

        Button btnAreaIncreaseDamage = (Button) findViewById(R.id.btnAreaShopBuyIncreaseDamage);
        Button btnAreaIncreaseDamageIcon = (Button) findViewById(R.id.btnAreaShopBuyIncreaseDamageIcon);
        btnAreaIncreaseDamage.setOnClickListener(buyIncreaseDamageWeaponUpgradeOnClickListener);
        btnAreaIncreaseDamageIcon.setOnClickListener(buyIncreaseDamageWeaponUpgradeOnClickListener);

        Button btnAreaIncreaseSpeed = (Button) findViewById(R.id.btnAreaShopBuyIncreaseSpeed);
        Button btnAreaIncreaseSpeedIcon = (Button) findViewById(R.id.btnAreaShopBuyIncreaseSpeedIcon);
        btnAreaIncreaseSpeed.setOnClickListener(buyIncreaseSpeedWeaponUpgradeOnClickListener);
        btnAreaIncreaseSpeedIcon.setOnClickListener(buyIncreaseSpeedWeaponUpgradeOnClickListener);

        Button btnAreaIncreaseRadius = (Button) findViewById(R.id.btnAreaShopBuyIncreaseRadius);
        Button btnAreaIncreaseRadiusIcon = (Button) findViewById(R.id.btnAreaShopBuyIncreaseRadiusIcon);
        btnAreaIncreaseRadius.setOnClickListener(buyIncreaseRadiusWeaponUpgradeOnClickListener);
        btnAreaIncreaseRadiusIcon.setOnClickListener(buyIncreaseRadiusWeaponUpgradeOnClickListener);

        Button btnAreaResearchNuke = (Button) findViewById(R.id.btnAreaShopBuyResearchNuke);
        Button btnAreaResearchNukeIcon = (Button) findViewById(R.id.btnAreaShopBuyResearchNukeIcon);
        btnAreaResearchNuke.setOnClickListener(buyResearchNukeWeaponUpgradeOnClickListener);
        btnAreaResearchNukeIcon.setOnClickListener(buyResearchNukeWeaponUpgradeOnClickListener);

        Button btnAreaResearchLaser = (Button) findViewById(R.id.btnAreaShopBuyResearchLaser);
        Button btnAreaResearchLaserIcon = (Button) findViewById(R.id.btnAreaShopBuyResearchLaserIcon);
        btnAreaResearchLaser.setOnClickListener(buyResearchLaserWeaponUpgradeOnClickListener);
        btnAreaResearchLaserIcon.setOnClickListener(buyResearchLaserWeaponUpgradeOnClickListener);

        Button btnAreaResearchContactBomb = (Button) findViewById(R.id.btnAreaShopBuyResearchContactBomb);
        Button btnAreaResearchContactBombIcon = (Button) findViewById(R.id.btnAreaShopBuyResearchContactBombIcon);
        btnAreaResearchContactBomb.setOnClickListener(buyResearchContactBombWeaponUpgradeOnClickListener);
        btnAreaResearchContactBombIcon.setOnClickListener(buyResearchContactBombWeaponUpgradeOnClickListener);
    }

    private void initializeShop() {
        m_TxtViewMoneyDisplay = (TextView)findViewById(R.id.txtViewShopMoney);
        updateMoneyDisplay();

        TextView txtViewPopulationDisplay = (TextView) findViewById(R.id.txtViewShopPopulation);
        txtViewPopulationDisplay.setText(String.format(getString(R.string.sv_population_display), Util.addLeadingZeros((int) GameManager.getInstance().getPopulation(), 5, true)));

        initializeShopDescriptions();
        initializeShopPrices();
        initializeShopOnClickListener();

        updateWeaponPrice(m_TxtViewPriceBigRocket, Weapons.BigRocket, Pustafin.BigRocketPacketCost);
        updateWeaponPrice(m_TxtViewPriceSmallNuke, Weapons.SmallNuke, Pustafin.SmallNukePacketCost);
        updateWeaponPrice(m_TxtViewPriceBigNuke, Weapons.BigNuke, Pustafin.BigNukePacketCost);
        updateWeaponPrice(m_TxtViewPriceSmallContactBomb, Weapons.SmallContactBomb, Pustafin.SmallContactBombPacketCost);
        updateWeaponPrice(m_TxtViewPriceBigContactBomb, Weapons.BigContactBomb, Pustafin.BigContactBombPacketCost);
        updateWeaponPrice(m_TxtViewPriceProBabypill, Weapons.ProBabyPill, Pustafin.ProBabypillPacketCost);

        updateWeaponUpgradePrice(m_TxtViewPriceIncreaseDamage, WeaponUpgrades.IncreaseDamage);
        updateWeaponUpgradePrice(m_TxtViewPriceIncreaseSpeed, WeaponUpgrades.IncreaseSpeed);
        updateWeaponUpgradePrice(m_TxtViewPriceIncreaseRadius, WeaponUpgrades.IncreaseRadius);
        updateWeaponUpgradePrice(m_TxtViewPriceResearchNuke, WeaponUpgrades.ResearchNuke);
        updateWeaponUpgradePrice(m_TxtViewPriceResearchLaser, WeaponUpgrades.ResearchLaser);
        updateWeaponUpgradePrice(m_TxtViewPriceResearchContactBomb, WeaponUpgrades.ResearchContactBomb);
    }

    private void updateMoneyDisplay() {
        m_TxtViewMoneyDisplay.setText(String.format(getString(R.string.sv_money_display), Util.addLeadingZeros(m_GameManager.getMoney(), 6, true)));
    }

    private void updateWeaponPrice(TextView textView, Weapons weapon, int weaponPrice) {
        int currenWeaponCount = m_GameManager.getWeaponCount(weapon);
        textView.setText(String.format(getString(R.string.sv_price_count_display), weaponPrice, currenWeaponCount));
    }

    private void updateWeaponUpgradePrice(TextView textView, WeaponUpgrades weaponUpgrade) {
        WeaponUpgrade weaponUpgradeObj = m_GameManager.getWeaponUpgrade(weaponUpgrade);
        if (weaponUpgradeObj == null) {
            return;
        }

        if (weaponUpgradeObj.getMaxLevel() <= 0) {
            // Weapon upgrade is disabled
            textView.setText(getString(R.string.sv_disabled));
        }
        else if (weaponUpgradeObj.isResearchable()) {
            // Weapon upgrade is researchable
            if (weaponUpgradeObj.isResearched()) {
                textView.setText(getString(R.string.sv_researched_display));
            }
            else {
                textView.setText(String.format(getString(R.string.sv_price_display), weaponUpgradeObj.getUpgradeCostsForNextLevel()));
            }
        }
        else if (weaponUpgradeObj.getLevel() < weaponUpgradeObj.getMaxLevel()) {
            textView.setText(String.format(getString(R.string.sv_price_level_display),
                    weaponUpgradeObj.getUpgradeCostsForNextLevel(), weaponUpgradeObj.getLevel()));
        }
        else {
            // Weapon upgrade is at its max level
            textView.setText(String.format(getString(R.string.sv_max_level_display), weaponUpgradeObj.getLevel()));
        }
    }

    private void onClickNextLevel() {
        if (m_Finished) {
            return;
        }

        Logger.D("Clicked Next Level Button...");

        m_GameManager.onStartNextLevel();

        Intent redirectIntent = new Intent(this, GameActivity.class);
        startActivity(redirectIntent);

        // Close current activity
        finish();

        // Start slide animation
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

        m_Finished = true;
    }

    private class WeaponBuyOnClickListener implements View.OnClickListener {
        private TextView m_TextView;
        private Weapons m_Weapon;
        private int m_Price;
        private int m_Count;
        private WeaponUpgrades m_RequiredWeaponUpgrade;

        public WeaponBuyOnClickListener(TextView textView, Weapons weapon, int price, int count, WeaponUpgrades requiredWeaponUpgrade) {
            m_TextView = textView;
            m_Weapon = weapon;
            m_Price = price;
            m_Count = count;
            m_RequiredWeaponUpgrade = requiredWeaponUpgrade;
        }

        @Override
        public void onClick(View v) {
            if (m_RequiredWeaponUpgrade != WeaponUpgrades.None &&
                !GameManager.getInstance().isWeaponUpgradeResearched(m_RequiredWeaponUpgrade)) {
                return;
            }

            if (GameManager.getInstance().getMoney() < m_Price) {
                return;
            }

            GameManager.getInstance().setMoney(GameManager.getInstance().getMoney() - m_Price);
            GameManager.getInstance().setWeaponCount(m_Weapon, GameManager.getInstance().getWeaponCount(m_Weapon) + m_Count);

            updateMoneyDisplay();
            updateWeaponPrice(m_TextView, m_Weapon, m_Price);
        }
    }

    private class BuyWeaponUpgradeOnClickListener implements View.OnClickListener {
        private TextView m_TextView;
        private WeaponUpgrades m_WeaponUpgrade;

        public BuyWeaponUpgradeOnClickListener(TextView textView, WeaponUpgrades weaponUpgrade) {
            m_TextView = textView;
            m_WeaponUpgrade = weaponUpgrade;
        }

        @Override
        public void onClick(View v) {
            WeaponUpgrade weaponUpgrade = GameManager.getInstance().getWeaponUpgrade(m_WeaponUpgrade);
            if (weaponUpgrade == null) {
                return;
            }

            if (!weaponUpgrade.upgrade()) {
                return;
            }

            updateMoneyDisplay();
            updateWeaponUpgradePrice(m_TextView, m_WeaponUpgrade);
        }
    }
}
