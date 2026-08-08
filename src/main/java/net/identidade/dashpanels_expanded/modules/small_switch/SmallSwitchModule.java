package net.identidade.dashpanels_expanded.modules.small_switch;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moth.boxxed.panels.api.module.IExternalUpdatable;
import moth.boxxed.panels.api.module.io.IMultiInput;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.panel.AbstractPanelBlockEntity;
import moth.boxxed.panels.compat.computercraft.IModuleLuaObject;
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

public class SmallSwitchModule extends Module implements IExternalUpdatable, IMultiInput, IModuleLuaObject {
    public float stickY = 0f;

    private float angleRender = 0f;
    private float lastAngleRender = 0f;

    public SmallSwitchModule(int x, int y) {
        super(PanelsExpandedModules.SMALL_SWITCH.get(), x, y);
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putFloat("stick_y", this.stickY);
        tag.putFloat("last_angle_render", this.lastAngleRender);
        tag.putFloat("angle_render", this.angleRender);
        return super.saveData(tag, registries);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.lastAngleRender = this.angleRender;
        this.angleRender = Math.lerp(this.angleRender, this.stickY, 0.5f);
        super.tick(level, blockPos, blockState);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.stickY = tag.getFloat("stick_y");
        this.lastAngleRender = tag.getFloat("last_angle_render");
        this.angleRender = tag.getFloat("angle_render");
        return super.loadData(tag, registries);
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.SMALL_SWITCH.isActive()) {
            PanelsExpandedHoldInteractions.SMALL_SWITCH.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(level, player);
        }
    }

    @Override
    public void render(AbstractPanelBlockEntity abstractPanelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        float angleY = Mth.map(Mth.lerp(v, this.lastAngleRender, this.angleRender), -1.0F, 1.0F, -30f, 30f);

        PanelsExpandedPreloadedModels.SMALL_SWITCH_BASE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.pushPose();
        poseStack.rotateAround(Axis.XP.rotationDegrees(angleY), 0f, 0, 0.06f);
        PanelsExpandedPreloadedModels.SMALL_SWITCH_HANDLE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
    }

    @Override
    public VoxelShape getVoxelShape() {
        return Block.box(0.25,0,0.25,1.75,1,1.75);
    }
    @Override
    public PolyVoxel getShape() {
        return new PolyVoxel(0, 0, 2, 2);
    }

    @Override
    public void update(ServerPlayer serverPlayer, CompoundTag compoundTag, HolderLookup.Provider provider) {
        this.stickY = compoundTag.getFloat("y") /100f;
        this.parentBlockEntity.getLevel().playSound((Player)null, this.getParentPos(), SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.1F, .9f);
    }

    @Override
    public void getValues(BiConsumer<String, AnalogResult> biConsumer) {
        int analogTop = (int) Mth.map(java.lang.Math.max(this.stickY, 0.0F), 0.0F, 1.0F, 0.0F, 15.0F);
        int analogBottom = (int)Mth.map(-java.lang.Math.min(this.stickY, 0.0F), 0.0F, 1.0F, 0.0F, 15.0F);
        biConsumer.accept("up", (IMultiInput.AnalogResult)() -> analogTop);
        biConsumer.accept("bottom", (IMultiInput.AnalogResult)() -> analogBottom);
    }

    @Override
    public void getMethods(BiConsumer<String, ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getY", (IModuleLuaObject.ReturnMethod) (args) -> this.stickY);
    }
}
