package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.view.GameView;

public class Rocket extends Weapon {
    private GameObject m_EngineFire;
    private float m_EngineFireLengthOffset;

    public Rocket(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_EngineFire = new GameObject(gameView, positionX, positionY, AssetManager.getInstance().getAnimationSet(AnimationSets.EngineFire));
        m_EngineFire.setScaleFactor((float)this.getSizeX() / m_EngineFire.getSizeX());
        m_EngineFire.setAnimationRepeatable(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (m_EngineFire != null) {
            m_EngineFire.setPositionX(this.getPositionX() - (this.getDirectionX() * (this.getHalfSizeX() + getHalfSizeX() + m_EngineFireLengthOffset)));
            m_EngineFire.setPositionY(this.getPositionY() - (this.getDirectionY() * (this.getHalfSizeY() + getHalfSizeY() + m_EngineFireLengthOffset)));
            m_EngineFire.setRotationAngle(180f + this.getRotationAngle());
        }
    }

    @Override
    public void onCollide(Collider collider) {
        super.onCollide(collider);

        if (collider.getOwner() instanceof Enemy) {
            SoundManager.getInstance().PlaySFX(R.raw.sfx_hit_rocket, 0.5f + (float)Math.random());
            explode(collider.getOwner(), AnimationSets.Explosion1);
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
        rocket.setRotationAngle((float)(Math.atan2(directionY, directionX) * 180.0 / Math.PI) + 90f);
        rocket.setDirection(directionX, directionY);
        rocket.setSpeed(GameManager.getInstance().getWeaponSpeed(Weapons.SmallRocket));
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.SmallRocket));
        rocket.setCollider(new CircleCollider(rocket, 32f));
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));
        return rocket;
    }

    public static Rocket createBigRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        Rocket rocket = new Rocket(player.getGameView(), positionX, positionY,
                AssetManager.getInstance().getAnimationSet(AnimationSets.BigRocket));
        rocket.setRotationAngle((float)(Math.atan2(directionY, directionX) * 180.0 / Math.PI) + 90f);
        rocket.setDirection(directionX, directionY);
        rocket.setSpeed(GameManager.getInstance().getWeaponSpeed(Weapons.BigRocket));
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.BigRocket));
        rocket.setCollider(new CircleCollider(rocket, 40f));
        rocket.setEngineFireLengthOffset(-10f);
        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));
        return rocket;
    }
}
