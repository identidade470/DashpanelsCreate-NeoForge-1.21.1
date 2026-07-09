package net.identidade.dashpanels_expanded;

import moth.boxxed.panels.api.module.interaction.ModuleHoldInteractionManager;
import net.identidade.dashpanels_expanded.modules.ControlValveHoldInteraction;
import net.identidade.dashpanels_expanded.modules.SingleLeverHoldInteraction;

public class PanelsExpandedHoldInteractions {
    public static SingleLeverHoldInteraction SINGLE_LEVER = (SingleLeverHoldInteraction) ModuleHoldInteractionManager.register(new SingleLeverHoldInteraction());
    public static ControlValveHoldInteraction CONTROL_VALVE = (ControlValveHoldInteraction) ModuleHoldInteractionManager.register(new ControlValveHoldInteraction());
}
