package net.identidade.dashpanels_expanded.modules.gearshift_lever;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import moth.boxxed.panels.Dashpanels;
import moth.boxxed.panels.api.module.interaction.ModuleHoldInteraction;
import net.identidade.dashpanels_expanded.DashpanelsExpanded;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GearshiftLeverHoldInteraction extends ModuleHoldInteraction<GearshiftLeverModule> {
    private static final ResourceLocation INDICATOR = Dashpanels.path("module/joystick/indicator");
    private static final ResourceLocation BACKGROUND = DashpanelsExpanded.path("module/gearshift_lever");

    private float valX = 0f;
    private float valY = 0f;

    @Override
    public void start() {
        this.valY = ((GearshiftLeverModule)this.module).handleY;
        this.valX = ((GearshiftLeverModule)this.module).handleX;
        this.getGuiContext().setCrosshairVisibility(false);
    }

    @Override
    public boolean activeMouseMove(double yaw, double pitch) {
        float oldValY = this.valY;
        float oldValX = this.valX;

        if (valX == 0) {
            valY -= pitch / 180f;

            if (valY > 0.9f) valY = 1;
            else if (valY < -0.9f) valY = -1;
            else if (Math.abs(valY) < 0.1f) valY = 0;
        }

        if (valY == -1 || valY == 0 || valY == 1) {
            valX += yaw / 180f;
        }

        this.valY = Math.clamp(this.valY, -1, 1);
        this.valX = Math.clamp(this.valX, -1, 0);

        if (oldValX != this.valX || oldValY != this.valY) {
            this.update(new Integer[]{(int) (this.valX * 100), (int) (this.valY * 100)});
        }

        return true;
    }

    @Override
    public void renderGui(GuiGraphics graphics, float partialTick) {
        int centerX = graphics.guiWidth() / 2;
        int centerY = graphics.guiHeight() / 2;
        int leftPos = centerX - 11;
        int topPos = centerY - 10;
        int indicatorX = (int) Mth.map(this.valX, -1.0F, 1.0F, (float)(leftPos - 1), (float)(leftPos + 19));
        int indicatorY = (int)Mth.map(-this.valY, -1.0F, 1.0F, (float)(topPos - 1), (float)(topPos + 19));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        graphics.blitSprite(BACKGROUND, leftPos, topPos, 21, 21);
        graphics.blitSprite(INDICATOR, indicatorX, indicatorY, 3, 3);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
