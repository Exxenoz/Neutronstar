package at.autrage.projects.zeta.animation;


import java.util.HashMap;
import java.util.Map;

public class AnimationSet {
    private int m_ID;
    private String m_Name;
    private Map<AnimationType, Animation> m_Animations;

    public AnimationSet(int ID, String name, Map<AnimationType, Animation> animations) {
        this.m_ID = ID;
        this.m_Name = name;
        this.m_Animations = animations;
    }

    public void insertAnimation(AnimationType animationType, Animation animation) {
        m_Animations.put(animationType, animation);
    }

    public int getID() {
        return m_ID;
    }

    public String getName() {
        return m_Name;
    }

    public Animation getAnimation(AnimationType animationType) {
        return m_Animations.get(animationType);
    }

    public static final AnimationSet None = null;
}
