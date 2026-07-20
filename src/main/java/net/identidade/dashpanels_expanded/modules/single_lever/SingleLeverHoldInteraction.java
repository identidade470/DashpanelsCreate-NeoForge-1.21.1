package net.identidade.dashpanels_expanded.modules.single_lever;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import moth.boxxed.panels.Dashpanels;
import moth.boxxed.panels.api.module.interaction.ModuleHoldInteraction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SingleLeverHoldInteraction extends ModuleHoldInteraction<SingleLeverModule> {

    private static final ResourceLocation LEVER_SPRITE = Dashpanels.path("module/control_lever");
    private float val = 0.0F;
    private int oldSignal = 0;
    private int signal = 0;
    private float renderSignal = 0.0F;

    @Override
    public void start() {
        this.signal = ((SingleLeverModule)this.module).getSignal();
        this.val = (float)this.signal / 15.0F;
        this.renderSignal = Mth.map((float)this.signal, 0.0F, 15.0F, 0.0F, 112.0F);
    }

    @Override
    public boolean activeMouseMove(double yaw, double pitch) {
        this.val -= (float)(pitch / (double)180.0F);
        this.val = Math.clamp(this.val, 0.0F, 1.0F);
        this.signal = Math.clamp((long)Math.round(this.val * 15.0F), 0, 15);
        if (this.oldSignal != this.signal) {
            this.update(new Integer[]{this.signal});
        }

        this.oldSignal = this.signal;
        return true;
    }

    @Override
    public void tick() {
        this.renderSignal = org.joml.Math.lerp(this.renderSignal, Mth.map((float)this.signal, 0.0F, 15.0F, 0.0F, 112.0F), 0.5F);
    }

    @Override
    public void renderGui(GuiGraphics graphics, float partialTick) {
        int section = this.signal;
        int centerX = graphics.guiWidth() / 2;
        int centerY = graphics.guiHeight() / 2;
        int x = centerX - 9;
        int y = centerY - 8;
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        graphics.blitSprite(LEVER_SPRITE, 272, 17, section * 17, 0, x, y, 17, 17);
        graphics.pose().pushPose();
        graphics.pose().translate((float)centerX, (float)(centerY + 12), 0.0F);
        graphics.pose().scale(0.5F, 0.5F, 0.5F);
        graphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(this.signal), 0, 0, -1426063361);
        graphics.pose().popPose();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
