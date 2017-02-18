package at.autrage.projects.zeta.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {
    private int m_ResourceId;
    private int m_TextureDataHandle;

    private int m_Width;
    private int m_Height;

    public Texture(int resourceId) {
        m_ResourceId = resourceId;
    }

    public void load(final Context context, int textureHandle)
    {
        if (textureHandle != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false; // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), m_ResourceId, options);

            m_Width = bitmap.getWidth();
            m_Height = bitmap.getHeight();

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        m_TextureDataHandle = textureHandle;
    }

    public int getResourceId() {
        return m_ResourceId;
    }

    public int getTextureDataHandle() {
        return m_TextureDataHandle;
    }

    public int getWidth() {
        return m_Width;
    }

    public int getHeight() {
        return m_Height;
    }
}
