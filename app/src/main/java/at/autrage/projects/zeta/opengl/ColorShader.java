package at.autrage.projects.zeta.opengl;

import android.opengl.GLES20;

public class ColorShader extends Shader {
    /** This will be used to pass in the vertices. */
    private int _positionHandle;
    /** This will be used to pass in the color. */
    private int _colorHandle;
    /** This will be used to pass in the model matrix. */
    private int _modelMatrixHandle;
    /** This will be used to pass in the view/projection matrix. */
    private int _vpMatrixHandle;

    public ColorShader() {
        super();
    }

    @Override
    public void bindAttribLocations() {
        GLES20.glBindAttribLocation(m_Program, 0, "a_Position");
        GLES20.glBindAttribLocation(m_Program, 1, "a_Color");
        GLES20.glBindAttribLocation(m_Program, 2, "u_ModelMatrix");
        GLES20.glBindAttribLocation(m_Program, 3, "u_VPMatrix");
    }

    @Override
    public void getAttribLocations() {
        // Get handle to vertex shader's a_Position member
        _positionHandle = GLES20.glGetAttribLocation(m_Program, "a_Position");
        // Get handle to fragment shader's v_Color member
        _colorHandle = GLES20.glGetUniformLocation(m_Program, "a_Color");
        // Get handle to shape's model matrix
        _modelMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_ModelMatrix");
        // Get handle to shape's view projection matrix
        _vpMatrixHandle = GLES20.glGetUniformLocation(m_Program, "u_VPMatrix");
    }

    @Override
    public void draw(ShaderParams shaderParams) {
        if (m_Program == 0){
            return;
        }

        Mesh mesh = shaderParams.Mesh;
        if (mesh == null || mesh.indexDrawCount <= 0) {
            return;
        }

        // Do not draw elements with invalid buffers
        if (mesh.vertexBuffer == null ||
            mesh.indexBuffer == null) {
            return;
        }

        // Do not draw invisible elements
        if (shaderParams.Color.getAlpha() == 0f) {
            return;
        }

        if (m_Program != _currProgram) {
            // Add program to OpenGL ES environment
            GLES20.glUseProgram(m_Program);

            _currProgram = m_Program;
        }

        // Prepare the coordinate data
        GLES20.glVertexAttribPointer(_positionHandle, PustafinGL.FLOATS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                PustafinGL.BYTES_PER_VERTEX, mesh.vertexBuffer);

        // Enable a handle to the vertices
        GLES20.glEnableVertexAttribArray(_positionHandle);

        // Set color for drawing the triangle
        GLES20.glUniform4fv(_colorHandle, 1, shaderParams.Color.getColor(), 0);

        // Pass the model transformation to the shader
        GLES20.glUniformMatrix4fv(_modelMatrixHandle, 1, false, shaderParams.ModelMatrix, 0);

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(_vpMatrixHandle, 1, false, shaderParams.VPMatrix, 0);

        // Draw the triangles
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.indexDrawCount, GLES20.GL_UNSIGNED_SHORT, mesh.indexBuffer);

        // Disable the handle to the vertices
        GLES20.glDisableVertexAttribArray(_positionHandle);
    }
}
