package net.identidade.dashpanels_expanded.registry;

import moth.boxxed.panels.api.module.interaction.ModuleHoldInteractionManager;
import net.identidade.dashpanels_expanded.modules.control_valve.ControlValveHoldInteraction;
import net.identidade.dashpanels_expanded.modules.copper_valve.CopperValveHoldInteraction;
import net.identidade.dashpanels_expanded.modules.dimmer_knob.DimmerKnobHoldInteraction;
import net.identidade.dashpanels_expanded.modules.push_button.PushButtonHoldInteraction;
import net.identidade.dashpanels_expanded.modules.single_lever.SingleLeverHoldInteraction;
import net.identidade.dashpanels_expanded.modules.slider_switch.SliderSwitchHoldInteraction;
import net.identidade.dashpanels_expanded.modules.small_switch.SmallSwitchHoldInteraction;
import net.identidade.dashpanels_expanded.modules.valve_switch.ValveSwitchHoldInteraction;

public class PanelsExpandedHoldInteractions {
    public static SingleLeverHoldInteraction SINGLE_LEVER = (SingleLeverHoldInteraction) ModuleHoldInteractionManager.register(new SingleLeverHoldInteraction());
    public static ControlValveHoldInteraction CONTROL_VALVE = (ControlValveHoldInteraction) ModuleHoldInteractionManager.register(new ControlValveHoldInteraction());
    public static ValveSwitchHoldInteraction VALVE_SWITCH = (ValveSwitchHoldInteraction) ModuleHoldInteractionManager.register(new ValveSwitchHoldInteraction());
    public static SmallSwitchHoldInteraction SMALL_SWITCH = (SmallSwitchHoldInteraction) ModuleHoldInteractionManager.register(new SmallSwitchHoldInteraction());
    public static CopperValveHoldInteraction COPPER_VALVE = (CopperValveHoldInteraction) ModuleHoldInteractionManager.register(new CopperValveHoldInteraction());
    public static SliderSwitchHoldInteraction SLIDER_SWITCH = (SliderSwitchHoldInteraction) ModuleHoldInteractionManager.register(new SliderSwitchHoldInteraction());
    public static PushButtonHoldInteraction PUSH_BUTTON = (PushButtonHoldInteraction) ModuleHoldInteractionManager.register(new PushButtonHoldInteraction());
    public static DimmerKnobHoldInteraction DIMMER_KNOB = (DimmerKnobHoldInteraction) ModuleHoldInteractionManager.register(new DimmerKnobHoldInteraction());
}
