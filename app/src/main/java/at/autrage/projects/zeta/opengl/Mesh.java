package at.autrage.projects.zeta.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Mesh {
    protected FloatBuffer m_VertexBuffer;
    protected ShortBuffer m_IndexBuffer;

    protected int m_VertexBufferSize;
    protected int m_IndexBufferSize;

    protected Mesh() {
        m_VertexBuffer = null;
        m_IndexBuffer = null;

        m_VertexBufferSize = 0;
        m_IndexBufferSize = 0;
    }

    public Mesh(float[] vertices, short[] indices) {
        m_VertexBuffer = createVertexBuffer(vertices);
        m_IndexBuffer = createIndexBuffer(indices);

        m_VertexBufferSize = vertices.length;
        m_IndexBufferSize = indices.length;
    }

    public void shift(ShaderParams shaderParams) {
        shaderParams.VertexBuffer = m_VertexBuffer;
        shaderParams.IndexBuffer = m_IndexBuffer;
        shaderParams.IndexBufferSize = m_IndexBufferSize;
    }

    public FloatBuffer createFloatBuffer(int capacity) {
        // Allocate bytes
        ByteBuffer bb = ByteBuffer.allocateDirect(capacity * PustafinGL.BYTES_PER_FLOAT);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a floating point buffer from the ByteBuffer
        return bb.asFloatBuffer();
    }

    public FloatBuffer createVertexBuffer(float[] vertices) {
        // Create a floating point buffer
        FloatBuffer vertexBuffer = createFloatBuffer(vertices.length);
        // Add the coordinates to the FloatBuffer
        vertexBuffer.put(vertices);
        // Set the buffer to read the first coordinate
        vertexBuffer.position(0);

        return vertexBuffer;
    }

    public ShortBuffer createShortBuffer(int capacity) {
        // Allocate bytes
        ByteBuffer bb = ByteBuffer.allocateDirect(capacity * PustafinGL.BYTES_PER_SHORT);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a short buffer from the ByteBuffer
        return bb.asShortBuffer();
    }

    public ShortBuffer createIndexBuffer(short[] indices) {
        // Create a short buffer
        ShortBuffer indexBuffer = createShortBuffer(indices.length);
        // Add the values to the ShortBuffer
        indexBuffer.put(indices);
        // Set the buffer to read the first value
        indexBuffer.position(0);

        return indexBuffer;
    }

    public int getVertexBufferSize() {
        return m_VertexBufferSize;
    }

    public int getIndexBufferSize() {
        return m_IndexBufferSize;
    }
}
