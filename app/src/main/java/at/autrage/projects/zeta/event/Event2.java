package at.autrage.projects.zeta.event;

import java.util.ArrayList;
import java.util.List;

public class Event2<T1, T2> {
    private List<Action2<T1, T2>> actions;

    public Event2() {
        actions = new ArrayList<>();
    }

    public void add(Action2<T1, T2> delegate) {
        actions.add(delegate);
    }

    public void remove(Action2<T1, T2> delegate) {
        actions.remove(delegate);
    }

    public void call(T1 arg1, T2 arg2) {
        for (Action2 action : actions) {
            action.invoke(arg1, arg2);
        }
    }
}