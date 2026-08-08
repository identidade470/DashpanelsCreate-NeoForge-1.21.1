package net.identidade.dashpanels_expanded.modules.control_valve;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moth.boxxed.panels.api.module.IExternalUpdatable;
import moth.boxxed.panels.api.module.config.ModuleConfig;
import moth.boxxed.panels.api.module.config.ModuleConfigValue;
import moth.boxxed.panels.api.module.io.IInput;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.ModuleType;
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

public class ControlValveModule extends Module implements IExternalUpdatable, IInput, IModuleLuaObject {
    private float lastRenderAngle = 0.0F;
    private float renderAngle = 0.0F;
    private float renderHeight = 0.0F;
    private float lastRenderHeight = 0.0F;
    private int angle = 0;

    public final ModuleConfigValue.IntRangeValue angleRange = new ModuleConfigValue.IntRangeValue("angle",
            0, 360, 0, 360).addChangeListener(
            (oldVal, newVal) -> {
                this.angle = Math.clamp(newVal.getMinimum(), newVal.getMaximum(), this.angle);
            }
    );
    public final ModuleConfigValue.BooleanValue inverted = new ModuleConfigValue.BooleanValue("inverted", false);
    public final ModuleConfigValue.IntRangeValue outputRange = new ModuleConfigValue.IntRangeValue("output", 0, 15, 0, 15);

    public ControlValveModule(int x, int y) {
        super((ModuleType) PanelsExpandedModules.CONTROL_VALVE.get(), x, y);
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.CONTROL_VALVE.isActive()) {
            PanelsExpandedHoldInteractions.CONTROL_VALVE.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(level, player);
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.lastRenderAngle = this.renderAngle;
        this.renderAngle = Math.lerp(this.renderAngle, (float)this.angle, 0.75F);
        this.lastRenderHeight = renderHeight;
        this.renderHeight = Math.lerp(this.renderHeight, Mth.map((float)this.angle, 0, 15f, 0, -0.0025f), 0.75f);
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("num", this.angle);
        tag.putFloat("render_angle", this.renderAngle);
        tag.putFloat("last_render_angle", this.lastRenderAngle);
        tag.putFloat("render_height", this.renderHeight);
        tag.putFloat("last_render_height", this.lastRenderHeight);
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.angle = tag.getInt("num");
        this.renderAngle = tag.getFloat("render_angle");
        this.lastRenderAngle = tag.getFloat("last_render_angle");
        this.renderHeight = tag.getFloat("render_height");
        this.lastRenderHeight = tag.getFloat("last_render_height");
        return super.loadData(tag, registries);
    }

    @Override
    public void render(AbstractPanelBlockEntity abstractPanelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        poseStack.pushPose();
        poseStack.translate(0, Mth.lerp(v, lastRenderHeight, renderHeight), 0);
        poseStack.rotateAround(Axis.YP.rotationDegrees(Mth.lerp(v, this.lastRenderAngle, this.renderAngle)), 0.22f, 0, 0.22f);
        PanelsExpandedPreloadedModels.CONTROL_VALVE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
    }

    @Override
    public void renderOutline(PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int color) {
        poseStack.pushPose();
        poseStack.translate(0f,0.125f,0f);
        poseStack.rotateAround(Axis.YP.rotationDegrees(Mth.lerp(partialTick, this.lastRenderAngle, this.renderAngle)), 0.22F, 0.0F, 0.22f);
        poseStack.translate(0, Mth.lerp(partialTick, this.lastRenderHeight, this.renderHeight), 0);
        super.renderOutline(poseStack, bufferSource, partialTick, color);
        poseStack.popPose();
    }

    public int getAngle() {
        return this.angle;
    }

    @Override
    public void update(ServerPlayer serverPlayer, CompoundTag compoundTag, HolderLookup.Provider provider) {
        this.angle = compoundTag.getInt("angle");
        float f = (float)(this.angle + 360) / 360.0F;
        this.parentBlockEntity.getLevel().playSound((Player)null, this.getParentPos(), SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.1F, f);
    }

    @Override
    public int getAnalog() {
        int maxValue = this.outputRange.get().getMaximum();
        int minValue = this.outputRange.get().getMinimum();
        int value = Math.round(Mth.map((float)this.angle, 0.0F, 360.0F, minValue, maxValue));

        return this.inverted.get()?maxValue+minValue-value:value;
    }

    @Override
    public VoxelShape getVoxelShape() {
        return Block.box(0,0,0,7,1,7);
    }

    @Override
    public PolyVoxel getShape() {
        return new PolyVoxel(0,0,7,7);
    }

    @Override
    public void getMethods(BiConsumer<String, ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getAngle", (IModuleLuaObject.ReturnMethod)(args) -> this.getAngle());
        biConsumer.accept("getValue", (IModuleLuaObject.ReturnMethod)(args) -> this.getAnalog());
        biConsumer.accept("setAngle", (IModuleLuaObject.ReturnMethod)(args) -> {
            if (args.count() != 1) {
                return false;
            } else {
                Object patt0$temp = args.get(0);
                if (patt0$temp instanceof Number) {
                    Number number = (Number)patt0$temp;
                    this.angle = Math.clamp(number.intValue(), 0, 360);
                    this.parentBlockEntity.networkUpdate(this.parentBlockEntity.getOrCreate());
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void createConfig(ModuleConfig.Builder builder) {
        builder.add(inverted);
        builder.add(angleRange);
        builder.add(outputRange);
    }
}
