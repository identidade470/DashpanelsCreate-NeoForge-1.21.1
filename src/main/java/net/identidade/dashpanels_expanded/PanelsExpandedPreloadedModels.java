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
           FLIP_SWITCH_OFF = regular("flip_switch/off"),
           SLIDER_SWITCH_BASE = regular("slider_switch/base"),
           SLIDER_SWITCH_SLIDER = regular("slider_switch/slider"),
           FIRE_BREAKER_ON = regular("fire_breaker/on"),
           FIRE_BREAKER_OFF = regular("fire_breaker/off"),
           DIMMER_KNOB_HANDLE = regular("dimmer_knob/handle"),
           DIMMER_KNOB_BASE = regular("dimmer_knob/base"),
           PUSH_BUTTON_BASE = regular("push_button/base"),
           PUSH_BUTTON_BUTTON = regular("push_button/button"),
           SMALL_SEVEN_SEGMENT = regular("small_seven_segment"),
           STICKY_NOTE = regular("sticky_note");

    private static PreLoadedModel regular(String name) {
        return PreLoadedModel.create(DashpanelsExpanded.path("block/" + name));
    }

    public static void init() {

    }
}
