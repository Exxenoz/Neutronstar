package at.autrage.projects.zeta.collision;

import android.text.method.Touch;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.exception.ArgumentNullException;
import at.autrage.projects.zeta.ui.TouchEvent;

public class ColliderManager {
    /**
     * Contains all active GVColliders
     */
    private List<Collider> GVColliders;

    /**
     * Contains all active UIColliders
     */
    private List<Collider> UIColliders;

    private int currGVColliderIdx;
    private int currUIColliderIdx;

    public ColliderManager() {
        GVColliders = new ArrayList<Collider>(256);
        UIColliders = new ArrayList<Collider>(256);

        currGVColliderIdx = -1;
        currUIColliderIdx = -1;
    }

    public void update() {
        updateGameViewColliders();
    }

    public void touch(TouchEvent touchEvent) {
        switch (touchEvent.motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(touchEvent);
                break;
            case MotionEvent.ACTION_UP:
                actionUp(touchEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(touchEvent);
                break;
        }
    }

    private void actionMove(TouchEvent e) {
        for (currUIColliderIdx = 0; currUIColliderIdx < UIColliders.size(); currUIColliderIdx++) {
            Collider collider = UIColliders.get(currUIColliderIdx);
            if (collider.intersects(e.x, e.y)){
                collider.getGameObject().touchMove(collider, e);
            }
        }

        currUIColliderIdx = -1;
    }

    private void actionUp(TouchEvent e) {
        for (currUIColliderIdx = 0; currUIColliderIdx < UIColliders.size(); currUIColliderIdx++) {
            Collider collider = UIColliders.get(currUIColliderIdx);
            if (collider.intersects(e.x, e.y)){
                collider.getGameObject().touchUp(collider, e);
            }
        }

        currUIColliderIdx = -1;
    }

    private void actionDown(TouchEvent e) {
        for (currUIColliderIdx = 0; currUIColliderIdx < UIColliders.size(); currUIColliderIdx++) {
            Collider collider = UIColliders.get(currUIColliderIdx);
            if (collider.intersects(e.x, e.y)){
                collider.getGameObject().touchDown(collider, e);
            }
        }

        currUIColliderIdx = -1;
    }

    private void updateGameViewColliders() {
        for (currGVColliderIdx = 0; currGVColliderIdx < GVColliders.size(); currGVColliderIdx++) {
            Collider colliderI = GVColliders.get(currGVColliderIdx);

            for (int j = 0; j < currGVColliderIdx; j++) {
                Collider colliderJ = GVColliders.get(j);

                if (colliderI.intersects(colliderJ)) {
                    colliderI.getGameObject().onCollide(colliderJ);
                    colliderJ.getGameObject().onCollide(colliderI);
                }
            }
        }

        currGVColliderIdx = -1;
    }

    public void addGVCollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        GVColliders.add(collider);
    }

    public void removeGVCollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        if (currGVColliderIdx > -1) {
            int colliderIndex = GVColliders.indexOf(collider);
            if (colliderIndex > -1 && colliderIndex <= currGVColliderIdx) {
                GVColliders.remove(colliderIndex);
                currGVColliderIdx--;
            }
        }
        else {
            GVColliders.remove(collider);
        }
    }

    public void addUICollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        UIColliders.add(collider);
    }

    public void removeUICollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        if (currUIColliderIdx > -1) {
            int colliderIndex = UIColliders.indexOf(collider);
            if (colliderIndex > -1 && colliderIndex <= currUIColliderIdx) {
                UIColliders.remove(colliderIndex);
                currUIColliderIdx--;
            }
        }
        else {
            UIColliders.remove(collider);
        }
    }
}
