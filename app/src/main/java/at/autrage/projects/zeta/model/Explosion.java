package at.autrage.projects.zeta.model;


import java.util.ArrayList;
import java.util.List;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents an explosion object in the game.
 */
public class Explosion extends Sprite {
    private Weapon m_Weapon;
    private List<GameObject> m_GameObjectsImmuneToAOE;

    public Explosion(GameObject gameObject, AnimationSet animationSet) {
        super(gameObject, animationSet);

        m_GameObjectsImmuneToAOE = new ArrayList<>();
    }

    @Override
    public void onCollide(Collider other) {
        super.onCollide(other);

        if (m_Weapon == null) {
            return;
        }

        if (other.gameObject.getComponent(Enemy.class) != null) {
            return;
        }

        if (!m_GameObjectsImmuneToAOE.contains(other.getGameObject())) {
            Enemy enemy = (Enemy) other.gameObject.getComponent(Enemy.class);
            enemy.receiveDamage(m_Weapon.getAOEDamage());

            if (enemy.isAlive()) {
                m_GameObjectsImmuneToAOE.add(enemy.gameObject);
            }
        }
    }

    @Override
    protected void onAnimationFinished() {
        super.onAnimationFinished();

        gameObject.destroy();
    }

    public Weapon getWeapon() {
        return m_Weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.m_Weapon = weapon;

        if (weapon == null) {
            return;
        }

        if (weapon.getAOEDamage() <= 0f) {
            return;
        }

        if (weapon.getAOERadius() <= 0f) {
            return;
        }

        new CircleCollider(gameObject, weapon.getAOERadius());
    }

    public void addImmuneToAOEGameObject(GameObject gameObject) {
        if (gameObject == null) {
            return;
        }

        m_GameObjectsImmuneToAOE.add(gameObject);
    }
}
