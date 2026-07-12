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

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
