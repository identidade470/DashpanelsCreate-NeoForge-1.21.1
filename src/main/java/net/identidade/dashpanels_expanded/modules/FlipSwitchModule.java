package net.identidade.dashpanels_expanded.modules;

import com.mojang.blaze3d.vertex.PoseStack;
import moth.boxxed.panels.api.module.IInput;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.ModuleType;
import moth.boxxed.panels.compat.computercraft.IModuleLuaObject;
import moth.boxxed.panels.content.panel.PanelBlockEntity;
import moth.boxxed.panels.util.PreLoadedModel;
import net.identidade.dashpanels_expanded.PanelsExpandedPreloadedModels;
import net.identidade.dashpanels_expanded.item.PanelsExpandedModules;
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

public class FlipSwitchModule extends Module implements IInput, IModuleLuaObject {
    private boolean switchState = false;

    public FlipSwitchModule(int x, int y) {
        super((ModuleType) PanelsExpandedModules.FLIP_SWITCH.get(), x, y, 2, 2);
    }

    @Override
    public InteractionResult onUse(Level level, Player player) {
        if (!level.isClientSide) {
            this.switchState = !this.switchState;
            this.parentBlockEntity.getLevel().playSound(null, this.getParentPos(), SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.1F, this.switchState? 0.6F:0.5F);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("state", this.switchState);
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.switchState = tag.getBoolean("state");
        return super.loadData(tag, registries);
    }

    @Override
    public void render(PanelBlockEntity panelBlockEntity, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        PreLoadedModel model = switchState? PanelsExpandedPreloadedModels.FLIP_SWITCH_ON: PanelsExpandedPreloadedModels.FLIP_SWITCH_OFF;
        model.render(poseStack, bufferSource, RenderType.solid(), packedLight);
    }

    @Override
    public VoxelShape getShape() {
        return Block.box(0,0,0,2,1,2);
    }

    @Override
    public int getAnalog() {
        return this.switchState?15:0;
    }

    @Override
    public void getMethods(BiConsumer<String, ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getState", (IModuleLuaObject.ReturnMethod)(args) -> this.switchState);
        biConsumer.accept("setState", (IModuleLuaObject.ReturnMethod)(args) -> {
            if (args.count() != 1) {
                return false;
            } else {
                Object patt0$temp = args.get(0);
                if (patt0$temp instanceof Boolean) {
                    Boolean bool = (Boolean)patt0$temp;
                    this.switchState = bool;
                    this.parentBlockEntity.networkUpdate(this.parentBlockEntity.getOrCreate());
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
