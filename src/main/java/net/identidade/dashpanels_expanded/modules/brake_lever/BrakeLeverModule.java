package net.identidade.dashpanels_expanded.modules.brake_lever;

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
import org.joml.Math;

import java.util.List;
import java.util.function.BiConsumer;

public class BrakeLeverModule extends Module implements IExternalUpdatable, IInput, IModuleLuaObject {

    public int signal = 0;
    private float renderSignal = 0f;
    private float lastRenderSignal = 0f;

    public BrakeLeverModule(int x, int y) {
        super(PanelsExpandedModules.BRAKE_LEVER.get(), x, y, 4, 5);
    }

    public int getSignal() {
        return signal;
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.BRAKE_LEVER.isActive()) {
            PanelsExpandedHoldInteractions.BRAKE_LEVER.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(level, player);
        }
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("signal", this.signal);
        tag.putFloat("renderSignal", renderSignal);
        tag.putFloat("lastRenderSignal", lastRenderSignal);
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.signal = tag.getInt("signal");
        this.renderSignal = tag.getFloat("renderSignal");
        this.lastRenderSignal = tag.getFloat("lastRenderSignal");
        return super.loadData(tag, registries);
    }

    @Override
    public void setNum(List<Integer> list) {
        this.signal = (Integer) list.getFirst();
        float f = (float)(this.signal + 15) / 15.0F;
        this.parentBlockEntity.getLevel().playSound((Player)null, this.getParentPos(), SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.1F, f);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.lastRenderSignal = this.renderSignal;
        this.renderSignal = Math.lerp(this.renderSignal, this.signal, 0.75f);
    }

    @Override
    public int getAnalog() {
        return this.signal;
    }

    @Override
    public void render(PanelBlockEntity panelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        float newSignal = Mth.lerp(v, this.lastRenderSignal, this.renderSignal);
        float angle = (newSignal / 15) * 90f;

        PanelsExpandedPreloadedModels.BRAKE_LEVER_BASE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.pushPose();
        poseStack.rotateAround(Axis.XP.rotationDegrees(angle), 0, .098f, 0.16f);
        PanelsExpandedPreloadedModels.BRAKE_LEVER_HANDLE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
    }

    @Override
    public VoxelShape getShape() {
        return Block.box(0,0,0, 4,4,5);
    }

    @Override
    public void getMethods(BiConsumer<String, ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getSignal", (IModuleLuaObject.ReturnMethod) (args) -> this.getSignal());
    }
}
