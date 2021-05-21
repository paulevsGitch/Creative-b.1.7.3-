package paulevs.creative.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Grass;
import net.minecraft.block.material.Material;
import paulevs.creative.ColorHelper;

@Mixin(Grass.class)
public abstract class GrassMixin extends BlockBase {
	protected GrassMixin(int id, int tex, Material material) {
		super(id, tex, material);
	}

	@Override
	public int getTextureForSide(int side) {
		if (ColorHelper.renderGrassTop) {
			if (side == 1) {
				GL11.glColor3f(
					ColorHelper.grassColor.r * ColorHelper.light,
					ColorHelper.grassColor.g * ColorHelper.light,
					ColorHelper.grassColor.b * ColorHelper.light
				);
			}
			else if (side > 1) {
				ColorHelper.renderGrassTop = false;
				GL11.glColor3f(ColorHelper.light, ColorHelper.light, ColorHelper.light);
			}
		}
		return side == 0 ? 2 : side == 1 ? 0 : 3;
	}
}
