package net.identidade.dashpanels_expanded.modules;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import moth.boxxed.panels.Dashpanels;
import moth.boxxed.panels.api.module.interaction.ModuleHoldInteraction;
import net.identidade.dashpanels_expanded.Config;
import net.identidade.dashpanels_expanded.DashpanelsExpanded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SmallSwitchHoldInteraction extends ModuleHoldInteraction<SmallSwitchModule> {
    private static final ResourceLocation CROSSHAIR = DashpanelsExpanded.path("module/small_switch");
    private static final ResourceLocation INDICATOR = Dashpanels.path("module/joystick/indicator");

    private float valY = 0f;

    @Override
    public void start() {
        this.valY = this.module.stickY;
        this.getGuiContext().setCrosshairVisibility(false);
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public boolean activeMouseMove(double yaw, double pitch) {
        float oldValY = this.valY;
        this.valY -= (float) (pitch / Config.SMALL_SWITCH_SENSIBILITY.getAsInt());

        if (this.valY > 0.8f) {
            this.valY = 1;
        } else if (this.valY < -0.8f) {
            this.valY = -1;
        } else {
            this.valY = 0;
        }
        if (oldValY != valY) {
            this.update(new Integer[]{(int) (this.valY * 100f)});
        }
        return true;
    }

    @Override
    public void renderGui(GuiGraphics graphics, float partialTick) {
        int centerX = graphics.guiWidth() / 2;
        int centerY = graphics.guiHeight() / 2;
        int leftPos = centerX - 11;
        int topPos = centerY - 10;
        int indicatorY = (int)Mth.map(-this.valY, -1.0F, 1.0F, (float)(topPos - 1), (float)(topPos + 19));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        graphics.blitSprite(CROSSHAIR, leftPos, topPos, 21, 21);
        graphics.blitSprite(INDICATOR, centerX - 2, indicatorY, 3, 3);
        graphics.pose().pushPose();
        graphics.pose().translate((float)centerX, (float)(centerY + 16), 0.0F);
        graphics.pose().scale(0.5F, 0.5F, 0.5F);
        graphics.pose().translate(0.0F, 12.0F, 0.0F);
        graphics.drawCenteredString(Minecraft.getInstance().font, "Y: %.2f".formatted(this.valY), 0, 0, -1429405748);
        graphics.pose().popPose();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
