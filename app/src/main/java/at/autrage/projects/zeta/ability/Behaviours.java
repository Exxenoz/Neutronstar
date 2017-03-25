package at.autrage.projects.zeta.ability;

import at.autrage.projects.zeta.ability.behaviour.DefaultBehaviour;
import at.autrage.projects.zeta.model.GameObject;

public enum Behaviours {
    Default;

    public Behaviour create(){
        switch (this){
            case Default:
                return new DefaultBehaviour();
            default:
                return new DefaultBehaviour();
        }
    }
}
