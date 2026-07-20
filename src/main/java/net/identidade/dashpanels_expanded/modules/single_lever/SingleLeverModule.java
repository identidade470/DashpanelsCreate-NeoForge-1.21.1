package net.identidade.dashpanels_expanded.modules.single_lever;

import com.mojang.blaze3d.vertex.PoseStack;
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
import org.joml.Math;

import java.util.List;
import java.util.function.BiConsumer;

public class SingleLeverModule extends Module implements IExternalUpdatable, IInput, IModuleLuaObject {

    private float lastRenderSignal = 0.0F;
    private float lastIndicatorRender = 0.0F;
    private float renderSignal = 0.0F;
    private float indicatorRender = 0.0F;
    private int signal = 0;

    public SingleLeverModule(int x, int y) {
        super((ModuleType) PanelsExpandedModules.SINGLE_LEVER.get(), x, y, 2, 6);
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("signal", this.signal);
        tag.putFloat("render_signal", this.renderSignal);
        tag.putFloat("indicator_render", this.indicatorRender);
        tag.putFloat("last_render_signal", this.lastRenderSignal);
        tag.putFloat("last_indicator_render", this.lastIndicatorRender);
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.signal = tag.getInt("signal");
        this.renderSignal = tag.getFloat("render_signal");
        this.indicatorRender = tag.getFloat("indicator_render");
        this.lastRenderSignal = tag.getFloat("last_render_signal");
        this.lastIndicatorRender = tag.getFloat("last_indicator_render");
        return super.loadData(tag, registries);
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.SINGLE_LEVER.isActive()) {
            PanelsExpandedHoldInteractions.SINGLE_LEVER.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(level, player);
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.lastRenderSignal = this.renderSignal;
        this.lastIndicatorRender = this.indicatorRender;
        this.renderSignal = org.joml.Math.lerp(this.renderSignal, Mth.map((float)this.signal, 0.0F, 15.0F, 0.0F, 0.F), 0.5F);
        this.indicatorRender = Math.lerp(this.indicatorRender, Mth.map((float)this.signal, 0.0F, 15.0F, 0.0F, 0.31F), 0.15F);
    }

    @Override
    public void render(PanelBlockEntity panelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        PanelsExpandedPreloadedModels.SINGLE_LEVER_BASE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, Mth.lerp(v, this.lastRenderSignal, this.renderSignal));
        PanelsExpandedPreloadedModels.SINGLE_LEVER_HANDLE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, Mth.lerp(v, this.lastIndicatorRender, this.indicatorRender));
        PanelsExpandedPreloadedModels.SINGLE_LEVER_INDICATOR.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
    }

    @Override
    public VoxelShape getShape() {
        return Block.box(0,0,0,2,1,6);
    }

    @Override
    public void setNum(List<Integer> list) {
        this.signal = (Integer)list.getFirst();
        float f = (float)(this.signal + 15) / 15.0F;
        this.parentBlockEntity.getLevel().playSound((Player)null, this.getParentPos(), SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.1F, f);
    }

    @Override
    public int getAnalog() {
        return this.signal;
    }

    public int getSignal() {
        return this.signal;
    }

    @Override
    public void getMethods(BiConsumer<String, ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getValue", (IModuleLuaObject.ReturnMethod)(args) -> this.getSignal());
        biConsumer.accept("setValue", (IModuleLuaObject.ReturnMethod)(args) -> {
            if (args.count() != 1) {
                return false;
            } else {
                Object patt0$temp = args.get(0);
                if (patt0$temp instanceof Number) {
                    Number number = (Number)patt0$temp;
                    this.signal = Math.clamp(0, 15, number.intValue());
                    this.parentBlockEntity.networkUpdate(this.parentBlockEntity.getOrCreate());
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
