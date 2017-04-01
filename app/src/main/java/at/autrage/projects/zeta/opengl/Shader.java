package at.autrage.projects.zeta.opengl;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import at.autrage.projects.zeta.module.Logger;

public abstract class Shader {
    protected int m_VertexShader;
    protected int m_FragmentShader;
    protected int m_Program;

    protected static int _currProgram = -1;
    protected static int _currTextureDataHandle = -1;

    public Shader() {
    }

    public abstract void bindAttribLocations();

    public abstract void getAttribLocations();

    private String readShaderFileContent(String shaderFile, Context context) {
        if (context == null) {
            Logger.E("Could not read shader \"" + shaderFile + "\", because context is null!");
            return null;
        }

        AssetManager am = context.getAssets();
        BufferedReader reader = null;

        String newLine = System.getProperty("line.separator");
        String nextLine;
        String body = "";

        try {
            reader = new BufferedReader(new InputStreamReader(am.open(shaderFile, AssetManager.ACCESS_BUFFER)));
            while ((nextLine = reader.readLine()) != null) {
                body += nextLine;
                body += newLine;
            }
        } catch (Exception e) {
            Logger.E("Could not read shader \"" + shaderFile + "\": " + e);
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Logger.E("Could not close reader for shader file \"" + shaderFile + "\": " + e);
                }
            }
        }

        return body;
    }

    private int createShader(int type, String shaderFile, Context context) {
        String shaderCode = readShaderFileContent(shaderFile, context);
        if (shaderCode == null) {
            return 0;
        }

        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        // Now see if the compilation worked.
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Logger.E("Load Shader Failed, Compilation\n" + GLES20.glGetShaderInfoLog(shader));
            return 0;
        }

        return shader;
    }

    public void createVertexShader(String vertexShaderFile, Context context) {
        if (m_VertexShader != 0) {
            Logger.E("Could not create vertex shader, because it is already loaded!");
            return;
        }

        m_VertexShader = createShader(GLES20.GL_VERTEX_SHADER, vertexShaderFile, context);
    }

    public void deleteVertexShader() {
        if (m_VertexShader == 0) {
            Logger.W("Could not delete vertex shader, because it is not loaded!");
            return;
        }

        GLES20.glDeleteShader(m_VertexShader);
        m_VertexShader = 0;
    }

    public void createFragmentShader(String fragmentShaderFile, Context context) {
        if (m_FragmentShader != 0) {
            Logger.E("Could not create fragment shader, because it is already loaded!");
            return;
        }

        m_FragmentShader = createShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderFile, context);
    }

    public void deleteFragmentShader() {
        if (m_FragmentShader == 0) {
            Logger.W("Could not delete fragment shader, because it is not loaded!");
            return;
        }

        GLES20.glDeleteShader(m_FragmentShader);
        m_FragmentShader = 0;
    }

    public boolean createProgram() {
        if (m_VertexShader == 0) {
            Logger.E("Could not create shader program, because vertex shader is not loaded!");
            return false;
        }

        if (m_FragmentShader == 0) {
            Logger.E("Could not create shader program, because fragment shader is not loaded!");
            return false;
        }

        m_Program = GLES20.glCreateProgram();               // Create empty OpenGL ES program
        GLES20.glAttachShader(m_Program, m_VertexShader);   // Add the vertex shader to program
        GLES20.glAttachShader(m_Program, m_FragmentShader); // Add the fragment shader to program

        bindAttribLocations();

        GLES20.glLinkProgram(m_Program);                    // Creates OpenGL ES program executables

        // Get the link status.
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(m_Program, GLES20.GL_LINK_STATUS, linkStatus, 0);

        // If the link failed, delete the program.
        if (linkStatus[0] == 0) {
            Logger.E("Could not link shader program!");
            GLES20.glDeleteProgram(m_Program);
            m_Program = 0;

            return false;
        }

        getAttribLocations();

        return true;
    }

    public void deleteProgram() {
        if (m_Program == 0) {
            Logger.W("Could not delete shader program, because it is not loaded!");
            return;
        }

        GLES20.glDeleteProgram(m_Program);
        m_Program = 0;
    }

    public void reset() {
        m_VertexShader = 0;
        m_FragmentShader = 0;
        m_Program = 0;

        _currProgram = -1;
        _currTextureDataHandle = -1;
    }

    public abstract void draw(ShaderParams shaderParams);
}
