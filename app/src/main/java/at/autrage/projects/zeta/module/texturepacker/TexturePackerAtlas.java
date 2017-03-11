package at.autrage.projects.zeta.module.texturepacker;

import java.util.LinkedHashMap;

public class TexturePackerAtlas {
	public final String filename;
	private final LinkedHashMap<String, PackedTexture> packedTextures;
	public final long w;
	public final long h;


	public TexturePackerAtlas(String filename, LinkedHashMap<String, PackedTexture> unpackedTextures, Long w, Long h) {
		super();
		this.filename = filename;
		this.packedTextures = unpackedTextures;
		this.w = w;
		this.h = h;
	}

	public PackedTexture getTextureByFilename(String filename){
		return packedTextures.get(filename);
	}
}
