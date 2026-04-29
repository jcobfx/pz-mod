package pl.pzmod.attachments.fluids;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public record AttachedFluids(List<FluidStack> contents) {
    public static final AttachedFluids EMPTY;
    public static final Codec<AttachedFluids> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, AttachedFluids> STREAM_CODEC;

    static {
        EMPTY = new AttachedFluids(Collections.emptyList());
        CODEC = FluidStack.OPTIONAL_CODEC.listOf().xmap(AttachedFluids::new, AttachedFluids::contents);
        STREAM_CODEC = FluidStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()).map(AttachedFluids::new, AttachedFluids::contents);
    }

    public static AttachedFluids withSize(int size) {
        return new AttachedFluids(NonNullList.withSize(size, FluidStack.EMPTY));
    }

    public AttachedFluids {
        contents = Collections.unmodifiableList(contents);
    }

    public void copyInto(NonNullList<FluidStack> list) {
        for(int i = 0; i < list.size(); ++i) {
            FluidStack stack = i < this.contents.size() ? this.contents.get(i) : FluidStack.EMPTY;
            list.set(i, stack.copy());
        }
    }
}
