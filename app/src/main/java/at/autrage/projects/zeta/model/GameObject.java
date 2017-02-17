package at.autrage.projects.zeta.model;

import android.opengl.Matrix;

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
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents an object in the game.
 */
public abstract class GameObject {
    private GameView m_GameView;

    protected Transform m_Transform;

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
    private MeshRenderer m_Renderer;

    private boolean m_Visible;

    public GameObject(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        m_GameView = gameView;

        m_Transform = new Transform(this);
        m_Transform.setPosition(positionX, positionY);

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

        m_Collider = null;
        m_Renderer = null;

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
            m_Transform.setPosition(
                m_Transform.getPositionX() + m_SpeedX * Time.getScaledDeltaTime(),
                m_Transform.getPositionY() + m_SpeedY * Time.getScaledDeltaTime()
            );
        }

        m_Transform.update();

        // Check for lost objects and destroy them
        if (Math.abs(m_Transform.getPositionX() - 960) >= Pustafin.GameObjectAutoDestroyDistance ||
            Math.abs(m_Transform.getPositionY() - 540) >= Pustafin.GameObjectAutoDestroyDistance) {
            Logger.D("Auto destroyed game object due to distance from planet.");
            destroy();
        }
    }

    protected void onAnimationFinished() {
    }

    public void onCollide(Collider collider) {
    }

    public void explode(GameObject target, AnimationSets animationSet) {
        explode(target, animationSet, false);
    }

    public void explode(GameObject target, AnimationSets animationSet, boolean disableAOEDamage) {
        float explosionSpawnPositionX = 0f;
        float explosionSpawnPositionY = 0f;

        // Move explosion center near target if there is any
        if (target != null) {
            float positionDeltaX = target.getTransform().getPositionX() - m_Transform.getPositionX();
            float positionDeltaY = target.getTransform().getPositionY() - m_Transform.getPositionY();

            double distance = Math.sqrt(positionDeltaX * positionDeltaX + positionDeltaY * positionDeltaY);

            float targetDirectionX = (float)(positionDeltaX / distance);
            float targetDirectionY = (float)(positionDeltaY / distance);

            explosionSpawnPositionX = m_Transform.getPositionX() + targetDirectionX * m_Transform.getHalfScaleX() / 2f;
            explosionSpawnPositionY = m_Transform.getPositionY() + targetDirectionY * m_Transform.getHalfScaleY() / 2f;
        }
        else {
            explosionSpawnPositionX = m_Transform.getPositionX();
            explosionSpawnPositionY = m_Transform.getPositionY();
        }

        Explosion explosion = new Explosion(getGameView(), explosionSpawnPositionX, explosionSpawnPositionY,
                AssetManager.getInstance().getAnimationSet(animationSet));

        if (!disableAOEDamage && this instanceof Weapon && ((Weapon)this).getAOERadius() > 0f) {
            Weapon weapon = (Weapon)this;
            explosion.setWeapon(weapon);
            //explosion.setScaleFactor((weapon.getAOERadius() * 2f / explosion.getSizeX()) * Pustafin.ExplosionSizeScaleFactorAOE);
            explosion.addImmuneToAOEGameObject(target);
        }
        else {
            //explosion.setScaleFactor((getSizeX() / explosion.getSizeX()) * Pustafin.ExplosionSizeScaleFactor);
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
        m_Transform.setScaleX(m_CurrAnimationFrame.getSizeX());
        m_Transform.setScaleY(m_CurrAnimationFrame.getSizeY());
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

    public GameView getGameView() {
        return m_GameView;
    }

    public Transform getTransform() {
        return m_Transform;
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

    public Collider getCollider() {
        return m_Collider;
    }

    public void setCollider(Collider collider) {
        m_Collider = collider;
    }

    public MeshRenderer getRenderer() {
        return m_Renderer;
    }

    public void setRenderer(MeshRenderer renderer) {
        if (m_Renderer != null && m_Renderer != renderer) {
            m_Renderer.setEnabled(false);
        }

        m_Renderer = renderer;
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

        setRenderer(null);
        setCollider(null);
    }
}
