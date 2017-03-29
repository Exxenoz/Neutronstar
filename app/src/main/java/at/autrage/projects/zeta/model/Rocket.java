package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.SoundManager;

public class Rocket extends Weapon {
    private Sprite m_EngineFire;
    private float engineFireOffset;

    public Rocket(GameObject gameObject) {
        super(gameObject);

        this.m_EngineFire = null;
        this.engineFireOffset = 0f;
    }

    @Override
    protected void onStart() {
        super.onStart();

        GameObject engineFireGameObject = new GameObject(gameObject.getGameView(), gameObject.getPositionX(), gameObject.getPositionY());
        engineFireGameObject.setParent(gameObject);

        m_EngineFire = engineFireGameObject.addComponent(Sprite.class);
        m_EngineFire.setAnimationSet(AnimationSets.EngineFire);
        m_EngineFire.setAnimationRepeatable(true);
        m_EngineFire.playDefaultAnimationFromSet();
        m_EngineFire.setScaleFactorToMatchFrameSizeX(gameObject.getScaleX());

        engineFireGameObject.setLocalRotationZ(180f);
        engineFireGameObject.setLocalPositionY(gameObject.getHalfScaleY() + engineFireGameObject.getHalfScaleY() + engineFireOffset);
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
            }

            gameObject.destroy();
        }
    }

    public float getEngineFireOffset() {
        return engineFireOffset;
    }

    public void setEngineFireOffset(float engineFireOffset) {
        this.engineFireOffset = engineFireOffset;

        if (m_EngineFire != null) {
            m_EngineFire.getGameObject().setLocalPositionY(gameObject.getHalfScaleY() + m_EngineFire.getGameObject().getHalfScaleY() + engineFireOffset);
        }
    }

    public static Rocket createSmallRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        GameObject gameObject = new GameObject(player.gameObject.getGameView(), positionX, positionY, "SmallRocket");
        gameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);

        Sprite sprite = gameObject.addComponent(Sprite.class);
        sprite.setAnimationSet(AnimationSets.SmallRocket);
        sprite.playDefaultAnimationFromSet();

        gameObject.addComponent(CircleCollider.class).setRadius(gameObject.getHalfScaleX());
        gameObject.addComponent(LinearMovement.class).initialize(directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.SmallRocket));

        Rocket rocket = gameObject.addComponent(Rocket.class);
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.SmallRocket));

        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        return rocket;
    }

    public static Rocket createBigRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        GameObject gameObject = new GameObject(player.gameObject.getGameView(), positionX, positionY, "BigRocket");
        gameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);

        Sprite sprite = gameObject.addComponent(Sprite.class);
        sprite.setAnimationSet(AnimationSets.BigRocket);
        sprite.playDefaultAnimationFromSet();

        gameObject.addComponent(CircleCollider.class).setRadius(gameObject.getHalfScaleX());
        gameObject.addComponent(LinearMovement.class).initialize(directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.BigRocket));

        Rocket rocket = gameObject.addComponent(Rocket.class);
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.BigRocket));
        rocket.setEngineFireOffset(-10f);

        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        return rocket;
    }
}
