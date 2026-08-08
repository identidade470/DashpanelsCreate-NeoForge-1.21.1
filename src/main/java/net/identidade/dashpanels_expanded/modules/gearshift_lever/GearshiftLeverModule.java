package net.identidade.dashpanels_expanded.modules.gearshift_lever;

import com.mojang.blaze3d.vertex.PoseStack;
import moth.boxxed.panels.api.module.*;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.io.IMultiInput;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Math;

import java.util.List;
import java.util.function.BiConsumer;

public class GearshiftLeverModule extends Module implements IMultiInput, IExternalUpdatable, IModuleLuaObject {

    public float handleX = 0f;
    public float handleY = 0f;

    private float handleRenderX = 0f;
    private float handleRenderY = 0f;
    private float lastHandleRenderX = 0f;
    private float lastHandleRenderY = 0f;

    public GearshiftLeverModule(int x, int y) {
        super(PanelsExpandedModules.GEARSHIFT_LEVER.get(), x, y);
    }

    @Override
    public void update(ServerPlayer serverPlayer, CompoundTag compoundTag, HolderLookup.Provider provider) {
        this.handleX = compoundTag.getFloat("x");
        this.handleY = compoundTag.getFloat("y");

        System.out.println(this.handleX);
        System.out.println(this.handleY);

        if (this.handleY == 0 || this.handleY == -1 || this.handleY == 1) {
            if (this.handleX == 0) {
                //this.parentBlockEntity.getLevel().playSound(null, this.getParentPos(), SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundSource.BLOCKS);
            } else if (this.handleX == -1) {
                this.parentBlockEntity.getLevel().playSound(null, this.getParentPos(), SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS);
            }
        }
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (level.isClientSide && player.isLocalPlayer() && !PanelsExpandedHoldInteractions.GEARSHIFT_LEVER.isActive()) {
            PanelsExpandedHoldInteractions.GEARSHIFT_LEVER.startHold(level, player, this);
            return InteractionResult.SUCCESS;
        } else {
            return super.onUse(level, player);
        }
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putFloat("handleX", this.handleX);
        tag.putFloat("handleY", this.handleY);
        tag.putFloat("handleRenderX", this.handleRenderX);
        tag.putFloat("handleRenderY", this.handleRenderY);
        tag.putFloat("lastHandleRenderX", this.lastHandleRenderX);
        tag.putFloat("lastHandleRenderY", this.lastHandleRenderY);
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.handleX = tag.getFloat("handleX");
        this.handleY = tag.getFloat("handleY");
        this.handleRenderX = tag.getFloat("handleRenderX");
        this.handleRenderY = tag.getFloat("handleRenderY");
        this.lastHandleRenderX = tag.getFloat("lastHandleRenderX");
        this.lastHandleRenderY = tag.getFloat("lastHandleRenderY");
        return super.loadData(tag, registries);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.lastHandleRenderY = this.handleRenderY;
        this.lastHandleRenderX = this.handleRenderX;

        this.handleRenderY = Math.lerp(this.handleRenderY, Mth.map((float)this.handleY, -1, 1,0, .375f), 0.5f);
        this.handleRenderX = Math.lerp(this.handleRenderX, Mth.map((float)this.handleX, -1, 0,0, .125f), 0.5f);
    }

    @Override
    public void render(AbstractPanelBlockEntity abstractPanelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        PanelsExpandedPreloadedModels.GEARSHIFT_LEVER_BASE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.pushPose();
        poseStack.translate(-Mth.lerp(v, this.lastHandleRenderX, this.handleRenderX), 0, Mth.lerp(v, this.lastHandleRenderY, this.handleRenderY));
        PanelsExpandedPreloadedModels.GEARSHIFT_LEVER_HANDLE.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
    }

    @Override
    public VoxelShape getVoxelShape() {
        return Block.box(0,0,0,5,1,9);
    }

    @Override
    public PolyVoxel getShape() {
        return new PolyVoxel(0, 0, 5, 9);
    }

    @Override
    public void getValues(BiConsumer<String, AnalogResult> biConsumer) {
        int analogTop = (handleX == -1 && handleY == 1)?15:0;
        int analogMiddle = (handleX == -1 && handleY == 0)?15:0;
        int analogBottom = (handleX == -1 && handleY == -1)?15:0;

        biConsumer.accept("top", (IMultiInput.AnalogResult) () -> analogTop);
        biConsumer.accept("middle", (IMultiInput.AnalogResult) () -> analogMiddle);
        biConsumer.accept("bottom", (IMultiInput.AnalogResult) () -> analogBottom);
    }

    @Override
    public void getMethods(BiConsumer<String, ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getMarch", (IModuleLuaObject.ReturnMethod) (args) -> {
            if (this.handleX == -1) {
                if (this.handleY == -1) return 1;
                if (this.handleY == 0) return 2;
                if (this.handleY == 1) return 3;
            }
            return 0;
        });
    }
}
