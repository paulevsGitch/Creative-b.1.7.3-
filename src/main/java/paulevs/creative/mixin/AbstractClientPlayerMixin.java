package paulevs.creative.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import paulevs.creative.CreativePlayer;
import paulevs.creative.FlyOption;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends PlayerBase {
	public AbstractClientPlayerMixin(Level arg) {
		super(arg);
	}
	
	@Shadow
	protected Minecraft minecraft;
	
	@Inject(method = "getCanSuffocate", at = @At("HEAD"), cancellable = true)
	private void creative_getCanSuffocate(int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
		if (((CreativePlayer) this).isCreative()) {
			info.setReturnValue(false);
			info.cancel();
		}
	}
	
	@Inject(method = "method_136", at = @At("HEAD"), cancellable = true)
	public void creative_onKeyPress(int i, boolean flag, CallbackInfo info) {
		CreativePlayer player = (CreativePlayer) this;
		if (player.isCreative()) {
			if (flag && i == ((FlyOption) minecraft.options).getFlyKey().key) {
				boolean fly = !player.isFlying();
				player.setFlying(fly);
				if (fly) {
					AbstractClientPlayer client = (AbstractClientPlayer) (Object) this;
					client.setPositionAndAngles(client.x, client.y - client.standingEyeHeight + 0.01, client.z, client.yaw, client.pitch);
					client.velocityY = Math.max(client.velocityY * 0.7, 0.01);
				}
				info.cancel();
			}
		}
	}
}
