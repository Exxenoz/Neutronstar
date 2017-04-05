package at.autrage.projects.zeta.prefab;

import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.model.AlarmArea;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Sprite;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.view.GameView;

public class GameViewUserInterfacePrefab extends Prefab {
    public GameObject ForegroundAlarmGameObject;

    public GameViewUserInterfacePrefab(GameView gameView) {
        super("GameViewUserInterfacePrefab", gameView);

        ForegroundAlarmGameObject = new GameObject(gameView, 0f, 0f, "ForegroundAlarm");
        Sprite sprite = ForegroundAlarmGameObject.addComponent(Sprite.class);
        sprite.setAnimationSet(AnimationSets.ForegroundAlarm);
        sprite.setAlpha(0f);
        sprite.playDefaultAnimationFromSet();
        ForegroundAlarmGameObject.addComponent(CircleCollider.class).setRadius(Pustafin.AlarmAreaRadius);
        ForegroundAlarmGameObject.addComponent(AlarmArea.class);

        Logger.D("Initialized game view user interface!");
    }

    @Override
    public void destroy() {
        if (ForegroundAlarmGameObject != null) {
            ForegroundAlarmGameObject.destroy();
            ForegroundAlarmGameObject = null;
        }
    }
}
