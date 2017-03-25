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

    public Explosion(AnimationSet animationSet, Weapon weapon) {
        super(animationSet);

        m_Weapon = weapon;
        m_GameObjectsImmuneToAOE = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (m_Weapon == null) {
            return;
        }

        if (m_Weapon.getAOEDamage() <= 0f) {
            return;
        }

        if (m_Weapon.getAOERadius() <= 0f) {
            return;
        }

        gameObject.addComponent(new CircleCollider(m_Weapon.getAOERadius()));
    }

    @Override
    public void onCollide(Collider other) {
        super.onCollide(other);

        if (m_Weapon == null) {
            return;
        }

        if (other.gameObject.getComponent(Enemy.class) == null) {
            return;
        }

        if (!m_GameObjectsImmuneToAOE.contains(other.getGameObject())) {
            Enemy enemy = other.gameObject.getComponent(Enemy.class);
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

    public void addImmuneToAOEGameObject(GameObject gameObject) {
        if (gameObject == null) {
            return;
        }

        m_GameObjectsImmuneToAOE.add(gameObject);
    }
}
