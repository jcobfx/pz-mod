package pl.pzmod.capabilities;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import pl.pzmod.blocks.entities.PZBlockEntity;
import pl.pzmod.capabilities.energy.IEnergyHolder;
import pl.pzmod.capabilities.fluid.IFluidHolder;
import pl.pzmod.capabilities.item.IItemHolder;
import pl.pzmod.items.PZItem;

import java.util.Optional;
import java.util.function.Supplier;

public sealed interface IDataHolder<H extends IEnergyHolder & IFluidHolder & IItemHolder, A, T> extends Supplier<H>
        permits IDataHolder.Block, IDataHolder.Item {
    Optional<A> getData(Supplier<? extends T> dataType);

    void setData(Supplier<? extends T> dataType, A value);

    final class Block<A> implements IDataHolder<PZBlockEntity, A, AttachmentType<A>> {
        public static <A> Block<A> from(PZBlockEntity blockEntity) {
            return new Block<>(blockEntity);
        }

        private final PZBlockEntity blockEntity;

        private Block(PZBlockEntity blockEntity) {
            this.blockEntity = blockEntity;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Optional<A> getData(Supplier<? extends AttachmentType<A>> dataType) {
            return Optional.of(blockEntity.getData((Supplier<AttachmentType<A>>) dataType));
        }

        @SuppressWarnings("unchecked")
        @Override
        public void setData(Supplier<? extends AttachmentType<A>> dataType, A value) {
            blockEntity.setData((Supplier<AttachmentType<A>>) dataType, value);
        }

        @Override
        public PZBlockEntity get() {
            return blockEntity;
        }
    }

    final class Item<A> implements IDataHolder<PZItem, A, DataComponentType<A>> {
        public static <A> Item<A> from(ItemStack stack) {
            return new Item<>(stack);
        }

        private final ItemStack itemStack;
        private final PZItem pzItem;

        private Item(ItemStack itemStack) {
            this.itemStack = itemStack;
            this.pzItem = (PZItem) itemStack.getItem();
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        @Override
        public Optional<A> getData(Supplier<? extends DataComponentType<A>> dataType) {
            return Optional.ofNullable(itemStack.get(dataType));
        }

        @Override
        public void setData(Supplier<? extends DataComponentType<A>> dataType, A value) {
            itemStack.set(dataType, value);
        }

        @Override
        public PZItem get() {
            return pzItem;
        }
    }
}
