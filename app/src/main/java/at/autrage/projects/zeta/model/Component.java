package at.autrage.projects.zeta.model;

public abstract class Component {
    private GameObject gameObject;

    public Component(GameObject gameObject) {
        if (gameObject == null) {
            throw new IllegalArgumentException("gameObject cannot be null!");
        }

        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void update() {

    }

    public final void destroy() {
        if (gameObject.removeComponent(this)) {
            onDestroy();
        }
    }

    public void onDestroy() {

    }
}
