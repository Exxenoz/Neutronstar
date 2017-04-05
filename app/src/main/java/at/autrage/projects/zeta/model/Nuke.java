package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.animation.AnimationSets;
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

    public Nuke(GameObject gameObject) {
        super(gameObject);

        m_EngineFire = null;
        m_ExplosionAnimationSet = AnimationSets.Explosion1;
    }

    @Override
    protected void onStart() {
        super.onStart();

        GameObject engineFireGameObject = new GameObject(gameObject.getGameView(), gameObject.getPositionX(), gameObject.getPositionY(), "EngineFire");
        engineFireGameObject.setParent(gameObject);

        m_EngineFire = engineFireGameObject.addComponent(Sprite.class);
        m_EngineFire.setAnimationSet(AnimationSets.EngineFire);
        m_EngineFire.setAnimationRepeatable(true);
        m_EngineFire.playDefaultAnimationFromSet();
        m_EngineFire.setScaleFactorToMatchFrameSizeX(gameObject.getScaleX());

        engineFireGameObject.setLocalRotationZ(180f);
        engineFireGameObject.setLocalPositionY(gameObject.getHalfScaleY() + engineFireGameObject.getHalfScaleY());
    }

    @Override
    public void onCollide(Collider other) {
        super.onCollide(other);

        Enemy enemy = other.gameObject.getComponent(Enemy.class);
        if (enemy != null) {
            enemy.receiveDamage(getHitDamage());

            SoundManager.getInstance().PlaySFX(R.raw.sfx_hit_rocket, 0.5f + (float) Math.random());

            Sprite sprite = gameObject.getComponent(Sprite.class);
            if (sprite != null) {
                sprite.explode(other.getGameObject(), AnimationSets.Explosion1, true);
                sprite.explode(other.getGameObject(), m_ExplosionAnimationSet);
            }

            gameObject.destroy();
        }
    }

    public AnimationSets getExplosionAnimationSet() {
        return m_ExplosionAnimationSet;
    }

    public void setExplosionAnimationSet(AnimationSets explosionAnimationSet) {
        this.m_ExplosionAnimationSet = explosionAnimationSet;
    }

    public static Nuke createSmallNuke(GameView gameView, float positionX, float positionY, float directionX, float directionY) {
        GameObject gameObject = new GameObject(gameView, positionX, positionY, "SmallNuke");
        gameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);

        Sprite sprite = gameObject.addComponent(Sprite.class);
        sprite.setAnimationSet(AnimationSets.SmallNuke);
        sprite.playDefaultAnimationFromSet();

        gameObject.addComponent(CircleCollider.class).setRadius(gameObject.getHalfScaleX());
        gameObject.addComponent(LinearMovement.class).initialize(directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.SmallNuke));

        Nuke nuke = gameObject.addComponent(Nuke.class);
        nuke.setExplosionAnimationSet(AnimationSets.Explosion2);
        nuke.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.SmallNuke));
        nuke.setAOEDamage(nuke.getHitDamage() * Pustafin.AOEDamageFactor);
        nuke.setAOERadius(GameManager.getInstance().getWeaponRadius(Weapons.SmallNuke));

        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        return nuke;
    }

    public static Nuke createBigNuke(GameView gameView, float positionX, float positionY, float directionX, float directionY) {
        GameObject gameObject = new GameObject(gameView, positionX, positionY, "BigNuke");
        gameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);

        Sprite sprite = gameObject.addComponent(Sprite.class);
        sprite.setAnimationSet(AnimationSets.BigNuke);
        sprite.playDefaultAnimationFromSet();

        gameObject.addComponent(CircleCollider.class).setRadius(gameObject.getHalfScaleX());
        gameObject.addComponent(LinearMovement.class).initialize(directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.BigNuke));

        Nuke nuke = gameObject.addComponent(Nuke.class);
        nuke.setExplosionAnimationSet(AnimationSets.Explosion3);
        nuke.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.BigNuke));
        nuke.setAOEDamage(nuke.getHitDamage() * Pustafin.AOEDamageFactor);
        nuke.setAOERadius(GameManager.getInstance().getWeaponRadius(Weapons.BigNuke));

        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        return nuke;
    }
}
