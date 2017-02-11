package at.autrage.projects.zeta.opengl;

public class SpriteMesh extends Mesh {
    public static final float BaseVertices[] = {
            -0.5f,  0.5f, 0.0f, // top left
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f, // bottom right
            0.5f,  0.5f, 0.0f  // top right
    };

    public static final short BaseIndices[] = {
            0, 1, 2, // Triangle1
            0, 2, 3  // Triangle2
    };

    public SpriteMesh() {
        super(BaseVertices, BaseIndices);
    }
}
