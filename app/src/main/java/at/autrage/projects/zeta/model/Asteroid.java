package at.autrage.projects.zeta.model;


import java.util.Random;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.view.GameView;

public class Asteroid extends Enemy {
    private float m_RotationSpeed;

    private static AnimationSets[] m_AsteroidAnimationSets = new AnimationSets[] {
            AnimationSets.Asteroid1,
            AnimationSets.Asteroid2,
            AnimationSets.Asteroid3
    };

    public Asteroid(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_RotationSpeed = 0f;
    }

    public static Asteroid createAsteroid(GameView gameView, AnimationSets animationSet, float scale, float moveSpeed,
                                          float positionX, float positionY, float directionX, float directionY, float health, EnemySpawner owner) {
        Asteroid asteroid = new Asteroid(gameView, positionX, positionY, AssetManager.getInstance().getAnimationSet(animationSet));
        asteroid.setScaleFactor(scale);
        asteroid.setDirection(directionX, directionY);
        asteroid.setSpeed(moveSpeed);
        asteroid.setRandomRotationSpeed();
        asteroid.setHealth(health);
        asteroid.setHitDamage(health * Pustafin.AsteroidImpactDamageFactor);
        asteroid.setBounty((int)(scale * Pustafin.AsteroidMoneyPerScaleFactor));
        asteroid.setPoints((int)(health * Pustafin.AsteroidPointsPerHealthFactor));
        asteroid.addComponent(new CircleCollider(asteroid, asteroid.getTransform().getHalfScaleX()));
        asteroid.setOwner(owner);

        Logger.D("Spawn asteroid at (%f, %f) with direction (%f, %f), scale (%f), move speed (%f), rotation speed (%f), health (%f), hit damage (%f), bounty (%d) and points (%d)",
                positionX, positionY, directionX, directionY, scale, moveSpeed, asteroid.getRotationSpeed(), health, asteroid.getHitDamage(), asteroid.getBounty(), asteroid.getPoints());

        return asteroid;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (m_RotationSpeed != 0f) {
            m_Transform.setRotationZ(m_Transform.getRotationZ() + m_RotationSpeed * Time.getScaledDeltaTime());
        }
    }

    public static AnimationSets getRandomAnimationSet(Random random) {
        int randomIdx = random.nextInt(m_AsteroidAnimationSets.length);
        return m_AsteroidAnimationSets[randomIdx];
    }

    public float getRotationSpeed() {
        return m_RotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.m_RotationSpeed = rotationSpeed;
    }

    public void setRandomRotationSpeed() {
        this.m_RotationSpeed = (float)(Math.random() * (Pustafin.AsteroidMaxRotationSpeed - Pustafin.AsteroidMinRotationSpeed) + Pustafin.AsteroidMinRotationSpeed);
        if (Math.random() < 0.5) {
            this.m_RotationSpeed *= -1f;
        }
    }
}
