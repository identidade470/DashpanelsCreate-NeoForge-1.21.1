package net.identidade.dashpanels_expanded.modules.spring_button;

import moth.boxxed.panels.api.module.interaction.ModuleHoldInteraction;

public class SpringButtonHoldInteraction extends ModuleHoldInteraction<SpringButtonModule> {

    private boolean pressed;

    @Override
    public void start() {
        this.update(new Integer[]{1});
    }

    @Override
    public void stop() {
        this.update(new Integer[]{0});
    }

    @Override
    public boolean activeMouseMove(double v, double v1) {
        return true;
    }
}
