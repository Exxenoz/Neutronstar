package at.autrage.projects.zeta.module;


public class Pustafin {
    public static final boolean DebugMode = true;

    public static final int MaxUpdateDelta = 16; // In ms
    public static final int MinRenderDelta = 16; // In ms
    public static final float MaxUpdateDeltaInSeconds = MaxUpdateDelta / 1000f; // In seconds

    public static final int LevelDuration = 120;  // In seconds
    public static final int LevelSpawnTime = 100; // In seconds
    public static final int StartBudget = 100;
    public static final int StartPopulation = 7390; // As from 2016
    public static final float PopulationIncreaseFactor = 1.0005f;
    public static final float ProBabypillPopulationIncreaseFactor = 0.00015f;

    public static final int BigRocketPacketSize = 5;
    public static final int SmallNukePacketSize = 10;
    public static final int BigNukePacketSize = 5;
    public static final int SmallContactBombPacketSize = 10;
    public static final int BigContactBombPacketSize = 5;
    public static final int ProBabypillPacketSize = 1;

    public static final float DamageUpgradeCostIncreaseFactor = 1.1f;
    public static final float SpeedUpgradeCostIncreaseFactor = 1.1f;
    public static final float RadiusUpgradeCostIncreaseFactor = 1.1f;

    public static final float DamageUpgradeIncreaseFactor = 1.1f;
    public static final float SpeedUpgradeIncreaseFactor = 1.1f;
    public static final float RadiusUpgradeIncreaseFactor = 1.1f;

    public static final int StartDamageUpgradeCost = 200;
    public static final int StartSpeedUpgradeCost = 100;
    public static final int StartRadiusUpgradeCost = 300;

    public static final int ResearchNukeCost = 1500;
    public static final int ResearchLaserCost = 3000;
    public static final int ResearchContactBombCost = 2000;

    public static final int SmallRocketPacketCost = 0;
    public static final int BigRocketPacketCost = 300;
    public static final int SmallNukePacketCost = 1000;
    public static final int BigNukePacketCost = 1500;
    public static final int SmallLaserCostPerSecond = 150;
    public static final int BigLaserCostPerSecond = 300;
    public static final int SmallContactBombPacketCost = 1000;
    public static final int BigConctactBombPacketCost = 1500;
    public static final int ProBabypillPacketCost = 1000;

    public static final float SmallRocketHitDamageBase = 20f;
    public static final float BigRocketHitDamageBase = 80f;
    public static final float SmallNukeHitDamageBase = 100f;
    public static final float BigNukeHitDamageBase = 200f;
    public static final float SmallLaserHitDamageBase = 150f;
    public static final float BigLaserHitDamageBase = 250f;
    public static final float SmallContactBombHitDamageBase = 100f;
    public static final float BigContactBombHitDamageBase = 200f;

    public static final float ExplosionSizeScaleFactor = 2f;

    public static final float SmallRocketSpeedBase = 100f;
    public static final float BigRocketSpeedBase = 80f;
    public static final float SmallNukeSpeedBase = 1f;
    public static final float BigNukeSpeedBase = 1f;
    public static final float SmallLaserSpeedBase = 0f;
    public static final float BigLaserSpeedBase = 0f;
    public static final float SmallContactBombSpeedBase = 1f;
    public static final float BigContactBombSpeedBase = 1f;

    public static final int   AsteroidStartCount = 16;
    public static final float AsteroidStartScale = 0.25f;
    public static final float AsteroidCountIncreaseFactor = 5f;
    public static final float AsteroidScaleIncreaseFactor = 0.05f;
    public static final float AsteroidBaseHealthPerScaleFactor = 100f;
    public static final float AsteroidMoneyPerScaleFactor = 200f;
    public static final float AsteroidImpactDamageFactor = 10f;

    public static final float AsteroidMinRotationSpeed = 10f; // Degrees per second
    public static final float AsteroidMaxRotationSpeed = 45f; // Degrees per second

    public static final float AsteroidEasySpawnPositionProbability = 0.7f;
    public static final float AsteroidHardSpawnPositionProbability = 1f - AsteroidEasySpawnPositionProbability;

    // Distance from planet where gameobjects are automatically destroyed
    public static final float GameObjectAutoDestroyDistance = 960 + 200;

    public static final int GameActivityRedirectionDelayOnWin = 2000; // In ms
    public static final int GameActivityRedirectionDelayOnLoose = 2000; // In ms
}
