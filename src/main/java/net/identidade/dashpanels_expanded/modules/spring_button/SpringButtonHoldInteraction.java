package net.identidade.dashpanels_expanded.modules.spring_button;

import moth.boxxed.panels.api.module.interaction.ModuleHoldInteraction;
import net.minecraft.nbt.CompoundTag;

public class SpringButtonHoldInteraction extends ModuleHoldInteraction<SpringButtonModule> {

    private boolean pressed;

    @Override
    public void start() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("pressed", true);
        this.update(tag);
    }

    @Override
    public void stop() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("pressed", false);
        this.update(tag);
    }

    @Override
    public boolean activeMouseMove(double v, double v1) {
        return true;
    }
}
