package at.autrage.projects.zeta.view;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;

public class GameViewRenderer implements GLSurfaceView.Renderer {
    /** Reference to the {@link GameView} object. */
    private GameView m_GameView;

    private final float[] m_VPMatrix = new float[16];
    private final float[] m_ProjectionMatrix = new float[16];
    private final float[] m_ViewMatrix = new float[16];

    private long m_LastTime;
    private long m_DiffTime;

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
        // Set the view port to draw on the whole screen
        GLES20.glViewport(0, 0, width, height);

        // This projection matrix is applied to object coordinates in the onDrawFrame() method
        Matrix.orthoM(m_ProjectionMatrix, 0,
                -Pustafin.HalfReferenceResolutionX, Pustafin.HalfReferenceResolutionX,  // Left / Right
                -Pustafin.HalfReferenceResolutionY, Pustafin.HalfReferenceResolutionY,  // Bottom / Top
                -Pustafin.HalfReferenceResolutionX, Pustafin.HalfReferenceResolutionX   // Near / Far
        );

        // Set the camera position (View matrix)
        Matrix.setLookAtM(m_ViewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(m_VPMatrix, 0, m_ProjectionMatrix, 0, m_ViewMatrix, 0);

        // Enable blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA );

        // Enable culling
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
    }

    @Override
    public void onDrawFrame(GL10 notUsed) {
        m_LastTime = System.currentTimeMillis();

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Draw game objects
        m_GameView.draw(m_VPMatrix);

        m_DiffTime = System.currentTimeMillis() - m_LastTime;
        Time.setDeltaTimeGLInMS(m_DiffTime);
    }
}
