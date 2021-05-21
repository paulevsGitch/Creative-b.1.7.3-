package paulevs.creative.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.entity.Item;
import net.minecraft.util.maths.Vec2i;
import net.modificationstation.stationloader.impl.client.texture.TextureRegistry;
import paulevs.creative.ColorHelper;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	private static final Vec2i CREATIVE_GRASS_UV1 = new Vec2i(7 << 4, 2 << 4);
	private static final Vec2i CREATIVE_GRASS_UV2 = new Vec2i(8 << 4, 3 << 4);
	
	@Inject(at = @At(value = "INVOKE", target = ColorHelper.TESSELLATOR_TARGET, ordinal = 0), method = "render", cancellable = true)
	private void creative_fixItemColorInWorld(Item item, double x, double y, double z, float yaw, float pitch, CallbackInfo info) {
		if (item.item.itemId == BlockBase.TALLGRASS.id && item.item.getDamage() > 0) {
			float light = item.getBrightnessAtEyes(pitch);
			float r = ColorHelper.grassColor.r * light;
			float g = ColorHelper.grassColor.g * light;
			float b = ColorHelper.grassColor.b * light;
			GL11.glColor3f(r, g, b);
		}
	}
	
	@Inject(at = @At(value = "INVOKE", target = ColorHelper.TESSELLATOR_TARGET, ordinal = 0, shift = Shift.AFTER), method = "method_1483", cancellable = true)
	private void creative_fixItemColorInGUIStart(int x, int y, int u, int v, int width, int height, CallbackInfo info) {
		if (creative_isTerrain() && creative_isCorrectUV(u, v)) {
			Tessellator.INSTANCE.colour(ColorHelper.intGrassColor);
		}
	}
	
	private boolean creative_isTerrain() {
		return TextureRegistry.currentRegistry().equals(TextureRegistry.TERRAIN);
	}
	
	private boolean creative_isCorrectUV(int u, int v) {
		return (u == CREATIVE_GRASS_UV1.x && v == CREATIVE_GRASS_UV1.z) || (u == CREATIVE_GRASS_UV2.x && v == CREATIVE_GRASS_UV2.z);
	}
}
