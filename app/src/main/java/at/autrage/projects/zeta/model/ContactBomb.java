package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents a contact bomb object in the game.
 */
public class ContactBomb extends Weapon {
    public ContactBomb(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);
    }
}
