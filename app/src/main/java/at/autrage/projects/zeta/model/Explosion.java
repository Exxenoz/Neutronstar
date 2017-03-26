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

    public Explosion(GameObject gameObject) {
        super(gameObject);

        m_Weapon = null;
        m_GameObjectsImmuneToAOE = new ArrayList<>();
    }

    @Override
    public void onCollide(Collider other) {
        super.onCollide(other);

        if (m_Weapon == null) {
            return;
        }

        Enemy enemy = other.gameObject.getComponent(Enemy.class);
        if (enemy == null) {
            return;
        }

        if (!m_GameObjectsImmuneToAOE.contains(other.getGameObject())) {
            enemy.receiveDamage(m_Weapon.getAOEDamage());
            m_GameObjectsImmuneToAOE.add(enemy.gameObject);
        }
    }

    @Override
    protected void onAnimationFinished() {
        super.onAnimationFinished();

        gameObject.destroy();
    }

    public void setWeapon(Weapon weapon) {
        this.m_Weapon = weapon;

        if (this.m_Weapon == null) {
            return;
        }

        if (this.m_Weapon.getAOEDamage() <= 0f) {
            return;
        }

        if (this.m_Weapon.getAOERadius() <= 0f) {
            return;
        }

        CircleCollider circleCollider = gameObject.getComponent(CircleCollider.class);
        if (circleCollider == null) {
            circleCollider = gameObject.addComponent(CircleCollider.class);
        }

        circleCollider.setRadius(this.m_Weapon.getAOERadius());
    }

    public void addImmuneToAOEGameObject(GameObject gameObject) {
        if (gameObject == null) {
            return;
        }

        m_GameObjectsImmuneToAOE.add(gameObject);
    }
}
