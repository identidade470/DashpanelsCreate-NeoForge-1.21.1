package net.identidade.dashpanels_expanded.registry;

import net.identidade.dashpanels_expanded.DashpanelsExpanded;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PanelsExpandedItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DashpanelsExpanded.MOD_ID);

    public static final DeferredItem<Item> SINGLE_LEVER = ITEMS.register("single_lever",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLIP_SWITCH = ITEMS.register("flip_switch",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CONTROL_VALVE = ITEMS.register("control_valve",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VALVE_SWITCH = ITEMS.register("valve_switch",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SMALL_SWITCH = ITEMS.register("small_switch",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COPPER_VALVE = ITEMS.register("copper_valve",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STICKY_NOTE = ITEMS.register("sticky_note",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SLIDER_SWITCH = ITEMS.register("slider_switch",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FIRE_BREAKER = ITEMS.register("fire_breaker",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIMMER_KNOB = ITEMS.register("dimmer_knob",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PUSH_BUTTON = ITEMS.register("push_button",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SMALL_SEVEN_SEGMENT = ITEMS.register("small_seven_segment",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BRAKE_LEVER = ITEMS.register("brake_lever",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPRING_BUTTON = ITEMS.register("spring_button",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GEARSHIFT_LEVER = ITEMS.register("gearshift_lever",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIAL = ITEMS.register("dial",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
