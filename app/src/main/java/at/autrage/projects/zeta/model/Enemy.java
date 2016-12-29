package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.AnimationSets;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.view.GameView;

public class Enemy extends GameObject {

    protected float m_Health;
    protected float m_HitDamage;
    protected int m_Bounty;
    protected int m_Points;

    public Enemy(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_Health = 1f;
        m_HitDamage = 0f;
        m_Bounty = 0;
        m_Points = 0;
    }

    @Override
    public void onCollide(Collider collider) {
        super.onCollide(collider);

        if (collider.getOwner() instanceof Weapon) {
            Weapon weapon = (Weapon)collider.getOwner();
            receiveDamage(weapon.getHitDamage());
        }
        else if (collider.getOwner() instanceof Player) {
            explode(collider.getOwner(), AnimationSets.Explosion1);
        }
    }

    public void receiveDamage(float damage) {
        m_Health -= damage;
        if (m_Health <= 0f) {
            m_Health = 0f;

            GameManager.getInstance().setMoney(GameManager.getInstance().getMoney() + m_Bounty);
            GameManager.getInstance().setScore(GameManager.getInstance().getScore() + m_Points);

            destroy();
        }
    }

    public boolean isAlive() {
        return m_Health > 0f;
    }

    public float getHealth() {
        return m_Health;
    }

    public void setHealth(float health) {
        this.m_Health = health;
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
