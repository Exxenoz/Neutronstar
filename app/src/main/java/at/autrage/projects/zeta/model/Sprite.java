package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.opengl.SpriteMaterial;
import at.autrage.projects.zeta.opengl.SpriteMesh;
import at.autrage.projects.zeta.view.GameView;

public class Sprite extends GameObject {
    public Sprite(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        setRenderer(new MeshRenderer(m_Transform));
        if (getRenderer() != null) {
            getRenderer().setMaterial(new SpriteMaterial());
            getRenderer().setMesh(new SpriteMesh());
            getRenderer().setEnabled(true);
        }
    }
}
