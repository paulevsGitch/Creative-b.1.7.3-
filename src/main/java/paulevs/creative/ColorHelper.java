package paulevs.creative;

import net.minecraft.client.render.block.GrassColour;

public class ColorHelper {
	public static final SimpleColor GRASS_COLOR;
	
	static {
		int color = GrassColour.get(0.75F, 0.75F);
		float r = ((color >> 16) & 255) / 255F;
		float g = ((color >> 8) & 255) / 255F;
		float b = (color & 255) / 255F;
		GRASS_COLOR = new SimpleColor(r, g, b);
	}
	
	public static final class SimpleColor {
		public final float r;
		public final float g;
		public final float b;
		
		public SimpleColor(float r, float g, float b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}
}
