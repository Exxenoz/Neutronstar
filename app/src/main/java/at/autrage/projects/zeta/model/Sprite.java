package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.opengl.SpriteMaterial;
import at.autrage.projects.zeta.opengl.SpriteMesh;
import at.autrage.projects.zeta.view.GameView;

public class Sprite extends GameObject {
    public Sprite(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, new MeshRenderer(new SpriteMaterial(), new SpriteMesh()), animationSet);
    }
}
