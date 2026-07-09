package net.identidade.dashpanels_expanded;

import moth.boxxed.panels.util.PreLoadedModel;

public class PanelsExpandedPreloadedModels {

   public static final PreLoadedModel
           SINGLE_LEVER_BASE = regular("single_lever/base"),
           SINGLE_LEVER_HANDLE = regular("single_lever/handle"),
           SINGLE_LEVER_INDICATOR = regular("single_lever/indicator"),
           CONTROL_VALVE = regular("control_valve"),
           FLIP_SWITCH_ON = regular("flip_switch/on"),
           FLIP_SWITCH_OFF = regular("flip_switch/off");

    private static PreLoadedModel regular(String name) {
        return PreLoadedModel.create(DashpanelsExpanded.path("block/" + name));
    }

    public static void init() {

    }
}
