package at.autrage.projects.zeta.collision;

import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Sprite;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.opengl.Color;

public class CircleCollider extends Collider {
    private float m_Radius;
    private Sprite m_DebugCircle;

    public CircleCollider(GameObject owner, float radius) {
        super(owner);

        m_Radius = radius;

        if (Pustafin.DebugMode) {
            m_DebugCircle = new Sprite(owner.getGameView(), transform.getPositionX(), transform.getPositionY(), AssetManager.getInstance().getAnimationSet(AnimationSets.DebugCircle));
            m_DebugCircle.setScaleFactor(2f * m_Radius / m_DebugCircle.getTransform().getScaleX());
            m_DebugCircle.getSpriteMaterial().getColor().setColor(Color.Green);
            m_DebugCircle.getTransform().setParent(transform);
        }
    }

    public float getRadius() {
        return m_Radius;
    }

    @Override
    public float getApproximatedHalfRectWidth() {
        return m_Radius;
    }

    @Override
    public float getApproximatedHalfRectHeight() {
        return m_Radius;
    }
}
