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
        // Initialize vertex byte buffer for shape coordinates
        // (Number of coordinate values * 4 bytes per float)
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a floating point buffer from the ByteBuffer
        m_Vertices = bb.asFloatBuffer();
        // Add the coordinates to the FloatBuffer
        m_Vertices.put(vertices);
        // Set the buffer to read the first coordinate
        m_Vertices.position(0);

        // Initialize byte buffer for the draw list
        // (Number of values * 2 bytes per short)
        bb = ByteBuffer.allocateDirect(indices.length * 2);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a short buffer from the ByteBuffer
        m_Indices = bb.asShortBuffer();
        // Add the values to the ShortBuffer
        m_Indices.put(indices);
        // Set the buffer to read the first value
        m_Indices.position(0);

        // Set vertex count
        m_VertexCount = vertices.length;
        // Set index count
        m_IndexCount = indices.length;
    }

    public FloatBuffer getVertices() {
        return m_Vertices;
    }

    public ShortBuffer getIndices() {
        return m_Indices;
    }

    public int getVertexCount() {
        return m_VertexCount;
    }

    public int getIndexCount() {
        return m_IndexCount;
    }
}
