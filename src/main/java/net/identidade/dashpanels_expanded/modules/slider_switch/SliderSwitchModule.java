package net.identidade.dashpanels_expanded.modules.slider_switch;

import com.mojang.blaze3d.vertex.PoseStack;
import moth.boxxed.panels.api.module.*;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.io.IInput;
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

public class SliderSwitchModule extends Module implements IInput, IModuleLuaObject, IExternalUpdatable {

    private float lastRenderSignal = 0.0F;
    private float renderSignal = 0.0F;
    private int signal = 0;

    public SliderSwitchModule(int x, int y) {
        super(PanelsExpandedModules.SLIDER_SWITCH.get(), x, y);
    }

    public int getSignal() {
        return signal;
    }

    @Override
    public int getAnalog() {
        return this.signal;
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("signal", this.signal);
        tag.putFloat("render_signal", this.renderSignal);
        tag.putFloat("last_render_signal", this.lastRenderSignal);
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.signal = tag.getInt("signal");
        this.renderSignal = tag.getFloat("render_signal");
        this.lastRenderSignal = tag.getFloat("last_render_signal");
        return super.loadData(tag, registries);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.lastRenderSignal = this.renderSignal;
        this.renderSignal = org.joml.Math.lerp(this.renderSignal, Mth.map((float)this.signal, 0.0F, 15.0F, 0.0F, 0.375F), 0.5F);
    }

    @Override
    public InteractionResult onUse(ModuleHitResult hitResult, Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.SLIDER_SWITCH.isActive()) {
            PanelsExpandedHoldInteractions.SLIDER_SWITCH.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(hitResult, level, player);
        }
    }

    @Override
    public void render(AbstractPanelBlockEntity abstractPanelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        PanelsExpandedPreloadedModels.SLIDER_SWITCH_BASE.render(poseStack, RenderType.solid(), i);
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, Mth.lerp(v, this.lastRenderSignal, this.renderSignal));
        PanelsExpandedPreloadedModels.SLIDER_SWITCH_SLIDER.render(poseStack, RenderType.solid(), i);
        poseStack.popPose();
    }

    @Override
    public VoxelShape getVoxelShape() {
        return Block.box(.25,0,0,1.75,.5,8);
    }

    @Override
    public PolyVoxel getShape() {
        return new PolyVoxel(0, 0, 2, 8);
    }

    @Override
    public void getMethods(BiConsumer<String, ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getValue", (IModuleLuaObject.ReturnMethod) (args) -> this.getSignal());
        biConsumer.accept("setValue", (IModuleLuaObject.ReturnMethod) (args) -> {
            if (args.count() != 1) {
                return false;
            } else {
                Object obj = args.get(0);
                if (obj instanceof Number) {
                    Number number = (Number) obj;
                    this.signal = Math.clamp(0, 15, number.intValue());
                    this.parentBlockEntity.networkUpdate(this.parentBlockEntity.getOrCreate());
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void update(ServerPlayer serverPlayer, CompoundTag compoundTag, HolderLookup.Provider provider) {
        this.signal = compoundTag.getInt("signal");
        float f = (float)(this.signal + 15) / 15.0F;
        this.parentBlockEntity.getLevel().playSound(null, this.getParentPos(), SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.1F, f);
    }
}
