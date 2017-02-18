package at.autrage.projects.zeta.ability;

import at.autrage.projects.zeta.ability.behaviour.DefaultBehaviour;
import at.autrage.projects.zeta.model.Actor;

public enum Behaviours {
    Default;

    public Behaviour create(Actor owner){
        switch (this){
            case Default:
                return new DefaultBehaviour(owner);
            default:
                return new DefaultBehaviour(owner);
        }
    }
}
