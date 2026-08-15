package com.example.tlmkiss.mixin;

import com.example.tlmkiss.client.TlmKissClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class CameraMixin {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void tlmKiss$modifyFov(Camera camera, float tickDelta, boolean changingFov,
                                   CallbackInfoReturnable<Double> cir) {
        if (TlmKissClient.cameraHandler.isActive()) {
            double original = cir.getReturnValue();
            float mult = TlmKissClient.cameraHandler.getFovMultiplier();