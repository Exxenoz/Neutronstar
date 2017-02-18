package at.autrage.projects.zeta.model;

import java.util.ArrayList;
import java.util.List;

import at.autrage.projects.zeta.ability.Behaviour;
import at.autrage.projects.zeta.ability.Behaviours;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.view.GameView;

public class Actor extends GameObject {
    private List<Behaviour> behaviours;

    public Actor(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);

        behaviours = new ArrayList<>();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        for (int i = 0; i < behaviours.size(); i++) {
            Behaviour behaviour = behaviours.get(i);
            behaviour.update();

            if (behaviour.getState() == Behaviour.State.Ended) {
                behaviours.remove(i);
            }
        }
    }

    public boolean startBehaviour(Behaviours behaviour) {
        Behaviour newBehaviour = behaviour.create(this);

        if (!newBehaviour.canStart()) {
            return false;
        }

        behaviours.add(newBehaviour);
        newBehaviour.start();

        return true;
    }
}
