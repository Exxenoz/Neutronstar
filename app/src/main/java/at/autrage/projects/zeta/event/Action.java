package at.autrage.projects.zeta.event;

public interface Action<T> {
    void invoke(T arg);
}