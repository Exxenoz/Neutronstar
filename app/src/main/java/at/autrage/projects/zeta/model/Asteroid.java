package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.view.GameView;

public class Asteroid extends Enemy {
    private float m_RotationSpeed;

    public Asteroid(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_RotationSpeed = 0f;
    }

    public static Asteroid createAsteroid(GameView gameView, AnimationSets animationSet, float scale, float moveSpeed, float rotationSpeed,
                                          float positionX, float positionY, float directionX, float directionY) {
        Asteroid asteroid = new Asteroid(gameView, positionX, positionY, AssetManager.getInstance().getAnimationSet(animationSet));
        asteroid.setScaleFactor(scale);
        asteroid.setDirection(directionX, directionY);
        asteroid.setSpeed(moveSpeed);
        asteroid.setRotationSpeed(rotationSpeed);
        asteroid.setCollider(new CircleCollider(asteroid, asteroid.getHalfSizeX() * scale));
        return asteroid;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (m_RotationSpeed != 0) {
            setRotationAngle(getRotationSpeed() + m_RotationSpeed * Time.getScaledDeltaTime());
        }
    }

    public float getRotationSpeed() {
        return m_RotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.m_RotationSpeed = rotationSpeed;
    }
}
