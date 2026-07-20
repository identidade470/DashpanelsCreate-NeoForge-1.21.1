package net.identidade.dashpanels_expanded.modules.valve_switch;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moth.boxxed.panels.api.module.IExternalUpdatable;
import moth.boxxed.panels.api.module.IInput;
import moth.boxxed.panels.api.module.Module;
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

public class ValveSwitchModule extends Module implements IExternalUpdatable, IInput, IModuleLuaObject {

    private float renderAngle = 0f;
    private float lastRenderAngle = 0f;
    private int signal = 0;

    public ValveSwitchModule(int x, int y) {
        super(PanelsExpandedModules.VALVE_SWITCH.get(), x, y, 4, 4);
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.VALVE_SWITCH.isActive()) {
            PanelsExpandedHoldInteractions.VALVE_SWITCH.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(level, player);
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.lastRenderAngle = this.renderAngle;
        this.renderAngle = Math.lerp(this.renderAngle, Mth.map((float)this.signal, 0.0F, 15.0F, 0.0F, 90F), 0.5F);;
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("signal", this.signal);
        tag.putFloat("render_angle", this.renderAngle);
        tag.putFloat("last_render_angle", this.lastRenderAngle);

        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.renderAngle = tag.getFloat("render_angle");
        this.lastRenderAngle = tag.getFloat("last_render_angle");
        this.signal = tag.getInt("signal");
        return super.loadData(tag, registries);
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
    public void render(PanelBlockEntity panelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        PanelsExpandedPreloadedModels.VALVE_SWITCH_BASE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.pushPose();
        poseStack.rotateAround(Axis.YP.rotationDegrees(Mth.lerp(v, this.lastRenderAngle, this.renderAngle)), 0.205f, 0f, 0.05f);
        PanelsExpandedPreloadedModels.VALVE_SWITCH_HANDLE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
    }

    @Override
    public VoxelShape getShape() {
        return Block.box(0,0,0,4,.5f,4);
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
