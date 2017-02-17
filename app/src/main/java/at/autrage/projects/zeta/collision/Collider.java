package at.autrage.projects.zeta.collision;

import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.module.Logger;

/**
 * Colliders are simple invisible geometric objects used by game objects for collision detection.
 */
public abstract class Collider {
    protected GameObject m_Owner;

    public Collider(GameObject owner) {
        m_Owner = owner;
    }

    /**
     * This method checks for collider intersection.
     *
     * @param collider The other collider object.
     * @return true if both colliders intersect, otherwise false.
     */
    public boolean intersects(Collider collider) {
        if (this instanceof CircleCollider && collider instanceof CircleCollider) {
            CircleCollider circleCollider1 = (CircleCollider) this;
            CircleCollider circleCollider2 = (CircleCollider) collider;

            float distanceX = circleCollider1.getPositionX() - circleCollider2.getPositionX();
            float distanceY = circleCollider1.getPositionY() - circleCollider2.getPositionY();
            float minDistance = circleCollider1.getRadius() + circleCollider2.getRadius();

            return (distanceX * distanceX + distanceY * distanceY) <= (minDistance * minDistance);
        }
        else if (this instanceof RectCollider && collider instanceof RectCollider) {
            RectCollider rectCollider1 = (RectCollider) this;
            RectCollider rectCollider2 = (RectCollider) collider;

            float x1 = rectCollider1.getPositionX() + rectCollider1.getRect().left;
            float x2 = rectCollider2.getPositionX() + rectCollider2.getRect().left;

            float y1 = rectCollider1.getPositionY() + rectCollider1.getRect().top;
            float y2 = rectCollider1.getPositionY() + rectCollider2.getRect().top;

            float width1 = rectCollider1.getRect().width();
            float width2 = rectCollider2.getRect().width();

            float height1 = rectCollider1.getRect().height();
            float height2 = rectCollider2.getRect().height();

            return x1 < x2 + width2 && x1 + width1 > x2 && y1 < y2 + height2 && y1 + height1 > y2;
        }
        else if (this instanceof RectCollider && collider instanceof CircleCollider ||
                 this instanceof CircleCollider && collider instanceof RectCollider) {
            CircleCollider circleCollider = (CircleCollider) (collider instanceof CircleCollider ? collider : this);
            RectCollider rectCollider = (RectCollider) (collider instanceof RectCollider ? collider : this);

            float circleDistanceX = Math.abs(circleCollider.getPositionX() - rectCollider.getPositionX());
            float circleDistanceY = Math.abs(circleCollider.getPositionY() - rectCollider.getPositionY());
            float halfWidth = rectCollider.getRect().width() / 2f;
            float halfHeight = rectCollider.getRect().height() / 2f;

            if (circleDistanceX > (halfWidth + circleCollider.getRadius())) { return false; }
            if (circleDistanceY > (halfHeight + circleCollider.getRadius())) { return false; }

            if (circleDistanceX <= (halfWidth)) { return true; }
            if (circleDistanceY <= (halfHeight)) { return true; }

            float d1 = circleDistanceX - halfWidth;
            float d2 = circleDistanceY - halfHeight;

            float cornerDistance_sq = (d1 * d1 + d2 * d2);

            return (cornerDistance_sq <= (circleCollider.getRadius() * circleCollider.getRadius()));
        }

        Logger.W("Intersect() is not defined for this collider type.");

        return false;
    }

    public GameObject getOwner() {
        return m_Owner;
    }

    public float getPositionX() {
        return m_Owner.getPositionX();
    }

    public float getPositionY() {
        return m_Owner.getPositionY();
    }
}
