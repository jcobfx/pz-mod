package pl.pzmod.data.containers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;
import pl.pzmod.data.SerializationConstants;
import pl.pzmod.data.SerializerHelper;

import java.util.Collections;
import java.util.List;

public record AttachedFluids(List<FluidStack> fluids) {
    public static final Codec<AttachedFluids> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, AttachedFluids> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SerializerHelper.LENIENT_OPTIONAL_FLUID_STACK_CODEC.listOf().fieldOf(SerializationConstants.FLUID_CONTAINERS)
                        .forGetter(AttachedFluids::fluids)
        ).apply(instance, AttachedFluids::new));

        STREAM_CODEC = FluidStack.OPTIONAL_STREAM_CODEC
                .<List<FluidStack>>apply(ByteBufCodecs.collection(NonNullList::createWithCapacity))
                .map(AttachedFluids::new, AttachedFluids::fluids);
    }

    public static AttachedFluids create(int size) {
        return new AttachedFluids(NonNullList.withSize(size, FluidStack.EMPTY));
    }

    public AttachedFluids {
        fluids = Collections.unmodifiableList(fluids);
    }
}
