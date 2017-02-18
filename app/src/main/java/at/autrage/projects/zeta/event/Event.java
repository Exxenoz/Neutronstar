package at.autrage.projects.zeta.event;

import java.util.ArrayList;
import java.util.List;

public class Event<T> {
    private List<Action<T>> actions;

    public Event() {
        actions = new ArrayList<>();
    }

    public void add(Action<T> delegate) {
        actions.add(delegate);
    }

    public void remove(Action<T> delegate) {
        actions.remove(delegate);
    }

    public void call(T arg) {
        for (Action action : actions) {
            action.invoke(arg);
        }
    }
}
