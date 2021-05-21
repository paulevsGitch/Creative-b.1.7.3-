package paulevs.creative.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.entity.Item;
import net.minecraft.util.maths.Vec2i;
import paulevs.creative.ColorHelper;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	private static final Vec2i GRASS_UV1 = new Vec2i(7 << 4, 2 << 4);
	private static final Vec2i GRASS_UV2 = new Vec2i(8 << 4, 3 << 4);
	
	@Inject(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/Tessellator;start()V",
			ordinal = 0
		),
		method = "render", cancellable = true
	)
	private void creative_fixItemColorInWorld(Item item, double x, double y, double z, float yaw, float pitch, CallbackInfo info) {
		if (item.item.itemId == BlockBase.TALLGRASS.id && item.item.getDamage() > 0) {
			float light = item.getBrightnessAtEyes(pitch);
			float r = ColorHelper.GRASS_COLOR.r * light;
			float g = ColorHelper.GRASS_COLOR.g * light;
			float b = ColorHelper.GRASS_COLOR.b * light;
			GL11.glColor3f(r, g, b);
		}
	}
	
	@Inject(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/Tessellator;start()V",
			ordinal = 0
		),
		method = "method_1483", cancellable = true
	)
	private void creative_fixItemColorInGUI(int x, int y, int u, int v, int width, int height, CallbackInfo info) {
		if ((u == GRASS_UV1.x && v == GRASS_UV1.z) || (u == GRASS_UV2.x && v == GRASS_UV2.z)) {
			float r = ColorHelper.GRASS_COLOR.r;
			float g = ColorHelper.GRASS_COLOR.g;
			float b = ColorHelper.GRASS_COLOR.b;
			GL11.glColor3f(r, g, b);
		}
	}
}
