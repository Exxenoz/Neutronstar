package at.autrage.projects.zeta.event;

import java.util.ArrayList;
import java.util.List;

public class EventFunc<T, TResult> {
    private List<Func<T, TResult>> funcs;

    public EventFunc() {
        funcs = new ArrayList<>();
    }

    public void add(Func<T, TResult> delegate) {
        funcs.add(delegate);
    }

    public void remove(Func<T, TResult> delegate) {
        funcs.remove(delegate);
    }

    public TResult call(T arg) {
        for (int i = 0; i < funcs.size() - 1; i++) {
            funcs.get(i).invoke(arg);
        }

        return funcs.get(funcs.size() - 1).invoke(arg);
    }
}
