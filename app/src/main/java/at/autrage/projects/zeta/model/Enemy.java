package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.view.GameView;

public class Enemy extends GameObject {

    private float m_Health;
    private float m_HitDamage;
    private int m_Bounty;
    private int m_Points;

    public Enemy(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);
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
