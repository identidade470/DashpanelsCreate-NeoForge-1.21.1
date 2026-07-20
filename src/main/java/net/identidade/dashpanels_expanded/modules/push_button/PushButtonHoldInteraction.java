package net.identidade.dashpanels_expanded.modules.push_button;

import moth.boxxed.panels.api.module.interaction.ModuleHoldInteraction;

public class PushButtonHoldInteraction extends ModuleHoldInteraction<PushButtonModule> {
    @Override
    public boolean activeMouseMove(double yaw, double pitch) {
        return true;
    }

    @Override
    public void start() {
        this.update(new Integer[]{1});
    }

    @Override
    public void stop() {
        this.update(new Integer[]{0});
    }
}
