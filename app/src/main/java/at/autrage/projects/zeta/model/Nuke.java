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
    private float m_EngineFireLengthOffset;
    private AnimationSets m_ExplosionAnimationSet;

    public Nuke(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_EngineFire = new Sprite(gameView, positionX, positionY, AssetManager.getInstance().getAnimationSet(AnimationSets.EngineFire));
        m_EngineFire.setScaleFactor(getScaleX() / m_EngineFire.getScaleX());
        m_EngineFire.setAnimationRepeatable(true);

        m_ExplosionAnimationSet = AnimationSets.Explosion1;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (m_EngineFire != null) {
            m_EngineFire.setPositionX(getPositionX() - (this.getDirectionX() * (getScaleX() + m_EngineFireLengthOffset)));
            m_EngineFire.setPositionY(getPositionY() - (this.getDirectionY() * (getScaleY() + m_EngineFireLengthOffset)));
            m_EngineFire.setRotationZ(180f + getRotationZ());
        }
    }

    @Override
    public void onCollide(Collider other) {
        super.onCollide(other);

        if (other.getGameObject() instanceof Enemy) {
            SoundManager.getInstance().PlaySFX(R.raw.sfx_hit_rocket, 0.5f + (float)Math.random());
            explode(other.getGameObject(), AnimationSets.Explosion1, true);
            explode(other.getGameObject(), m_ExplosionAnimationSet);
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        if (m_EngineFire != null) {
            m_EngineFire.destroy();
            m_EngineFire = null;
        }
    }

    public float getEngineFireLengthOffset() {
        return m_EngineFireLengthOffset;
    }

    public void setEngineFireLengthOffset(float engineFireLengthOffset) {
        this.m_EngineFireLengthOffset = engineFireLengthOffset;
    }

    public AnimationSets getExplosionAnimationSet() {
        return m_ExplosionAnimationSet;
    }

    public void setExplosionAnimationSet(AnimationSets explosionAnimationSet) {
        this.m_ExplosionAnimationSet = explosionAnimationSet;
    }

    public static Nuke createSmallNuke(Player player, float positionX, float positionY, float directionX, float directionY) {
        Nuke nuke = new Nuke(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.SmallNuke));
        nuke.setRotationZ((float)(Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        nuke.setDirection(directionX, directionY);
        nuke.setSpeed(GameManager.getInstance().getWeaponSpeed(Weapons.SmallNuke));
        nuke.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.SmallNuke));
        nuke.setAOEDamage(nuke.getHitDamage() * Pustafin.AOEDamageFactor);
        nuke.setAOERadius(GameManager.getInstance().getWeaponRadius(Weapons.SmallNuke));
        nuke.setExplosionAnimationSet(AnimationSets.Explosion2);
        nuke.addComponent(new CircleCollider(nuke, nuke.getHalfScaleX()));
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));
        return nuke;
    }

    public static Nuke createBigNuke(Player player, float positionX, float positionY, float directionX, float directionY) {
        Nuke nuke = new Nuke(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.BigNuke));
        nuke.setRotationZ((float)(Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        nuke.setDirection(directionX, directionY);
        nuke.setSpeed(GameManager.getInstance().getWeaponSpeed(Weapons.BigNuke));
        nuke.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.BigNuke));
        nuke.setAOEDamage(nuke.getHitDamage() * Pustafin.AOEDamageFactor);
        nuke.setAOERadius(GameManager.getInstance().getWeaponRadius(Weapons.BigNuke));
        nuke.setExplosionAnimationSet(AnimationSets.Explosion3);
        nuke.addComponent(new CircleCollider(nuke, nuke.getHalfScaleX()));
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));
        return nuke;
    }
}
