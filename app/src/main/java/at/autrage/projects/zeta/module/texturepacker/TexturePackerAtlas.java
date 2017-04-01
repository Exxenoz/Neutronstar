package at.autrage.projects.zeta.module.texturepacker;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TexturePackerAtlas {
	public final String fileName;
	public final int w;
	public final int h;

	private final HashMap<String, PackedTexture> packedTextures;

	public TexturePackerAtlas(String fileName, int w, int h, HashMap<String, PackedTexture> unpackedTextures) {
		this.fileName = fileName;
		this.w = w;
		this.h = h;
		this.packedTextures = unpackedTextures;
	}

	public PackedTexture getTextureByFilename(String filename){
		return packedTextures.get(filename);
	}
}
