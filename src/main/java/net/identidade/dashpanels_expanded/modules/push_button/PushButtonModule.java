package net.identidade.dashpanels_expanded.modules.push_button;

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

public class PushButtonModule extends Module implements IInput, IExternalUpdatable, IModuleLuaObject {

    public boolean activated = false;
    private float pressedValue = 0f;
    private float lastPressedValue = 0f;

    public PushButtonModule(int x, int y) {
        super(PanelsExpandedModules.PUSH_BUTTON.get(), x, y, 8,8);
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("activated", this.activated);
        tag.putFloat("pressValue", this.pressedValue);
        tag.putFloat("lastPressValue", this.lastPressedValue);
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.activated = tag.getBoolean("activated");
        this.pressedValue = tag.getFloat("pressValue");
        this.lastPressedValue = tag.getFloat("lastPressValue");
        return super.loadData(tag, registries);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        float target = this.activated?-0.1f:0f;
        this.lastPressedValue = this.pressedValue;
        this.pressedValue = Math.lerp(this.pressedValue, target, 0.5F);

    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.PUSH_BUTTON.isActive()) {
            PanelsExpandedHoldInteractions.PUSH_BUTTON.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(level, player);
        }
    }

    @Override
    public void render(PanelBlockEntity panelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        PanelsExpandedPreloadedModels.PUSH_BUTTON_BASE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.pushPose();
        poseStack.translate(0, Mth.lerp(v, this.lastPressedValue, this.pressedValue), 0);
        PanelsExpandedPreloadedModels.PUSH_BUTTON_BUTTON.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
    }

    @Override
    public VoxelShape getShape() {
        return Block.box(0,0,0,8,3,8);
    }

    @Override
    public int getAnalog() {
        return this.activated?15:0;
    }

    @Override
    public void setNum(List<Integer> list) {
        this.activated = (Integer) list.getFirst() == 1;
        if (this.activated) {
            this.parentBlockEntity.getLevel().playSound(null, this.getParentPos(), SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS);
        } else {
            this.parentBlockEntity.getLevel().playSound(null, this.getParentPos(), SoundEvents.STONE_BUTTON_CLICK_OFF, SoundSource.BLOCKS);
        }
    }

    @Override
    public void getMethods(BiConsumer<String, ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getState", (IModuleLuaObject.ReturnMethod) (args) -> this.activated);
    }
}
