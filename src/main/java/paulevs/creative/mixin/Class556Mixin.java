package paulevs.creative.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.class_556;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import paulevs.creative.ColorHelper;

@Mixin(class_556.class)
public class Class556Mixin {
	@Inject(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/Tessellator;start()V",
			ordinal = 0
		),
		method = "method_1862", cancellable = true
	)
	private void creative_fixItemColorInHand(Living player, ItemInstance item, CallbackInfo info) {
		if (item.itemId == BlockBase.TALLGRASS.id && item.getDamage() > 0) {
			float light = player.getBrightnessAtEyes(player.pitch);
			float r = ColorHelper.GRASS_COLOR.r * light;
			float g = ColorHelper.GRASS_COLOR.g * light;
			float b = ColorHelper.GRASS_COLOR.b * light;
			GL11.glColor3f(r, g, b);
		}
	}
}
