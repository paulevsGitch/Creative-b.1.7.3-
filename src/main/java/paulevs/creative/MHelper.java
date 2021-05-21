package paulevs.creative;

public class MHelper {
	public static final float PI = (float) Math.PI;

	public static double clamp(double value, double min, double max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static double sign(double value) {
		return value > 0 ? 1 : value < 0 ? -1 : 0;
	}
	
	public static int getColor(int r, int g, int b, int a) {
		return a << 24 | r << 16 | g << 8 | b;
	}
	
	public static boolean isInRange(double value, double min, double max) {
		return value > min && value < max;
	}
	
	public static boolean isNearZero(double value) {
		return isInRange(value, -0.01, 0.01);
	}
}
