package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.Collider;
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
            Weapon weapon = (Weapon) collider.getOwner();
            m_Health -= weapon.getHitDamage();
            if (m_Health <= 0f) {
                m_Health = 0f;

                Player player = getGameView().getPlayer();
                if (player != null) {
                    player.setMoney(player.getMoney() + m_Bounty);
                    player.setScore(player.getScore() + m_Points);
                }

                destroy();
            }
        }
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
