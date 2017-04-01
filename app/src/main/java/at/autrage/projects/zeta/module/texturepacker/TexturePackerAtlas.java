package at.autrage.projects.zeta.module.texturepacker;

import java.util.HashMap;

public class TexturePackerAtlas {
	public final String fileName;
	public final int w;
	public final int h;

	private final HashMap<String, PackedTexture> packedTextures;

	public TexturePackerAtlas(String fileName, int w, int h, HashMap<String, PackedTexture> packedTextures) {
		this.fileName = fileName;
		this.w = w;
		this.h = h;
		this.packedTextures = packedTextures;
	}

	public PackedTexture getTextureByFileName(String filename){
		return packedTextures.get(filename);
	}
}
