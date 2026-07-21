package net.identidade.dashpanels_expanded.modules.spring_button;

import com.mojang.blaze3d.vertex.PoseStack;
import moth.boxxed.panels.api.module.IExternalUpdatable;
import moth.boxxed.panels.api.module.IInput;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.content.panel.PanelBlockEntity;
import net.identidade.dashpanels_expanded.PanelsExpandedPreloadedModels;
import net.identidade.dashpanels_expanded.registry.PanelsExpandedHoldInteractions;
import net.identidade.dashpanels_expanded.registry.PanelsExpandedModules;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Math;

import java.util.List;

public class SpringButtonModule extends Module implements IInput, IExternalUpdatable {

    public boolean pressed = false;
    private float pressedRender = 0f;
    private float lastPressedRender = 0f;

    public SpringButtonModule(int x, int y) {
        super(PanelsExpandedModules.SPRING_BUTTON.get(), x, y, 5,5);
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("pressed", this.pressed);
        tag.putFloat("pressedRender", this.pressedRender);
        tag.putFloat("lastPressedRender", this.lastPressedRender);
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.pressed = tag.getBoolean("pressed");
        this.pressedRender = tag.getFloat("pressedRender");
        this.lastPressedRender = tag.getFloat("lastPressedRender");
        return super.loadData(tag, registries);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.lastPressedRender = this.pressedRender;
        float target = this.pressed?-0.05f:0;
        this.pressedRender = Math.lerp(this.pressedRender, target, 0.6f);
    }

    @Override
    public void setNum(List<Integer> list) {
        this.pressed = (Integer)list.getFirst() == 1;
    }

    @Override
    public int getAnalog() {
        return pressed?15:0;
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.SPRING_BUTTON.isActive()) {
            PanelsExpandedHoldInteractions.SPRING_BUTTON.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(level, player);
        }
    }

    @Override
    public void render(PanelBlockEntity panelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        PanelsExpandedPreloadedModels.SPRING_BUTTON_BASE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.pushPose();
        poseStack.translate(0, Mth.lerp(v, this.lastPressedRender, this.pressedRender), 0);
        PanelsExpandedPreloadedModels.SPRING_BUTTON_BUTTON.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
    }

    @Override
    public VoxelShape getShape() {
        return Block.box(0,0,0,5,1,5);
    }
}
