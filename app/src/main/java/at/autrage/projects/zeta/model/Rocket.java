package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.view.GameView;

public class Rocket extends Weapon {
    public Rocket(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);
    }

    @Override
    public void onCollide(Collider collider) {
        super.onCollide(collider);

        if (collider.getOwner() instanceof Enemy) {
            float explosionSpawnPositionX = getPositionX() + (collider.getOwner().getPositionX() - getPositionX()) / 2f;
            float explosionSpawnPositionY = getPositionY() + (collider.getOwner().getPositionY() - getPositionY()) / 2f;

            Explosion explosion = new Explosion(getGameView(), explosionSpawnPositionX, explosionSpawnPositionY,
                    AssetManager.getInstance().getAnimationSet(AnimationSets.Explosion1));
            explosion.setScaleFactor((getSizeX() / explosion.getSizeX()) * Pustafin.ExplosionSizeScaleFactor);

            destroy();
        }
    }

    public static Rocket createSmallRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        Rocket rocket = new Rocket(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.SmallRocket));
        rocket.setRotationAngle((float)(Math.atan2(directionY, directionX) * 180.0 / Math.PI) + 90f);
        rocket.setDirection(directionX, directionY);
        rocket.setSpeed(Pustafin.SmallRocketSpeedBase);
        rocket.setCollider(new CircleCollider(rocket, 32f));
        return rocket;
    }

    public static Rocket createBigRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        Rocket rocket = new Rocket(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.BigRocket));
        rocket.setRotationAngle((float)(Math.atan2(directionY, directionX) * 180.0 / Math.PI) + 90f);
        rocket.setDirection(directionX, directionY);
        rocket.setSpeed(Pustafin.BigRocketSpeedBase);
        rocket.setCollider(new CircleCollider(rocket, 40f));
        return rocket;
    }
}
