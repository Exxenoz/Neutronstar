package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.view.GameView;

public class Rocket extends Weapon {
    public Rocket(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);
    }

    public static Rocket createSmallRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        Rocket smallRocket = new Rocket(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.SmallRocket));
        smallRocket.setRotationAngle((float)(Math.atan2(directionY, directionX) * 180.0 / Math.PI) + 90f);
        smallRocket.setDirection(directionX, directionY);
        smallRocket.setSpeed(Pustafin.SmallRocketSpeedBase);
        return smallRocket;
    }

    public static Rocket createBigRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        Rocket smallRocket = new Rocket(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.BigRocket));
        smallRocket.setRotationAngle((float)(Math.atan2(directionY, directionX) * 180.0 / Math.PI) + 90f);
        smallRocket.setDirection(directionX, directionY);
        smallRocket.setSpeed(Pustafin.BigRocketSpeedBase);
        return smallRocket;
    }
}
