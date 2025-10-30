package pl.pzmod.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackpackItem extends PZItem {
    public BackpackItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           @NotNull Player player,
                                                           @NotNull InteractionHand usedHand) {
        ItemStack backpackItem = player.getItemInHand(usedHand);
        if (usedHand == InteractionHand.MAIN_HAND) {
            IItemHandler inventory = getInventory(backpackItem);
            if (inventory != null) {
                ItemStack offhandItem = player.getOffhandItem();
                ItemStack packedItem = inventory.getStackInSlot(0);
                int toExtract = offhandItem.getMaxStackSize() - offhandItem.getCount();
                if (offhandItem.isEmpty() || (!packedItem.isEmpty() && toExtract > 0
                        && ItemStack.isSameItemSameComponents(offhandItem, packedItem))) {
                    if (offhandItem.isEmpty()) {
                        ItemStack extractedItem = inventory.extractItem(0, packedItem.getMaxStackSize(), false);
                        player.setItemInHand(InteractionHand.OFF_HAND, extractedItem);
                    } else {
                        ItemStack extractedItem = inventory.extractItem(0, toExtract, false);
                        offhandItem.setCount(offhandItem.getCount() + extractedItem.getCount());
                        player.setItemInHand(InteractionHand.OFF_HAND, offhandItem);
                    }
                    return InteractionResultHolder.success(backpackItem);
                } else if (!offhandItem.isEmpty() && !(offhandItem.getItem() instanceof BackpackItem)) {
                    offhandItem = inventory.insertItem(0, offhandItem, false);
                    player.setItemInHand(InteractionHand.OFF_HAND, offhandItem);
                    return InteractionResultHolder.success(backpackItem);
                }
            }
        }
        return InteractionResultHolder.pass(backpackItem);
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public int getSlotLimit() {
        return 2;
    }

    private @Nullable IItemHandler getInventory(@NotNull ItemStack stack) {
        return stack.getCapability(Capabilities.ItemHandler.ITEM);
    }
}
