package net.identidade.dashpanels_expanded.registry;

import moth.boxxed.panels.Dashpanels;
import net.identidade.dashpanels_expanded.DashpanelsExpanded;
import net.mcexpanded.fancytabsections.FancyTabSections;
import net.mcexpanded.fancytabsections.creativetab.ConglomerateOfItems;
import net.mcexpanded.fancytabsections.creativetab.SectionTextured;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;

public class PanelsExpandedCreativeModeTab {
    public static void addItems() {
        FancyTabSections.addSection(Dashpanels.path("dashpanels"),
                new SectionTextured(
                        DashpanelsExpanded.path("modules"),
                        Component.translatable("itemGroup.dashpanels_expanded.modules"),
                        Dashpanels.path("textures/gui/fancy_tab_section/dashpanels.png"),
                        0xFFFFFFFF,
                        ConglomerateOfItems.create()
                                .add(PanelsExpandedItems.SINGLE_LEVER)
                                .add(PanelsExpandedItems.FLIP_SWITCH)
                                .add(PanelsExpandedItems.CONTROL_VALVE)
                                .add(PanelsExpandedItems.VALVE_SWITCH)
                                .add(PanelsExpandedItems.SMALL_SWITCH)
                                .add(PanelsExpandedItems.COPPER_VALVE)
                ));
    }

    public static void register(IEventBus bus) {
        addItems();
    }
}
