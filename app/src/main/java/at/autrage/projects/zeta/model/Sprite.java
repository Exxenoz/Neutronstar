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

public class Sprite extends Component {
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
    private MeshRenderer meshRenderer;

    public Sprite(GameObject gameObject) {
        super(gameObject);

        m_Animation = null;
        m_AnimationSet = null;
        m_CurrAnimationFrame = null;
        m_NextAnimationFrame = null;
        m_AnimationTimer = 0;
        m_AnimationReversed = false;
        m_AnimationRepeatable = false;
        m_AnimationPaused = false;

        m_ScaleFactor = 1f;

        m_SpriteMaterial = new SpriteMaterial();
        this.meshRenderer = null;
    }

    @Override
    protected void onStart() {
        meshRenderer = gameObject.addComponent(MeshRenderer.class);
        meshRenderer.setMaterial(m_SpriteMaterial);
        meshRenderer.setMesh(new SpriteMesh());
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
            float positionDeltaX = target.getPositionX() - gameObject.getPositionX();
            float positionDeltaY = target.getPositionY() - gameObject.getPositionY();

            double distance = Math.sqrt(positionDeltaX * positionDeltaX + positionDeltaY * positionDeltaY);

            float targetDirectionX = (float)(positionDeltaX / distance);
            float targetDirectionY = (float)(positionDeltaY / distance);

            explosionSpawnPositionX = gameObject.getPositionX() + targetDirectionX * gameObject.getHalfScaleX() / 2f;
            explosionSpawnPositionY = gameObject.getPositionY() + targetDirectionY * gameObject.getHalfScaleY() / 2f;
        }
        else {
            explosionSpawnPositionX = gameObject.getPositionX();
            explosionSpawnPositionY = gameObject.getPositionY();
        }

        Weapon weapon = gameObject.getComponent(Weapon.class);

        GameObject explosionGameObject = new GameObject(gameObject.getGameView(), explosionSpawnPositionX, explosionSpawnPositionY, "Explosion");

        Explosion explosion = explosionGameObject.addComponent(Explosion.class);
        explosion.setAnimationSet(AssetManager.getInstance().getAnimationSet(animationSet));
        explosion.playDefaultAnimationFromSet();

        if (!disableAOEDamage && weapon != null && weapon.getAOERadius() > 0f) {
            explosion.setWeapon(weapon);
            explosion.addImmuneToAOEGameObject(target); // Target was already hit, so do not deal AOE damage too...
            explosion.setScaleFactorToMatchFrameSizeX(weapon.getAOERadius() * 2f);
            explosion.setScaleFactor(explosion.getScaleFactor() * Pustafin.ExplosionSizeScaleFactorAOE);
        }
        else {
            explosion.setScaleFactorToMatchFrameSizeX(gameObject.getScaleX());
            explosion.setScaleFactor(explosion.getScaleFactor() * Pustafin.ExplosionSizeScaleFactor);
        }
    }

    protected void onAnimationFinished() {
    }

    public void playDefaultAnimationFromSet() {
        playAnimationFromSet(AnimationType.Default);
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
            Logger.W("Could not play animation %s, because it contains no animation frames.",
                    animation.getName());
            return;
        }

        m_Animation = animation;
        setCurrentAnimationFrame(animationFrame);
        m_AnimationTimer = 0;
        m_AnimationPaused = false;
    }

    @Override
    protected void onDestroy() {
        if (meshRenderer != null) {
            meshRenderer.destroy();
        }
    }

    public AnimationSet getAnimationSet() {
        return m_AnimationSet;
    }

    public void setAnimationSet(AnimationSet animationSet) {
        this.m_AnimationSet = animationSet;
    }

    public void setAnimationSet(AnimationSets animationSet) {
        this.m_AnimationSet = AssetManager.getInstance().getAnimationSet(animationSet);
    }

    public float getAnimationFrameSizeX() {
        return m_CurrAnimationFrame != null ? m_CurrAnimationFrame.getFrameSizeX() : 0f;
    }

    public float getAnimationFrameSizeY() {
        return m_CurrAnimationFrame != null ? m_CurrAnimationFrame.getFrameSizeY() : 0f;
    }

    private void setCurrentAnimationFrame(AnimationFrame animationFrame) {
        if (animationFrame == null) {
            return;
        }

        m_CurrAnimationFrame = animationFrame;
        gameObject.setScaleX(m_CurrAnimationFrame.getFrameSizeX() * m_ScaleFactor);
        gameObject.setScaleY(m_CurrAnimationFrame.getFrameSizeY() * m_ScaleFactor);

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

    public void setScaleFactorToMatchFrameSizeX(float scaleX) {
        if (getAnimationFrameSizeX() <= 0f) {
            Logger.W("Could not match scale X of game object, because current animation frame size X is 0!");
            return;
        }

        setScaleFactor(scaleX / getAnimationFrameSizeX());
    }

    public float getScaleFactor() {
        return m_ScaleFactor;
    }

    public SpriteMaterial getSpriteMaterial() {
        return m_SpriteMaterial;
    }
}
