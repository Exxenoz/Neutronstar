package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents an enemy object in the game.
 */
public class Enemy extends Sprite {
    protected EnemySpawner m_Owner;
    protected HealthBar m_HealthBar;
    protected float m_Health;
    protected float m_HealthMaximum;
    protected float m_HealthPercent;
    protected float m_HitDamage;
    protected int m_Bounty;
    protected int m_Points;

    public Enemy(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_Owner = null;
        m_HealthBar = new HealthBar(gameView, getTransform(), Pustafin.EnemyHealthBarWidth, Pustafin.EnemyHealthBarHalfHeight);
        m_Health = 1f;
        m_HealthMaximum = 1f;
        m_HealthPercent = 1f;
        m_HitDamage = 0f;
        m_Bounty = 0;
        m_Points = 0;
    }

    @Override
    public void onCollide(Collider collider) {
        super.onCollide(collider);

        if (collider.getGameObject() instanceof Weapon) {
            Weapon weapon = (Weapon)collider.getGameObject();
            receiveDamage(weapon.getHitDamage());
        }
        else if (collider.getGameObject() instanceof Player) {
            explode(collider.getGameObject(), AnimationSets.Explosion1);
        }
    }

    public void receiveDamage(float damage) {
        setHealth(m_Health - damage);
        if (m_Health <= 0f) {
            GameManager.getInstance().setMoney(GameManager.getInstance().getMoney() + m_Bounty);
            GameManager.getInstance().setScore(GameManager.getInstance().getScore() + m_Points);

            destroy();
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        if (m_Owner != null) {
            m_Owner.onDestroyEnemy(this);
        }

        if (m_HealthBar != null) {
            m_HealthBar.destroy();
            m_HealthBar = null;
        }
    }

    public EnemySpawner getOwner() {
        return m_Owner;
    }

    public void setOwner(EnemySpawner owner) {
        this.m_Owner =owner;
    }

    public boolean isAlive() {
        return m_Health > 0f;
    }

    public float getHealth() {
        return m_Health;
    }

    public void setHealth(float health) {
        if (health < 0f) {
            health = 0f;
        }

        this.m_Health = health;

        if (health > m_HealthMaximum) {
            m_HealthMaximum = health;
        }

        m_HealthPercent = m_Health / m_HealthMaximum;

        if (m_HealthBar != null) {
            m_HealthBar.setHealthPercent(m_HealthPercent);
        }
    }

    public float getHealthMaximum() {
        return m_HealthMaximum;
    }

    public void setHealthMaximum(float healthMaximum) {
        this.m_HealthMaximum = healthMaximum;
        if (m_Health > healthMaximum) {
            m_Health = healthMaximum;
        }
    }

    public float getHitDamage() {
        return m_HitDamage;
    }

    public void setHitDamage(float hitDamage) {
        this.m_HitDamage = hitDamage;
    }

    public int getBounty() {
        return m_Bounty;
    }

    public void setBounty(int bounty) {
        this.m_Bounty = bounty;
    }

    public int getPoints() {
        return m_Points;
    }

    public void setPoints(int points) {
        this.m_Points = points;
    }
}
