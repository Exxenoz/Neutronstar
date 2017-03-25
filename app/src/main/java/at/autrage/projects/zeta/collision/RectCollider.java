package at.autrage.projects.zeta.collision;

import android.graphics.Rect;

import at.autrage.projects.zeta.model.GameObject;

public class RectCollider extends Collider {
    private Rect rect;
    private float halfWidth;
    private float halfHeight;

    public RectCollider(Rect rect) {
        super();

        this.rect = rect;

        halfWidth = rect.width() / 2f;
        halfHeight = rect.height() / 2f;
    }

    public RectCollider(int left, int right, int top, int bottom) {
        super();

        rect = new Rect(left, top, right, bottom);

        halfWidth = rect.width() / 2f;
        halfHeight = rect.height() / 2f;
    }

    public Rect getRect() {
        return rect;
    }

    @Override
    public boolean intersects(float x, float y) {
        float centerX = gameObject.getPositionX();
        float centerY = gameObject.getPositionY();

        return centerX - halfWidth <= x && x <= centerX + halfWidth &&
                centerY - halfHeight <= y && y <= centerY + halfHeight;
    }
}
