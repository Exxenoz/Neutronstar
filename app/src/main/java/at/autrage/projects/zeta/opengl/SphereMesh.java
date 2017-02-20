package at.autrage.projects.zeta.opengl;

import java.nio.FloatBuffer;

public class SphereMesh extends Mesh {
    private FloatBuffer m_TextureCoordBuffer;

    public SphereMesh(int stacks, int slices) {
        super();

        createSphere(0.5f, stacks, slices);
    }

    private void createSphere(float radius, int stacks, int slices)
    {
        int vertexCount = (stacks + 1) * (slices + 1);

        m_VertexBufferSize = vertexCount * PustafinGL.FLOATS_PER_VERTEX;
        m_IndexBufferSize = vertexCount * PustafinGL.SHORTS_PER_TRIANGLE_INDEX;

        m_VertexBuffer = createFloatBuffer(m_VertexBufferSize);
        m_IndexBuffer = createShortBuffer(m_IndexBufferSize);
        //m_NormalBuffer = createFloatBuffer(vertexCount * PustafinGL.FLOATS_PER_NORMAL);
        m_TextureCoordBuffer  = createFloatBuffer(vertexCount * PustafinGL.FLOATS_PER_TEXTURE_COORD);

        for (int stackNumber = 0; stackNumber <= stacks; ++stackNumber)
        {
            for (int sliceNumber = 0; sliceNumber <= slices; ++sliceNumber)
            {
                double theta = stackNumber * Math.PI / stacks;
                double phi = sliceNumber * 2 * Math.PI / slices;
                double sinTheta = Math.sin(theta);
                double sinPhi = Math.sin(phi);
                double cosTheta = Math.cos(theta);
                double cosPhi = Math.cos(phi);

                double nx = cosPhi * sinTheta;
                double ny = cosTheta;
                double nz = sinPhi * sinTheta;

                double x = radius * nx;
                double y = radius * ny;
                double z = radius * nz;

                float u = 1.f - ((float)sliceNumber / (float)slices);
                float v = (float)stackNumber / (float)stacks;

                //m_NormalBuffer.put((float)nx);
                //m_NormalBuffer.put((float)ny);
                //m_NormalBuffer.put((float)nz);

                m_VertexBuffer.put((float)x);
                m_VertexBuffer.put((float)y);
                m_VertexBuffer.put((float)z);

                m_TextureCoordBuffer.put(u);
                m_TextureCoordBuffer.put(v);
            }
        }

        for (int stackNumber = 0; stackNumber < stacks; ++stackNumber)
        {
            for (int sliceNumber = 0; sliceNumber < slices; ++sliceNumber)
            {
                int second = (sliceNumber * (stacks + 1)) + stackNumber;
                int first = second + stacks + 1;

                //int first = (stackNumber * slices) + (sliceNumber % slices);
                //int second = ((stackNumber + 1) * slices) + (sliceNumber % slices);

                m_IndexBuffer.put((short) first);
                m_IndexBuffer.put((short) second);
                m_IndexBuffer.put((short) (first + 1));

                m_IndexBuffer.put((short) second);
                m_IndexBuffer.put((short) (second + 1));
                m_IndexBuffer.put((short) (first + 1));
            }
        }

        m_VertexBuffer.rewind();
        m_IndexBuffer.rewind();
        //m_NormalBuffer.rewind();
        m_TextureCoordBuffer.rewind();
    }

    public FloatBuffer getTextureCoordBuffer() {
        return m_TextureCoordBuffer;
    }
}
