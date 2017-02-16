package at.autrage.projects.zeta.ability;

import at.autrage.projects.zeta.model.Actor;

public abstract class Operation {
    private Behaviour behaviour;

    public Operation(Behaviour behaviour) {
        if (behaviour==null){
            throw new IllegalArgumentException("behaviour can not be null!");
        }

        this.behaviour = behaviour;
    }

    public final Behaviour getBehaviour() {
        return behaviour;
    }

    public final Actor getActor() {
        return behaviour.getActor();
    }

    public boolean canStart() {
        return true;
    }

    public boolean canUpdate() {
        return true;
    }

    public boolean shouldComplete() {
        return false;
    }

    public boolean shouldCancel() {
        return false;
    }

    public void start() {
    }

    public void update() {
    }

    public void complete() {
    }

    public void cancel() {
    }

    public void end() {
    }
}
