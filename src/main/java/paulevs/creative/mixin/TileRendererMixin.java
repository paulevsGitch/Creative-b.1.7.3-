package paulevs.creative.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.TileRenderer;
import paulevs.creative.ColorHelper;

@Mixin(TileRenderer.class)
public class TileRendererMixin {
	@Inject(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/Tessellator;start()V",
			ordinal = 0
		),
		method = "method_48", cancellable = true
	)
	private void creative_fixGrassStart(BlockBase block, int meta, float light, CallbackInfo info) {
		if (block == BlockBase.GRASS) {
			ColorHelper.renderGrassTop = true;
			ColorHelper.light = light;
		}
	}
}
