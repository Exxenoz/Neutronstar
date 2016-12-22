package at.autrage.projects.zeta.model;


import java.util.Random;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Logger;
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

    public static Asteroid createAsteroid(GameView gameView, AnimationSets animationSet, float scale, float moveSpeed, float rotationSpeed,
                                          float positionX, float positionY, float directionX, float directionY, float health, float hitDamage, int bounty, int points) {
        Asteroid asteroid = new Asteroid(gameView, positionX, positionY, AssetManager.getInstance().getAnimationSet(animationSet));
        asteroid.setScaleFactor(scale);
        asteroid.setDirection(directionX, directionY);
        asteroid.setSpeed(moveSpeed);
        asteroid.setRotationSpeed(rotationSpeed);
        asteroid.setHealth(health);
        asteroid.setHitDamage(hitDamage);
        asteroid.setBounty(bounty);
        asteroid.setPoints(points);
        asteroid.setCollider(new CircleCollider(asteroid, asteroid.getHalfSizeX()));

        Logger.D("Spawn asteroid at (%f, %f) with direction (%f, %f), scale factor (%f), health (%f), hit damage (%f), bounty (%d) and points (%d)",
                positionX, positionY, directionX, directionY, scale, health, hitDamage, bounty, points);

        return asteroid;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (m_RotationSpeed != 0) {
            setRotationAngle(getRotationSpeed() + m_RotationSpeed * Time.getScaledDeltaTime());
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
}
