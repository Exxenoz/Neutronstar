package at.autrage.projects.zeta.opengl;

public class TextureInfo {
    public final int ResourceID;
    public final Texture.Filter Filter;

    public TextureInfo(int resourceID, Texture.Filter filter) {
        ResourceID = resourceID;
        Filter = filter;
    }
}
