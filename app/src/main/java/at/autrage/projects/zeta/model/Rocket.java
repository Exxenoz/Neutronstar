package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.view.GameView;

public class Rocket extends Weapon {
    private Sprite m_EngineFire;

    public Rocket(GameObject gameObject, AnimationSet animationSet, float engineFireOffset) {
        super(gameObject);

        new Sprite(gameObject, animationSet);

        GameObject engineFireGameObject = new GameObject(gameObject.getGameView(), gameObject.getPositionX(), gameObject.getPositionY());
        m_EngineFire = new Sprite(engineFireGameObject, AssetManager.getInstance().getAnimationSet(AnimationSets.EngineFire));
        m_EngineFire.setScaleFactor(gameObject.getScaleX() / engineFireGameObject.getScaleX());
        m_EngineFire.setAnimationRepeatable(true);
        engineFireGameObject.setLocalRotationZ(180f);
        engineFireGameObject.setParent(gameObject);
        engineFireGameObject.setLocalPositionY(gameObject.getHalfScaleY() + engineFireGameObject.getHalfScaleY() + engineFireOffset);
    }

    @Override
    public void onCollide(Collider other) {
        super.onCollide(other);

        if (other.gameObject.getComponent(Enemy.class) != null) {
            Sprite sprite = gameObject.getComponent(Sprite.class);
            if (sprite != null) {
                SoundManager.getInstance().PlaySFX(R.raw.sfx_hit_rocket, 0.5f + (float) Math.random());
                sprite.explode(other.getGameObject(), AnimationSets.Explosion1);
            }
        }
    }

    public static Rocket createSmallRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        GameObject rocketGameObject = new GameObject(player.gameObject.getGameView(), positionX, positionY);

        Rocket rocket = new Rocket(rocketGameObject, AssetManager.getInstance().getAnimationSet(AnimationSets.SmallRocket), 0);
        rocketGameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.SmallRocket));
        new CircleCollider(rocketGameObject, rocketGameObject.getHalfScaleX());
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        new LinearMovement(rocketGameObject, directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.SmallRocket));

        return rocket;
    }

    public static Rocket createBigRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        GameObject bigRocketGameObject = new GameObject(player.gameObject.getGameView(), positionX, positionY);

        Rocket rocket = new Rocket(bigRocketGameObject, AssetManager.getInstance().getAnimationSet(AnimationSets.BigRocket), -10f);
        bigRocketGameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.BigRocket));
        new CircleCollider(bigRocketGameObject, bigRocketGameObject.getHalfScaleX());
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        new LinearMovement(bigRocketGameObject, directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.BigRocket));

        return rocket;
    }
}
