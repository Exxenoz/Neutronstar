package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.animation.Animation;
import at.autrage.projects.zeta.animation.AnimationFrame;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationType;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.opengl.SpriteMaterial;
import at.autrage.projects.zeta.opengl.SpriteMesh;
import at.autrage.projects.zeta.view.GameView;

public class Sprite extends GameObject {
    private Animation m_Animation;
    private AnimationSet m_AnimationSet;

    private AnimationFrame m_CurrAnimationFrame;
    private AnimationFrame m_NextAnimationFrame;

    private float m_AnimationTimer;

    private boolean m_AnimationReversed;
    private boolean m_AnimationRepeatable;
    private boolean m_AnimationPaused;

    private float m_ScaleFactor;

    private SpriteMaterial m_SpriteMaterial;

    public Sprite(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY);

        m_Animation = null;
        m_AnimationSet = animationSet;
        m_CurrAnimationFrame = null;
        m_NextAnimationFrame = null;
        m_AnimationTimer = 0;
        m_AnimationReversed = false;
        m_AnimationRepeatable = false;
        m_AnimationPaused = false;

        m_ScaleFactor = 1f;

        m_SpriteMaterial = new SpriteMaterial();

        if (m_AnimationSet != null) {
            playAnimationFromSet(AnimationType.Default);
        }

        MeshRenderer meshRenderer = new MeshRenderer(this);
        meshRenderer.setMaterial(m_SpriteMaterial);
        meshRenderer.setMesh(new SpriteMesh());
        meshRenderer.setEnabled(true);
        addComponent(meshRenderer);
    }

    @Override
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

        super.onUpdate();
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
            explosion.setScaleFactor((weapon.getAOERadius() * 2f / explosion.getScaleX()) * Pustafin.ExplosionSizeScaleFactorAOE);
            explosion.addImmuneToAOEGameObject(target);
        }
        else {
            explosion.setScaleFactor((getScaleX() / explosion.getScaleX()) * Pustafin.ExplosionSizeScaleFactor);
        }

        destroy();
    }

    protected void onAnimationFinished() {
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
        setScaleX(m_CurrAnimationFrame.getFrameSizeX() * m_ScaleFactor);
        setScaleY(m_CurrAnimationFrame.getFrameSizeY() * m_ScaleFactor);

        if (m_CurrAnimationFrame.getTexture() != null) {
            m_SpriteMaterial.setTexture(m_CurrAnimationFrame.getTexture());
            m_SpriteMaterial.setTextureCoordinates(m_CurrAnimationFrame.getNormalisedTexCoordinates());
        }
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

    public void setScaleFactor(float scaleFactor) {
        m_ScaleFactor = scaleFactor;
        setCurrentAnimationFrame(m_CurrAnimationFrame);
    }

    public float getScaleFactor() {
        return m_ScaleFactor;
    }

    public SpriteMaterial getSpriteMaterial() {
        return m_SpriteMaterial;
    }
}
