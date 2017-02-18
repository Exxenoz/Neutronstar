package at.autrage.projects.zeta.event;

public interface Action2<T1, T2> {
    void invoke(T1 arg1, T2 arg2);
}