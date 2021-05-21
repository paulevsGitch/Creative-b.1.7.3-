package paulevs.creative.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;
import paulevs.creative.CreativePlayer;
import paulevs.creative.MHelper;

@Mixin(PlayerBase.class)
public abstract class PlayerBaseMixin extends Living implements CreativePlayer {
	private static final float CREATIVE_MAX_SPEED = 0.4F;
	private final Vec3f creative_flightSpeed = Vec3f.method_1293(0, 0, 0);
	private boolean creative_isCreative;
	private boolean creative_isFlying;
	
	public PlayerBaseMixin(Level arg) {
		super(arg);
	}
	
	@Override
	public boolean isCreative() {
		return creative_isCreative;
	}
	
	@Override
	public void setCreative(boolean creative) {
		this.creative_isCreative = creative;
	}
	
	@Override
	public boolean isFlying() {
		return creative_isFlying;
	}
	
	@Override
	public void setFlying(boolean flying) {
		this.creative_isFlying = flying;
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
			if (this.isFlying() && !this.isSleeping()) {
				LivingAccessor living = (LivingAccessor) (Object) this;
				float perpen = living.getPerpendicularMovement();
				float parall = living.getParallelMovement();
				float angle = this.yaw * MHelper.PI / 180.0F;
				float sin = MathHelper.sin(angle);
				float cos = MathHelper.cos(angle);
				float dx = (perpen * cos - parall * sin);
				float dz = (parall * cos + perpen * sin);
				creative_flightSpeed.x = creative_flightSpeed.x * 0.9 + dx * 0.05;
				creative_flightSpeed.z = creative_flightSpeed.z * 0.9 + dz * 0.05;
				creative_flightSpeed.x = MHelper.clamp(creative_flightSpeed.x, -CREATIVE_MAX_SPEED, CREATIVE_MAX_SPEED);
				creative_flightSpeed.z = MHelper.clamp(creative_flightSpeed.z, -CREATIVE_MAX_SPEED, CREATIVE_MAX_SPEED);
				
				boolean sneaking = this.method_1373();
				
				creative_flightSpeed.y *= 0.9;
				if (this.jumping) {
					creative_flightSpeed.y += 0.05;
					if (creative_flightSpeed.y > CREATIVE_MAX_SPEED) {
						creative_flightSpeed.y = CREATIVE_MAX_SPEED;
					}
				}
				if (sneaking) {
					creative_flightSpeed.y -= 0.05;
					if (creative_flightSpeed.y < -CREATIVE_MAX_SPEED) {
						creative_flightSpeed.y = -CREATIVE_MAX_SPEED;
					}
				}
				
				this.velocityX = creative_flightSpeed.x;
				this.velocityY = creative_flightSpeed.y;
				this.velocityZ = creative_flightSpeed.z;
				
				if (this.onGround) {
					this.setFlying(false);
					this.velocityX = 0;
					this.velocityY = 0;
					this.velocityZ = 0;
					creative_flightSpeed.x = 0;
					creative_flightSpeed.y = 0;
					creative_flightSpeed.z = 0;
				}
			}
		}
	}
}
