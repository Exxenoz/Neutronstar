package at.autrage.projects.zeta.collision;

import android.view.MotionEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.exception.ArgumentNullException;
import at.autrage.projects.zeta.framework.Synchronitron;
import at.autrage.projects.zeta.ui.TouchEvent;

public class ColliderManager {
    /**
     * Contains all active gameViewColliders
     */
    private Synchronitron<Collider> gameViewColliders;
    /**
     * Contains all active UIColliders
     */
    private Synchronitron<Collider> UIColliders;

    private ConcurrentLinkedQueue<TouchEvent> touchEvents;
    public ColliderManager() {
        gameViewColliders = new Synchronitron<Collider>(Collider.class, 256);
        UIColliders = new Synchronitron<Collider>(Collider.class, 256);

        touchEvents = new ConcurrentLinkedQueue<>();
    }

    public void update() {
        updateGameViewColliders();

        updateTouchEvents();
    }

    private void updateTouchEvents() {
        for (TouchEvent touchEvent = touchEvents.poll(); touchEvent != null; touchEvent = touchEvents.poll()) {
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
    }

    private void actionMove(TouchEvent e) {
        for (Collider collider : UIColliders) {
            if (collider.intersects(e.x, e.y)){
                collider.getGameObject().touchMove(collider, e);
            }
        }
    }

    private void actionUp(TouchEvent e) {
        for (Collider collider : UIColliders) {
            if (collider.intersects(e.x, e.y)){
                collider.getGameObject().touchUp(collider, e);
            }
        }
    }

    private void actionDown(TouchEvent e) {
        for (Collider collider : UIColliders) {
            if (collider.intersects(e.x, e.y)){
                collider.getGameObject().touchDown(collider, e);
            }
        }
    }

    private void updateGameViewColliders() {
        gameViewColliders.synchronize();

        for (int i = 0, size = gameViewColliders.size(); i < size; i++) {
            Collider colliderI = gameViewColliders.get(i);

            for (int j = 0; j < i; j++) {
                Collider colliderJ = gameViewColliders.get(j);

                if (colliderI.intersects(colliderJ)) {
                    colliderI.getGameObject().onCollide(colliderJ);
                    colliderJ.getGameObject().onCollide(colliderI);
                }
            }
        }
    }

    public void addGameViewCollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        gameViewColliders.add(collider);
    }

    public void removeGameViewCollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        gameViewColliders.remove(collider);
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

        UIColliders.remove(collider);
    }

    public void touch(MotionEvent e) {
        float x = e.getRawX() * SuperActivity.getScaleFactorX();
        float y = e.getRawY() * SuperActivity.getScaleFactorY();

        touchEvents.add(new TouchEvent(e, x, y));
    }
}
