package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents the alarm area around the planet.
 */
public class AlarmArea extends Sprite {
    public AlarmArea(GameView gameView, float positionX, float positionY) {
        super(gameView, positionX, positionY, AnimationSet.None);
    }

    @Override
    public void onCollide(Collider collider) {
        super.onCollide(collider);

        if (collider.getOwner() instanceof Enemy) {
            if (!getGameView().isAlarmEnabled()) {
                getGameView().setAlarmEnabled(true);
            }
        }
    }
}
