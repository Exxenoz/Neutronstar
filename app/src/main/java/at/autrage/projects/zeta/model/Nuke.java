package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents a nuke object in the game.
 */
public class Nuke extends Weapon {
    private Sprite m_EngineFire;
    private AnimationSets m_ExplosionAnimationSet;

    public Nuke(GameObject gameObject, AnimationSet animationSet) {
        super(gameObject);

        new Sprite(gameObject, animationSet);

        GameObject engineFireGameObject = new GameObject(gameObject.getGameView(), gameObject.getPositionX(), gameObject.getPositionY());
        m_EngineFire = new Sprite(engineFireGameObject, AssetManager.getInstance().getAnimationSet(AnimationSets.EngineFire));
        m_EngineFire.setScaleFactor(gameObject.getScaleX() / engineFireGameObject.getScaleX());
        m_EngineFire.setAnimationRepeatable(true);
        engineFireGameObject.setLocalRotationZ(180f);
        engineFireGameObject.setParent(gameObject);
        engineFireGameObject.setLocalPositionY(gameObject.getHalfScaleY() + engineFireGameObject.getHalfScaleY());

        m_ExplosionAnimationSet = AnimationSets.Explosion1;
    }

    @Override
    public void onCollide(Collider other) {
        super.onCollide(other);

        if (other.gameObject.getComponent(Enemy.class) != null) {
            Sprite sprite = gameObject.getComponent(Sprite.class);
            if (sprite != null) {
                SoundManager.getInstance().PlaySFX(R.raw.sfx_hit_rocket, 0.5f + (float) Math.random());
                sprite.explode(other.getGameObject(), AnimationSets.Explosion1, true);
                sprite.explode(other.getGameObject(), m_ExplosionAnimationSet);
            }
        }
    }

    public AnimationSets getExplosionAnimationSet() {
        return m_ExplosionAnimationSet;
    }

    public void setExplosionAnimationSet(AnimationSets explosionAnimationSet) {
        this.m_ExplosionAnimationSet = explosionAnimationSet;
    }

    public static Nuke createSmallNuke(Player player, float positionX, float positionY, float directionX, float directionY) {
        GameObject nukeGameObject = new GameObject(player.gameObject.getGameView(), positionX, positionY);

        Nuke nuke = new Nuke(nukeGameObject, AssetManager.getInstance().getAnimationSet(AnimationSets.SmallNuke));
        nukeGameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        nuke.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.SmallNuke));
        nuke.setAOEDamage(nuke.getHitDamage() * Pustafin.AOEDamageFactor);
        nuke.setAOERadius(GameManager.getInstance().getWeaponRadius(Weapons.SmallNuke));
        nuke.setExplosionAnimationSet(AnimationSets.Explosion2);
        new CircleCollider(nukeGameObject, nukeGameObject.getHalfScaleX());
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        new LinearMovement(nukeGameObject, directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.SmallNuke));

        return nuke;
    }

    public static Nuke createBigNuke(Player player, float positionX, float positionY, float directionX, float directionY) {
        GameObject bigNukeGameObject = new GameObject(player.gameObject.getGameView(), positionX, positionY);

        Nuke nuke = new Nuke(bigNukeGameObject, AssetManager.getInstance().getAnimationSet(AnimationSets.BigNuke));
        bigNukeGameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        nuke.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.BigNuke));
        nuke.setAOEDamage(nuke.getHitDamage() * Pustafin.AOEDamageFactor);
        nuke.setAOERadius(GameManager.getInstance().getWeaponRadius(Weapons.BigNuke));
        nuke.setExplosionAnimationSet(AnimationSets.Explosion3);
        new CircleCollider(bigNukeGameObject, bigNukeGameObject.getHalfScaleX());
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        new LinearMovement(bigNukeGameObject, directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.BigNuke));

        return nuke;
    }
}
