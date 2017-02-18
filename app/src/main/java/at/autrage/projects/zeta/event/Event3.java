package at.autrage.projects.zeta.event;

import java.util.ArrayList;
import java.util.List;

public class Event3<T1, T2, T3> {
    private List<Action3<T1, T2, T3>> actions;

    public Event3() {
        actions = new ArrayList<>();
    }

    public void add(Action3<T1, T2, T3> delegate) {
        actions.add(delegate);
    }

    public void remove(Action3<T1, T2, T3> delegate) {
        actions.remove(delegate);
    }

    public void call(T1 arg1, T2 arg2, T3 arg3) {
        for (Action3 action : actions) {
            action.invoke(arg1, arg2, arg3);
        }
    }
}
