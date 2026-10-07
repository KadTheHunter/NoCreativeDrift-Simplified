package org.kaddicus.nocreativedriftsimplified;

import net.minecraft.network.chat.Component;

public enum Drift {
    VANILLA(1.0, "vanilla"),
    STRONG(0.9, "strong"),
    WEAK(0.7, "weak"),
    DISABLED(0.0, "disabled");

    private final double multi;
    private final Component text;

    Drift(double multi, String name) {
        this.multi = multi;
        this.text = Component.translatable("hud.nocreativedrift-simplified.drift_strength_" + name);
    }

    public double getMulti() {
        return multi;
    }
    public Component getText() {
        return text;
    }
}