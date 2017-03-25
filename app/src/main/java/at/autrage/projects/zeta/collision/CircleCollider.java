package at.autrage.projects.zeta.collision;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Sprite;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.opengl.Color;

public class CircleCollider extends Collider {
    private float m_Radius;
    private float m_RadiusSquared;
    private Sprite m_DebugCircle;

    public CircleCollider(float radius) {
        super();

        m_Radius = radius;
        m_RadiusSquared = radius * radius;
        m_DebugCircle = null;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Pustafin.DebugMode) {
            GameObject debugCircleGameObject = new GameObject(gameObject.getGameView(), gameObject.getPositionX(), gameObject.getPositionY());
            debugCircleGameObject.setIgnoreParentRotation(true);
            debugCircleGameObject.setParent(gameObject);

            m_DebugCircle = new Sprite(AssetManager.getInstance().getAnimationSet(AnimationSets.DebugCircle));
            m_DebugCircle.setScaleFactor(2 * m_Radius / debugCircleGameObject.getScaleX());
            m_DebugCircle.getSpriteMaterial().getColor().setColor(Color.Green);
            debugCircleGameObject.addComponent(m_DebugCircle);
        }
    }

    public float getRadius() {
        return m_Radius;
    }

    @Override
    public boolean intersects(float x, float y) {
        float diffX = gameObject.getPositionX() - x;
        float diffY = gameObject.getPositionY() - y;
        return diffX * diffX + diffY * diffY <= m_RadiusSquared;
    }
}
