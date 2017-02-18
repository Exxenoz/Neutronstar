package at.autrage.projects.zeta.ability;

import java.util.List;

import at.autrage.projects.zeta.model.Actor;

public abstract class Behaviour {
    public enum State {
        None,
        Started,
        Updated,
        Completed,
        Cancelled,
        Ended
    }

    private Actor actor;
    private State state;
    protected List<Operation> operations;

    public Behaviour(Actor actor) {
        if (actor == null) {
            throw new IllegalArgumentException("actor can not be null!");
        }

        this.actor = actor;
        state = State.None;
    }

    public Actor getActor() {
        return actor;
    }

    public State getState() {
        return state;
    }

    public final boolean canStart() {
        if (state != State.None) {
            return false;
        }

        for (Operation operation : operations) {
            if (!operation.canStart()) {
                return false;
            }
        }

        return true;
    }

    public final boolean canUpdate() {
        if (state != State.Started && state != State.Updated) {
            return false;
        }

        for (Operation operation : operations) {
            if (!operation.canUpdate()) {
                return false;
            }
        }

        return true;
    }

    public final boolean shouldComplete() {
        for (Operation operation : operations) {
            if (operation.shouldComplete()) {
                return true;
            }
        }

        return false;
    }

    public final boolean shouldCancel() {
        for (Operation operation : operations) {
            if (operation.shouldCancel()) {
                return true;
            }
        }

        return false;
    }

    public final boolean start() {
        if (!canStart()) {
            return false;
        }

        for (Operation operation : operations) {
            operation.start();
        }

        state = State.Started;

        return true;
    }

    public final void update() {
        if (!canUpdate()) {
            return;
        }

        if (shouldCancel()) {
            cancel();
            return;
        }

        if (shouldComplete()) {
            complete();
            return;
        }

        for (Operation operation : operations) {
            operation.update();
        }

        state = State.Updated;
    }

    private final void complete() {
        for (Operation operation : operations) {
            operation.complete();
        }

        state = State.Completed;
    }

    private final void cancel() {
        for (Operation operation : operations) {
            operation.cancel();
        }

        state = State.Cancelled;
    }

    private final void end() {
        for (Operation operation : operations) {
            operation.end();
        }

        state = State.Ended;
    }
}
