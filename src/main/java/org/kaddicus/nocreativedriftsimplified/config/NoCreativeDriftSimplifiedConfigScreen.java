package org.kaddicus.nocreativedriftsimplified.config;

import org.kaddicus.nocreativedriftsimplified.Drift;
import org.kaddicus.nocreativedriftsimplified.NoCreativeDriftSimplified;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class NoCreativeDriftSimplifiedConfigScreen {
    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("text.autoconfig.nocreativedrift-simplified.title"));

        builder.setSavingRunnable(NoCreativeDriftSimplifiedConfig::save);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("text.autoconfig.nocreativedrift-simplified.title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Disable Vertical Drift
        general.addEntry(entryBuilder
                .startBooleanToggle(
                        Component.translatable("text.autoconfig.nocreativedrift-simplified.option.disableVerticalDrift"),
                        NoCreativeDriftSimplifiedConfig.disableVerticalDrift)
                .setDefaultValue(NoCreativeDriftSimplifiedConfig.DEFAULT_DISABLE_VERTICAL_DRIFT)
                .setSaveConsumer((replace) -> NoCreativeDriftSimplifiedConfig.disableVerticalDrift = replace)
                .build());

        // Disable Non-Creative Drift
        general.addEntry(entryBuilder
                .startBooleanToggle(
                        Component.translatable("text.autoconfig.nocreativedrift-simplified.option.disableNonCreativeDrift"),
                        NoCreativeDriftSimplifiedConfig.disableNonCreativeDrift)
                .setDefaultValue(NoCreativeDriftSimplifiedConfig.DEFAULT_DISABLE_NON_CREATIVE_DRIFT)
                .setSaveConsumer((replace) -> NoCreativeDriftSimplifiedConfig.disableNonCreativeDrift = replace)
                .build());

        // Drift Strength (Uses the existing Drift enum)
        general.addEntry(entryBuilder
                .startEnumSelector(
                        Component.translatable("text.autoconfig.nocreativedrift-simplified.option.driftStrength"),
                        Drift.class,
                        NoCreativeDriftSimplified.getCurrentDrift())
                .setEnumNameProvider(enumValue -> Component.translatable("hud.nocreativedrift-simplified.drift_strength_" + enumValue.name().toLowerCase()))
                .setDefaultValue(Drift.values()[NoCreativeDriftSimplifiedConfig.DEFAULT_DRIFT_STRENGTH])
                .setSaveConsumer((replace) -> NoCreativeDriftSimplifiedConfig.driftStrength = replace.ordinal())
                .build());

        // Confirmation
        general.addEntry(entryBuilder
                .startBooleanToggle(
                        Component.translatable("text.autoconfig.nocreativedrift-simplified.option.confirmation"),
                        NoCreativeDriftSimplifiedConfig.confirmation)
                .setDefaultValue(NoCreativeDriftSimplifiedConfig.DEFAULT_CONFIRMATION)
                .setSaveConsumer((replace) -> NoCreativeDriftSimplifiedConfig.confirmation = replace)
                .build());

        // Confirmation Type (Chat vs HUD)
        enum ConfirmTypeLabel {
            CHAT,
            HUD
        }

        general.addEntry(entryBuilder
                .startEnumSelector(
                        Component.translatable("text.autoconfig.nocreativedrift-simplified.option.confirmationType"),
                        ConfirmTypeLabel.class,
                        !NoCreativeDriftSimplifiedConfig.confirmationType ? ConfirmTypeLabel.CHAT : ConfirmTypeLabel.HUD)
                .setEnumNameProvider(enumValue -> Component.translatable("text.autoconfig.nocreativedrift-simplified.option.confirmationType." + enumValue.name().toLowerCase()))
                .setDefaultValue(ConfirmTypeLabel.HUD)
                .setSaveConsumer((replace) -> NoCreativeDriftSimplifiedConfig.confirmationType = replace == ConfirmTypeLabel.HUD)
                .build());

        return builder.build();
    }
}