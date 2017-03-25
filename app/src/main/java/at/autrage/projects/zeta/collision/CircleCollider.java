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

    public CircleCollider(GameObject owner, float radius) {
        super(owner);

        m_Radius = radius;
        m_RadiusSquared = radius * radius;

        if (Pustafin.DebugMode) {
            GameObject debugCircleGameObject = new GameObject(owner.getGameView(), owner.getPositionX(), owner.getPositionY());
            debugCircleGameObject.setIgnoreParentRotation(true);
            debugCircleGameObject.setParent(owner);
            m_DebugCircle = new Sprite(debugCircleGameObject, AssetManager.getInstance().getAnimationSet(AnimationSets.DebugCircle));
            m_DebugCircle.setScaleFactor(2 * m_Radius / debugCircleGameObject.getScaleX());
            m_DebugCircle.getSpriteMaterial().getColor().setColor(Color.Green);
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
