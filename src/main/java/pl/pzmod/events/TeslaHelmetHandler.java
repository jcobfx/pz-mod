package pl.pzmod.events;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import pl.pzmod.PZMod;
import pl.pzmod.items.BatteryItem;

import java.util.List;

@EventBusSubscriber(modid = PZMod.MODID)
public class TeslaHelmetHandler {

    private static final int ENERGY_USAGE_PER_SEC = 150;
    private static final double DAMAGE_RADIUS = 6.0;
    private static final float DAMAGE_AMOUNT = 3.0f;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.isEmpty() || !(helmet.getItem() instanceof pl.pzmod.items.armor.TeslaHelmetItem)) {
            return;
        }

        ItemStack offhandItem = player.getOffhandItem();
        if (offhandItem.isEmpty() || !(offhandItem.getItem() instanceof BatteryItem)) {
            return;
        }

        if (player.tickCount % 20 == 0) {
            IEnergyStorage batteryCap = offhandItem.getCapability(Capabilities.EnergyStorage.ITEM);

            if (batteryCap != null && batteryCap.getEnergyStored() >= ENERGY_USAGE_PER_SEC) {

                batteryCap.extractEnergy(ENERGY_USAGE_PER_SEC, false);

                ServerLevel serverLevel = (ServerLevel) player.level();

                AABB area = player.getBoundingBox().inflate(DAMAGE_RADIUS);
                List<LivingEntity> targets = serverLevel.getEntitiesOfClass(
                        LivingEntity.class,
                        area,
                        entity -> entity != player && entity.isAlive() && !entity.isAlliedTo(player)
                );

                if (!targets.isEmpty()) {
                    Vec3 startPos = player.position().add(0, player.getEyeHeight() * 0.8, 0);

                    for (LivingEntity target : targets) {
                        target.hurt(serverLevel.damageSources().generic(), DAMAGE_AMOUNT);

                        Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2.0, 0);

                        spawnLightningParticles(serverLevel, startPos, targetPos);
                    }
                }
            }
        }
    }

    private static void spawnLightningParticles(ServerLevel level, Vec3 start, Vec3 end) {
        double distance = start.distanceTo(end);
        int particleCount = (int) (distance * 3);
        for (int i = 0; i <= particleCount; i++) {
            double alpha = (double) i / particleCount;
            double x = start.x + (end.x - start.x) * alpha;
            double y = start.y + (end.y - start.y) * alpha;
            double z = start.z + (end.z - start.z) * alpha;

            double offsetX = (level.random.nextDouble() - 0.5) * 0.15;
            double offsetY = (level.random.nextDouble() - 0.5) * 0.15;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.15;

            level.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    x + offsetX, y + offsetY, z + offsetZ,
                    1,
                    0, 0, 0,
                    0.0
            );
        }
    }
}