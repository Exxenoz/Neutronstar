package at.autrage.projects.zeta.ability;

import java.util.List;

import at.autrage.projects.zeta.model.Component;
import at.autrage.projects.zeta.model.GameObject;

public abstract class Behaviour extends Component {
    protected List<Pattern> patterns;

    public Behaviour(GameObject gameObject) {
        super(gameObject);
    }

    public final boolean canStart() {
        for (Pattern pattern : patterns) {
            if (!pattern.canStart()) {
                return false;
            }
        }

        return true;
    }

    public final boolean canUpdate() {
        for (Pattern pattern : patterns) {
            if (!pattern.canUpdate()) {
                return false;
            }
        }

        return true;
    }

    public final boolean shouldComplete() {
        for (Pattern pattern : patterns) {
            if (pattern.shouldComplete()) {
                return true;
            }
        }

        return false;
    }

    public final boolean shouldCancel() {
        for (Pattern pattern : patterns) {
            if (pattern.shouldCancel()) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onStart() {
        if (canStart()){

        }
    }

    public final boolean start() {
        if (!canStart()) {
            return false;
        }

        for (Pattern pattern : patterns) {
            pattern.start();
        }

        return true;
    }

    @Override
    public final void onUpdate() {
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

        for (Pattern pattern : patterns) {
            pattern.update();
        }
    }

    private final void complete() {
        for (Pattern pattern : patterns) {
            pattern.complete();
        }
    }

    private final void cancel() {
        for (Pattern pattern : patterns) {
            pattern.cancel();
        }
    }

    private final void end() {
        for (Pattern pattern : patterns) {
            pattern.end();
        }

        destroy();
    }
}
