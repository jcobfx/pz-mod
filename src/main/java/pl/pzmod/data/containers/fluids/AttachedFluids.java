package pl.pzmod.data.containers.fluids;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.SerializationConstants;
import pl.pzmod.data.SerializerHelper;
import pl.pzmod.data.containers.IAttachedContainers;

import java.util.Collections;
import java.util.List;

public record AttachedFluids(List<FluidStack> contents) implements IAttachedContainers<FluidStack, AttachedFluids> {
    public static final AttachedFluids EMPTY = new AttachedFluids(Collections.emptyList());
    public static final Codec<AttachedFluids> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, AttachedFluids> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SerializerHelper.LENIENT_OPTIONAL_FLUID_STACK_CODEC.listOf().fieldOf(SerializationConstants.FLUID_CONTAINERS)
                        .forGetter(AttachedFluids::contents)
        ).apply(instance, AttachedFluids::new));

        STREAM_CODEC = FluidStack.OPTIONAL_STREAM_CODEC
                .<List<FluidStack>>apply(ByteBufCodecs.collection(NonNullList::createWithCapacity))
                .map(AttachedFluids::new, AttachedFluids::contents);
    }

    public AttachedFluids {
        contents = Collections.unmodifiableList(contents);
    }

    @Override
    public @NotNull FluidStack getDefault() {
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull AttachedFluids create(@NotNull List<FluidStack> contents) {
        return new AttachedFluids(contents);
    }
}
