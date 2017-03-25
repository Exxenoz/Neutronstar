package at.autrage.projects.zeta.ui;

import android.view.MotionEvent;

import java.util.logging.XMLFormatter;

import at.autrage.projects.zeta.activity.SuperActivity;

public class TouchEvent {
    public final MotionEvent motionEvent;
    public final float x;
    public final float y;

    public TouchEvent(MotionEvent motionEvent) {
        this.motionEvent = motionEvent;
        this.x = motionEvent.getRawX() * SuperActivity.getScaleFactorX();
        this.y = motionEvent.getRawY() * SuperActivity.getScaleFactorY();
    }
}
