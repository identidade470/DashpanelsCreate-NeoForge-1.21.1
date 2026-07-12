package net.identidade.dashpanels_expanded.registry;

import moth.boxxed.panels.api.module.interaction.ModuleHoldInteractionManager;
import net.identidade.dashpanels_expanded.modules.*;

public class PanelsExpandedHoldInteractions {
    public static SingleLeverHoldInteraction SINGLE_LEVER = (SingleLeverHoldInteraction) ModuleHoldInteractionManager.register(new SingleLeverHoldInteraction());
    public static ControlValveHoldInteraction CONTROL_VALVE = (ControlValveHoldInteraction) ModuleHoldInteractionManager.register(new ControlValveHoldInteraction());
    public static ValveSwitchHoldInteraction VALVE_SWITCH = (ValveSwitchHoldInteraction) ModuleHoldInteractionManager.register(new ValveSwitchHoldInteraction());
    public static SmallSwitchHoldInteraction SMALL_SWITCH = (SmallSwitchHoldInteraction) ModuleHoldInteractionManager.register(new SmallSwitchHoldInteraction());
    public static CopperValveHoldInteraction COPPER_VALVE = (CopperValveHoldInteraction) ModuleHoldInteractionManager.register(new CopperValveHoldInteraction());
}
