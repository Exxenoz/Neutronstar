package at.autrage.projects.zeta.collision;

import at.autrage.projects.zeta.exception.ArgumentNullException;
import at.autrage.projects.zeta.framework.Synchronitron;

public class ColliderManager {
    /**
     * Contains all active colliders
     */
    private Synchronitron<Collider> colliders;

    public ColliderManager() {
        colliders = new Synchronitron<Collider>(Collider.class, 256);
    }

    public void update() {
        colliders.synchronize();

        for (int i = 0, size = colliders.size(); i < size; i++) {
            Collider colliderI = colliders.get(i);

            for (int j = 0; j < i; j++) {
                Collider colliderJ = colliders.get(j);

                if (colliderI.intersects(colliderJ)) {
                    colliderI.getGameObject().onCollide(colliderJ);
                    colliderJ.getGameObject().onCollide(colliderI);
                }
            }
        }
    }

    public void addCollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        colliders.add(collider);
    }

    public void removeCollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        colliders.remove(collider);
    }
}
