package at.autrage.projects.zeta.view;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Logger;

public class GameViewRenderer implements GLSurfaceView.Renderer {
    /** Reference to the {@link GameView} object. */
    private GameView m_GameView;

    private final float[] m_MVPMatrix = new float[16];
    private final float[] m_ProjectionMatrix = new float[16];
    private final float[] m_ViewMatrix = new float[16];

    public GameViewRenderer(GameView gameView) {
        m_GameView = gameView;
    }

    @Override
    public void onSurfaceCreated(GL10 notUsed, EGLConfig config) {
        Logger.D("GameViewRenderer::onSurfaceCreated");
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);

        // Load assets from the OpenGL thread
        AssetManager.getInstance().onSurfaceCreatedFromOpenGL(m_GameView.getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 notUsed, int width, int height) {
        Logger.D("GameViewRenderer::onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(m_ProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 notUsed) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(m_ViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(m_MVPMatrix, 0, m_ProjectionMatrix, 0, m_ViewMatrix, 0);

        // Draw game objects
        m_GameView.draw(m_MVPMatrix);
    }
}
