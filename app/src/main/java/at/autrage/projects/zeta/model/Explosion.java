package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.view.GameView;

public class Explosion extends GameObject {
    public Explosion(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);
    }


}
