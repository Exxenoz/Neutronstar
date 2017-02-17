package at.autrage.projects.zeta.opengl;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import at.autrage.projects.zeta.module.Logger;

public class SpriteShader extends Shader {
    private int _positionHandle;
    private int _colorHandle;
    private int _modelMatrixHandle;
    private int _vpMatrixHandle;

    public SpriteShader() {
        super();
    }

    public void init() {
        // Get handle to vertex shader's a_Position member
        _positionHandle = GLES20.glGetAttribLocation(m_Program, "a_Position");
        // Get handle to fragment shader's v_Color member
        _colorHandle = GLES20.glGetUniformLocation(m_Program, "a_Color");
        // Get handle to shape's model matrix
        _modelMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ModelMatrix");
        // Get handle to shape's view projection matrix
        _vpMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_VPMatrix");
        // Enable a handle to the vertices
        GLES20.glEnableVertexAttribArray(_positionHandle);
    }

    public void draw(FloatBuffer vertices, ShortBuffer indices, int indexCount, float[] color, float[] modelMatrix, float[] vpMatrix) {
        if (m_Program != _lastProgram) {
            // Add program to OpenGL ES environment
            GLES20.glUseProgram(m_Program);

            _lastProgram = m_Program;
        }

        // Prepare the coordinate data
        GLES20.glVertexAttribPointer(_positionHandle, Mesh.CoordsPerVertex,
                GLES20.GL_FLOAT, false,
                Mesh.VertexStride, vertices);

        // Set color for drawing the triangle
        GLES20.glUniform4fv(_colorHandle, 1, color, 0);

        // Pass the model transformation to the shader
        GLES20.glUniformMatrix4fv(_modelMatrixHandle, 1, false, modelMatrix, 0);

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(_vpMatrixHandle, 1, false, vpMatrix, 0);

        // Draw the triangles
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_SHORT, indices);
    }
}
