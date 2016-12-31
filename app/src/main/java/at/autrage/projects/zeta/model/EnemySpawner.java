package at.autrage.projects.zeta.model;


import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.view.GameView;

public class EnemySpawner extends GameObject {
    private int m_AsteroidSpawnCount;
    private List<Float> m_AsteroidSpawnScales;
    private float m_AsteroidMaxScale;
    private float m_AsteroidScaleStep;

    private float m_AsteroidMaxSpeed;
    private float m_AsteroidMinSpeed;
    private float m_AsteroidSpeedStep;

    private float m_AsteroidSpawnTimer;
    private float m_AsteroidSpawnTimeDelta;
    private int m_AsteroidCountDestroyed;

    private Random m_Random;
    private boolean m_Initialized;

    public EnemySpawner(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_Initialized = false;
    }

    public void initialize() {
        if (m_Initialized) {
            return;
        }

        m_AsteroidSpawnCount = (int) (Pustafin.AsteroidStartCount + GameManager.getInstance().getLevel() * Pustafin.AsteroidCountIncreaseFactor);
        m_AsteroidSpawnScales = new ArrayList<Float>(m_AsteroidSpawnCount);
        m_AsteroidMaxScale = Pustafin.AsteroidStartScale + GameManager.getInstance().getLevel() * Pustafin.AsteroidScaleIncreaseFactor;
        m_AsteroidScaleStep = (m_AsteroidMaxScale - Pustafin.AsteroidStartScale) / m_AsteroidSpawnCount;

        m_AsteroidMaxSpeed = Math.min(Pustafin.AsteroidStartMaxSpeed + Pustafin.AsteroidStartMaxSpeedIncreasePerLevel * GameManager.getInstance().getLevel(),
                Pustafin.AsteroidMaxSpeed);
        m_AsteroidMinSpeed = Math.max(Pustafin.AsteroidStartMinSpeed - Pustafin.AsteroidStartMinSpeedDecreasePerLevel * GameManager.getInstance().getLevel(),
                Pustafin.AsteroidMinSpeed);
        m_AsteroidSpeedStep = (m_AsteroidMaxSpeed - m_AsteroidMinSpeed) / m_AsteroidSpawnCount;

        m_AsteroidSpawnTimeDelta = (float)Pustafin.LevelSpawnTime / m_AsteroidSpawnCount;
        m_AsteroidSpawnTimer = m_AsteroidSpawnTimeDelta; // Spawn first asteroid on next tick
        m_AsteroidCountDestroyed = 0;

        m_Random = new Random();

        Logger.D("Initialize enemy spawner for level %d", GameManager.getInstance().getLevel());
        Logger.D("Asteroid spawn count: %d", m_AsteroidSpawnCount);
        Logger.D("Asteroid min scale: %f", Pustafin.AsteroidStartScale);
        Logger.D("Asteroid max scale: %f", m_AsteroidMaxScale);
        Logger.D("Asteroid scale step: %f", m_AsteroidScaleStep);
        Logger.D("Asteroid spawn time delta: %fs", m_AsteroidSpawnTimeDelta);

        for(int i = 0; i < m_AsteroidSpawnCount; i++) {
            m_AsteroidSpawnScales.add((Pustafin.AsteroidStartScale + (m_AsteroidScaleStep * i)));
        }
        // Do not shuffle this array because of "Math.log(randomIdx + 1)" in spawnAsteroid method!

        m_Initialized = true;
    }

    private Asteroid spawnAsteroid() {
        if (m_AsteroidSpawnScales.size() == 0) {
            return null;
        }

        int randomIdx = m_Random.nextInt(m_AsteroidSpawnScales.size());

        float asteroidScale = m_AsteroidSpawnScales.get(randomIdx);
        m_AsteroidSpawnScales.remove(asteroidScale);

        float asteroidHealth = (float) (asteroidScale * Pustafin.AsteroidBaseHealthPerScaleFactor * Math.pow(1 + asteroidScale, Math.log(randomIdx + 1)));
        float asteroidHitDamage = asteroidHealth * Pustafin.AsteroidImpactDamageFactor;

        int asteroidBounty = (int) (asteroidScale * Pustafin.AsteroidMoneyPerScaleFactor);
        int asteroidPoints = (int) (asteroidHealth * Pustafin.AsteroidPointsPerHealthFactor);

        float asteroidSpeed = m_AsteroidMinSpeed + m_AsteroidSpeedStep * randomIdx;

        float asteroidRotationSpeed = (float) (Math.random() * (Pustafin.AsteroidMaxRotationSpeed - Pustafin.AsteroidMinRotationSpeed) + Pustafin.AsteroidMinRotationSpeed);
        if (m_Random.nextBoolean()) {
            asteroidRotationSpeed *= -1f;
        }

        float asteroidSpawnDirectionX = 0f;
        float asteroidSpawnDirectionY = 0f;

        int asteroidSpawnAngle = m_Random.nextInt(91);

        float asteroidSpawnSectorProbability = m_Random.nextInt(101) / 100f;
        if (asteroidSpawnSectorProbability <= Pustafin.AsteroidEasySpawnPositionProbability) {
            boolean leftSpawn = m_Random.nextBoolean();

            if (leftSpawn) {
                asteroidSpawnDirectionX = (float) -Math.cos(Math.toRadians(135f + asteroidSpawnAngle));
                asteroidSpawnDirectionY = (float) -Math.sin(Math.toRadians(135f + asteroidSpawnAngle));
            }
            else {
                asteroidSpawnDirectionX = (float) -Math.cos(Math.toRadians(-45f + asteroidSpawnAngle));
                asteroidSpawnDirectionY = (float) -Math.sin(Math.toRadians(-45f + asteroidSpawnAngle));
            }
        }
        else {
            boolean topSpawn = m_Random.nextBoolean();

            if (topSpawn) {
                asteroidSpawnDirectionX = (float) -Math.cos(Math.toRadians(45f + asteroidSpawnAngle));
                asteroidSpawnDirectionY = (float) -Math.sin(Math.toRadians(45f + asteroidSpawnAngle));
            }
            else {
                asteroidSpawnDirectionX = (float) -Math.cos(Math.toRadians(225f + asteroidSpawnAngle));
                asteroidSpawnDirectionY = (float) -Math.sin(Math.toRadians(225f + asteroidSpawnAngle));
            }
        }

        float asteroidSpawnPositionX = -asteroidSpawnDirectionX * (960 + 192) + 960;
        float asteroidSpawnPositionY = -asteroidSpawnDirectionY * (960 + 192) + 540;

        return Asteroid.createAsteroid(getGameView(), Asteroid.getRandomAnimationSet(m_Random),
                asteroidScale, asteroidSpeed, asteroidRotationSpeed, asteroidSpawnPositionX, asteroidSpawnPositionY,
                asteroidSpawnDirectionX, asteroidSpawnDirectionY, asteroidHealth, asteroidHitDamage, asteroidBounty, asteroidPoints, this);
    }

    @Override
    public void onUpdate() {
        if (!m_AsteroidSpawnScales.isEmpty()) {
            m_AsteroidSpawnTimer += Time.getScaledDeltaTime();

            while (m_AsteroidSpawnTimer >= m_AsteroidSpawnTimeDelta) {
                m_AsteroidSpawnTimer -= m_AsteroidSpawnTimeDelta;
                if (!m_AsteroidSpawnScales.isEmpty()) {
                    spawnAsteroid();
                }
            }
        }

        m_DstRect.set(0, 0, SuperActivity.getCurrentResolutionX(), SuperActivity.getCurrentResolutionY());
    }

    public void onDestroyEnemy(Enemy enemy) {
        if (enemy == null) {
            return;
        }

        if (enemy instanceof Asteroid) {
            m_AsteroidCountDestroyed++;
        }

        if (m_AsteroidCountDestroyed >= m_AsteroidSpawnCount) {
            getGameView().win();
        }
    }
}
