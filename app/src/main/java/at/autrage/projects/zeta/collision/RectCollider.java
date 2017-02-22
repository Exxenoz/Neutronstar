package at.autrage.projects.zeta.collision;

import android.graphics.Rect;

import at.autrage.projects.zeta.model.GameObject;

public class RectCollider extends Collider {

    private Rect m_Rect;
    private float m_HalfWidth;
    private float m_HalfHeight;

    public RectCollider(GameObject owner, Rect rect) {
        super(owner);

        m_Rect = rect;
        m_HalfWidth = m_Rect.width() / 2f;
        m_HalfHeight = m_Rect.height() / 2f;
    }

    public RectCollider(GameObject owner, int left, int right, int top, int bottom) {
        super(owner);

        m_Rect = new Rect(left, top, right, bottom);
        m_HalfWidth = m_Rect.width() / 2f;
        m_HalfHeight = m_Rect.height() / 2f;
    }

    public Rect getRect() {
        return m_Rect;
    }

    @Override
    public float getApproximatedHalfRectWidth() {
        return m_HalfWidth;
    }

    @Override
    public float getApproximatedHalfRectHeight() {
        return m_HalfHeight;
    }
}
