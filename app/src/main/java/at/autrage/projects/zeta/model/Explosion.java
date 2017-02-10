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

    public Explosion(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        m_GameObjectsImmuneToAOE = new ArrayList<GameObject>();
    }

    @Override
    public void onCollide(Collider collider) {
        super.onCollide(collider);

        if (m_Weapon == null) {
            return;
        }

        if (!(collider.getOwner() instanceof Enemy)) {
            return;
        }

        if (!m_GameObjectsImmuneToAOE.contains(collider.getOwner())) {
            Enemy enemy = (Enemy) collider.getOwner();
            enemy.receiveDamage(m_Weapon.getAOEDamage());

            if (enemy.isAlive()) {
                m_GameObjectsImmuneToAOE.add(enemy);
            }
        }
    }

    @Override
    protected void onAnimationFinished() {
        super.onAnimationFinished();

        destroy();
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

        setCollider(new CircleCollider(this, weapon.getAOERadius()));
    }

    public void addImmuneToAOEGameObject(GameObject gameObject) {
        if (gameObject == null) {
            return;
        }

        m_GameObjectsImmuneToAOE.add(gameObject);
    }
}
