package paulevs.creative.mixin;

import java.util.Locale;

import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.TallGrass;
import net.minecraft.client.render.Tessellator;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;

@Mixin(TallGrass.class)
public class TallGrassMixin implements BlockItemProvider {
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Override
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				Tessellator.INSTANCE.colour(255, 0, 0);
				return BlockBase.TALLGRASS.getTextureForSide(0, damage);
			}
			
			@Environment(EnvType.CLIENT)
			public String getTranslationKey(ItemInstance item) {
				return String.format(Locale.ROOT, "Tall Grass [%d]", item.getDamage());
			}
		};
	}
}
