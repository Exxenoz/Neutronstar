package at.autrage.projects.zeta.ui;

import android.view.MotionEvent;

import at.autrage.projects.zeta.collision.Collider;

public interface TouchMove {
    void touchMove(Collider collider, TouchEvent e);
}
