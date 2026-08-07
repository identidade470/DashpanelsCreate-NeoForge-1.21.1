package net.identidade.dashpanels_expanded.modules;

import com.mojang.blaze3d.vertex.PoseStack;
import moth.boxxed.panels.api.module.io.IInput;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.ModuleType;
import moth.boxxed.panels.api.panel.AbstractPanelBlockEntity;
import moth.boxxed.panels.compat.computercraft.IModuleLuaObject;
import moth.boxxed.panels.util.PolyVoxel;
import moth.boxxed.panels.util.PreLoadedModel;
import net.identidade.dashpanels_expanded.PanelsExpandedPreloadedModels;
import net.identidade.dashpanels_expanded.registry.PanelsExpandedModules;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiConsumer;

public class FireBreakerModule extends Module implements IInput, IModuleLuaObject {

    public boolean activated = false;

    public FireBreakerModule(int x, int y) {
        super(PanelsExpandedModules.FIRE_BREAKER.get(), x, y, 4,6);
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (!level.isClientSide) {
            this.activated = !this.activated;
            this.parentBlockEntity.getLevel().playSound(null, this.getParentPos(), SoundEvents.LEVER_CLICK, SoundSource.BLOCKS);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("activated", activated);
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.activated = tag.getBoolean("activated");
        return super.loadData(tag, registries);
    }

    @Override
    public int getAnalog() {
        return activated?15:0;
    }

    @Override
    public void render(AbstractPanelBlockEntity abstractPanelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        PreLoadedModel renderedModel = activated? PanelsExpandedPreloadedModels.FIRE_BREAKER_ON:PanelsExpandedPreloadedModels.FIRE_BREAKER_OFF;
        renderedModel.render(poseStack, multiBufferSource, RenderType.solid(), i);
    }

    @Override
    public VoxelShape getVoxelShape() {
        return Block.box(0,0,0,4,1,6);
    }

    @Override
    public PolyVoxel getShape() {
        return new PolyVoxel(0, 0, 4, 6);
    }

    @Override
    public void getMethods(BiConsumer<String, ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getState", (IModuleLuaObject.ReturnMethod) (args) -> this.activated);
    }
}
