package pl.pzmod.capabilities.item;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.blocks.entities.GeneratorBlockEntity;

public class GeneratorBlockItemCapabilityResolver extends BlockItemCapabilityResolver {
    public GeneratorBlockItemCapabilityResolver(GeneratorBlockEntity blockEntity, Direction side) {
        super(blockEntity, side);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return (slot != 0 || stack.getBurnTime(RecipeType.SMELTING) > 0) && super.isItemValid(slot, stack);
    }
}
