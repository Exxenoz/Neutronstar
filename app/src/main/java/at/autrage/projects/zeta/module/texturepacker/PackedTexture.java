package at.autrage.projects.zeta.module.texturepacker;

public class PackedTexture {
	public final String filename;
	public final long width;
	public final long height;
	public final long x;
	public final long y;


	public PackedTexture(String filename, long width, long height, long x, long y) {
		super();
		this.filename = filename;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

}
