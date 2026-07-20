package net.identidade.dashpanels_expanded.modules;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.ModuleType;
import moth.boxxed.panels.compat.computercraft.IModuleLuaObject;
import moth.boxxed.panels.content.panel.PanelBlockEntity;
import net.identidade.dashpanels_expanded.PanelsExpandedPreloadedModels;
import net.identidade.dashpanels_expanded.registry.PanelsExpandedModules;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.function.BiConsumer;

public class StickyNoteModule extends Module {
    public StickyNoteModule(int x, int y) {
        super((ModuleType) PanelsExpandedModules.STICKY_NOTE.get(), x, y, 3, 4);
    }

    @Override
    public void render(PanelBlockEntity panelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        PanelsExpandedPreloadedModels.STICKY_NOTE.render(poseStack, multiBufferSource, RenderType.solid(), i);

        var font = Minecraft.getInstance().font;

        float scale = 0.004f;

        poseStack.pushPose();
        poseStack.translate(0.095, 0.001, .23);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.XN.rotationDegrees(90));
        poseStack.scale(scale, -scale, scale);

        List<FormattedCharSequence> lines = font.split(Component.literal(this.name), 40);
        int totalHeight = lines.size() * font.lineHeight;
        float yOffset = 0;

        for (FormattedCharSequence line: lines) {
            float x = -font.width(line) / 2f;
            font.drawInBatch(
                    line,
                    x,
                    yOffset,
                    0x00000000,
                    false,
                    poseStack.last().pose(),
                    multiBufferSource,
                    Font.DisplayMode.NORMAL,
                    0,
                    LightTexture.FULL_BRIGHT);

            yOffset += font.lineHeight;
        }

        poseStack.popPose();
    }

    @Override
    public VoxelShape getShape() {
        return Block.box(0,0,0,3,.01,4);
    }
}
