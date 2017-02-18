package at.autrage.projects.zeta.event;

public interface Func3<T1, T2, T3, TResult> {
    TResult invoke(T1 arg1,T2 arg2,T3 arg3);
}
