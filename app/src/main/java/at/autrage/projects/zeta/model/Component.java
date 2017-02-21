package at.autrage.projects.zeta.model;

public abstract class Component {
    private GameObject gameObject;

    private boolean started;
    private boolean enabled;

    public Component(GameObject gameObject) {
        if (gameObject == null) {
            throw new IllegalArgumentException("gameObject cannot be null!");
        }

        this.gameObject = gameObject;
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
        }
    }

    protected void onStart() {

    }

    public final void enable() {
        if (!enabled) {
            enabled = true;
            start();
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

    public final void destroy() {
        if (gameObject.removeComponent(this)) {
            onDestroy();
        }
    }

    protected void onDestroy() {

    }
}
