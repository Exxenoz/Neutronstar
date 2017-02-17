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

    /** The model matrix transforms a position in a model to the position in the world. */
    private float[] m_ModelMatrix;
    /** The rotation/translation matrix cache. */
    private float[] m_RTMatrix;
    /** The translation matrix translates a position. */
    private float[] m_TranslationMatrix;
    /** The rotation matrix rotates a position. */
    private float[] m_RotationMatrix;
    /** The scale matrix scales a position. */
    private float[] m_ScaleMatrix;

    private float m_PositionX;
    private float m_PositionY;
    private float m_PositionZ;

    private float m_RotationX;
    private float m_RotationY;
    private float m_RotationZ;

    private float m_ScaleX;
    private float m_ScaleY;
    private float m_ScaleZ;

    private float m_HalfScaleX;
    private float m_HalfScaleY;
    private float m_HalfScaleZ;

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

        m_ModelMatrix = new float[16];
        m_RTMatrix = new float[16];
        m_TranslationMatrix = new float[16];
        m_RotationMatrix = new float[16];
        m_ScaleMatrix = new float[16];

        setPosition(positionX, positionY, 1f);
        setRotation(0f, 0f, 0f);
        setScale(1f, 1f, 1f);

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
            m_PositionX += m_SpeedX * Time.getScaledDeltaTime();
            m_PositionY += m_SpeedY * Time.getScaledDeltaTime();
        }

        updateTranslationMatrix();
        updateRotationMatrix();
        updateScaleMatrix();
        updateModelMatrix();

        // Check for lost objects and destroy them
        if (Math.abs(m_PositionX - 960) >= Pustafin.GameObjectAutoDestroyDistance ||
            Math.abs(m_PositionY - 540) >= Pustafin.GameObjectAutoDestroyDistance) {
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
            float positionDeltaX = target.getPositionX() - getPositionX();
            float positionDeltaY = target.getPositionY() - getPositionY();

            double distance = Math.sqrt(positionDeltaX * positionDeltaX + positionDeltaY * positionDeltaY);

            float targetDirectionX = (float)(positionDeltaX / distance);
            float targetDirectionY = (float)(positionDeltaY / distance);

            explosionSpawnPositionX = getPositionX() + targetDirectionX * getHalfScaleX() / 2f;
            explosionSpawnPositionY = getPositionY() + targetDirectionY * getHalfScaleY() / 2f;
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
        m_ScaleX = m_CurrAnimationFrame.getSizeX();
        m_ScaleY = m_CurrAnimationFrame.getSizeY();

        m_HalfScaleX = m_ScaleX / 2f;
        m_HalfScaleY = m_ScaleY / 2f;
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
        if (renderer != null && renderer.getOwner() != this) {
            Logger.E("Could not set renderer, because owner is not equal to current game object.");
            return;
        }

        if (m_Renderer != null && m_Renderer != renderer) {
            m_Renderer.setEnabled(false);
        }

        m_Renderer = renderer;
    }

    public GameView getGameView() {
        return m_GameView;
    }

    private void updateModelMatrix() {
        Matrix.multiplyMM(m_RTMatrix, 0, m_RotationMatrix, 0, m_TranslationMatrix, 0);
        Matrix.multiplyMM(m_ModelMatrix, 0, m_RTMatrix, 0, m_ScaleMatrix, 0);
    }

    public float[] getModelMatrix() {
        return m_ModelMatrix;
    }

    private void updateTranslationMatrix() {
        Matrix.setIdentityM(m_TranslationMatrix, 0);
        Matrix.translateM(m_TranslationMatrix, 0, m_PositionX, m_PositionY, m_PositionZ);
    }

    public float[] getTranslationMatrix() {
        return m_TranslationMatrix;
    }

    private void updateRotationMatrix() {
        Matrix.setIdentityM(m_RotationMatrix, 0);

        if (m_RotationX != 0f) {
            Matrix.rotateM(m_RotationMatrix, 0, m_RotationX, 1f, 0f, 0f);
        }

        if (m_RotationY != 0f) {
            Matrix.rotateM(m_RotationMatrix, 0, m_RotationY, 0f, 1f, 0f);
        }

        if (m_RotationZ != 0f) {
            Matrix.rotateM(m_RotationMatrix, 0, m_RotationZ, 0f, 0f, 1f);
        }
    }

    public float[] getRotationMatrix() {
        return m_RotationMatrix;
    }

    private void updateScaleMatrix() {
        Matrix.setIdentityM(m_ScaleMatrix, 0);
        Matrix.scaleM(m_ScaleMatrix, 0, m_ScaleX, m_ScaleY, m_ScaleZ);
    }

    public float[] getScaleMatrix() {
        return m_ScaleMatrix;
    }

    public float getPositionX() {
        return m_PositionX;
    }

    public float getPositionY() {
        return m_PositionY;
    }

    public float getPositionZ() {
        return m_PositionZ;
    }

    public void setPositionX(float positionX) {
        m_PositionX = positionX;
    }

    public void setPositionY(float positionY) {
        m_PositionY = positionY;
    }

    public void setPositionZ(float positionZ) {
        m_PositionZ = positionZ;
    }

    public void setPosition(float positionX, float positionY) {
        m_PositionX = positionX;
        m_PositionY = positionY;
    }

    public void setPosition(float positionX, float positionY, float positionZ) {
        m_PositionX = positionX;
        m_PositionY = positionY;
        m_PositionZ = positionZ;
    }

    public float getRotationX() {
        return m_RotationX;
    }

    public float getRotationY() {
        return m_RotationY;
    }

    public float getRotationZ() {
        return m_RotationZ;
    }

    public void setRotationX(float rotationX) {
        m_RotationX = rotationX;
    }

    public void setRotationY(float rotationY) {
        m_RotationY = rotationY;
    }

    public void setRotationZ(float rotationZ) {
        m_RotationZ = rotationZ;
    }

    public void setRotation(float rotationX, float rotationY) {
        m_RotationX = rotationX;
        m_RotationY = rotationY;
    }

    public void setRotation(float rotationX, float rotationY, float rotationZ) {
        m_RotationX = rotationX;
        m_RotationY = rotationY;
        m_RotationZ = rotationZ;
    }

    public float getScaleX() {
        return m_ScaleX;
    }

    public float getScaleY() {
        return m_ScaleY;
    }

    public float getScaleZ() {
        return m_ScaleZ;
    }

    public float getHalfScaleX() {
        return m_HalfScaleX;
    }

    public float getHalfScaleY() {
        return m_HalfScaleY;
    }

    public float getHalfScaleZ() {
        return m_HalfScaleZ;
    }

    public void setScaleX(float scaleX) {
        m_ScaleX = scaleX;
        m_HalfScaleX = scaleX / 2f;
    }

    public void setScaleY(float scaleY) {
        m_ScaleY = scaleY;
        m_HalfScaleY = scaleY / 2f;
    }

    public void setScaleZ(float scaleZ) {
        m_ScaleZ = scaleZ;
        m_HalfScaleZ = scaleZ / 2f;
    }

    public void setScale(float scaleX, float scaleY) {
        m_ScaleX = scaleX;
        m_ScaleY = scaleY;
        m_HalfScaleX = scaleX / 2f;
        m_HalfScaleY = scaleY / 2f;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        m_ScaleX = scaleX;
        m_ScaleY = scaleY;
        m_ScaleZ = scaleZ;
        m_HalfScaleX = scaleX / 2f;
        m_HalfScaleY = scaleY / 2f;
        m_HalfScaleZ = scaleZ / 2f;
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
