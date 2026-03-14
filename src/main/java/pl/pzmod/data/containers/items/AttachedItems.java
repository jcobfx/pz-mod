package pl.pzmod.data.containers.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.SerializationConstants;
import pl.pzmod.data.SerializerHelper;
import pl.pzmod.data.containers.IAttachedContainers;

import java.util.Collections;
import java.util.List;

public record AttachedItems(List<ItemStack> contents) implements IAttachedContainers<ItemStack, AttachedItems> {
    public static final AttachedItems EMPTY = new AttachedItems(Collections.emptyList());
    public static final Codec<AttachedItems> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, AttachedItems> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SerializerHelper.LENIENT_OPTIONAL_OVERSIZED_ITEM_STACK_CODEC.listOf()
                        .fieldOf(SerializationConstants.ITEM_CONTAINERS).forGetter(AttachedItems::contents)
        ).apply(instance, AttachedItems::new));

        STREAM_CODEC = ItemStack.OPTIONAL_LIST_STREAM_CODEC
                .map(AttachedItems::new, AttachedItems::contents);
    }

    public AttachedItems {
        contents = Collections.unmodifiableList(contents);
    }

    @Override
    public @NotNull ItemStack getDefault() {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull AttachedItems create(@NotNull List<ItemStack> contents) {
        return new AttachedItems(contents);
    }
}
