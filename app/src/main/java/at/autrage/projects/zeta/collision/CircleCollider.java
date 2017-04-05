package at.autrage.projects.zeta.collision;

import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Sprite;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.opengl.Color;

public class CircleCollider extends Collider {
    private float m_Radius;
    private float m_RadiusSquared;
    private Sprite m_DebugCircle;

    public CircleCollider(GameObject gameObject) {
        super(gameObject);

        m_Radius = 0f;
        m_RadiusSquared = 0f;
        m_DebugCircle = null;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Pustafin.DebugMode) {
            GameObject debugCircleGameObject = new GameObject(gameObject.getGameView(), gameObject.getPositionX(), gameObject.getPositionY(), "debugCircle");
            debugCircleGameObject.setIgnoreParentRotation(true);
            debugCircleGameObject.setParent(gameObject);

            m_DebugCircle = debugCircleGameObject.addComponent(Sprite.class);
            m_DebugCircle.setAnimationSet(AnimationSets.DebugCircle);
            m_DebugCircle.getSpriteMaterial().getColor().setColor(gameObject.getLayer() == GameObject.Layer.UI ? Color.Blue : Color.Green);
            m_DebugCircle.playDefaultAnimationFromSet();
            m_DebugCircle.setScaleFactorToMatchFrameSizeX(2 * m_Radius);
        }
    }

    public float getRadius() {
        return m_Radius;
    }

    public void setRadius(float radius) {
        this.m_Radius = radius;
        this.m_RadiusSquared = radius * radius;

        if (m_DebugCircle != null) {
            m_DebugCircle.setScaleFactorToMatchFrameSizeX(2 * m_Radius);
        }
    }

    @Override
    public boolean intersects(float x, float y) {
        float diffX = gameObject.getPositionX() - x;
        float diffY = gameObject.getPositionY() - y;
        return diffX * diffX + diffY * diffY <= m_RadiusSquared;
    }
}
