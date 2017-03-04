package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.Logger;

public abstract class Component {
    protected GameObject gameObject;

    private boolean started;
    private boolean enabled;

    public Component(GameObject gameObject) {
        if (gameObject == null) {
            throw new IllegalArgumentException("gameObject cannot be null!");
        }

        this.gameObject = gameObject;
        this.started = false;
        this.enabled = false;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        if (enabled) {
            enable();
        } else {
            disable();
        }
    }

    private void start() {
        if (!started) {
            onStart();
            started = true;
        }
    }

    protected void onStart() {

    }

    public final void enable() {
        if (!enabled) {
            enabled = true;

            if (!started) {
                start();
            }

            onEnable();
        }
    }

    protected void onEnable() {

    }

    public final void disable() {
        if (enabled) {
            enabled = false;
            onDisable();
        }
    }

    protected void onDisable() {

    }

    public final void update() {
        if (enabled) {
            onUpdate();
        }
    }

    protected void onUpdate() {

    }

    public final void collide(Collider other) {
        if (enabled) {
            onCollide(other);
        }
    }

    protected void onCollide(Collider other) {

    }

    public final void destroy() {
        if (gameObject.removeComponent(this)) {
            onDestroy();
        }
    }

    protected void onDestroy() {

    }
}
