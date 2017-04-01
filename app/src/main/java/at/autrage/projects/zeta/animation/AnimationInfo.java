package at.autrage.projects.zeta.animation;

public class AnimationInfo {
    public final int TextureResourceID;
    public final Animations AnimationID;
    public final String PackedTextureName;
    public final String TextureAtlasJSONFile;

    public final int TextureSizeX;
    public final int TextureSizeY;

    public final int FrameSizeX;
    public final int FrameSizeY;

    public final int StartTexCoordX;
    public final int StartTexCoordY;

    public final int EndTexCoordX;
    public final int EndTexCoordY;

    public final float Duration;

    public AnimationInfo(int textureResourceID, Animations animationID, String packedTextureName, String textureAtlasJSONFile) {
        TextureResourceID = textureResourceID;
        AnimationID = animationID;
        PackedTextureName = packedTextureName;
        TextureAtlasJSONFile = textureAtlasJSONFile;

        TextureSizeX = -1;
        TextureSizeY = -1;

        FrameSizeX = -1;
        FrameSizeY = -1;

        StartTexCoordX = -1;
        StartTexCoordY = -1;

        EndTexCoordX = -1;
        EndTexCoordY = -1;

        Duration = 0f;
    }

    public AnimationInfo(int textureResourceID, Animations animationID, String packedTextureName, String textureAtlasJSONFile, int frameSizeX, int frameSizeY, float duration) {
        TextureResourceID = textureResourceID;
        AnimationID = animationID;
        PackedTextureName = packedTextureName;
        TextureAtlasJSONFile = textureAtlasJSONFile;

        TextureSizeX = -1;
        TextureSizeY = -1;

        FrameSizeX = frameSizeX;
        FrameSizeY = frameSizeY;

        StartTexCoordX = -1;
        StartTexCoordY = -1;

        EndTexCoordX = -1;
        EndTexCoordY = -1;

        Duration = duration;
    }

    public AnimationInfo(int textureResourceID, Animations animationID, String packedTextureName, int textureSizeX, int textureSizeY) {
        TextureResourceID = textureResourceID;
        AnimationID = animationID;
        PackedTextureName = packedTextureName;
        TextureAtlasJSONFile = null;

        TextureSizeX = textureSizeX;
        TextureSizeY = textureSizeY;

        FrameSizeX = textureSizeX;
        FrameSizeY = textureSizeY;

        StartTexCoordX = 0;
        StartTexCoordY = 0;

        EndTexCoordX = textureSizeX;
        EndTexCoordY = textureSizeY;

        Duration = 0f;
    }

    public AnimationInfo(int textureResourceID, Animations animationID, String packedTextureName, int textureSizeX, int textureSizeY, int frameSizeX, int frameSizeY, float duration) {
        TextureResourceID = textureResourceID;
        AnimationID = animationID;
        PackedTextureName = packedTextureName;
        TextureAtlasJSONFile = null;

        TextureSizeX = textureSizeX;
        TextureSizeY = textureSizeY;

        FrameSizeX = frameSizeX;
        FrameSizeY = frameSizeY;

        StartTexCoordX = 0;
        StartTexCoordY = 0;

        EndTexCoordX = textureSizeX;
        EndTexCoordY = textureSizeY;

        Duration = duration;
    }

    public AnimationInfo(int textureResourceID, Animations animationID, String packedTextureName, int textureSizeX, int textureSizeY, int frameSizeX, int frameSizeY, int startTexCoordX, int startTexCoordY, int endTexCoordX, int endTexCoordY, float duration) {
        TextureResourceID = textureResourceID;
        AnimationID = animationID;
        PackedTextureName = packedTextureName;
        TextureAtlasJSONFile = null;

        TextureSizeX = textureSizeX;
        TextureSizeY = textureSizeY;

        FrameSizeX = frameSizeX;
        FrameSizeY = frameSizeY;

        StartTexCoordX = startTexCoordX;
        StartTexCoordY = startTexCoordY;

        EndTexCoordX = endTexCoordX;
        EndTexCoordY = endTexCoordY;

        Duration = duration;
    }
}
