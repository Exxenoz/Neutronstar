package at.autrage.projects.zeta.ui;

import android.view.MotionEvent;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.module.Pustafin;

public class TouchEvent {
    public final int Action;
    public final int ActionIndex;
    public final float WorldCoordX;
    public final float WorldCoordY;
    public final float ScreenCoordX;
    public final float ScreenCoordY;

    public TouchEvent(MotionEvent motionEvent) {
        this.Action = motionEvent.getAction();
        this.ActionIndex = motionEvent.getActionIndex();
        this.WorldCoordX = motionEvent.getRawX() * SuperActivity.getScaleFactorX() - Pustafin.HalfReferenceResolutionX;
        this.WorldCoordY = motionEvent.getRawY() * SuperActivity.getScaleFactorY() - Pustafin.HalfReferenceResolutionY;
        this.ScreenCoordX = motionEvent.getX();
        this.ScreenCoordY = motionEvent.getY();
    }
}
