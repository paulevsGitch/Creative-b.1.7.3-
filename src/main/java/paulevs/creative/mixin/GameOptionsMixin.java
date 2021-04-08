package paulevs.creative.mixin;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.TranslationStorage;
import paulevs.creative.FlyOption;

@Mixin(GameOptions.class)
public class GameOptionsMixin implements FlyOption {
	private KeyBinding flyKey = new KeyBinding("key.fly", Keyboard.KEY_G);
	private String keyName = "key.creative:fly";
	private int flyKeyIndex;
	
	@Shadow
	public KeyBinding[] keyBindings;
	
	@Inject(method = "load", at = @At("HEAD"))
	private void creative_onGameOptionsLoad(CallbackInfo info) {
		KeyBinding[] oldBindings = keyBindings;
		keyBindings = new KeyBinding[oldBindings.length + 1];
		System.arraycopy(oldBindings, 0, keyBindings, 0, oldBindings.length);
		flyKeyIndex = keyBindings.length - 1;
		keyBindings[flyKeyIndex] = flyKey;
	}

	@Override
	public KeyBinding getFlyKey() {
		return flyKey;
	}
	
	@Inject(method = "getKeybindName", at = @At("HEAD"), cancellable = true)
	private void creative_getKeybindName(int index, CallbackInfoReturnable<String> info) {
		if (index == flyKeyIndex) {
			TranslationStorage storage = TranslationStorage.getInstance();
			info.setReturnValue(storage.translate(keyName));
			info.cancel();
		}
	}
}
