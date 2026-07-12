package net.identidade.dashpanels_expanded;

import moth.boxxed.panels.util.PreLoadedModel;

public class PanelsExpandedPreloadedModels {

   public static final PreLoadedModel
           SINGLE_LEVER_BASE = regular("single_lever/base"),
           SINGLE_LEVER_HANDLE = regular("single_lever/handle"),
           SINGLE_LEVER_INDICATOR = regular("single_lever/indicator"),
           CONTROL_VALVE = regular("control_valve"),
           VALVE_SWITCH_BASE = regular("valve_switch/base"),
           VALVE_SWITCH_HANDLE = regular("valve_switch/handle"),
           SMALL_SWITCH_BASE = regular("small_switch/base"),
           SMALL_SWITCH_HANDLE = regular("small_switch/handle"),
           COPPER_VALVE_BASE = regular("copper_valve/base"),
           COPPER_VALVE_HANDLE = regular("copper_valve/handle"),
           FLIP_SWITCH_ON = regular("flip_switch/on"),
           FLIP_SWITCH_OFF = regular("flip_switch/off");

    private static PreLoadedModel regular(String name) {
        return PreLoadedModel.create(DashpanelsExpanded.path("block/" + name));
    }

    public static void init() {

    }
}
