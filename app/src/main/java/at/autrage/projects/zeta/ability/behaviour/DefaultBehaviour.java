package at.autrage.projects.zeta.ability.behaviour;

import at.autrage.projects.zeta.ability.Behaviour;
import at.autrage.projects.zeta.ability.behaviour.operation.DoNothing;
import at.autrage.projects.zeta.model.GameObject;

public class DefaultBehaviour extends Behaviour {
    public DefaultBehaviour() {
        super();

        patterns.add(new DoNothing(this));
    }
}
