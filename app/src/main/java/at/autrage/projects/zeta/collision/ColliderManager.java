package at.autrage.projects.zeta.collision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import at.autrage.projects.zeta.exception.ArgumentNullException;
import at.autrage.projects.zeta.module.Pustafin;

public class ColliderManager {
    /** Contains all active colliders */
    private List<Collider> colliderList;
    /** Contains a collider list for each cell */
    private Map<Long, List<Collider>> colliderGrid;

    private Queue<Collider> collidersToInsert;
    private Queue<Collider> collidersToRemove;

    public ColliderManager() {
        colliderList = new ArrayList<>(256);
        colliderGrid = new HashMap<>(128);

        collidersToInsert = new LinkedList<>();
        collidersToRemove = new LinkedList<>();
    }

    public void update() {
        while (collidersToInsert.size() > 0) {
            addCollider(collidersToInsert.poll());
        }

        while (collidersToRemove.size() > 0) {
            removeCollider(collidersToRemove.poll());
        }

        for (int i = 0, size = colliderList.size(); i < size; i++) {
            Collider collider = colliderList.get(i);

            long cellX = (long) collider.getPositionX() >> Pustafin.ColliderGridSizeBits;
            long cellY = (long) collider.getPositionY() >> Pustafin.ColliderGridSizeBits;

            long cellID = (cellX << 32) | cellY;

            if (cellID == collider.cellID) {
                continue;
            }

            if (collider.cellList != null) {
                collider.cellList.remove(collider);
            }

            collider.cellList = colliderGrid.get(cellID);
            if (collider.cellList == null) {
                colliderGrid.put(cellID, collider.cellList = new ArrayList<>());
            }

            collider.cellID = cellID;
            collider.cellList.add(collider);
        }

        for (int i = 0, size = colliderList.size(); i < size; i++) {
            Collider collider = colliderList.get(i);

            long topLeftCellX = (long)(collider.getPositionX() - collider.getApproximatedHalfRectWidth()) >> Pustafin.ColliderGridSizeBits;
            long topLeftCellY = (long)(collider.getPositionY() + collider.getApproximatedHalfRectHeight()) >> Pustafin.ColliderGridSizeBits;

            long bottomRightCellX = (long)(collider.getPositionX() + collider.getApproximatedHalfRectWidth()) >> Pustafin.ColliderGridSizeBits;
            long bottomRightCellY = (long)(collider.getPositionY() - collider.getApproximatedHalfRectHeight()) >> Pustafin.ColliderGridSizeBits;

            for (long cellY = topLeftCellY; cellY >= bottomRightCellY; cellY--) {
                for (long cellX = topLeftCellX; cellX <= bottomRightCellX; cellX++) {
                    List<Collider> cellList = colliderGrid.get((cellX << 32) | cellY);

                    if (cellList == null) {
                        continue;
                    }

                    for (int j = 0, size2 = cellList.size(); j < size2; j++) {
                        Collider collider2 = cellList.get(j);

                        if (collider2 == collider) {
                            continue;
                        }

                        if (collider2.intersects(collider)) {
                            collider2.getGameObject().onCollide(collider);
                        }
                    }
                }
            }
        }
    }

    public void addColliderToInsertQueue(Collider collider) {
        collidersToInsert.add(collider);
    }

    public void addColliderToRemoveQueue(Collider collider) {
        collidersToRemove.add(collider);
    }

    private boolean addCollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        if (colliderList.contains(collider)) {
            throw new IllegalArgumentException();
        }

        if (collider.cellList != null) {
            throw new IllegalArgumentException();
        }

        long cellX = (long) collider.getPositionX() >> Pustafin.ColliderGridSizeBits;
        long cellY = (long) collider.getPositionY() >> Pustafin.ColliderGridSizeBits;

        long cellID = (cellX << 32) | cellY;

        List<Collider> cellList = colliderGrid.get(cellID);
        if (cellList == null) {
            colliderGrid.put(cellID, cellList = new ArrayList<>());
        }

        collider.cellID = cellID;
        collider.cellList = cellList;
        collider.cellList.add(collider);

        colliderList.add(collider);

        return true;
    }

    private boolean removeCollider(Collider collider) {
        if (collider == null) {
            throw new ArgumentNullException();
        }

        if (!colliderList.remove(collider)) {
            throw new IllegalArgumentException();
        }

        if (collider.cellList == null) {
            throw new IllegalArgumentException();
        }

        collider.cellID = 0;
        collider.cellList.remove(collider);
        collider.cellList = null;

        return true;
    }
}
