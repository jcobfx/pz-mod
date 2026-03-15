package pl.pzmod.menus;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.blocks.entities.PZBlockEntity;
import pl.pzmod.capabilities.Capabilities;

import java.util.function.Supplier;

public class PZBlockEntityContainerMenu<E extends PZBlockEntity> extends PZContainerMenu {
    private final E blockEntity;

    public PZBlockEntityContainerMenu(Supplier<MenuType<PZContainerMenu>> menuType,
                                      int containerId,
                                      Inventory playerInventory,
                                      @NotNull E blockEntity) {
        super(menuType, containerId, playerInventory);
        this.blockEntity = blockEntity;
    }

    @Override
    protected void addSlots() {
        super.addSlots();
        if (blockEntity.hasItemContainers()) {
            var itemCap = Capabilities.ITEM.getCapability(blockEntity.getLevel(), blockEntity.getBlockPos(), null);
            if (itemCap != null) {

            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        var level = blockEntity.getLevel();
        return level != null && stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, blockEntity.getBlockHolder().value());
    }
}
