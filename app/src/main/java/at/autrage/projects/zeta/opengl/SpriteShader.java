package at.autrage.projects.zeta.opengl;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class SpriteShader extends Shader {
    public SpriteShader(Context context) {
        super(context);
    }

    public void draw(int coords_per_vertex, int vertexStride, int vertexCount, FloatBuffer vertexBuffer, int indexCount, ShortBuffer indexBuffer, float[] color, float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(m_Program);

        // Get handle to vertex shader's a_Position member
        int positionHandle = GLES20.glGetAttribLocation(m_Program, "a_Position");

        // Enable a handle to the vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the coordinate data
        GLES20.glVertexAttribPointer(positionHandle, coords_per_vertex,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // Get handle to fragment shader's v_Color member
        int colorHandle = GLES20.glGetUniformLocation(m_Program, "a_Color");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // Get handle to shape's transformation matrix
        int mvpMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_MVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangles
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
