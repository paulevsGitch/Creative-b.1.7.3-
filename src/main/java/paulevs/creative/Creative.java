package paulevs.creative;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import paulevs.creative.api.SimpleTab;
import paulevs.creative.api.TabRegister;
import paulevs.creative.api.CreativeTabs;

public class Creative implements StationMod {
	public static final String MOD_ID = "creative";
	
	@Override
	public void preInit() {
		TabRegister.EVENT.register(new TabRegister() {
			@Override
			public void registerTabs() {
				SimpleTab tab = CreativeTabs.register(new SimpleTab("test", "coolmod", ItemBase.string));
				tab.addItem(new ItemInstance(ItemBase.stonePickaxe));
				tab.addItem(new ItemInstance(BlockBase.BEDROCK));
			}
		});
	}
}
