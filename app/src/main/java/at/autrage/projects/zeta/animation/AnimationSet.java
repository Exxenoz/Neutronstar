package at.autrage.projects.zeta.animation;

import java.util.HashMap;
import java.util.Map;

public class AnimationSet {
    private String m_Name;
    private Map<AnimationType, Animation> m_Animations;

    public AnimationSet(String name, Map<AnimationType, Animation> animations) {
        this.m_Name = name;
        this.m_Animations = animations;
    }

    public AnimationSet(String name) {
        this.m_Name = name;
        this.m_Animations = new HashMap<>();
    }

    public String getName() {
        return m_Name;
    }

    public void addAnimation(AnimationType animationType, Animation animation) {
        m_Animations.put(animationType, animation);
    }

    public Animation getAnimation(AnimationType animationType) {
        return m_Animations.get(animationType);
    }

    public static final AnimationSet None = null;
}
