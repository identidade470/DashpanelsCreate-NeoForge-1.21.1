package net.identidade.dashpanels_expanded.modules.dial;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moth.boxxed.panels.api.module.IExternalUpdatable;
import moth.boxxed.panels.api.module.io.IMultiInput;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.ModuleType;
import moth.boxxed.panels.api.panel.AbstractPanelBlockEntity;
import moth.boxxed.panels.util.PolyVoxel;
import net.identidade.dashpanels_expanded.PanelsExpandedPreloadedModels;
import net.identidade.dashpanels_expanded.registry.PanelsExpandedHoldInteractions;
import net.identidade.dashpanels_expanded.registry.PanelsExpandedModules;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Math;

import java.util.List;
import java.util.function.BiConsumer;

public class DialModule extends Module implements IMultiInput, IExternalUpdatable {

    public int angle = 0;
    private float renderAngle = 0f;
    private float lastRenderAngle = 0f;

    public DialModule(int x, int y) {
        super(PanelsExpandedModules.DIAL.get(), x, y);
    }

    @Override
    public void getValues(BiConsumer<String, AnalogResult> biConsumer) {
        int firstAnalog = (this.angle==60)?15:0;
        int secondAnalog = (this.angle==120)?15:0;
        int thirdAnalog = (this.angle==180)?15:0;
        int fourthAnalog = (this.angle==240)?15:0;
        int fiftyAnalog = (this.angle==300)?15:0;
        biConsumer.accept("1", (IMultiInput.AnalogResult) () -> firstAnalog);
        biConsumer.accept("2", (IMultiInput.AnalogResult) () -> secondAnalog);
        biConsumer.accept("3", (IMultiInput.AnalogResult) () -> thirdAnalog);
        biConsumer.accept("4", (IMultiInput.AnalogResult) () -> fourthAnalog);
        biConsumer.accept("5", (IMultiInput.AnalogResult) () -> fiftyAnalog);
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.DIAL.isActive()) {
            PanelsExpandedHoldInteractions.DIAL.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(level, player);
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.lastRenderAngle = this.renderAngle;
        this.renderAngle = Math.lerp(this.renderAngle, (float)this.angle, 0.5f);
    }

    @Override
    public void render(AbstractPanelBlockEntity abstractPanelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        PanelsExpandedPreloadedModels.DIAL_BASE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.pushPose();
        poseStack.rotateAround(Axis.YP.rotationDegrees(Mth.lerp(v, this.lastRenderAngle, this.renderAngle)), .157f, 0, .157f);
        PanelsExpandedPreloadedModels.DIAL_TOP.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
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
    public VoxelShape getVoxelShape() {
        return Block.box(0,0,0,5,1,5);
    }

    @Override
    public PolyVoxel getShape() {
        return new PolyVoxel(0, 0, 5, 5);
    }

    @Override
    public void update(ServerPlayer serverPlayer, CompoundTag compoundTag, HolderLookup.Provider provider) {
        this.angle = compoundTag.getInt("angle");
        this.parentBlockEntity.getLevel().playSound(null, this.getParentPos(), SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, .5f, 1);
    }
}
