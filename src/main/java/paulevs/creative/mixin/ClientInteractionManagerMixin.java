package paulevs.creative.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import paulevs.creative.CreativePlayer;

@Mixin(ClientInteractionManager.class)
public class ClientInteractionManagerMixin {
	@Shadow
	private Minecraft minecraft;
	
	@Inject(method = "method_1722", at = @At("HEAD"), cancellable = true)
	private void creative_renderHud(CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(!((CreativePlayer) minecraft.player).isCreative());
	}
}
