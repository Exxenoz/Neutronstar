package at.autrage.projects.zeta.ui;

import android.view.MotionEvent;

import java.util.logging.XMLFormatter;

public class TouchEvent {
    public final MotionEvent motionEvent;
    public final float x;
    public final float y;

    public TouchEvent(MotionEvent motionEvent, float x, float y) {
        this.motionEvent = motionEvent;
        this.x = x;
        this.y = y;
    }
}
