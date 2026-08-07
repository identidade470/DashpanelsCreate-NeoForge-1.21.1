package net.identidade.dashpanels_expanded.modules;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moth.boxxed.panels.api.module.io.IOutput;
import moth.boxxed.panels.api.module.Module;
import moth.boxxed.panels.api.module.ModuleType;
import moth.boxxed.panels.api.panel.AbstractPanelBlockEntity;
import moth.boxxed.panels.compat.computercraft.IModuleLuaObject;
import moth.boxxed.panels.index.PanelPreloadedModels;
import moth.boxxed.panels.util.PolyVoxel;
import net.identidade.dashpanels_expanded.PanelsExpandedPreloadedModels;
import net.identidade.dashpanels_expanded.registry.PanelsExpandedModules;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiConsumer;

public class SmallSevenSegmentModule extends Module implements IModuleLuaObject, IOutput {

    public String displayText = "----";
    public DyeColor color;

    public SmallSevenSegmentModule(int x, int y) {
        super(PanelsExpandedModules.SMALL_SEVEN_SEGMENT.get(), x, y, 6,2);
        this.color = DyeColor.WHITE;
    }

    @Override
    public boolean saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putString("displayText", this.displayText);
        tag.putInt("color", this.color.getId());
        return super.saveData(tag, registries);
    }

    @Override
    public boolean loadData(CompoundTag tag, HolderLookup.Provider registries) {
        this.displayText = tag.getString("displayText");
        this.color = DyeColor.byId(tag.getInt("color"));
        return super.loadData(tag, registries);
    }

    @Override
    public ItemInteractionResult onItemUse(ItemStack stack, Level level, Player player) {
        Item item = stack.getItem();
        if (item instanceof DyeItem dyeItem) {
            this.color = dyeItem.getDyeColor();
            return ItemInteractionResult.SUCCESS;
        } else {
            return super.onItemUse(stack, level, player);
        }
    }

    @Override
    public void setAnalog(int i) {
        this.displayText = String.valueOf(i);
    }

    @Override
    public void getMethods(BiConsumer<String, IModuleLuaObject.ReturnMethod<?>> biConsumer) {
        biConsumer.accept("getDisplay", (IModuleLuaObject.ReturnMethod)(args) -> this.displayText);
        biConsumer.accept("getColor", (IModuleLuaObject.ReturnMethod)(args) -> this.color.getSerializedName());
        biConsumer.accept("setDisplay", (IModuleLuaObject.ReturnMethod)(args) -> {
            if (args.count() != 1) {
                return false;
            } else {
                Object patt0$temp = args.get(0);
                if (patt0$temp instanceof String) {
                    String str = (String)patt0$temp;
                    if (str.length() <= 4) {
                        this.displayText = str.isEmpty() ? "----" : str;
                        this.parentBlockEntity.networkUpdate(this.parentBlockEntity.getOrCreate());
                        return true;
                    }
                }

                return false;
            }
        });
        biConsumer.accept("setColor", (IModuleLuaObject.ReturnMethod)(args) -> {
            if (args.count() != 1) {
                return false;
            } else {
                Object patt0$temp = args.get(0);
                if (patt0$temp instanceof String) {
                    String string = (String)patt0$temp;
                    this.color = DyeColor.byName(string, DyeColor.WHITE);
                    this.parentBlockEntity.networkUpdate(this.parentBlockEntity.getOrCreate());
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void render(AbstractPanelBlockEntity abstractPanelBlockEntity, PoseStack poseStack, float v, MultiBufferSource multiBufferSource, int i, int i1) {
        poseStack.pushPose();
        poseStack.translate(0.0F, -0.03125F, 0.0F);
        PanelsExpandedPreloadedModels.SMALL_SEVEN_SEGMENT.render(poseStack, multiBufferSource, RenderType.solid(), i);
        poseStack.popPose();
        Font font = Minecraft.getInstance().font;
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.03125F, 0.0F);
        poseStack.scale(0.01562F, 0.01562F, 0.01562F);
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        float offset = this.displayText.length() == 1 ? 6.0F : 0.0F;
        FormattedCharSequence sequence = Component.literal(this.displayText).getVisualOrderText();
        font.drawInBatch(sequence, -23F + offset, -7.5F, this.color.getTextColor(), false, poseStack.last().pose(), multiBufferSource, Font.DisplayMode.POLYGON_OFFSET, 0, 15728880);
        poseStack.popPose();
        poseStack.popPose();
    }

    @Override
    public VoxelShape getVoxelShape() {
        return Block.box(0,0,0,6, .5f, 2);
    }

    @Override
    public PolyVoxel getShape() {
        return new PolyVoxel(0, 0, 6, 2);
    }
}
