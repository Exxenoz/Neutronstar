package at.autrage.projects.zeta.model;

import java.util.Vector;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.view.GameView;

public class Weapon extends GameObject {

    private float m_HitDamage;
    private float m_AOEDamage;
    private float m_AOERadius;

    public Weapon(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_HitDamage = 0f;
        m_AOEDamage = 0f;
        m_AOERadius = 0f;
    }

    public float getHitDamage() {
        return m_HitDamage;
    }

    public void setHitDamage(float hitDamage) {
        this.m_HitDamage = hitDamage;
    }

    public float getAOEDamage() {
        return m_AOEDamage;
    }

    public void setAOEDamage(float AOEDamage) {
        this.m_AOEDamage = AOEDamage;
    }

    public float getAOERadius() {
        return m_AOERadius;
    }

    public void setAOERadius(float AOERadius) {
        this.m_AOERadius = AOERadius;
    }
}
