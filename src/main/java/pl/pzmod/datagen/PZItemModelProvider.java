package pl.pzmod.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import pl.pzmod.PZMod;
import pl.pzmod.registries.PZItems;

public class PZItemModelProvider extends ItemModelProvider {
    public PZItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PZMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
//        basicItem(PZItems.BACKPACK.get());
        basicItem(PZItems.BUCK_CHUCKETS.get());
        basicItem(PZItems.ENERGY_CELL.get());
        basicItem(PZItems.COIL.get());
    }
}
