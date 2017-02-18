package at.autrage.projects.zeta.event;

import java.util.ArrayList;
import java.util.List;

public class EventFunc2<T1, T2, TResult> {
    private List<Func2<T1, T2, TResult>> funcs;

    public EventFunc2() {
        funcs = new ArrayList<>();
    }

    public void add(Func2<T1, T2, TResult> delegate) {
        funcs.add(delegate);
    }

    public void remove(Func2<T1, T2, TResult> delegate) {
        funcs.remove(delegate);
    }

    public TResult call(T1 arg1, T2 arg2) {
        for (int i = 0; i < funcs.size() - 1; i++) {
            funcs.get(i).invoke(arg1, arg2);
        }

        return funcs.get(funcs.size() - 1).invoke(arg1, arg2);
    }
}
