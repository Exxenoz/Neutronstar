package at.autrage.projects.zeta.module;

public class UpdateFlags {
    public static final int None                  = 0;
    public static final int Population            = 1 << 0;
    public static final int Money                 = 1 << 1;
    public static final int Level                 = 1 << 2;
    public static final int Score                 = 1 << 3;
    public static final int Time                  = 1 << 4;
    public static final int FPS                   = 1 << 5;
    public static final int SmallRocketCount      = 1 << 6;
    public static final int BigRocketCount        = 1 << 7;
    public static final int SmallNukeCount        = 1 << 8;
    public static final int BigNukeCount          = 1 << 9;
    public static final int SmallLaserCount       = 1 << 10;
    public static final int BigLaserCount         = 1 << 11;
    public static final int SmallContactBombCount = 1 << 12;
    public static final int BigContactBombCount   = 1 << 13;

    public static final int All = ~None;
}
