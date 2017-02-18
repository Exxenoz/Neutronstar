package at.autrage.projects.zeta.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Mesh {
    private FloatBuffer m_Vertices;
    private ShortBuffer m_Indices;

    private int m_VertexCount;
    private int m_IndexCount;

    public static final int CoordsPerVertex = 3;
    public static final int VertexStride = CoordsPerVertex * 4; // 4 bytes per coordinate

    public Mesh(float[] vertices, short[] indices) {
        m_Vertices = createVertexBuffer(vertices);
        m_Indices = createIndexBuffer(indices);

        m_VertexCount = vertices.length;
        m_IndexCount = indices.length;
    }

    public void draw(Material material, ShaderParams shaderParams) {
        shaderParams.Vertices = m_Vertices;
        shaderParams.Indices = m_Indices;
        shaderParams.IndexCount = m_IndexCount;

        material.draw(shaderParams);
    }

    public FloatBuffer createVertexBuffer(float[] vertices) {
        // Initialize vertex byte buffer for shape coordinates
        // (Number of coordinate values * 4 bytes per float)
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a floating point buffer from the ByteBuffer
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        // Add the coordinates to the FloatBuffer
        vertexBuffer.put(vertices);
        // Set the buffer to read the first coordinate
        vertexBuffer.position(0);

        return vertexBuffer;
    }

    public ShortBuffer createIndexBuffer(short[] indices) {
        // Initialize byte buffer for the draw list
        // (Number of values * 2 bytes per short)
        ByteBuffer bb = ByteBuffer.allocateDirect(indices.length * 2);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a short buffer from the ByteBuffer
        ShortBuffer indexBuffer = bb.asShortBuffer();
        // Add the values to the ShortBuffer
        indexBuffer.put(indices);
        // Set the buffer to read the first value
        indexBuffer.position(0);

        return indexBuffer;
    }

    public int getVertexCount() {
        return m_VertexCount;
    }

    public int getIndexCount() {
        return m_IndexCount;
    }
}
