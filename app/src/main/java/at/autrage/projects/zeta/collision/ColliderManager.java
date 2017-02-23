package at.autrage.projects.zeta.collision;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import at.autrage.projects.zeta.exception.ArgumentNullException;
import at.autrage.projects.zeta.module.Pustafin;

public class ColliderManager {
    /**
     * Contains all active colliders
     */
    private List<Collider> colliderList;

    private Queue<Collider> collidersToAdd;
    private Queue<Collider> collidersToRemove;

    public ColliderManager() {
        colliderList = new ArrayList<>(256);

        collidersToAdd = new LinkedList<>();
        collidersToRemove = new LinkedList<>();
    }

    public void update() {
        while (collidersToAdd.size() > 0) {
            colliderList.add(collidersToAdd.poll());
        }

        while (collidersToRemove.size() > 0) {
            colliderList.remove(collidersToRemove.poll());
        }

        for (int i = 0, size = colliderList.size(); i < size; i++) {
            Collider colliderI = colliderList.get(i);

            for (int j = 0; j < i; j++) {
                Collider colliderJ = colliderList.get(j);

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

        if (colliderList.contains(collider)) {
            throw new IllegalArgumentException();
        }

        collidersToAdd.add(collider);
    }

    public void removeCollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        collidersToRemove.add(collider);
    }
}
