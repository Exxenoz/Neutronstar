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

    public Nuke() {
        super();

        m_EngineFire = null;
        m_ExplosionAnimationSet = AnimationSets.Explosion1;
    }

    @Override
    protected void onStart() {
        super.onStart();

        GameObject engineFireGameObject = new GameObject(gameObject.getGameView(), gameObject.getPositionX(), gameObject.getPositionY());
        engineFireGameObject.setLocalRotationZ(180f);
        engineFireGameObject.setParent(gameObject);
        engineFireGameObject.setLocalPositionY(gameObject.getHalfScaleY() + engineFireGameObject.getHalfScaleY());

        m_EngineFire = new Sprite(AssetManager.getInstance().getAnimationSet(AnimationSets.EngineFire), gameObject.getScaleX() / engineFireGameObject.getScaleX());
        m_EngineFire.setAnimationRepeatable(true);
        engineFireGameObject.addComponent(m_EngineFire);
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
        GameObject gameObject = new GameObject(player.gameObject.getGameView(), positionX, positionY);
        gameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        gameObject.addComponent(new Sprite(AssetManager.getInstance().getAnimationSet(AnimationSets.SmallNuke)));
        gameObject.addComponent(new CircleCollider(gameObject.getHalfScaleX()));
        gameObject.addComponent(new LinearMovement(directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.SmallNuke)));

        Nuke nuke = new Nuke();
        nuke.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.SmallNuke));
        nuke.setAOEDamage(nuke.getHitDamage() * Pustafin.AOEDamageFactor);
        nuke.setAOERadius(GameManager.getInstance().getWeaponRadius(Weapons.SmallNuke));
        nuke.setExplosionAnimationSet(AnimationSets.Explosion2);
        gameObject.addComponent(nuke);

        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        return nuke;
    }

    public static Nuke createBigNuke(Player player, float positionX, float positionY, float directionX, float directionY) {
        GameObject gameObject = new GameObject(player.gameObject.getGameView(), positionX, positionY);
        gameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        gameObject.addComponent(new Sprite(AssetManager.getInstance().getAnimationSet(AnimationSets.BigNuke)));
        gameObject.addComponent(new CircleCollider(gameObject.getHalfScaleX()));
        gameObject.addComponent(new LinearMovement(directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.BigNuke)));

        Nuke nuke = new Nuke();
        nuke.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.BigNuke));
        nuke.setAOEDamage(nuke.getHitDamage() * Pustafin.AOEDamageFactor);
        nuke.setAOERadius(GameManager.getInstance().getWeaponRadius(Weapons.BigNuke));
        nuke.setExplosionAnimationSet(AnimationSets.Explosion3);
        gameObject.addComponent(nuke);

        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        return nuke;
    }
}
