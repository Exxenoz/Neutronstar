package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents the alarm area around the planet.
 */
public class AlarmArea extends Component {
    public AlarmArea(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void onCollide(Collider other) {
        if (other.getGameObject().getComponent(Enemy.class) != null) {
            if (!gameObject.getGameView().isAlarmEnabled()) {
                gameObject.getGameView().setAlarmEnabled(true);
            }
        }
    }
}
