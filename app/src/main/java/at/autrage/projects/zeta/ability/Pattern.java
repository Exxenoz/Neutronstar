package at.autrage.projects.zeta.ability;

import at.autrage.projects.zeta.model.GameObject;

public abstract class Pattern {
    private Behaviour behaviour;

    public Pattern(Behaviour behaviour) {
        if (behaviour == null) {
            throw new IllegalArgumentException("behaviour can not be null!");
        }

        this.behaviour = behaviour;
    }

    public final Behaviour getBehaviour() {
        return behaviour;
    }

    public final GameObject getGameObject() {
        return behaviour.getGameObject();
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
