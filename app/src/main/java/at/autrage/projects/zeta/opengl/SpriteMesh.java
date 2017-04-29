package at.autrage.projects.zeta.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class SpriteMesh extends Mesh {
    public static final float QuadVertices[] = {
            -0.5f, -0.5f, 0.0f, // Bottom Left
             0.5f, -0.5f, 0.0f, // Bottom Right
            -0.5f,  0.5f, 0.0f, // Top Left
             0.5f,  0.5f, 0.0f  // Top Right
    };

    public static final short QuadIndices[] = {
            0, 1, 2, // Triangle1
            1, 3, 2  // Triangle2
    };

    private static final FloatBuffer quadVertexBuffer;
    private static final ShortBuffer quadIndexBuffer;

    static {
        quadVertexBuffer = createVertexBuffer(QuadVertices);
        quadIndexBuffer = createIndexBuffer(QuadIndices);
    }

    public SpriteMesh() {
        vertexBuffer = quadVertexBuffer;
        indexBuffer = quadIndexBuffer;

        vertexCount = QuadVertices.length;
        indexDrawCount = QuadIndices.length;
    }
}
