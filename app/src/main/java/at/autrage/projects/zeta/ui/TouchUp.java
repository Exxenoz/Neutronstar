package at.autrage.projects.zeta.ui;

import android.view.MotionEvent;

import at.autrage.projects.zeta.collision.Collider;

public interface TouchUp {
    void touchUp(Collider collider, TouchEvent e);
}
