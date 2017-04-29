package at.autrage.projects.zeta.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Mesh {
    protected FloatBuffer vertexBuffer;
    protected ShortBuffer indexBuffer;
    protected FloatBuffer textureCoordBuffer;

    protected int vertexCount;
    protected int indexDrawCount;

    protected Mesh() {
        vertexBuffer = null;
        indexBuffer = null;
        textureCoordBuffer = null;

        vertexCount = 0;
        indexDrawCount = 0;
    }

    public Mesh(float[] vertices, short[] indices) {
        vertexBuffer = createVertexBuffer(vertices);
        indexBuffer = createIndexBuffer(indices);

        vertexCount = vertices.length;
        indexDrawCount = indices.length;
    }

    public static FloatBuffer createFloatBuffer(int capacity) {
        // Allocate bytes
        ByteBuffer bb = ByteBuffer.allocateDirect(capacity * PustafinGL.BYTES_PER_FLOAT);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a floating point buffer from the ByteBuffer
        return bb.asFloatBuffer();
    }

    public static FloatBuffer createVertexBuffer(float[] vertices) {
        // Create a floating point buffer
        FloatBuffer vertexBuffer = createFloatBuffer(vertices.length);
        // Add the coordinates to the FloatBuffer
        vertexBuffer.put(vertices);
        // Set the buffer to read the first coordinate
        vertexBuffer.position(0);

        return vertexBuffer;
    }

    public static ShortBuffer createShortBuffer(int capacity) {
        // Allocate bytes
        ByteBuffer bb = ByteBuffer.allocateDirect(capacity * PustafinGL.BYTES_PER_SHORT);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a short buffer from the ByteBuffer
        return bb.asShortBuffer();
    }

    public static ShortBuffer createIndexBuffer(short[] indices) {
        // Create a short buffer
        ShortBuffer indexBuffer = createShortBuffer(indices.length);
        // Add the values to the ShortBuffer
        indexBuffer.put(indices);
        // Set the buffer to read the first value
        indexBuffer.position(0);

        return indexBuffer;
    }

    public void setTextureCoordBuffer(FloatBuffer textureCoordBuffer) {
        this.textureCoordBuffer = textureCoordBuffer;
    }
}
