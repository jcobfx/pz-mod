package pl.pzmod.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class BackpackItem extends Item {
    public static final int SLOTS = 2;

    public BackpackItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           @NotNull Player player,
                                                           @NotNull InteractionHand usedHand) {
        ItemStack backpack = player.getItemInHand(usedHand);
        if (usedHand == InteractionHand.MAIN_HAND) {
            IItemHandler itemCap = backpack.getCapability(Capabilities.ItemHandler.ITEM);
            if (itemCap != null) {
                ItemStack offhandItem = player.getOffhandItem();
                ItemStack packedItem = itemCap.getStackInSlot(0);
                int toExtract = offhandItem.getMaxStackSize() - offhandItem.getCount();
                if (offhandItem.isEmpty()) {
                    ItemStack extractedItem = itemCap.extractItem(0, packedItem.getMaxStackSize(), false);
                    player.setItemInHand(InteractionHand.OFF_HAND, extractedItem);
                } else if (ItemStack.isSameItemSameComponents(offhandItem, packedItem) && toExtract > 0) {
                    int extracted = itemCap.extractItem(0, toExtract, false).getCount();
                    player.setItemInHand(InteractionHand.OFF_HAND,
                            offhandItem.copyWithCount(offhandItem.getCount() + extracted));
                } else {
                    offhandItem = itemCap.insertItem(0, offhandItem, false);
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
