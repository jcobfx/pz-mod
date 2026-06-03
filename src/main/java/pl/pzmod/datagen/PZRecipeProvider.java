package pl.pzmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.registries.PZBlocks;
import pl.pzmod.registries.PZItems;

import java.util.concurrent.CompletableFuture;

public class PZRecipeProvider extends RecipeProvider {
    public PZRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, PZItems.BUCK_CHUCKETS)
                .pattern(" I ")
                .pattern("I I")
                .pattern("B B")
                .define('I', Tags.Items.NUGGETS_IRON)
                .define('B', Tags.Items.BUCKETS_EMPTY)
                .unlockedBy("has_bucket", has(Tags.Items.BUCKETS_EMPTY))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, PZItems.ENERGY_CELL)
                .pattern(" G ")
                .pattern("IRI")
                .pattern("IRI")
                .define('I', Tags.Items.NUGGETS_IRON)
                .define('G', Tags.Items.NUGGETS_GOLD)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PZItems.BATTERY)
                .pattern(" I ")
                .pattern("IEI")
                .pattern("IRI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('E', PZItems.ENERGY_CELL)
                .unlockedBy("has_energy_cell", has(PZItems.ENERGY_CELL))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PZItems.COIL, 4)
                .pattern("CIC")
                .pattern("CIC")
                .pattern("CIC")
                .define('I', Tags.Items.INGOTS_COPPER)
                .define('C', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, PZItems.TESLA_HELMET)
                .pattern("C C")
                .pattern("IRI")
                .pattern("IEI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', PZItems.COIL)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('E', PZItems.ENERGY_CELL)
                .unlockedBy("has_coil", has(PZItems.COIL))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PZBlocks.GENERATOR)
                .pattern("RIR")
                .pattern("EFE")
                .pattern("RIR")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('F', Tags.Items.PLAYER_WORKSTATIONS_FURNACES)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('E', PZItems.ENERGY_CELL)
                .unlockedBy("has_furnace", has(Tags.Items.PLAYER_WORKSTATIONS_FURNACES))
                .save(output);
    }
}
