package at.autrage.projects.zeta.collision;

import android.graphics.Rect;

import at.autrage.projects.zeta.model.GameObject;

public class RectCollider extends Collider {

    private Rect m_Rect;

    public RectCollider(GameObject owner, Rect rect) {
        super(owner);

        m_Rect = rect;
    }

    public RectCollider(GameObject owner, int left, int right, int top, int bottom) {
        super(owner);

        m_Rect = new Rect(left, top, right, bottom);
    }

    public Rect getRect() {
        return m_Rect;
    }
}
