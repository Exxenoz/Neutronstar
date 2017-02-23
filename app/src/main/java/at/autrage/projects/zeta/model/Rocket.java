package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.view.GameView;

public class Rocket extends Weapon {
    private Sprite m_EngineFire;
    private float m_EngineFireLengthOffset;

    public Rocket(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_EngineFire = new Sprite(gameView, positionX, positionY, AssetManager.getInstance().getAnimationSet(AnimationSets.EngineFire));
        m_EngineFire.setScaleFactor(getScaleX() / m_EngineFire.getScaleX());
        m_EngineFire.setAnimationRepeatable(true);
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
            explode(other.getGameObject(), AnimationSets.Explosion1);
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

    public static Rocket createSmallRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        Rocket rocket = new Rocket(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.SmallRocket));
        rocket.setRotationZ((float)(Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        rocket.setDirection(directionX, directionY);
        rocket.setSpeed(GameManager.getInstance().getWeaponSpeed(Weapons.SmallRocket));
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.SmallRocket));
        rocket.addComponent(new CircleCollider(rocket, rocket.getHalfScaleX()));
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));
        return rocket;
    }

    public static Rocket createBigRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        Rocket rocket = new Rocket(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.BigRocket));
        rocket.setRotationZ((float)(Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        rocket.setDirection(directionX, directionY);
        rocket.setSpeed(GameManager.getInstance().getWeaponSpeed(Weapons.BigRocket));
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.BigRocket));
        rocket.addComponent(new CircleCollider(rocket, rocket.getHalfScaleX()));
        rocket.setEngineFireLengthOffset(-10f);
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));
        return rocket;
    }
}
