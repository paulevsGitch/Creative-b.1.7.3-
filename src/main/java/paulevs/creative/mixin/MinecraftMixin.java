package paulevs.creative.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import paulevs.creative.ColorHelper;
import paulevs.creative.api.CreativeTabs;
import paulevs.creative.api.TabRegister;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(method = "init", at = @At("TAIL"))
	public void creative_init(CallbackInfo info) {
		CreativeTabs.initVanilla();
		TabRegister.EVENT.getInvoker().registerTabs();
		CreativeTabs.initTabs();
		ColorHelper.init();
	}
}
