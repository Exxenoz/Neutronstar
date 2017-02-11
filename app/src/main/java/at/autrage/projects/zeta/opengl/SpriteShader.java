package at.autrage.projects.zeta.opengl;

import android.opengl.GLES20;

public class SpriteShader extends Shader {
    public SpriteShader() {
        super();
    }

    public void draw(Mesh mesh, float[] color, float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(m_Program);

        // Get handle to vertex shader's a_Position member
        int positionHandle = GLES20.glGetAttribLocation(m_Program, "a_Position");

        // Enable a handle to the vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the coordinate data
        GLES20.glVertexAttribPointer(positionHandle, Mesh.CoordsPerVertex,
                GLES20.GL_FLOAT, false,
                Mesh.VertexStride, mesh.getVertices());

        // Get handle to fragment shader's v_Color member
        int colorHandle = GLES20.glGetUniformLocation(m_Program, "a_Color");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // Get handle to shape's transformation matrix
        int mvpMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_MVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangles
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getIndexCount(), GLES20.GL_UNSIGNED_SHORT, mesh.getIndices());

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
