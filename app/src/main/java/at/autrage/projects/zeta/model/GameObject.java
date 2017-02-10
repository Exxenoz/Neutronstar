package at.autrage.projects.zeta.model;

import android.graphics.Canvas;
import android.graphics.Rect;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.animation.Animation;
import at.autrage.projects.zeta.animation.AnimationFrame;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationType;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents an object in the game.
 */
public class GameObject {
    private GameView m_GameView;

    private float m_PositionX;
    private float m_PositionY;

    private float m_ScaledPositionX;
    private float m_ScaledPositionY;

    private float m_RotationAngle;

    private int m_SizeX;
    private int m_SizeY;

    private float m_HalfSizeX;
    private float m_HalfSizeY;

    private float m_ScaledSizeX;
    private float m_ScaledSizeY;

    private float m_ScaledHalfSizeX;
    private float m_ScaledHalfSizeY;

    private float m_ScaleFactor;

    protected Rect m_DstRect;

    private Animation m_Animation;
    private AnimationSet m_AnimationSet;
    private AnimationFrame m_CurrAnimationFrame;
    private AnimationFrame m_NextAnimationFrame;
    private float m_AnimationTimer;
    private boolean m_AnimationReversed;
    private boolean m_AnimationRepeatable;
    private boolean m_AnimationPaused;

    private float m_DirectionX;
    private float m_DirectionY;
    private float m_SpeedX;
    private float m_SpeedY;
    private float m_Speed;

    private Collider m_Collider;

    private boolean m_Visible;

    public GameObject(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        m_GameView = gameView;

        m_PositionX = positionX;
        m_PositionY = positionY;

        m_ScaledPositionX = 0;
        m_ScaledPositionY = 0;

        m_RotationAngle = 0f;

        m_SizeX = m_SizeY = 0;
        m_HalfSizeX = m_HalfSizeY = 0;
        m_ScaledSizeX = m_ScaledSizeY = 0;
        m_ScaledHalfSizeX = m_ScaledHalfSizeY = 0;

        m_ScaleFactor = 1f;

        m_DstRect = new Rect();

        m_Animation = null;
        m_AnimationSet = animationSet;
        m_CurrAnimationFrame = null;
        m_NextAnimationFrame = null;
        m_AnimationTimer = 0;
        m_AnimationReversed = false;
        m_AnimationRepeatable = false;
        m_AnimationPaused = false;

        m_DirectionX = 0f;
        m_DirectionY = 0f;
        setSpeed(0f);

        m_Visible = true;

        if (m_AnimationSet != null) {
            playAnimationFromSet(AnimationType.Default);
        }

        if (m_GameView != null) {
            m_GameView.addGameObjectToInsertQueue(this);
        }
    }

    public void onUpdate() {
        if (m_CurrAnimationFrame != null && !m_AnimationPaused &&
            m_CurrAnimationFrame != m_CurrAnimationFrame.getNextFrame())
        {
            m_AnimationTimer += Time.getScaledDeltaTime();

            if (m_AnimationTimer >= m_CurrAnimationFrame.getDuration())
            {
                m_AnimationTimer -= m_CurrAnimationFrame.getDuration();

                if (m_AnimationReversed) {
                    m_NextAnimationFrame = m_CurrAnimationFrame.getLastFrame();
                }
                else {
                    m_NextAnimationFrame = m_CurrAnimationFrame.getNextFrame();
                }

                if (m_NextAnimationFrame == m_Animation.getFirstAnimationFrame()) {
                    onAnimationFinished();

                    if (!m_AnimationRepeatable) {
                        m_AnimationPaused = true;
                    }
                    else {
                        setCurrentAnimationFrame(m_NextAnimationFrame);
                    }
                }
                else {
                    setCurrentAnimationFrame(m_NextAnimationFrame);
                }
            }
        }

        if (m_Speed != 0f) {
            m_PositionX += m_SpeedX * Time.getScaledDeltaTime();
            m_PositionY += m_SpeedY * Time.getScaledDeltaTime();
        }

        // Check for lost objects and destroy them
        if (Math.abs(m_PositionX - 960) >= Pustafin.GameObjectAutoDestroyDistance ||
            Math.abs(m_PositionY - 540) >= Pustafin.GameObjectAutoDestroyDistance) {
            Logger.D("Auto destroyed game object due to distance from planet.");
            destroy();
        }

        float scaleFactor = SuperActivity.getScaleFactor();
        m_ScaledPositionX = m_PositionX * scaleFactor;
        m_ScaledPositionY = m_PositionY * scaleFactor;

        float scaledPivotPositionX = m_ScaledPositionX - m_ScaledHalfSizeX;
        float scaledPivotPositionY = m_ScaledPositionY - m_ScaledHalfSizeY;

        m_DstRect.set((int)(scaledPivotPositionX), (int)(scaledPivotPositionY),
                (int)(scaledPivotPositionX + m_ScaledSizeX), (int)(scaledPivotPositionY + m_ScaledSizeY));
    }

    protected void onAnimationFinished() {
    }

    public void onCollide(Collider collider) {
    }

    public abstract void onRender();

    public void explode(GameObject target, AnimationSets animationSet) {
        explode(target, animationSet, false);
    }

    public void explode(GameObject target, AnimationSets animationSet, boolean disableAOEDamage) {
        float explosionSpawnPositionX = 0f;
        float explosionSpawnPositionY = 0f;

        // Move explosion center near target if there is any
        if (target != null) {
            float positionDeltaX = target.getPositionX() - getPositionX();
            float positionDeltaY = target.getPositionY() - getPositionY();

            double distance = Math.sqrt(positionDeltaX * positionDeltaX + positionDeltaY * positionDeltaY);

            float targetDirectionX = (float)(positionDeltaX / distance);
            float targetDirectionY = (float)(positionDeltaY / distance);

            explosionSpawnPositionX = getPositionX() + targetDirectionX * getHalfSizeX() / 2f;
            explosionSpawnPositionY = getPositionY() + targetDirectionY * getHalfSizeY() / 2f;
        }
        else {
            explosionSpawnPositionX = getPositionX();
            explosionSpawnPositionY = getPositionY();
        }

        Explosion explosion = new Explosion(getGameView(), explosionSpawnPositionX, explosionSpawnPositionY,
                AssetManager.getInstance().getAnimationSet(animationSet));

        if (!disableAOEDamage && this instanceof Weapon && ((Weapon)this).getAOERadius() > 0f) {
            Weapon weapon = (Weapon)this;
            explosion.setWeapon(weapon);
            explosion.setScaleFactor((weapon.getAOERadius() * 2f / explosion.getSizeX()) * Pustafin.ExplosionSizeScaleFactorAOE);
            explosion.addImmuneToAOEGameObject(target);
        }
        else {
            explosion.setScaleFactor((getSizeX() / explosion.getSizeX()) * Pustafin.ExplosionSizeScaleFactor);
        }

        destroy();
    }

    public void playAnimationFromSet(AnimationType animationType) {
        if (m_AnimationSet == null)
        {
            Logger.W("Could not play animation type " + animationType + ", because animation set reference is null.");
            return;
        }

        Animation animation = m_AnimationSet.getAnimation(animationType);
        if (animation == null)
        {
            Logger.W("Could not play animation type " + animationType + ", because it could not be found in animation set %i.",
                    m_AnimationSet.getID());
            return;
        }

        AnimationFrame animationFrame = animation.getFirstAnimationFrame();
        if (animationFrame == null)
        {
            Logger.W("Could not play animation %i, because it contains no animation frames.",
                    animation.getID());
            return;
        }

        m_Animation = animation;
        setCurrentAnimationFrame(animationFrame);
        m_AnimationTimer = 0;
        m_AnimationPaused = false;
    }

    private void setCurrentAnimationFrame(AnimationFrame animationFrame) {
        if (animationFrame == null) {
            return;
        }

        m_CurrAnimationFrame = animationFrame;
        m_SizeX = (int)(m_CurrAnimationFrame.getSizeX() * m_ScaleFactor);
        m_SizeY = (int)(m_CurrAnimationFrame.getSizeY() * m_ScaleFactor);

        m_HalfSizeX = m_SizeX / 2f;
        m_HalfSizeY = m_SizeY / 2f;

        float scaleFactor = SuperActivity.getScaleFactor();
        m_ScaledSizeX = m_SizeX * scaleFactor;
        m_ScaledSizeY = m_SizeY * scaleFactor;

        m_ScaledHalfSizeX = m_HalfSizeX * scaleFactor;
        m_ScaledHalfSizeY = m_HalfSizeY * scaleFactor;
    }

    public void setAnimationReversed(boolean animationReversed) {
        this.m_AnimationReversed = animationReversed;
    }

    public void setAnimationRepeatable(boolean animationRepeatable) {
        this.m_AnimationRepeatable = animationRepeatable;
    }

    public void setAnimationPaused(boolean animationPaused) {
        this.m_AnimationPaused = animationPaused;
    }

    public void setRotationAngle(float rotationAngle) {
        m_RotationAngle = rotationAngle;
    }

    public void setScaleFactor(float scaleFactor) {
        m_ScaleFactor = scaleFactor;
        setCurrentAnimationFrame(m_CurrAnimationFrame);
    }

    public void setHalfSizeX(float halfSizeX) {
        this.m_HalfSizeX = halfSizeX;
    }

    public void setHalfSizeY(float halfSizeY) {
        this.m_HalfSizeY = halfSizeY;
    }

    public void setScaledHalfSizeX(float scaledHalfSizeX) {
        this.m_ScaledHalfSizeX = scaledHalfSizeX;
    }

    public void setScaledHalfSizeY(float scaledHalfSizeY) {
        this.m_ScaledHalfSizeY = scaledHalfSizeY;
    }

    public float getDirectionX() {
        return m_DirectionX;
    }

    public float getDirectionY() {
        return m_DirectionY;
    }

    public void setDirectionX(float directionX) {
        this.m_DirectionX = directionX;
        setSpeed(m_Speed);
    }

    public void setDirectionY(float directionY) {
        this.m_DirectionY = directionY;
        setSpeed(m_Speed);
    }

    public void setDirection(float directionX, float directionY) {
        this.m_DirectionX = directionX;
        this.m_DirectionY = directionY;
        setSpeed(m_Speed);
    }

    public float getSpeed() {
        return m_Speed;
    }

    public void setSpeed(float speed) {
        m_Speed = speed;
        m_SpeedX = m_DirectionX * speed;
        m_SpeedY = m_DirectionY * speed;
    }

    public void setCollider(Collider collider) {
        m_Collider = collider;
    }

    public GameView getGameView() {
        return m_GameView;
    }

    public float getPositionX() {
        return m_PositionX;
    }

    public float getPositionY() {
        return m_PositionY;
    }

    public void setPositionX(float positionX) {
        this.m_PositionX = positionX;
    }

    public void setPositionY(float positionY) {
        this.m_PositionY = positionY;
    }

    public void setPosition(float positionX, float positionY) {
        this.m_PositionX = positionX;
        this.m_PositionY = positionY;
    }

    public float getScaledPositionX() {
        return m_ScaledPositionX;
    }

    public float getScaledPositionY() {
        return m_ScaledPositionY;
    }

    public float getRotationAngle() {
        return m_RotationAngle;
    }

    public int getSizeX() {
        return m_SizeX;
    }

    public int getSizeY() {
        return m_SizeY;
    }

    public float getHalfSizeX() {
        return m_HalfSizeX;
    }

    public float getHalfSizeY() {
        return m_HalfSizeY;
    }

    public float getScaledHalfSizeX() {
        return m_ScaledHalfSizeX;
    }

    public float getScaledHalfSizeY() {
        return m_ScaledHalfSizeY;
    }

    public float getScaleFactor() {
        return m_ScaleFactor;
    }

    public Collider getCollider() {
        return m_Collider;
    }

    public boolean isVisible() {
        return m_Visible;
    }

    public void setVisible(boolean visible) {
        this.m_Visible = visible;
    }

    public void destroy() {
        if (m_GameView != null) {
            m_GameView.addGameObjectToDeleteQueue(this);
        }
    }
}
