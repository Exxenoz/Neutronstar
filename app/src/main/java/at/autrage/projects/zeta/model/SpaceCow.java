package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.view.GameView;

public class SpaceCow extends Enemy {
    public SpaceCow(GameObject gameObject, AnimationSets animationSet) {
        super(gameObject, AssetManager.getInstance().getAnimationSet(animationSet));
    }
}
