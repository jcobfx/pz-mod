package pl.pzmod.data.containers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.SerializationConstants;
import pl.pzmod.data.SerializerHelper;

import java.util.Collections;
import java.util.List;

public record AttachedFluids(List<FluidStack> fluids) {
    public static final AttachedFluids EMPTY = new AttachedFluids(NonNullList.create());
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

    public void copyInto(@NotNull NonNullList<FluidStack> list) {
        for (int i = 0; i < list.size(); ++i) {
            FluidStack stack = i < this.fluids.size() ? this.fluids.get(i) : FluidStack.EMPTY;
            list.set(i, stack.copy());
        }
    }

    public int getTanks() {
        return fluids.size();
    }

    public @NotNull FluidStack getFluidInTank(int tank) {
        return fluids.get(tank);
    }
}
