package at.autrage.projects.zeta.event;

import java.util.ArrayList;
import java.util.List;

public class EventFunc3<T1, T2, T3, TResult> {
    private List<Func3<T1, T2, T3, TResult>> funcs;

    public EventFunc3() {
        funcs = new ArrayList<>();
    }

    public void add(Func3<T1, T2, T3, TResult> delegate) {
        funcs.add(delegate);
    }

    public void remove(Func3<T1, T2, T3, TResult> delegate) {
        funcs.remove(delegate);
    }

    public TResult call(T1 arg1, T2 arg2, T3 arg3) {
        for (int i = 0; i < funcs.size() - 1; i++) {
            funcs.get(i).invoke(arg1, arg2, arg3);
        }

        return funcs.get(funcs.size() - 1).invoke(arg1, arg2, arg3);
    }
}
