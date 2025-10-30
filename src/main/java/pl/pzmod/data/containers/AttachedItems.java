package pl.pzmod.data.containers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import pl.pzmod.data.SerializationConstants;
import pl.pzmod.data.SerializerHelper;

import java.util.Collections;
import java.util.List;

public record AttachedItems(List<ItemStack> items) {
    public static final Codec<AttachedItems> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, AttachedItems> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SerializerHelper.LENIENT_OPTIONAL_OVERSIZED_ITEM_STACK_CODEC.listOf()
                        .fieldOf(SerializationConstants.ITEM_CONTAINERS).forGetter(AttachedItems::items)
        ).apply(instance, AttachedItems::new));

        STREAM_CODEC = ItemStack.OPTIONAL_LIST_STREAM_CODEC
                .map(AttachedItems::new, AttachedItems::items);
    }

    public static AttachedItems create(int size) {
        return new AttachedItems(NonNullList.withSize(size, ItemStack.EMPTY));
    }

    public AttachedItems {
        items = Collections.unmodifiableList(items);
    }
}
