package pl.pzmod.screen.generator;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import pl.pzmod.PZMod;

public class GeneratorScreen extends AbstractContainerScreen<GeneratorMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "textures/gui/generator/generator_gui.png");

    private static final ResourceLocation ENERGY_BAR =
            ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "textures/gui/generator/energy_bar.png");
    private static final ResourceLocation PROGRESS_ARROW =
            ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "textures/gui/generator/arrow_progress.png");

    public GeneratorScreen(GeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float vPartialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        int arrowWidth = menu.getBurnProgress();
        if (arrowWidth > 0) {
            guiGraphics.blit(PROGRESS_ARROW, x + 70, y + 33, 0, 0, arrowWidth, 17, 25, 17);
        }

        int energyHeight = menu.getEnergyScaled();
        if (energyHeight > 0) {
            int barX = x + 127;
            int barY = y + 18 + (46 - energyHeight);

            int vOffset = 46 - energyHeight;

            guiGraphics.blit(ENERGY_BAR, barX, barY, 0, vOffset, 28, energyHeight, 28, 46);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        // Poprawiony obszar Tooltipu dla energii (zgodnie z nowymi kordynatami 127,18 -> 155,64)
        if (mouseX >= leftPos + 127 && mouseX <= leftPos + 155 && mouseY >= topPos + 18 && mouseY <= topPos + 64) {
            guiGraphics.renderTooltip(this.font,
                    Component.literal(menu.getEnergyStored() + " / " + menu.getMaxEnergy() + " FE"),
                    mouseX, mouseY);
        }
    }
}