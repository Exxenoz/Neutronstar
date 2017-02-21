package at.autrage.projects.zeta.ability;

import at.autrage.projects.zeta.ability.behaviour.DefaultBehaviour;
import at.autrage.projects.zeta.model.GameObject;

public enum Behaviours {
    Default;

    public Behaviour create(GameObject owner){
        switch (this){
            case Default:
                return new DefaultBehaviour(owner);
            default:
                return new DefaultBehaviour(owner);
        }
    }
}
