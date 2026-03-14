package pl.pzmod.capabilities;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;

public record Capability<H>(BlockCapability<H, @Nullable Direction> block,
                            EntityCapability<H, ?> entity,
                            ItemCapability<H, Void> item) implements ICapability<H, H> {
}
