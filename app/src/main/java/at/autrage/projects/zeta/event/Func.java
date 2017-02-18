package at.autrage.projects.zeta.event;

public interface Func<T, TResult> {
    TResult invoke(T arg);
}