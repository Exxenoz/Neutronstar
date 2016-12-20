package at.autrage.projects.zeta.collision;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.model.GameObject;

public class CircleCollider extends Collider {

    private float m_Radius;
    private float m_ScaledRadius;
    private Paint m_Paint;

    public CircleCollider(GameObject owner, float radius) {
        super(owner);

        m_Radius = radius;
        m_ScaledRadius = m_Radius * SuperActivity.getScaleFactor();

        m_Paint = new Paint();
        m_Paint.setColor(Color.GREEN);
        m_Paint.setStyle(Paint.Style.STROKE);
        m_Paint.setStrokeWidth(2f);
    }

    public float getRadius() {
        return m_Radius;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawOval(m_Owner.getScaledPositionX() - m_ScaledRadius,
                            m_Owner.getScaledPositionY() - m_ScaledRadius,
                            m_Owner.getScaledPositionX() + m_ScaledRadius,
                            m_Owner.getScaledPositionY() + m_ScaledRadius,
                            m_Paint);
        }
    }
}
