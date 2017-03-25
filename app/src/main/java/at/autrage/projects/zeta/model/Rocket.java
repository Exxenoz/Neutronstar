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
    private float engineFireOffset;

    public Rocket(float engineFireOffset) {
        super();

        this.m_EngineFire = null;
        this.engineFireOffset = engineFireOffset;
    }

    @Override
    protected void onStart() {
        super.onStart();

        GameObject engineFireGameObject = new GameObject(gameObject.getGameView(), gameObject.getPositionX(), gameObject.getPositionY());
        engineFireGameObject.setLocalRotationZ(180f);
        engineFireGameObject.setParent(gameObject);
        engineFireGameObject.setLocalPositionY(gameObject.getHalfScaleY() + engineFireGameObject.getHalfScaleY() + engineFireOffset);

        m_EngineFire = new Sprite(AssetManager.getInstance().getAnimationSet(AnimationSets.EngineFire));
        m_EngineFire.setScaleFactor(gameObject.getScaleX() / engineFireGameObject.getScaleX());
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
                sprite.explode(other.getGameObject(), AnimationSets.Explosion1);
            }
        }
    }

    public static Rocket createSmallRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        GameObject gameObject = new GameObject(player.gameObject.getGameView(), positionX, positionY);
        gameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        gameObject.addComponent(new Sprite(AssetManager.getInstance().getAnimationSet(AnimationSets.SmallRocket)));
        gameObject.addComponent(new CircleCollider(gameObject.getHalfScaleX()));
        gameObject.addComponent(new LinearMovement(directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.SmallRocket)));

        Rocket rocket = new Rocket(0f);
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.SmallRocket));
        gameObject.addComponent(rocket);

        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        return rocket;
    }

    public static Rocket createBigRocket(Player player, float positionX, float positionY, float directionX, float directionY) {
        GameObject gameObject = new GameObject(player.gameObject.getGameView(), positionX, positionY);
        gameObject.setRotationZ((float) (Math.atan2(directionY, directionX) * 180.0 / Math.PI) - 90f);
        gameObject.addComponent(new Sprite(AssetManager.getInstance().getAnimationSet(AnimationSets.BigRocket)));
        gameObject.addComponent(new CircleCollider(gameObject.getHalfScaleX()));
        gameObject.addComponent(new LinearMovement(directionX, directionY, GameManager.getInstance().getWeaponSpeed(Weapons.BigRocket)));

        Rocket rocket = new Rocket(-10f);
        rocket.setHitDamage(GameManager.getInstance().getWeaponHitDamage(Weapons.BigRocket));
        gameObject.addComponent(rocket);

        SoundManager.getInstance().PlaySFX(R.raw.sfx_launch_rocket, (float) (Math.random() + 0.5f));

        return rocket;
    }
}
