package pl.pzmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.registries.PZBlocks;

import java.util.Set;

public class PZBlockLootSubProvider extends BlockLootSubProvider {
    public PZBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return PZBlocks.BLOCKS.getEntries().stream()
                .map(e -> (Block) e.get())
                .toList();
    }

    @Override
    protected void generate() {
        dropSelf(PZBlocks.GENERATOR.get());
    }
}
