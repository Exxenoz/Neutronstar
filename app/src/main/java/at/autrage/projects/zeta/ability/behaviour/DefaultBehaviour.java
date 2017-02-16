package at.autrage.projects.zeta.ability.behaviour;

import at.autrage.projects.zeta.model.Actor;
import at.autrage.projects.zeta.ability.Behaviour;
import at.autrage.projects.zeta.ability.behaviour.operation.DoNothing;

public class DefaultBehaviour extends Behaviour {
    public DefaultBehaviour(Actor owner) {
        super(owner);

        operations.add(new DoNothing(this));
    }
}
