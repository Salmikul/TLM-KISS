package com.example.tlmkiss.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class KissCameraHandler {

    private static final int ZOOM_IN_TICKS = 10;
    private static final int HOLD_TICKS = 15;
    private static final int ZOOM_OUT_TICKS = 15;
    private static final int TOTAL = ZOOM_IN_TICKS + HOLD_TICKS + ZOOM_OUT_TICKS;
    private static final float TARGET_FOV_SCALE = 0.55f;

    private boolean active = false;
    private int tick = 0;
    private Entity target;
    private float currentFovMultiplier = 1.0f;

    public void start(Entity maid) {
        this.active = true;
        this.tick = 0;
        this.target = maid;
        this.currentFovMultiplier = 1.0f;
    }

    public boolean isActive() {
        return active;
    }

    public float getFovMultiplier() {
        return currentFovMultiplier;
    }

    public void tick(MinecraftClient client) {
        if (!active) return;
        tick++;

        if (tick <= ZOOM_IN_TICKS) {
            float p = tick / (float) ZOOM_IN_TICKS;
            currentFovMultiplier = MathHelper.lerp(easeInOutCubic(p), 1.0f, TARGET_FOV_SCALE);
        } else if (tick <= ZOOM_IN_TICKS + HOLD_TICKS) {
            currentFovMultiplier = TARGET_FOV_SCALE;
            if (target != null && target.isAlive() && client.world != null && tick % 4 == 0) {
                Vec3d pos = target.getPos().add(0, target.getHeight() * 0.75, 0);
                client.world.addParticle(
                        net.minecraft.particle.ParticleTypes.HEART,
                        pos.x + (client.world.random.nextDouble() - 0.5) * 0.5,
                        pos.y,
                        pos.z + (client.world.random.nextDouble() - 0.5) * 0.5,
                        0, 0.03, 0);
            }
        } else if (tick <= TOTAL) {
            float p = (tick - ZOOM_IN_TICKS - HOLD_TICKS) / (float) ZOOM_OUT_TICKS;
            currentFovMultiplier = MathHelper.lerp(easeInOutCubic(p), TARGET_FOV_SCALE, 1.0f);
        } else {
            active = false;
            currentFovMultiplier = 1.0f;
            target = null;
            return;
        }

        if (target != null && target.isAlive() && client.player != null
                && tick < ZOOM_IN_TICKS + HOLD_TICKS) {
            lookAtMaid(client, target, 0.18f);
        }
    }

    private void lookAtMaid(MinecraftClient client, Entity maid, float factor) {
        Entity player = client.player;
        if (player == null) return;

        Vec3d eye = player.getEyePos();
        Vec3d targetPos = maid.getPos().add(0, maid.getStandingEyeHeight() * 0.6, 0);
        Vec3d dir = targetPos.subtract(eye).normalize();

        float targetYaw = (float) (MathHelper.atan2(dir.z, dir.x) * (180.0 / Math.PI)) - 90.0f;
        float targetPitch = (float) -(MathHelper.atan2(dir.y,
                Math.sqrt(dir.x * dir.x + dir.z * dir.z)) * (180.0 / Math.PI));

        float newYaw = MathHelper.lerpAngleDegrees(factor, player.getYaw(), targetYaw);
        float newPitch = MathHelper.lerp(factor, player.getPitch(), targetPitch);

        player.setYaw(newYaw);
        player.setPitch(newPitch);
        player.prevYaw = newYaw;
        player.prevPitch = newPitch;
    }

    private static float easeInOutCubic(float t) {
        return t < 0.5f
                ? 4f * t * t * t
                : 1f - (float) Math.pow(-2 * t + 2, 3) / 2f;
    }
}