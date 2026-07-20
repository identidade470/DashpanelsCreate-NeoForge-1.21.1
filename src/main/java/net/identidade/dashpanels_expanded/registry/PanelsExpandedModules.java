package net.identidade.dashpanels_expanded.registry;

import moth.boxxed.panels.api.module.ModuleType;
import moth.boxxed.panels.api.registry.ModulesRegistry;
import net.identidade.dashpanels_expanded.DashpanelsExpanded;
import net.identidade.dashpanels_expanded.modules.*;
import net.identidade.dashpanels_expanded.modules.control_valve.ControlValveModule;
import net.identidade.dashpanels_expanded.modules.copper_valve.CopperValveModule;
import net.identidade.dashpanels_expanded.modules.dimmer_knob.DimmerKnobModule;
import net.identidade.dashpanels_expanded.modules.push_button.PushButtonModule;
import net.identidade.dashpanels_expanded.modules.single_lever.SingleLeverModule;
import net.identidade.dashpanels_expanded.modules.slider_switch.SliderSwitchModule;
import net.identidade.dashpanels_expanded.modules.small_switch.SmallSwitchModule;
import net.identidade.dashpanels_expanded.modules.valve_switch.ValveSwitchModule;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PanelsExpandedModules {

    public static final DeferredRegister<ModuleType<?>> MODULES = DeferredRegister.create(ModulesRegistry.MODULE_REGISTRY, DashpanelsExpanded.MOD_ID);

    public static final Supplier<ModuleType<FlipSwitchModule>> FLIP_SWITCH =
            MODULES.register("flip_switch", () -> new ModuleType<>(FlipSwitchModule::new, PanelsExpandedItems.FLIP_SWITCH));
    public static final Supplier<ModuleType<SingleLeverModule>> SINGLE_LEVER =
            MODULES.register("single_lever", () -> new ModuleType<>(SingleLeverModule::new, PanelsExpandedItems.SINGLE_LEVER));
    public static final Supplier<ModuleType<ControlValveModule>> CONTROL_VALVE =
            MODULES.register("control_valve", () -> new ModuleType<>(ControlValveModule::new, PanelsExpandedItems.CONTROL_VALVE));
    public static final Supplier<ModuleType<CopperValveModule>> COPPER_VALVE =
            MODULES.register("copper_valve", () -> new ModuleType<>(CopperValveModule::new, PanelsExpandedItems.COPPER_VALVE));
    public static final Supplier<ModuleType<ValveSwitchModule>> VALVE_SWITCH =
            MODULES.register("valve_switch", () -> new ModuleType<>(ValveSwitchModule::new, PanelsExpandedItems.VALVE_SWITCH));
    public static final Supplier<ModuleType<SmallSwitchModule>> SMALL_SWITCH =
            MODULES.register("small_switch", () -> new ModuleType<>(SmallSwitchModule::new, PanelsExpandedItems.SMALL_SWITCH));
    public static final Supplier<ModuleType<StickyNoteModule>> STICKY_NOTE =
            MODULES.register("sticky_note", () -> new ModuleType<>(StickyNoteModule::new, PanelsExpandedItems.STICKY_NOTE));
    public static final Supplier<ModuleType<SliderSwitchModule>> SLIDER_SWITCH =
            MODULES.register("slider_switch", () -> new ModuleType<>(SliderSwitchModule::new, PanelsExpandedItems.SLIDER_SWITCH));
    public static final Supplier<ModuleType<FireBreakerModule>> FIRE_BREAKER =
            MODULES.register("fire_breaker", () -> new ModuleType<>(FireBreakerModule::new, PanelsExpandedItems.FIRE_BREAKER));
    public static final Supplier<ModuleType<PushButtonModule>> PUSH_BUTTON =
            MODULES.register("push_button", () -> new ModuleType<>(PushButtonModule::new, PanelsExpandedItems.PUSH_BUTTON));
    public static final Supplier<ModuleType<DimmerKnobModule>> DIMMER_KNOB =
            MODULES.register("dimmer_knob", () -> new ModuleType<>(DimmerKnobModule::new, PanelsExpandedItems.DIMMER_KNOB));
    public static final Supplier<ModuleType<SmallSevenSegmentModule>> SMALL_SEVEN_SEGMENT =
            MODULES.register("small_seven_segment", () -> new ModuleType<>(SmallSevenSegmentModule::new, PanelsExpandedItems.SMALL_SEVEN_SEGMENT));


    public static void register(IEventBus bus) {
        MODULES.register(bus);
    }
}
