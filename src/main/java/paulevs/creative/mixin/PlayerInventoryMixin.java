package paulevs.creative.mixin;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.client.gui.screen.container.PlayerInventory;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.maths.MathHelper;
import paulevs.creative.CreativePlayer;
import paulevs.creative.MHelper;
import paulevs.creative.api.CreativeTab;
import paulevs.creative.api.CreativeTabs;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin extends ContainerBase {
	private static final int COLOR_FILLER = MHelper.getColor(198, 198, 198, 128);
	
	private static ItemRenderer itemRenderer = new ItemRenderer();
	private List<ItemInstance> items;
	private boolean normalGUI;
	private int mouseDelta;
	private String selected;
	private int rowIndex;
	private int maxIndex;
	private float slider;
	private boolean drag;
	private int tabIndex;
	private int tabPage;
	
	private ItemInstance creativeIcon;
	private ItemInstance survivalIcon;
	
	public PlayerInventoryMixin(net.minecraft.container.ContainerBase container) {
		super(container);
	}
	
	@Shadow
	private float mouseX;
	@Shadow
	private float mouseY;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void creative_initPlayerInventory(PlayerBase player, CallbackInfo info) {
		CreativeTab tab = CreativeTabs.getTab(tabPage, tabIndex);
		items = tab.getItems();
		maxIndex = creative_getMaxIndex();
		selected = tab.getTranslatedName();
		rowIndex = 0;
		tabIndex = 0;
		tabPage = 0;
		creativeIcon = new ItemInstance(ItemBase.diamond);
		survivalIcon = new ItemInstance(BlockBase.WORKBENCH);
	}
	
	@Inject(method = "renderContainerBackground", at = @At("RETURN"), cancellable = true)
	private void creative_renderBackgroundEnd(float f, CallbackInfo info) {
		if (creative_isInCreative() && normalGUI) {
			int texture = this.minecraft.textureManager.getTextureId("/assets/creative/textures/gui/creative_list.png");
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.textureManager.bindTexture(texture);
			
			int posX = (this.width - this.containerWidth) / 2;
			int posY = (this.height - this.containerHeight) / 2;
			this.blit(posX + 173, posY + 138, 176, 32, 25, 24);
			
			GL11.glPushMatrix();
			GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
			RenderHelper.enableLighting();
			GL11.glPopMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(32826);
			
			creative_renderItem(creativeIcon, posX + 173 + 4, posY + 114 + 4);
			creative_renderItem(survivalIcon, posX + 173 + 4, posY + 138 + 4);
			
			int tabX = (int) mouseX - posX - 173;
			int tabY = (int) mouseY - posY - 114;
			if (tabX >= 0 && tabX < 25 && tabY >= 0 && tabY < 24) {
				creative_renderString("Creative");
			}
			
			tabY = (int) mouseY - posY - 138;
			if (tabX >= 0 && tabX < 25 && tabY >= 0 && tabY < 24) {
				creative_renderString("Inventory");
			}
		}
	}
	
	@Inject(method = "renderContainerBackground", at = @At("HEAD"), cancellable = true)
	private void creative_renderBackgroundStart(float f, CallbackInfo info) {
		if (creative_isInCreative()) {
			int posX = (this.width - this.containerWidth) / 2;
			int posY = (this.height - this.containerHeight) / 2;
			
			int texture = this.minecraft.textureManager.getTextureId("/assets/creative/textures/gui/creative_list.png");
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.textureManager.bindTexture(texture);
			
			if (normalGUI) {
				this.blit(posX + 173, posY + 114, 176, 32, 25, 24); // Survival
			}
			else {
				net.minecraft.entity.player.PlayerInventory inventory = this.minecraft.player.inventory;
				
				int count = CreativeTabs.getTabCount(tabPage);
				for (int i = 0; i < count; i++) {
					if (i != tabIndex) {
						this.blit(posX + 4 + i * 24, posY - 21, 176, 0, 24, 24);
					}
				}
				
				this.blit(posX + 173, posY + 138, 176, 32, 25, 24);
				this.blit(posX, posY, 0, 0, this.containerWidth, this.containerHeight);
				this.blit(posX + 173, posY + 114, 176, 32, 25, 24);
				
				this.blit(posX + 150, posY + 4, 208, 0, 9, 8);
				if (tabPage == 0) {
					this.fill(posX + 150, posY + 4, posX + 150 + 9, posY + 4 + 8, COLOR_FILLER);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				}
				this.blit(posX + 160, posY + 4, 208, 8, 9, 8);
				if (tabPage >= CreativeTabs.getPagesCount() - 1) {
					this.fill(posX + 160, posY + 4, posX + 160 + 9, posY + 4 + 8, COLOR_FILLER);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				}
				
				int sliderX = posX + 154;
				int sliderY = posY + 14 + MathHelper.floor(slider * 109);
				this.blit(sliderX, sliderY, 240, 1, 14, 15);
				
				this.blit(posX + 4 + tabIndex * 24, posY - 21, 176, 0, 24, 24);
				
				GL11.glPushMatrix();
				GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
				RenderHelper.enableLighting();
				GL11.glPopMatrix();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(32826);
				
				creative_renderItem(creativeIcon, posX + 173 + 4, posY + 114 + 4);
				creative_renderItem(survivalIcon, posX + 173 + 4, posY + 138 + 4);
				
				for (int i = 0; i < count; i++) {
					CreativeTab tab = CreativeTabs.getTab(tabPage, i);
					creative_renderItem(tab.getIcon(), posX + 8 + i * 24, posY - 17);
				}
				
				for (int i = 0; i < 56; i++) {
					int index = rowIndex + i;
					if (index >= 0 && index < items.size()) {
						ItemInstance instance = items.get(index);
						int x = posX + (i & 7) * 18 + 8;
						int y = posY + (i / 8) * 18 + 14;
						creative_renderItem(instance, x, y);
					}
				}
				
				for (int i = 0; i < 9; i++) {
					ItemInstance item = inventory.main[i];
					int x = posX + i * 18 + 8;
					int y = posY + 142;
					creative_renderItem(item, x, y);
				}
				
				int slotX = MathHelper.floor((mouseX - posX - 8) / 18);
				if (slotX >= 0) {
					int slotY = MathHelper.floor((mouseY - posY - 14) / 18);
					if (slotX < 8 && slotY >= 0 && slotY < 7) {
						int x = slotX * 18 + posX + 8;
						int y = slotY * 18 + posY + 14;
						creative_renderSlotOverlay(x, y);
						
						int index = slotY * 8 + slotX + rowIndex;
						ItemInstance item = index < items.size() ? items.get(index) : null;
						creative_renderName(item);
					}
					slotY = MathHelper.floor((mouseY - posY - 142) / 18);
					if (slotX < 9 && slotY == 0) {
						int x = slotX * 18 + posX + 8;
						int y = slotY * 18 + posY + 142;
						creative_renderSlotOverlay(x, y);
						creative_renderName(inventory.main[slotX]);
					}
				}
				
				int tabX = ((int) mouseX - posX - 4) / 24;
				int tabY = (int) mouseY - posY + 21;
				if (tabX >= 0 && tabX < count && tabY >= 0 && tabY < 24) {
					CreativeTab tab = CreativeTabs.getTab(tabPage, tabX);
					creative_renderString(tab.getTranslatedName());
				}
				
				tabX = (int) mouseX - posX - 173;
				tabY = (int) mouseY - posY - 114;
				if (tabX >= 0 && tabX < 25 && tabY >= 0 && tabY < 24) {
					creative_renderString("Creative");
				}
				
				tabY = (int) mouseY - posY - 138;
				if (tabX >= 0 && tabX < 25 && tabY >= 0 && tabY < 24) {
					creative_renderString("Inventory");
				}
				
				info.cancel();
			}
		}
	}
	
	private void creative_renderName(ItemInstance item) {
		if (item == null) {
			return;
		}
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		String key = item.getTranslationKey();
		String translated = TranslationStorage.getInstance().method_995(key);
		creative_renderString(translated.isEmpty() ? key : translated);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	private void creative_renderString(String string) {
		if (string == null) {
			return;
		}
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		if (string.length() > 0) {
			int var9 = (int) mouseX + 12;
			int var10 = (int) mouseY - 12;
			int var11 = this.textManager.getTextWidth(string);
			this.fillGradient(var9 - 3, var10 - 3, var9 + var11 + 3, var10 + 8 + 3, -1073741824, -1073741824);
			this.textManager.drawTextWithShadow(string, var9, var10, -1);
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	@Inject(method = "renderForeground", at = @At("HEAD"), cancellable = true)
	private void creative_renderForeground(CallbackInfo info) {
		if (creative_isInCreative() && !normalGUI) {
			this.textManager.drawText(selected, 8, 4, 4210752);
			info.cancel();
		}
	}
	
	private boolean creative_isInCreative() {
		return ((CreativePlayer) minecraft.player).isCreative();
	}
	
	private void creative_renderItem(ItemInstance instance, int x, int y) {
		if (instance == null) {
			return;
		}
		itemRenderer.method_1487(this.textManager, this.minecraft.textureManager, instance, x, y);
		itemRenderer.method_1488(this.textManager, this.minecraft.textureManager, instance, x, y);
	}
	
	private void creative_renderSlotOverlay(int x, int y) {
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		this.fillGradient(x, y, x + 16, y + 16, -2130706433, -2130706433);
		GL11.glEnable(2896);
		GL11.glEnable(2929);
	}
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void creative_render(int mouseX, int mouseY, float delta, CallbackInfo info) {
		if (creative_isInCreative() && !normalGUI) {
			creative_mouseScroll();
			
			this.renderBackground();
			int posX = (this.width - this.containerWidth) / 2;
			int posY = (this.height - this.containerHeight) / 2;
			this.renderContainerBackground(delta);
			
			GL11.glPushMatrix();
			GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
			RenderHelper.enableLighting();
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glTranslatef((float)posX, (float)posY, 0.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(32826);
	
			net.minecraft.entity.player.PlayerInventory inventory = this.minecraft.player.inventory;
			if (inventory.getCursorItem() != null) {
				GL11.glTranslatef(0.0F, 0.0F, 32.0F);
				itemRenderer.method_1487(this.textManager, this.minecraft.textureManager, inventory.getCursorItem(), mouseX - posX - 8, mouseY - posY - 8);
				itemRenderer.method_1488(this.textManager, this.minecraft.textureManager, inventory.getCursorItem(), mouseX - posX - 8, mouseY - posY - 8);
			}
	
			GL11.glDisable(32826);
			RenderHelper.disableLighting();
			GL11.glDisable(2896);
			GL11.glDisable(2929);
			this.renderForeground();
	
			GL11.glPopMatrix();
			GL11.glEnable(2896);
			GL11.glEnable(2929);
			
			this.mouseX = (float) mouseX;
			this.mouseY = (float) mouseY;
			info.cancel();
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		if (creative_isInCreative()) {
			int posX = (this.width - this.containerWidth) / 2;
			int posY = (this.height - this.containerHeight) / 2;
			
			int tabX = (int) mouseX - posX - 173;
			int tabY = (int) mouseY - posY - 114;
			if (tabX >= 0 && tabX < 25 && tabY >= 0 && tabY < 24) {
				normalGUI = false;
				creative_playSound();
			}
			
			tabY = (int) mouseY - posY - 138;
			if (tabX >= 0 && tabX < 25 && tabY >= 0 && tabY < 24) {
				normalGUI = true;
				creative_playSound();
			}
			
			if (normalGUI) {
				super.mouseClicked(mouseX, mouseY, button);
				return;
			}
			
			tabX = ((int) mouseX - posX - 4) / 24;
			tabY = (int) mouseY - posY + 21;
			if (tabX >= 0 && tabX < 7 && tabY >= 0 && tabY < 24) {
				tabIndex = tabX;
				
				CreativeTab tab = CreativeTabs.getTab(tabPage, tabIndex);
				selected = tab.getTranslatedName();
				items = tab.getItems();
				maxIndex = creative_getMaxIndex();
				rowIndex = 0;
				slider = 0F;
				
				creative_playSound();
				
				return;
			}
			
			int buttonY = (int) mouseY - posY - 4;
			if (buttonY > 0 && buttonY < 8) {
				int buttonX = (int) mouseX - posX - 150;
				if (tabPage > 0 && buttonX >= 0 && buttonX < 9) {
					tabIndex = 0;
					tabPage --;
					
					creative_playSound();
					CreativeTab tab = CreativeTabs.getTab(tabPage, tabIndex);
					rowIndex = 0;
					slider = 0F;
					if (tab == null) {
						return;
					}
					selected = tab.getTranslatedName();
					items = tab.getItems();
					maxIndex = creative_getMaxIndex();
					
					return;
				}
				
				buttonX = (int) mouseX - posX - 160;
				if ((tabPage < (CreativeTabs.getPagesCount() - 1)) && buttonX >= 0 && buttonX < 9) {
					tabIndex = 0;
					tabPage ++;
					
					creative_playSound();
					CreativeTab tab = CreativeTabs.getTab(tabPage, tabIndex);
					rowIndex = 0;
					slider = 0F;
					if (tab == null) {
						return;
					}
					selected = tab.getTranslatedName();
					items = tab.getItems();
					maxIndex = creative_getMaxIndex();
					
					return;
				}
			}
			
			int sliderX = (int) mouseX - posX - 154;
			int sliderY = (int) mouseY - posY - 14 - MathHelper.floor(slider * 109);
			if (sliderX > 0 && sliderX < 14 && sliderY > 0 && sliderY < 15) {
				mouseDelta = posY + 14 + sliderY;
				drag = true;
				return;
			}
			
			int slotX = MathHelper.floor((mouseX - posX - 8) / 18F);
			int slotY = MathHelper.floor((mouseY - posY - 14) / 18F);
			
			net.minecraft.entity.player.PlayerInventory inventory = this.minecraft.player.inventory;
			if (slotY >= 0 && slotY < 7 && slotX >= 0 && slotX < 8) {
				int index = slotY * 8 + slotX + rowIndex;
				if (index < items.size()) {
					ItemInstance item = items.get(index);
					boolean isSame = inventory.getCursorItem() != null && inventory.getCursorItem().isEqualIgnoreFlags(item);
					if (inventory.getCursorItem() == null || ItemBase.byId[inventory.getCursorItem().itemId] == null || isSame) {
						if (item != null) {
							if (isSame) {
								inventory.getCursorItem().count++;
							}
							else {
								inventory.setCursorItem(item.copy());
							}
							if (button == 2) {
								inventory.getCursorItem().count = ItemBase.byId[inventory.getCursorItem().itemId].getMaxStackSize();
							}
							return;
						}
					}
				}
				if (button == 1 && inventory.getCursorItem().count > 1) {
					inventory.getCursorItem().count--;
				}
				else {
					inventory.setCursorItem(null);
				}
				return;
			}
			
			slotY = MathHelper.floor((mouseY - posY - 142) / 18);
			if (slotY == 0 && slotX >= 0 && slotX < 9) {
				if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					inventory.main[slotX] = null;
				}
				super.mouseClicked(mouseX, mouseY, button);
			}
		}
		else {
			super.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int button) {
		super.mouseReleased(mouseX, mouseY, button);
		if (button > -1) {
			drag = false;
		}
	}
	
	private void creative_mouseScroll() {
		if (items.size() < 56) {
			return;
		}
		if (drag) {
			int mousePos = (int) mouseY - mouseDelta;
			slider = (float) mousePos / 109;
			slider = slider < 0 ? 0 : slider > 1 ? 1 : slider;
			rowIndex = ((int) (slider * maxIndex) >> 3) << 3;
			if (rowIndex > maxIndex) {
				rowIndex = maxIndex;
			}
			slider = (float) rowIndex / maxIndex;
			return;
		}
		int wheel = Mouse.getDWheel();
		if (wheel > 0) {
			rowIndex -= 8;
			if (rowIndex < 0) {
				rowIndex = 0;
			}
			slider = (float) rowIndex / maxIndex;
		}
		else if (wheel < 0) {
			rowIndex += 8;
			if (rowIndex > maxIndex) {
				rowIndex = maxIndex;
			}
			slider = (float) rowIndex / maxIndex;
		}
	}
	
	private int creative_getMaxIndex() {
		return (((items.size() - 48) >> 3)) << 3;
	}
	
	private void creative_playSound() {
		this.minecraft.soundHelper.playSound("random.click", 1.0F, 1.0F);
	}
}
