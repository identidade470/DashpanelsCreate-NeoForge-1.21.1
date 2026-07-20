package net.identidade.dashpanels_expanded.modules.dimmer_knob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moth.boxxed.panels.api.module.IExternalUpdatable;
import moth.boxxed.panels.api.module.IInput;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.ModuleType;
import moth.boxxed.panels.compat.computercraft.IModuleLuaObject;
import moth.boxxed.panels.content.panel.PanelBlockEntity;
import net.identidade.dashpanels_expanded.PanelsExpandedPreloadedModels;
import net.identidade.dashpanels_expanded.registry.PanelsExpandedHoldInteractions;
import net.identidade.dashpanels_expanded.registry.PanelsExpandedModules;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.function.BiConsumer;

public class DimmerKnobModule extends Module implements IInput, IExternalUpdatable, IModuleLuaObject {

    public int angle = 0;
    private float renderAngle = 0f;
    private float lastRenderAngle = 0f;

    public DimmerKnobModule(int x, int y) {
        super(PanelsExpandedModules.DIMMER_KNOB.get(), x, y, 3,3);
    }

    public int getAngle() {
        return angle;
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.DIMMER_KNOB.isActive()) {
            PanelsExpandedHoldInteractions.DIMMER_KNOB.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(level, player);
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.lastRenderAngle = this.renderAngle;
        this.renderAngle = org.joml.Math.lerp(this.renderAngle, (float) this.angle, 0.75f);
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("angle", this.angle);
        tag.putFloat("renderAngle", this.renderAngle);
        tag.putFloat("lastRenderAngle", this.lastRenderAngle);
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.angle = tag.getInt("angle");
        this.renderAngle = tag.getFloat("renderAngle");
        this.lastRenderAngle = tag.getFloat("lastRenderAngle");
        return super.loadData(tag, registries);
    }

    @Override
    public void render(PanelBlockEntity panelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        PanelsExpandedPreloadedModels.DIMMER_KNOB_BASE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.pushPose();
        poseStack.rotateAround(Axis.YP.rotationDegrees(Mth.lerp(v, this.lastRenderAngle, this.renderAngle)), 0.0937f, 0, 0.09f);
        PanelsExpandedPreloadedModels.DIMMER_KNOB_HANDLE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
    }

    @Override
    public VoxelShape getShape() {
        return Block.box(0,0,0,3,1,3);
    }

    @Override
    public void setNum(List<Integer> list) {
        this.angle = (Integer) list.getFirst();
        float f = (float) (this.angle + 180) /180f;
        this.parentBlockEntity.getLevel().playSound(null, this.getParentPos(), SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.1f, f);
    }

    @Override
    public int getAnalog() {
        return Math.round(Mth.map((float) this.angle, 0f, 180f, 0f, 15));
    }

    @Override
    public void getMethods(BiConsumer<String, ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getAngle", (IModuleLuaObject.ReturnMethod) (args) -> this.angle);
        biConsumer.accept("getValue", (IModuleLuaObject.ReturnMethod) (args) -> this.getAnalog());
    }
}
