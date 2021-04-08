package paulevs.creative.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import paulevs.creative.CreativePlayer;
import paulevs.creative.MHelper;

@Mixin(PlayerBase.class)
public abstract class PlayerBaseMixin extends Living implements CreativePlayer {
	private boolean isCreative;
	private boolean flying;
	private int jumpTicks;
	//private boolean flyingPressed;
	//private boolean sneakingPressed;
	
	public PlayerBaseMixin(Level arg) {
		super(arg);
	}
	
	@Override
	public boolean isCreative() {
		return isCreative;
	}
	
	@Override
	public void setCreative(boolean creative) {
		this.isCreative = creative;
	}
	
	@Override
	public boolean isFlying() {
		return flying;
	}
	
	@Override
	public void setFlying(boolean flying) {
		this.flying = flying;
	}
	
	@Override
	public int getJumpTicks() {
		return jumpTicks;
	}
	
	@Override
	public void setJumpTicks(int ticks) {
		this.jumpTicks = ticks;
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void creative_damage(EntityBase target, int amount, CallbackInfoReturnable<Boolean> info) {
		if (this.isCreative()) {
			info.setReturnValue(false);
			info.cancel();
		}
	}
	
	@Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
	private void creative_applyDamage(int damageAmount, CallbackInfo info) {
		if (this.isCreative()) {
			info.cancel();
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void creative_writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		tag.put("Creative", isCreative());
		tag.put("Flying", isFlying());
	}

	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void creative_readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		setCreative(tag.getBoolean("Creative"));
		setFlying(tag.getBoolean("Flying"));
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void creative_tick(CallbackInfo info) {
		if (this.onGround || !this.isCreative()) {
			this.setFlying(false);
		}
		if (this.isCreative()) {
			/*if (this.jumping) {
				this.setJumpTicks(this.getJumpTicks() + 1);
			}
			else {
				this.setJumpTicks(0);
			}
			if (getJumpTicks() > 10) {
				this.setFlying(true);
			}*/
			if (this.isFlying() && !this.isSleeping()) {
				//this.onGround = true;
				//this.movementSpeed = 10F;
				if (this.jumping) {
					this.velocityY = MHelper.clamp(this.velocityY + 0.1, 0.1, 1.0);
				}
				else if (this.method_1373()) {
					velocityY += 0.08;
					this.velocityY = MHelper.clamp(this.velocityY - 0.1, -1.0, -0.1);
				}
				else {
					if (this.velocityY < 0.2 && this.velocityY > -0.2) {
						this.velocityY = 0;
					}
					else {
						this.velocityY *= 0.25;
					}
				}
				
				//this.field_1060 = 0.0F;
				//this.field_1029 = 0.0F;
				//this.field_1030 = 0.0F;
				
				//this.velocityX = MHelper.clamp(this.velocityX + MHelper.sign(this.velocityX) * 0.05, -1.5, 1.5);
				//this.velocityZ = MHelper.clamp(this.velocityZ + MHelper.sign(this.velocityZ) * 0.05, -1.5, 1.5);
				
				if (this.onGround) {
					this.setFlying(false);
					this.velocityX = 0;
					this.velocityY = 0;
					this.velocityZ = 0;
				}
			}
		}
	}
	
	@Inject(method = "canRemoveBlock", at = @At("HEAD"), cancellable = true)
	private void creative_canRemoveBlock(BlockBase arg, CallbackInfoReturnable<Boolean> info) {
		if (this.isCreative()) {
			info.setReturnValue(true);
			info.cancel();
		}
	}
	
	/*public boolean isFlyingKeyPressed() {
		return flyingPressed;
	}
	
	public boolean isSneakingKeyPressed() {
		return sneakingPressed;
	}
	
	public void setFlyingKeyPressed(boolean value) {
		flyingPressed = value;
	}
	
	public void setSneakingKeyPressed(boolean value) {
		sneakingPressed = value;
	}*/
}
