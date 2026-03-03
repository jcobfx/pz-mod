package pl.pzmod.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import pl.pzmod.PZMod;

import java.util.function.Supplier;

public class PZCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PZMod.MODID);

    public static final Supplier<CreativeModeTab> PZ_ITEMS_TAB = CREATIVE_TAB.register("pz_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(PZItems.BATTERY.get()))
                    .title(Component.translatable("creativetab.pz_mod.items"))
                    .displayItems((itemDisplayParameter, output) ->{
                        output.accept(PZItems.BATTERY.get());
                        output.accept(PZItems.BACKPACK.get());
                        output.accept(PZItems.BIG_BUCKET.get());
                    }).build());

    public static final Supplier<CreativeModeTab> PZ_BLOCKS_TAB = CREATIVE_TAB.register("pz_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(PZBlocks.GENERATOR.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(PZMod.MODID,"pz_items_tab"))
                    .title(Component.translatable("creativetab.pz_mod.blocks"))
                    .displayItems((itemDisplayParameter, output) -> {
                        output.accept(new ItemStack(PZBlocks.GENERATOR));
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TAB.register(eventBus);
    }

    private PZCreativeTab() {
    }
}
