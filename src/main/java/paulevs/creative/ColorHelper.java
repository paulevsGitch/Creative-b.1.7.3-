package paulevs.creative;

import net.minecraft.client.render.block.GrassColour;

public class ColorHelper {
	public static SimpleColor grassColor;
	public static boolean renderGrassTop;
	public static int intGrassColor;
	public static float light;
	
	public static void init() {
		intGrassColor = GrassColour.get(0.75F, 0.75F);
		float r = ((intGrassColor >> 16) & 255) / 255F;
		float g = ((intGrassColor >> 8) & 255) / 255F;
		float b = (intGrassColor & 255) / 255F;
		grassColor = new SimpleColor(r, g, b);
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
