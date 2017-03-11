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
public class Enemy extends Component {
    protected EnemySpawner m_Owner;
    protected HealthBar m_HealthBar;
    protected float m_Health;
    protected float m_HealthMaximum;
    protected float m_HealthPercent;
    protected float m_HitDamage;
    protected int m_Bounty;
    protected int m_Points;

    public Enemy(GameObject gameObject) {
        super(gameObject);

        GameObject healthBarGameObject = new GameObject(gameObject.getGameView(), gameObject.getPositionX(), gameObject.getPositionY());
        healthBarGameObject.setParent(gameObject);
        healthBarGameObject.setLocalPosition(
                Pustafin.EnemyHealthBarOffsetX - (Pustafin.EnemyHealthBarWidth - gameObject.getScaleX()) / 2f,
                gameObject.getHalfScaleY() + Pustafin.EnemyHealthBarHalfHeight + Pustafin.EnemyHealthBarOffsetY
        );

        m_Owner = null;
        m_HealthBar = new HealthBar(healthBarGameObject, Pustafin.EnemyHealthBarWidth, Pustafin.EnemyHealthBarHalfHeight);
        m_Health = 1f;
        m_HealthMaximum = 1f;
        m_HealthPercent = 1f;
        m_HitDamage = 0f;
        m_Bounty = 0;
        m_Points = 0;
    }

    @Override
    public void onCollide(Collider other) {
        Weapon weapon = other.gameObject.getComponent(Weapon.class);
        if (weapon != null) {
            receiveDamage(weapon.getHitDamage());
        }
        else if (other.gameObject.getComponent(Player.class) != null) {
            Sprite sprite = other.gameObject.getComponent(Sprite.class);
            if (sprite != null) {
                sprite.explode(other.getGameObject(), AnimationSets.Explosion1);
            }
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
    protected void onDestroy() {
        if (m_Owner != null) {
            m_Owner.onDestroyEnemy(this);
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
