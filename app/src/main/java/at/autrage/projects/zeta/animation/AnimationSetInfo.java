package at.autrage.projects.zeta.animation;

import java.util.HashMap;

public class AnimationSetInfo {
    public final AnimationSets AnimationSetID;
    public final String Name;
    public final HashMap<AnimationType, Animations> Animations;

    public AnimationSetInfo(AnimationSets animationSetID, String name, HashMap<AnimationType, Animations> animations) {
        AnimationSetID = animationSetID;
        Name = name;
        Animations = animations;
    }

    public AnimationSetInfo(AnimationSets animationSetID, String name, final Animations defaultAnimationID) {
        AnimationSetID = animationSetID;
        Name = name;
        Animations = new HashMap<AnimationType, Animations>() {{
            put(AnimationType.Default, defaultAnimationID);
        }};
    }
}
