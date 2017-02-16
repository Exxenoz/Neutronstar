package at.autrage.projects.zeta.event;

public interface Action3<T1, T2, T3> {
    void invoke(T1 arg1, T2 arg2, T3 arg3);
}