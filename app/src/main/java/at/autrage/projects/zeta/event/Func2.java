package at.autrage.projects.zeta.event;

public interface Func2<T1, T2, TResult> {
    TResult invoke(T1 arg1, T2 arg2);
}
