package at.autrage.projects.zeta.opengl;

import at.autrage.projects.zeta.module.Logger;

public class SphereMesh extends Mesh {
    public SphereMesh(int stacks, int slices) {
        super();

        createSphere(0.5f, stacks, slices);
    }

    private void createSphere(float radius, int stacks, int slices)
    {
        int vertexCount = (stacks + 1) * (slices + 1);

        this.vertexCount = vertexCount * PustafinGL.FLOATS_PER_VERTEX;
        indexDrawCount = vertexCount * PustafinGL.SHORTS_PER_TRIANGLE_INDEX;

        vertexBuffer = createFloatBuffer(this.vertexCount);
        indexBuffer = createShortBuffer(indexDrawCount);
        //m_NormalBuffer = createFloatBuffer(vertexCount * PustafinGL.FLOATS_PER_NORMAL);
        textureCoordBuffer  = createFloatBuffer(vertexCount * PustafinGL.FLOATS_PER_TEXTURE_COORD);

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

                vertexBuffer.put((float)x);
                vertexBuffer.put((float)y);
                vertexBuffer.put((float)z);

                textureCoordBuffer.put(u);
                textureCoordBuffer.put(v);
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

                indexBuffer.put((short) first);
                indexBuffer.put((short) second);
                indexBuffer.put((short) (first + 1));

                indexBuffer.put((short) second);
                indexBuffer.put((short) (second + 1));
                indexBuffer.put((short) (first + 1));
            }
        }

        vertexBuffer.rewind();
        indexBuffer.rewind();
        //m_NormalBuffer.rewind();
        textureCoordBuffer.rewind();

        Logger.D("New SphereMesh created.");
    }
}
