package pl.pzmod.data.containers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import pl.pzmod.data.SerializationConstants;
import pl.pzmod.data.SerializerHelper;

import java.util.Collections;
import java.util.List;

public record AttachedEnergy(List<Integer> containers) {
    public static final Codec<AttachedEnergy> CODEC;
    public static final StreamCodec<ByteBuf, AttachedEnergy> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SerializerHelper.POSITIVE_INT_CODEC.listOf().fieldOf(SerializationConstants.ENERGY_CONTAINERS)
                        .forGetter(AttachedEnergy::containers)
        ).apply(instance, AttachedEnergy::new));

        STREAM_CODEC = ByteBufCodecs.VAR_INT.<List<Integer>>apply(ByteBufCodecs.collection(NonNullList::createWithCapacity))
                .map(AttachedEnergy::new, AttachedEnergy::containers);
    }

    public static AttachedEnergy create(int size) {
        return new AttachedEnergy(NonNullList.withSize(size, 0));
    }

    public AttachedEnergy {
        containers = Collections.unmodifiableList(containers);
    }
}
