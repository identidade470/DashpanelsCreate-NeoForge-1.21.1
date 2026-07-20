package net.identidade.dashpanels_expanded.modules.dimmer_knob;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import moth.boxxed.panels.Dashpanels;
import moth.boxxed.panels.api.module.interaction.ModuleHoldInteraction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DimmerKnobHoldInteraction extends ModuleHoldInteraction<DimmerKnobModule> {
    private static final ResourceLocation KNOB_SPRITE = Dashpanels.path("module/knob");

    private float val = 0f;
    private int oldAngle = 0;
    private int angle = 0;

    @Override
    public void start() {
        this.angle = ((DimmerKnobModule) this.module).getAngle();
        this.val = (float) this.angle / 180;
    }

    @Override
    public boolean activeMouseMove(double yaw, double pitch) {
        this.val += (float)(yaw / (double)180f);
        this.val = Math.clamp(this.val, 0.0F, 1.0F);
        this.angle = Math.clamp((long)Math.round(this.val * 180f), 0, 180);
        if (this.oldAngle != this.angle) {
            this.update(new Integer[]{this.angle});
        }

        this.oldAngle = this.angle;
        return true;
    }

    @Override
    public void renderGui(GuiGraphics graphics, float partialTick) {
        int section = Math.round(Mth.map((float)this.angle, 0.0F, 360.0F, 0.0F, 16.0F));
        int centerX = graphics.guiWidth() / 2;
        int centerY = graphics.guiHeight() / 2;
        int x = centerX - 9;
        int y = centerY - 8;
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        graphics.blitSprite(KNOB_SPRITE, 289, 17, section * 17, 0, x, y, 17, 17);
        graphics.pose().pushPose();
        graphics.pose().translate((float)centerX, (float)(centerY + 12), 0.0F);
        graphics.pose().scale(0.5F, 0.5F, 0.5F);
        graphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(this.angle), 0, 0, -1426063361);
        graphics.pose().popPose();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
