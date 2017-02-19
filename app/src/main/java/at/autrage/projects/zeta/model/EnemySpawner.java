package at.autrage.projects.zeta.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.module.TutorialManager;
import at.autrage.projects.zeta.view.GameView;

public class EnemySpawner extends Sprite {
    private Random m_Random;

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

    public EnemySpawner(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_Random = new Random();

        if (!GameManager.getInstance().isTutorialMode()) {
            initializeAsteroidGenerator();
        }
        else {
            m_AsteroidSpawnScales = new ArrayList<>();
        }
    }

    private void initializeAsteroidGenerator() {
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

        for(int i = 0; i < m_AsteroidSpawnCount; i++) {
            m_AsteroidSpawnScales.add((Pustafin.AsteroidStartScale + (m_AsteroidScaleStep * i)));
        }
        // Do not shuffle this array because of "Math.log(randomIdx + 1)" in spawnAsteroid method!

        Logger.D("Initialized asteroid generator for level %d", GameManager.getInstance().getLevel());
        Logger.D("Asteroid spawn count: %d", m_AsteroidSpawnCount);
        Logger.D("Asteroid min scale: %f", Pustafin.AsteroidStartScale);
        Logger.D("Asteroid max scale: %f", m_AsteroidMaxScale);
        Logger.D("Asteroid scale step: %f", m_AsteroidScaleStep);
        Logger.D("Asteroid spawn time delta: %fs", m_AsteroidSpawnTimeDelta);
    }

    private Asteroid spawnAsteroid() {
        if (m_AsteroidSpawnScales.size() == 0) {
            return null;
        }

        int randomIdx = m_Random.nextInt(m_AsteroidSpawnScales.size());

        float asteroidScale = m_AsteroidSpawnScales.get(randomIdx);
        m_AsteroidSpawnScales.remove(asteroidScale);

        float asteroidHealth = (float) (asteroidScale * Pustafin.AsteroidBaseHealthPerScaleFactor * Math.pow(1 + asteroidScale, Math.log(randomIdx + 1)));
        float asteroidSpeed = m_AsteroidMinSpeed + m_AsteroidSpeedStep * randomIdx;

        Vector2D asteroidSpawnDirection = calculateSpawnDirection(Math.random() <= Pustafin.AsteroidEasySpawnPositionProbability, m_Random.nextBoolean(), m_Random.nextInt(91));

        float asteroidSpawnPositionX = -asteroidSpawnDirection.X * (960 + 192);
        float asteroidSpawnPositionY = -asteroidSpawnDirection.Y * (960 + 192);

        return Asteroid.createAsteroid(getGameView(), Asteroid.getRandomAnimationSet(m_Random),
                asteroidScale, asteroidSpeed, asteroidSpawnPositionX, asteroidSpawnPositionY,
                asteroidSpawnDirection.X, asteroidSpawnDirection.Y, asteroidHealth, this);
    }

    public void spawnTutorialAsteroid() {
        float asteroidScale = 0.4f;

        float asteroidHealth = 2 * Pustafin.SmallRocketHitDamageBase;
        float asteroidSpeed = Pustafin.AsteroidMinSpeed;

        Vector2D asteroidSpawnDirection = calculateSpawnDirection(true, false, 32 + m_Random.nextInt(27));

        float asteroidSpawnPositionX = -asteroidSpawnDirection.X * (960 + 192);
        float asteroidSpawnPositionY = -asteroidSpawnDirection.Y * (960 + 192);

        Asteroid.createAsteroid(getGameView(), Asteroid.getRandomAnimationSet(m_Random),
            asteroidScale, asteroidSpeed, asteroidSpawnPositionX, asteroidSpawnPositionY,
            asteroidSpawnDirection.X, asteroidSpawnDirection.Y, asteroidHealth, this);
    }

    public Vector2D calculateSpawnDirection(boolean easySpawnDirection, boolean leftOrTopSpawn, int spawnAngle) {
        Vector2D spawnDirection = new Vector2D();

        if (easySpawnDirection) {
            if (leftOrTopSpawn) {
                spawnDirection.X = (float) -Math.cos(Math.toRadians(135f + spawnAngle));
                spawnDirection.Y = (float) -Math.sin(Math.toRadians(135f + spawnAngle));
            }
            else {
                spawnDirection.X = (float) -Math.cos(Math.toRadians(315f + spawnAngle));
                spawnDirection.Y = (float) -Math.sin(Math.toRadians(315f + spawnAngle));
            }
        }
        else {
            if (leftOrTopSpawn) {
                spawnDirection.X = (float) -Math.cos(Math.toRadians(45f + spawnAngle));
                spawnDirection.Y = (float) -Math.sin(Math.toRadians(45f + spawnAngle));
            }
            else {
                spawnDirection.X = (float) -Math.cos(Math.toRadians(225f + spawnAngle));
                spawnDirection.Y = (float) -Math.sin(Math.toRadians(225f + spawnAngle));
            }
        }

        return spawnDirection;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!m_AsteroidSpawnScales.isEmpty()) {
            m_AsteroidSpawnTimer += Time.getScaledDeltaTime();

            while (m_AsteroidSpawnTimer >= m_AsteroidSpawnTimeDelta) {
                m_AsteroidSpawnTimer -= m_AsteroidSpawnTimeDelta;
                if (!m_AsteroidSpawnScales.isEmpty()) {
                    spawnAsteroid();
                }
            }
        }
    }

    public void onDestroyEnemy(Enemy enemy) {
        if (enemy == null) {
            return;
        }

        if (GameManager.getInstance().isTutorialMode()) {
            TutorialManager.getInstance().onDestroyEnemy(enemy);
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
