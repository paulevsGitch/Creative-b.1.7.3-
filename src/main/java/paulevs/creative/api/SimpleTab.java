package paulevs.creative.api;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;

public class SimpleTab extends CreativeTab {
	private final ItemInstance icon;
	
	public SimpleTab(String name, String modID, ItemInstance icon) {
		super(name, modID);
		this.icon = icon;
	}
	
	public SimpleTab(String name, String modID, int id, int meta) {
		this(name, modID, new ItemInstance(id, 1, meta));
	}
	
	public SimpleTab(String name, String modID, BlockBase block) {
		this(name, modID, new ItemInstance(block));
	}
	
	public SimpleTab(String name, String modID, ItemBase item) {
		this(name, modID, new ItemInstance(item));
	}

	@Override
	public ItemInstance getIcon() {
		return icon;
	}
}
