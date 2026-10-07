package org.kaddicus.nocreativedriftsimplified;

import org.kaddicus.nocreativedriftsimplified.config.NoCreativeDriftSimplifiedConfig;
import org.lwjgl.sdl.SDLKeycode;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.client.player.LocalPlayer;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class NoCreativeDriftSimplified implements ClientModInitializer {
    public static Minecraft MC;

    private static boolean keyJumpPressed = false;
    private static boolean keySneakPressed = false;

    @Override
    public void onInitializeClient() {
        NoCreativeDriftSimplifiedConfig.load();
        MC = Minecraft.getInstance();

        Category keybindCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("nocreativedrift-simplified", "category"));

        KeyMapping toggle_keybind = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.nocreativedrift-simplified.toggle_drift", InputConstants.Type.KEYBOARD, SDLKeycode.SDLK_UNKNOWN, keybindCategory));

        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            LocalPlayer player = MC.player;
            if (player == null) return;

            while (toggle_keybind.consumeClick()) {
                cycleDrift();
                NoCreativeDriftSimplifiedConfig.save();

                if (NoCreativeDriftSimplifiedConfig.confirmation) {
                    Component message = Component.translatable("hud.nocreativedrift-simplified.drift_strength", getCurrentDrift().getText());
                    if (NoCreativeDriftSimplifiedConfig.confirmationType) {
                        player.sendOverlayMessage(message);
                    } else {
                        player.sendSystemMessage(message);
                    }
                }
            }

            if ((!NoCreativeDriftSimplifiedConfig.disableNonCreativeDrift || player.isCreative()) && player.getAbilities().flying) {
                stopDrift(player);
            }
        });
    }

    private static void cycleDrift() {
        NoCreativeDriftSimplifiedConfig.driftStrength = (NoCreativeDriftSimplifiedConfig.driftStrength + 1) % Drift.values().length;
    }

    public static Drift getCurrentDrift() {
        return Drift.values()[NoCreativeDriftSimplifiedConfig.driftStrength];
    }

    private static void stopDrift(LocalPlayer player) {
        Vec3 motion = player.getDeltaMovement();
        stopHorizontalDrift(player);

        if (!NoCreativeDriftSimplifiedConfig.disableVerticalDrift) return;

        if (keyJumpPressed && !player.input.keyPresses.jump()) {
            player.setDeltaMovement(motion.x, motion.y * getCurrentDrift().getMulti(), motion.z);
            keyJumpPressed = false;
        } else if (player.input.keyPresses.jump()) {
            keyJumpPressed = true;
        }

        if (keySneakPressed && !player.input.keyPresses.shift()) {
            player.setDeltaMovement(motion.x, motion.y * getCurrentDrift().getMulti(), motion.z);
            keySneakPressed = false;
        } else if (player.input.keyPresses.shift()) {
            keySneakPressed = true;
        }
    }

    private static void stopHorizontalDrift(LocalPlayer player) {
        if (!horizontalControlsUsed(player)) {
            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(motion.x * getCurrentDrift().getMulti(), motion.y, motion.z * getCurrentDrift().getMulti());
        }
    }

    private static boolean horizontalControlsUsed(LocalPlayer player) {
        return Math.abs(player.input.getMoveVector().y) > Mth.EPSILON || Math.abs(player.input.getMoveVector().x) > Mth.EPSILON;
    }
}