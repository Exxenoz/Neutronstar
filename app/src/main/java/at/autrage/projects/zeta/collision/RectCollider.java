package at.autrage.projects.zeta.collision;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.model.GameObject;

public class RectCollider extends Collider {

    private Rect m_Rect;
    private Paint m_Paint;

    public RectCollider(GameObject owner, Rect rect) {
        super(owner);

        m_Rect = rect;

        m_Paint = new Paint();
        m_Paint.setColor(Color.GREEN);
        m_Paint.setStyle(Paint.Style.STROKE);
        m_Paint.setStrokeWidth(2f);
    }

    public RectCollider(GameObject owner, int left, int right, int top, int bottom) {
        super(owner);

        m_Rect = new Rect(left, top, right, bottom);
    }

    public Rect getRect() {
        return m_Rect;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRect(m_Owner.getScaledPositionX(),
                            m_Owner.getScaledPositionY(),
                            m_Owner.getScaledPositionX() + m_Rect.width() * SuperActivity.getScaleFactor(),
                            m_Owner.getScaledPositionY() + m_Rect.height() * SuperActivity.getScaleFactor(),
                            m_Paint);
        }
    }
}
