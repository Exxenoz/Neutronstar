package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents an alien enemy object in the game.
 */
public class Alien extends Enemy {
    public Alien(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);
    }
}
