package pl.pzmod.data.containers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import pl.pzmod.data.SerializationConstants;
import pl.pzmod.data.SerializerHelper;

public record AttachedEnergy(Integer energy) {
    public static final Codec<AttachedEnergy> CODEC;
    public static final StreamCodec<ByteBuf, AttachedEnergy> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SerializerHelper.POSITIVE_INT_CODEC.fieldOf(SerializationConstants.ENERGY_CONTAINERS)
                        .forGetter(AttachedEnergy::energy)
        ).apply(instance, AttachedEnergy::new));

        STREAM_CODEC = ByteBufCodecs.VAR_INT.map(AttachedEnergy::new, AttachedEnergy::energy);
    }

    public static AttachedEnergy empty() {
        return new AttachedEnergy(0);
    }
}
