package at.autrage.projects.zeta.module.texturepacker;

public class PackedTexture {
	public final String fileName;
	public final int w;
	public final int h;
	public final int x;
	public final int y;

	public PackedTexture(String fileName, int w, int h, int x, int y) {
		this.fileName = fileName;
		this.w = w;
		this.h = h;
		this.x = x;
		this.y = y;
	}
}
