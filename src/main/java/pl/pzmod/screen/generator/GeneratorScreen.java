package pl.pzmod.screen.generator;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.PZMod;

public class GeneratorScreen extends AbstractContainerScreen<GeneratorMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "textures/gui/generator/generator_gui.png");
    private static final ResourceLocation ENERGY_BAR =
            ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "textures/gui/generator/energy_bar.png");
    private static final ResourceLocation PROGRESS_ARROW =
            ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "textures/gui/generator/arrow_progress.png");

    public static final int PROGRESS_ARROW_HEIGHT = 17;
    public static final int PROGRESS_ARROW_WIDTH = 25;
    public static final int ENERGY_BAR_HEIGHT = 46;
    public static final int ENERGY_BAR_WIDTH = 28;

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

        int burnTimeTotal = menu.getBurnTimeTotal();
        int progressArrowWidth = burnTimeTotal <= 0 ? 0 : menu.getBurnTime() * PROGRESS_ARROW_WIDTH / burnTimeTotal;
        if (progressArrowWidth > 0) {
            guiGraphics.blit(PROGRESS_ARROW,
                    x + 70, y + 33,
                    0, 0,
                    progressArrowWidth, PROGRESS_ARROW_HEIGHT,
                    PROGRESS_ARROW_WIDTH, PROGRESS_ARROW_HEIGHT);
        }

        int maxEnergyStored = menu.getMaxEnergyStored();
        int energyBarHeight = maxEnergyStored <= 0 ? 0 : menu.getEnergyStored() * ENERGY_BAR_HEIGHT / maxEnergyStored;
        if (energyBarHeight > 0) {
            guiGraphics.blit(ENERGY_BAR,
                    x + 127, y + 18 + (ENERGY_BAR_HEIGHT - energyBarHeight),
                    0, (float) ENERGY_BAR_HEIGHT - energyBarHeight,
                    ENERGY_BAR_WIDTH, energyBarHeight,
                    ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        if (mouseX >= leftPos + 127 && mouseX <= leftPos + 155 && mouseY >= topPos + 18 && mouseY <= topPos + 64) {
            guiGraphics.renderTooltip(this.font,
                    Component.literal(menu.getEnergyStored() + " / " + menu.getMaxEnergyStored() + " FE"),
                    mouseX, mouseY);
        }
    }
}