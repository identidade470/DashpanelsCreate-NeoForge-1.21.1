package net.identidade.dashpanels_expanded.item;

import moth.boxxed.panels.api.module.ModuleType;
import moth.boxxed.panels.api.registry.ModulesRegistry;
import net.identidade.dashpanels_expanded.DashpanelsExpanded;
import net.identidade.dashpanels_expanded.modules.ControlValveModule;
import net.identidade.dashpanels_expanded.modules.SingleLeverModule;
import net.identidade.dashpanels_expanded.modules.FlipSwitchModule;
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

    public static void register(IEventBus bus) {
        MODULES.register(bus);
    }
}
