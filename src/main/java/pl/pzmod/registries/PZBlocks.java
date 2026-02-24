package pl.pzmod.registries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import pl.pzmod.PZMod;
import pl.pzmod.blocks.GeneratorBlock;

import java.util.function.Supplier;

public class PZBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PZMod.MODID);

    public static final DeferredBlock<Block> GENERATOR = registerBlock("generator",
            () -> new GeneratorBlock(Block.Properties.of().noOcclusion()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        PZItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}