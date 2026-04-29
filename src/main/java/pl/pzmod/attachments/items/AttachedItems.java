package pl.pzmod.attachments.items;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record AttachedItems(List<ItemStack> contents) {
    public static final AttachedItems EMPTY;
    public static final Codec<AttachedItems> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, AttachedItems> STREAM_CODEC;

    static {
        EMPTY = new AttachedItems(Collections.emptyList());
        CODEC = ItemStack.OPTIONAL_CODEC.listOf().xmap(AttachedItems::new, AttachedItems::contents);
        STREAM_CODEC = ItemStack.OPTIONAL_LIST_STREAM_CODEC.map(AttachedItems::new, AttachedItems::contents);
    }

    public static AttachedItems withSize(int size) {
        return new AttachedItems(NonNullList.withSize(size, ItemStack.EMPTY));
    }

    public AttachedItems {
        contents = Collections.unmodifiableList(contents);
    }

    public AttachedItems with(int slot, ItemStack stack) {
        List<ItemStack> copy = new ArrayList<>(contents);
        copy.set(slot, stack);
        return new AttachedItems(copy);
    }

    public void copyInto(NonNullList<ItemStack> list) {
        for (int i = 0; i < list.size(); ++i) {
            ItemStack stack = i < this.contents.size() ? this.contents.get(i) : ItemStack.EMPTY;
            list.set(i, stack.copy());
        }
    }

    public ItemStack getOrDefault(int slot) {
        return validateSlotIndex(slot) ? contents.get(slot) : ItemStack.EMPTY;
    }

    private boolean validateSlotIndex(int slot) {
        return slot >= 0 && slot < contents.size();
    }
}
