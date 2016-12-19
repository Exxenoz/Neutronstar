package at.autrage.projects.zeta.collision;


import at.autrage.projects.zeta.model.GameObject;

public class CircleCollider extends Collider {

    private float m_Radius;

    public CircleCollider(GameObject owner, float radius) {
        super(owner);

        m_Radius = radius;
    }

    public float getRadius() {
        return m_Radius;
    }
}
