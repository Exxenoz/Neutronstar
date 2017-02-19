package at.autrage.projects.zeta.opengl;

public class SpriteMesh extends Mesh {
    public static final float BaseVertices[] = {
            -0.5f, -0.5f, 0.0f, // Bottom Left
             0.5f, -0.5f, 0.0f, // Bottom Right
            -0.5f,  0.5f, 0.0f, // Top Left
             0.5f,  0.5f, 0.0f  // Top Right
    };

    public static final short BaseIndices[] = {
            0, 1, 2, // Triangle1
            1, 3, 2  // Triangle2
    };

    public SpriteMesh() {
        super(BaseVertices, BaseIndices);
    }
}
