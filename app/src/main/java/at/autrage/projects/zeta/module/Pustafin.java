package at.autrage.projects.zeta.module;


public class Pustafin {
    public static final boolean DebugMode = false;

    public static final int ReferenceResolutionX = 1920;
    public static final int ReferenceResolutionY = 1080;

    public static final int HalfReferenceResolutionX = ReferenceResolutionX / 2;
    public static final int HalfReferenceResolutionY = ReferenceResolutionY / 2;

    public static final int MaxUpdateDelta = 16; // In ms
    public static final int MinUpdateDelta = 16; // In ms
    public static final int MinRenderDelta = 16; // In ms
    public static final float MaxUpdateDeltaInSeconds = MaxUpdateDelta / 1000f; // In seconds

    public static final int PlanetMeshStacks = 10;
    public static final int PlanetMeshSlices = 10;

    public static final float PlanetScale = 256f;
    public static final float PlanetTurnSpeed = 360f / 30f;
    public static final float PlanetTouchRadius = 200f;

    public static final int ColliderGridSizeBits = 8;
    public static final int ColliderGridSize = 1 << ColliderGridSizeBits;

    public static final float AlarmAreaRadius = 280f;
    public static final float AlarmForegroundBlinkDuration = 2f;

    public static final int LevelDuration = 120;  // In seconds
    public static final int LevelSpawnTime = 100; // In seconds

    public static final float PopulationIncreaseFactor = 0.0005f;
    public static final float ProBabypillPopulationIncreaseFactor = 0.00015f;

    public static final int StartBudget = 100000;
    public static final int StartPopulation = 7390; // As from 2016

    public static final int SmallRocketStartCount = -1;
    public static final int BigRocketStartCount = 5;
    public static final int SmallNukeStartCount = 0;
    public static final int BigNukeStartCount = 0;
    public static final int SmallContactBombStartCount = 0;
    public static final int BigContactBombStartCount = 0;
    public static final int ProBabypillStartCount = 0;

    public static final int DamageUpgradeStartLevel = 0;
    public static final int SpeedUpgradeStartLevel = 0;
    public static final int RadiusUpgradeStartLevel = 0;

    public static final int ResearchNukeUpgradeStartLevel = 0;
    public static final int ResearchLaserUpgradeStartLevel = 0;
    public static final int ResearchContactBombUpgradeStartLevel = 0;

    public static final int SmallRocketPacketSize = 0;
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

    public static final int DamageUpgradeMaxLevel = 10;
    public static final int SpeedUpgradeMaxLevel = 10;
    public static final int RadiusUpgradeMaxLevel = 10;

    public static final int ResearchNukeUpgradeMaxLevel = 1;
    public static final int ResearchLaserUpgradeMaxLevel = 0; // NYI - disabled
    public static final int ResearchContactBombUpgradeMaxLevel = 0; // NYI - disabled

    public static final int StartDamageUpgradeCost = 200;
    public static final int StartSpeedUpgradeCost = 100;
    public static final int StartRadiusUpgradeCost = 300;

    public static final int ResearchNukeCost = 1500;
    public static final int ResearchLaserCost = 3000;
    public static final int ResearchContactBombCost = 2000;

    public static final int SmallRocketPacketCost = 0;
    public static final int BigRocketPacketCost = 100;
    public static final int SmallNukePacketCost = 300;
    public static final int BigNukePacketCost = 500;
    public static final int SmallLaserCostPerSecond = 50;
    public static final int BigLaserCostPerSecond = 100;
    public static final int SmallContactBombPacketCost = 300;
    public static final int BigContactBombPacketCost = 500;
    public static final int ProBabypillPacketCost = 1000;

    public static final float SmallRocketHitDamageBase = 20f;
    public static final float BigRocketHitDamageBase = 80f;
    public static final float SmallNukeHitDamageBase = 100f;
    public static final float BigNukeHitDamageBase = 200f;
    public static final float SmallLaserHitDamageBase = 150f;
    public static final float BigLaserHitDamageBase = 250f;
    public static final float SmallContactBombHitDamageBase = 100f;
    public static final float BigContactBombHitDamageBase = 200f;

    public static final float AOEDamageFactor = 0.4f;

    public static final float ExplosionSizeScaleFactor = 2f;
    public static final float ExplosionSizeScaleFactorAOE = 1f;

    public static final float SmallRocketSpeedBase = 80f;
    public static final float BigRocketSpeedBase = 50f;
    public static final float SmallNukeSpeedBase = 80f;
    public static final float BigNukeSpeedBase = 50f;
    public static final float SmallContactBombSpeedBase = 80f;
    public static final float BigContactBombSpeedBase = 50f;

    public static final float SmallNukeRadiusBase = 60f;
    public static final float BigNukeRadiusBase = 80f;
    public static final float SmallContactBombRadiusBase = 80f;
    public static final float BigContactBombRadiusBase = 100f;

    public static final float EnemyHealthBarWidth = 80f;
    public static final float EnemyHealthBarHeight = 15f;
    public static final float EnemyHealthBarHalfWidth = EnemyHealthBarWidth / 2f;
    public static final float EnemyHealthBarHalfHeight = EnemyHealthBarHeight / 2f;
    public static final float EnemyHealthBarOffsetX = 0f;
    public static final float EnemyHealthBarOffsetY = 10f;
    public static final float EnemyHealthBarMinPercentageColorGreen = 0.66f;
    public static final float EnemyHealthBarMinPercentageColorOrange = 0.33f;

    public static final int   AsteroidStartCount = 16;
    public static final float AsteroidStartScale = 0.25f;
    public static final float AsteroidCountIncreaseFactor = 5f;
    public static final float AsteroidScaleIncreaseFactor = 0.05f;
    public static final float AsteroidBaseHealthPerScaleFactor = 100f;
    public static final float AsteroidMoneyPerScaleFactor = 200f;
    public static final float AsteroidPointsPerHealthFactor = 1f;
    public static final float AsteroidImpactDamageFactor = 10f;

    public static final float AsteroidMinSpeed = 25f;
    public static final float AsteroidMaxSpeed = 100f;
    public static final float AsteroidStartMinSpeed = 45f;
    public static final float AsteroidStartMaxSpeed = 55f;
    public static final float AsteroidStartMinSpeedDecreasePerLevel = 1f;
    public static final float AsteroidStartMaxSpeedIncreasePerLevel = 1f;
    public static final float AsteroidMinRotationSpeed = 10f; // Degrees per second
    public static final float AsteroidMaxRotationSpeed = 45f; // Degrees per second

    public static final float AsteroidEasySpawnPositionProbability = 0.7f;
    public static final float AsteroidHardSpawnPositionProbability = 1f - AsteroidEasySpawnPositionProbability;

    // Distance from planet where gameobjects are automatically destroyed
    public static final float GameObjectAutoDestroyDistance = 960 + 200;

    public static final int GameActivityRedirectionDelayOnWin = 2000; // In ms
    public static final int GameActivityRedirectionDelayOnLoose = 2000; // In ms
    public static final int GameActivityRedirectionDelayOnTutorialFinish = 500; // In ms
}
