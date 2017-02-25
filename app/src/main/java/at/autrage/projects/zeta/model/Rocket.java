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

    public Rocket(GameView gameView, float positionX, float positionY, AnimationSet animationSet, float engineFireOffset) {
        super(gameView, positionX, positionY, animationSet);

        m_EngineFire = new Sprite(gameView, positionX, positionY, AssetManager.getInstance().getAnimationSet(AnimationSets.EngineFire));
        m_EngineFire.setScaleFactor(getScaleX() / m_EngineFire.getScaleX());
        m_EngineFire.setAnimationRepeatable(true);
        m_EngineFire.setLocalRotationZ(180f);
        m_EngineFire.setParent(this);
        m_EngineFire.setLocalPositionY(getHalfScaleY() + m_EngineFire.getHalfScaleY() + engineFireOffset);
    }

    @Override
    public void onCollide(Collider other) {
        super.onCollide(other);

        if (other.getGameObject() instanceof Enemy) {
            SoundManager.getInstance().PlaySFX(R.raw.sfx_hit_rocket, 0.5f + (float) Math.random());
            explode(other.getGameObject(), AnimationSets.Explosion1);
        }
    }

    public static Rocket createSmallRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        Rocket rocket = new Rocket(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.SmallRocket), 0);
        rocket.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.SmallRocket));
        rocket.addComponent(new CircleCollider(rocket, rocket.getHalfScaleX()));
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        rocket.addComponent(new LinearMovement(rocket, directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.SmallRocket)));

        return rocket;
    }

    public static Rocket createBigRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        Rocket rocket = new Rocket(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.BigRocket), -10f);
        rocket.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.BigRocket));
        rocket.addComponent(new CircleCollider(rocket, rocket.getHalfScaleX()));
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        rocket.addComponent(new LinearMovement(rocket, directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.BigRocket)));

        return rocket;
    }
}
