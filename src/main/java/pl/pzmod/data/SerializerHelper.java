package pl.pzmod.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import pl.pzmod.PZMod;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SerializerHelper {
    private SerializerHelper() {
    }

    public static final Codec<Integer> POSITIVE_INT_CODEC;

    public static final Codec<FluidStack> LENIENT_OPTIONAL_FLUID_STACK_CODEC;

    private static final Consumer<String> ON_STACK_LOAD_ERROR;
    public static final Codec<ItemStack> OVERSIZED_ITEM_STACK_CODEC;
    public static final Codec<ItemStack> OPTIONAL_OVERSIZED_ITEM_STACK_CODEC;
    public static final Codec<ItemStack> LENIENT_OPTIONAL_OVERSIZED_ITEM_STACK_CODEC;

    static {
        POSITIVE_INT_CODEC = Util.make(() -> {
            final Function<Integer, DataResult<Integer>> checker = Codec.checkRange(0, Integer.MAX_VALUE);
            return Codec.INT.flatXmap(checker, checker);
        });

        LENIENT_OPTIONAL_FLUID_STACK_CODEC = FluidStack.OPTIONAL_CODEC
                .promotePartial(error -> PZMod.LOGGER.error("Tried to load invalid fluid: '{}'", error))
                .orElse(FluidStack.EMPTY);

        ON_STACK_LOAD_ERROR = error -> PZMod.LOGGER.error("Tried to load invalid item: {}", error);

        OVERSIZED_ITEM_STACK_CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.ITEM_NON_AIR_CODEC.fieldOf(SerializationConstants.ID).forGetter(ItemStack::getItemHolder),
                ExtraCodecs.POSITIVE_INT.fieldOf(SerializationConstants.COUNT).orElse(1).forGetter(ItemStack::getCount),
                DataComponentPatch.CODEC.optionalFieldOf(SerializationConstants.COMPONENTS, DataComponentPatch.EMPTY)
                        .forGetter(ItemStack::getComponentsPatch)
        ).apply(instance, ItemStack::new)));

        OPTIONAL_OVERSIZED_ITEM_STACK_CODEC = ExtraCodecs.optionalEmptyMap(OVERSIZED_ITEM_STACK_CODEC)
                .xmap(opt -> opt.orElse(ItemStack.EMPTY),
                        stack -> stack.isEmpty() ? Optional.empty() : Optional.of(stack));

        LENIENT_OPTIONAL_OVERSIZED_ITEM_STACK_CODEC = OPTIONAL_OVERSIZED_ITEM_STACK_CODEC
                .promotePartial(ON_STACK_LOAD_ERROR)
                .orElse(ItemStack.EMPTY);
    }

    public static Tag saveOversized(HolderLookup.Provider registryAccess, ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty ItemStack");
        }
        return OVERSIZED_ITEM_STACK_CODEC.encodeStart(registryAccess.createSerializationContext(NbtOps.INSTANCE), stack)
                .getOrThrow();
    }

    public static Optional<ItemStack> parseOversized(HolderLookup.Provider lookupProvider, Tag tag) {
        return OVERSIZED_ITEM_STACK_CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag)
                .resultOrPartial(ON_STACK_LOAD_ERROR);
    }
}
