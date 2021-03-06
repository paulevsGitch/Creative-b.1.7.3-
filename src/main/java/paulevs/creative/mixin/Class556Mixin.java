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
	@Inject(at = @At(value = "INVOKE", target = ColorHelper.TESSELLATOR_TARGET, ordinal = 0), method = "method_1862", cancellable = true)
	private void creative_fixItemColorInHand(Living player, ItemInstance item, CallbackInfo info) {
		if (item.itemId == BlockBase.TALLGRASS.id && item.getDamage() > 0) {
			float light = player.getBrightnessAtEyes(player.pitch);
			float r = ColorHelper.grassColor.r * light;
			float g = ColorHelper.grassColor.g * light;
			float b = ColorHelper.grassColor.b * light;
			GL11.glColor3f(r, g, b);
		}
	}
}
