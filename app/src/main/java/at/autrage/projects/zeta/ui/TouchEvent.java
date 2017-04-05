package at.autrage.projects.zeta.ui;

import android.view.MotionEvent;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.util.CoordinateTranslator;

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
        this.WorldCoordX = CoordinateTranslator.ScreenCoordXToWorldCoordX(motionEvent.getRawX());
        this.WorldCoordY = CoordinateTranslator.ScreenCoordYToWorldCoordY(motionEvent.getRawY());
        this.ScreenCoordX = motionEvent.getX();
        this.ScreenCoordY = motionEvent.getY();
    }
}
