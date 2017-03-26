package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;

/**
 * This class represents an enemy object in the game.
 */
public class Enemy extends Component {
    private EnemySpawner spawner;
    private HealthBar m_HealthBar;
    private float m_HealthMaximum;
    private float m_HealthPercent;
    private float m_Health;
    private float m_HitDamage;
    private int m_Bounty;
    private int m_Points;

    public Enemy(GameObject gameObject) {
        super(gameObject);

        this.spawner = null;
        this.m_HealthBar = null;
        this.m_HealthMaximum = 1f;
        this.m_HealthPercent = 0f;
        this.m_Health = 0f;
        this.m_HitDamage = 0f;
        this.m_Bounty = 0;
        this.m_Points = 0;
    }

    public void initialize(EnemySpawner spawner, float health, float hitDamage, int bounty, int points) {
        setSpawner(spawner);
        setHealth(health);
        setHitDamage(hitDamage);
        setBounty(bounty);
        setPoints(points);
    }

    @Override
    protected void onStart() {
        createHealthbar();
    }

    private void createHealthbar() {
        GameObject healthBarGameObject = new GameObject(gameObject.getGameView(), gameObject.getPositionX(), gameObject.getPositionY());
        healthBarGameObject.setIgnoreParentRotation(true);
        healthBarGameObject.setParent(gameObject);
        healthBarGameObject.setLocalPosition(
                Pustafin.EnemyHealthBarOffsetX,
                gameObject.getHalfScaleY() + Pustafin.EnemyHealthBarHalfHeight + Pustafin.EnemyHealthBarOffsetY
        );

        m_HealthBar = healthBarGameObject.addComponent(HealthBar.class);
        m_HealthBar.setFullWidth(Pustafin.EnemyHealthBarWidth);
        m_HealthBar.setFullHeight(Pustafin.EnemyHealthBarHeight);
        m_HealthBar.setHealthPercent(m_HealthPercent);
    }

    @Override
    public void onCollide(Collider other) {
        Player player = other.gameObject.getComponent(Player.class);
        if (player != null) {
            player.receiveDamage(m_HitDamage);

            SoundManager.getInstance().PlaySFX(R.raw.sfx_hit_planet, 0.5f + (float) Math.random());

            Sprite sprite = gameObject.getComponent(Sprite.class);
            if (sprite != null) {
                sprite.explode(other.getGameObject(), AnimationSets.Explosion1);
            }

            gameObject.destroy();
        }
    }

    public void receiveDamage(float damage) {
        setHealth(m_Health - damage);
        if (m_Health <= 0f) {
            GameManager.getInstance().setMoney(GameManager.getInstance().getMoney() + m_Bounty);
            GameManager.getInstance().setScore(GameManager.getInstance().getScore() + m_Points);

            gameObject.destroy();
        }
    }

    @Override
    protected void onDestroy() {
        if (spawner != null) {
            spawner.onDestroyEnemy(this);
        }

        if (m_HealthBar != null) {
            m_HealthBar.destroy();
        }
    }

    public EnemySpawner getSpawner() {
        return spawner;
    }

    private void setSpawner(EnemySpawner spawner) {
        this.spawner = spawner;
    }

    public HealthBar getHealthBar() {
        return m_HealthBar;
    }

    private void setHealthBar(HealthBar healthBar) {
        this.m_HealthBar = healthBar;
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
