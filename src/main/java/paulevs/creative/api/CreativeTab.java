package paulevs.creative.api;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.google.common.collect.Lists;

import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.Coal;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.Leaves;
import net.minecraft.item.Log;
import net.minecraft.item.Sapling;
import net.modificationstation.stationloader.mixin.common.accessor.TranslationStorageAccessor;

public abstract class CreativeTab implements Comparable<CreativeTab> {
	private final List<ItemInstance> items = Lists.newArrayList();
	private final String modID;
	private final String name;
	
	public CreativeTab(String name, String modID) {
		this.modID = modID;
		this.name = name;
	}
	
	public abstract ItemInstance getIcon();
	
	public List<ItemInstance> getItems() {
		return items;
	}
	
	public String getTranslationKey() {
		return String.format(Locale.ROOT, "tab.%s:%s.name", modID, name);
	}
	
	public String getTranslatedName() {
		Properties translations = ((TranslationStorageAccessor) TranslationStorage.getInstance()).getTranslations();
		String key = getTranslationKey();
		String result = translations.getProperty(getTranslationKey());
		return result == null || result.isEmpty() ? key : result;
	}
	
	public String getName() {
		return name;
	}
	
	public void addItem(ItemInstance item) {
		items.add(item);
	}
	
	public void addItemWithVariants(ItemBase item) {
		if (item.getHasSubItems() && !item.hasDurability()) {
			Properties translations = ((TranslationStorageAccessor) TranslationStorage.getInstance()).getTranslations();
			int maxMeta = 16;
			if (item instanceof Coal) {
				maxMeta = 1;
			}
			else if (item instanceof Leaves || item instanceof Log || item instanceof Sapling) {
				maxMeta = 4;
			}
			for (int meta = 0; meta < maxMeta; meta++) {
				try {
					ItemInstance instance = new ItemInstance(item, 1, meta);
					String key = item.getTranslationKey(instance) + ".name";
					key = translations.getProperty(key);
					if (key == null || key.isEmpty()) {
						return;
					}
					items.add(instance);
				}
				catch (Exception e) {
					return;
				}
			}
		}
		else {
			items.add(new ItemInstance(item.id, 1, 0));
		}
	}
	
	@Override
	public int compareTo(CreativeTab tab) {
		String key1 = getTranslationKey();
		String key2 = tab.getTranslationKey();
		return key1.compareTo(key2);
	}

	public String getModID() {
		return modID;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "[tab, %s, %s]", modID, name);
	}
}
