package at.autrage.projects.zeta.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.view.GameView;

public class Sprite extends GameObject {

    public static final float BaseVertices[] = {
        -0.5f,  0.5f, 0.0f, // top left
        -0.5f, -0.5f, 0.0f, // bottom left
         0.5f, -0.5f, 0.0f, // bottom right
         0.5f,  0.5f, 0.0f  // top right
    };

    public static final short BaseIndices[] = {
        0, 1, 2, // Triangle1
        0, 2, 3  // Triangle2
    };

    private FloatBuffer m_Vertices;
    private ShortBuffer m_Indices;

    public Sprite(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, AssetManager.getInstance().getSpriteShader(), animationSet);

        // Initialize vertex byte buffer for shape coordinates
        // (Number of coordinate values * 4 bytes per float)
        ByteBuffer bb = ByteBuffer.allocateDirect(BaseVertices.length * 4);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a floating point buffer from the ByteBuffer
        m_Vertices = bb.asFloatBuffer();
        // Add the coordinates to the FloatBuffer
        m_Vertices.put(BaseVertices);
        // Set the buffer to read the first coordinate
        m_Vertices.position(0);

        // Initialize byte buffer for the draw list
        // (Number of values * 2 bytes per short)
        bb = ByteBuffer.allocateDirect(BaseIndices.length * 2);
        // Use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // Create a short buffer from the ByteBuffer
        m_Indices = bb.asShortBuffer();
        // Add the values to the ShortBuffer
        m_Indices.put(BaseIndices);
        // Set the buffer to read the first value
        m_Indices.position(0);
    }

    @Override
    public void onRender() {

    }
}
