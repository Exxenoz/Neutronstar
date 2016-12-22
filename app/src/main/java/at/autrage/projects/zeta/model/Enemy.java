package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.view.GameView;

public class Enemy extends GameObject {

    private float m_Health;
    private float m_HitDamage;
    private float m_Bounty;
    private float m_Points;

    public Enemy(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_Health = 1f;
        m_HitDamage = 0f;
        m_Bounty = 0f;
        m_Points = 0f;
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

    public float getBounty() {
        return m_Bounty;
    }

    public void setBounty(float bounty) {
        this.m_Bounty = bounty;
    }

    public float getPoints() {
        return m_Points;
    }

    public void setPoints(float points) {
        this.m_Points = points;
    }
}
