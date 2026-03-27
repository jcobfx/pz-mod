package pl.pzmod.attachments.containers.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.SerializationConstants;
import pl.pzmod.attachments.SerializerHelper;
import pl.pzmod.attachments.containers.IAttachedContainers;

import java.util.Collections;
import java.util.List;

public record AttachedEnergy(List<Integer> contents) implements IAttachedContainers<Integer, AttachedEnergy> {
    public static final AttachedEnergy EMPTY = new AttachedEnergy(Collections.emptyList());
    public static final Codec<AttachedEnergy> CODEC;
    public static final StreamCodec<ByteBuf, AttachedEnergy> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SerializerHelper.POSITIVE_INT_CODEC.listOf()
                        .fieldOf(SerializationConstants.ENERGY_CONTAINERS)
                        .forGetter(AttachedEnergy::contents)
        ).apply(instance, AttachedEnergy::new));

        STREAM_CODEC = ByteBufCodecs.VAR_INT
                .<List<Integer>>apply(ByteBufCodecs.collection(NonNullList::createWithCapacity))
                .map(AttachedEnergy::new, AttachedEnergy::contents);
    }

    public static AttachedEnergy create(int containers) {
        return new AttachedEnergy(NonNullList.withSize(containers, 0));
    }

    public AttachedEnergy {
        contents = Collections.unmodifiableList(contents);
    }

    @Override
    public @NotNull Integer getDefault() {
        return 0;
    }

    @Override
    public @NotNull AttachedEnergy create(@NotNull List<Integer> contents) {
        return new AttachedEnergy(contents);
    }
}
