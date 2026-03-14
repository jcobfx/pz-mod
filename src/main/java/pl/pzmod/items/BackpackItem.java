package pl.pzmod.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.capabilities.ContainerHandlerHelper;
import pl.pzmod.capabilities.items.ItemContainerConfig;
import pl.pzmod.data.containers.IContainerHolder;
import pl.pzmod.data.containers.items.ItemHandler;
import pl.pzmod.utils.ConstantPredicates;

public class BackpackItem extends PZItem {
    public BackpackItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    protected @Nullable ItemHandler getInitialItemHandler(@NotNull ItemStack stack) {
        return ContainerHandlerHelper.builder(new ItemHandler(), IContainerHolder.from(stack, 1))
                .addContainer(ItemContainerConfig.inout(ConstantPredicates.alwaysTrue(), () -> 2))
                .build();
    }

    @Override
    public boolean canHandleItems() {
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           @NotNull Player player,
                                                           @NotNull InteractionHand usedHand) {
        ItemStack backpack = player.getItemInHand(usedHand);
        if (usedHand == InteractionHand.MAIN_HAND) {
            IItemHandler inventory = Capabilities.ITEM.getCapability(backpack);
            if (inventory != null) {
                ItemStack offhandItem = player.getOffhandItem();
                ItemStack packedItem = inventory.getStackInSlot(0);
                int toExtract = offhandItem.getMaxStackSize() - offhandItem.getCount();
                if (offhandItem.isEmpty()) {
                    ItemStack extractedItem = inventory.extractItem(0, packedItem.getMaxStackSize(), false);
                    player.setItemInHand(InteractionHand.OFF_HAND, extractedItem);
                } else if (ItemStack.isSameItemSameComponents(offhandItem, packedItem) && toExtract > 0) {
                    int extracted = inventory.extractItem(0, toExtract, false).getCount();
                    player.setItemInHand(InteractionHand.OFF_HAND,
                            offhandItem.copyWithCount(offhandItem.getCount() + extracted));
                } else {
                    offhandItem = inventory.insertItem(0, offhandItem, false);
                    player.setItemInHand(InteractionHand.OFF_HAND, offhandItem);
                }
                return InteractionResultHolder.success(backpack);
            }
        }
        return InteractionResultHolder.pass(backpack);
    }

    @Override
    public boolean canFitInsideContainerItems(@NotNull ItemStack stack) {
        return false;
    }
}
