package com.example.tlmkiss.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class TlmKissClient implements ClientModInitializer {

    private static KeyBinding kissKey;
    public static final KissCameraHandler cameraHandler = new KissCameraHandler();

    @Override
    public void onInitializeClient() {
        kissKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tlm_kiss_addon.kiss",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.tlm_kiss_addon"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (kissKey.wasPressed()) {
                tryKiss(client);
            }
            cameraHandler.tick(client);
        });
    }

    private void tryKiss(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (cameraHandler.isActive()) return;

        Entity target = findTargetMaid(client);
        if (target == null) return;

        spawnHeartParticles(client, target);
        cameraHandler.start(target);
    }

    private Entity findTargetMaid(MinecraftClient client) {
        HitResult hit = client.crosshairTarget;
        if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) hit).getEntity();
            if (isMaid(entity) && isValidTarget(client.player, entity)) {
                return entity;
            }
        }

        Entity nearest = null;
        double nearestDist = 8.0 * 8.0;
        PlayerEntity player = client.player;
        for (Entity e : client.world.getEntities()) {
            if (isMaid(e) && isValidTarget(player, e)) {
                double d = player.squaredDistanceTo(e);
                if (d < nearestDist) {
                    nearestDist = d;
                    nearest = e;
                }
            }
        }
        return nearest;
    }

    private boolean isMaid(Entity entity) {
        if (entity == null) return false;
        String name = entity.getClass().getSimpleName();
        return name.contains("Maid") && !name.contains("Fishing") && !name.contains("Bed");
    }

    private boolean isValidTarget(PlayerEntity player, Entity maid) {
        return maid.isAlive() && player.squaredDistanceTo(maid) < 64.0;
    }

    private void spawnHeartParticles(MinecraftClient client, Entity maid) {
        if (client.world == null) return;
        Vec3d pos = maid.getPos().add(0, maid.getHeight() * 0.7, 0);
        for (int i = 0; i < 12; i++) {
            double ox = (client.world.random.nextDouble() - 0.5) * 0.8;
            double oy = client.world.random.nextDouble() * 0.6;
            double oz = (client.world.random.nextDouble() - 0.5) * 0.8;
            client.world.addParticle(ParticleTypes.HEART,
                    pos.x + ox, pos.y + oy, pos.z + oz,
                    0, 0.05 + client.world.random.nextDouble() * 0.05, 0);
        }
        for (int i = 0; i < 5; i++) {
            client.world.addParticle(ParticleTypes.HEART,
                    pos.x, pos.y + 0.2 + i * 0.15, pos.z,
                    (client.world.random.nextDouble() - 0.5) * 0.02,
                    0.08,
                    (client.world.random.nextDouble() - 0.5) * 0.02);
        }
    }
}