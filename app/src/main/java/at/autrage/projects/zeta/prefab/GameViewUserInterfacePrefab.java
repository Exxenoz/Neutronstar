package at.autrage.projects.zeta.prefab;

import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.model.AlarmArea;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Sprite;
import at.autrage.projects.zeta.model.WeaponLaunchArea;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.view.GameView;

public class GameViewUserInterfacePrefab extends Prefab {
    public GameObject ForegroundAlarmGameObject;
    public GameObject WeaponLaunchAreaGameObject;

    public GameViewUserInterfacePrefab(GameView gameView) {
        super("GameViewUserInterfacePrefab", gameView);

        ForegroundAlarmGameObject = new GameObject(gameView, 0f, 0f, "ForegroundAlarm");
        Sprite sprite = ForegroundAlarmGameObject.addComponent(Sprite.class);
        sprite.setAnimationSet(AnimationSets.ForegroundAlarm);
        sprite.setAlpha(0f);
        sprite.setDrawOrderID(1000);
        sprite.playDefaultAnimationFromSet();
        ForegroundAlarmGameObject.addComponent(CircleCollider.class).setRadius(Pustafin.AlarmAreaRadius);
        ForegroundAlarmGameObject.addComponent(AlarmArea.class);

        WeaponLaunchAreaGameObject = new GameObject(gameView, 0f, 0f, GameObject.Layer.UI, "WeaponLaunchAreaGameObject");
        WeaponLaunchAreaGameObject.addComponent(CircleCollider.class).setRadius(Pustafin.PlanetTouchRadius);
        WeaponLaunchAreaGameObject.addComponent(WeaponLaunchArea.class);

        Logger.D("Initialized game view user interface!");
    }

    @Override
    public void destroy() {
        if (ForegroundAlarmGameObject != null) {
            ForegroundAlarmGameObject.destroy();
            ForegroundAlarmGameObject = null;
        }

        if (WeaponLaunchAreaGameObject != null) {
            WeaponLaunchAreaGameObject.destroy();
            WeaponLaunchAreaGameObject = null;
        }
    }
}
