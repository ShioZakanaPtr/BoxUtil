package org.boxutil.backends.core.instancedrendering;

import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.backends.core.dev.BUtil_GLDrawInfo;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.backends.util.BUtil_MiscUtil;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.GLWrapper;
import org.boxutil.define.InstanceType;
import org.boxutil.manager.ShaderCore;
import org.boxutil.units.standard.entity.TextFieldEntity;
import org.boxutil.units.standard.misc.TextFieldObject;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.Locale;

public final class BUtil_GLDrawInstanceMemoryUsage implements BUtil_GLDrawInfo.Drawable {
    private final TextFieldObject[] title = new TextFieldObject[InstanceType.values().length];
    private final TextFieldObject[] hoverText = new TextFieldObject[InstanceType.values().length];
    private final InstanceType[] types;
    private final byte[][] textColor;
    private final long[][] last_state = new long[InstanceType.values().length][2];

    public BUtil_GLDrawInstanceMemoryUsage() {
        for (final var type : InstanceType.values()) {
            this.initTitleText(type);
            this.initHoverText(type);
        }

        this.types = InstanceType.values();
        this.textColor = new byte[][]{CommonUtil.colorToByteArray(Misc.getNegativeHighlightColor()), CommonUtil.colorToByteArray(Misc.getPositiveHighlightColor())};
    }

    private void initTitleText(final InstanceType type) {
        final Color highLightColor = Misc.getHighlightColor();
        byte ordinal = (byte) type.ordinal();
        this.title[ordinal] = new TextFieldObject("graphics/fonts/FiraCodeModified/Fira_Code_SemiBold_14.fnt");
        final var text = this.title[ordinal];
        text.mallocTextData(50);
        text.addText(String.format("%-11s", type.name()), 0.0f, Misc.getNegativeHighlightColor(), true);
        text.addText("▼ ");
        text.addText("    0 Byte", 0.0f, highLightColor);
        text.addText(" / ");
        text.addText("    0 Byte", 0.0f, highLightColor);
        text.addText(" ≈ ");
        text.addText("  - %", 0.0f, highLightColor);
        text.addText(" Usage");
        text.setFieldWidth(512.0f);
        text.setFieldHeight(32.0f);
        text.setTextDataRefreshIndex(0);
        text.setTextDataRefreshAllFromCurrentIndex();
        text.submitText();
        text.setShouldRenderingCharCount(50);
        text.setRefreshRenderingLengthWhenSubmit(false);
        text.setTextDataRefreshIndex(0);
        text.setTextDataRefreshSize(7);
    }

    private void initHoverText(final InstanceType type) {
        byte ordinal = (byte) type.ordinal();
        this.hoverText[ordinal] = new TextFieldObject("graphics/fonts/FiraCodeModified/Fira_Code_SemiBold_14.fnt");
        final var text = this.hoverText[ordinal];
        text.setAlignment(TextFieldEntity.Alignment.MID);
        text.mallocTextData(60);
        text.addText("0");
        text.addText(" / ");
        text.addText("0");
        text.addText(" Byte");
        text.setFieldWidth(512.0f);
        text.setFieldHeight(32.0f);
        text.setTextDataRefreshIndex(0);
        text.setTextDataRefreshAllFromCurrentIndex();
        text.submitText();
    }

    public boolean isShown() {
        return BoxConfigs.isShowInstanceMemoryUsage() && !BUtil_InstanceDataMemoryPool.isPoolInvalid();
    }

    public float glDraw(float totalYOffset) {
        final float barWidth = 512.0f, barHeight = 32.0f, topPadding = 24.0f, widgetSpace = 10.0f,
                fixedTextHeight = this.title[0].getFontMap().getLineHeight(), hoverTextHeight = this.hoverText[0].getFontMap().getLineHeight(),
                locX = ShaderCore.getScreenWidth(), locY = ShaderCore.getScreenHeight();

        final Vector2f mousePos = new Vector2f(BUtil_GLImpl.getMouseX(), BUtil_GLImpl.getMouseY()), renderingPos = new Vector2f(0.0f, 0.0f);
        final var aabb = new Vector2f[]{new Vector2f(), new Vector2f(locX - widgetSpace, locY - topPadding - totalYOffset)};
        aabb[0].x = aabb[1].x - barWidth;
        aabb[0].y = aabb[1].y - fixedTextHeight - barHeight;
        GL11.glPushMatrix();
        GL11.glTranslatef(aabb[0].x - locX, aabb[0].y - locY, 0.0f);

        TextFieldObject text;
        TextFieldEntity.TextData textData;
        byte ordinal;
        long usageValue = 0;
        float drawOffset = -topPadding, totalDrawOffset = 0.0f;
        byte[] pickTextColor;
        final long[] values = new long[2];
        boolean notShowHover = true, refreshText;
        for (final var type : this.types) {
            ordinal = (byte) type.ordinal();
            if (totalDrawOffset < -topPadding) GL11.glTranslatef(0.0f, drawOffset, 0.0f);

            GLWrapper.Operation.glDisable(GLWrapper.Texture.GL_TEXTURE_2D);
            GL11.glPushMatrix();
            GL11.glScalef(barWidth, barHeight, 1.0f);
            BUtil_InstanceDataMemoryPool.getPool(type).glDrawMemoryUsage(values);
            GL11.glPopMatrix();

            text = this.title[ordinal];
            refreshText = values[0] != this.last_state[ordinal][0] || values[1] != this.last_state[ordinal][1];
            if (refreshText) {
                this.last_state[ordinal][0] = values[0];
                this.last_state[ordinal][1] = values[1];
                usageValue = values[1] - values[0];
                
                textData = text.getTextDataList().get(0);
                textData.setText(String.format("%-11s", type.name()));
                pickTextColor = values[1] < 1 ? this.textColor[0] : this.textColor[1];
                textData.setColor(pickTextColor[0], pickTextColor[1], pickTextColor[2], pickTextColor[3]);
                text.getTextDataList().get(2).setText(BUtil_MiscUtil.getMemoryNumStr(usageValue));
                text.getTextDataList().get(4).setText(BUtil_MiscUtil.getMemoryNumStr(values[1]));
                text.getTextDataList().get(6).setText(BUtil_MiscUtil.getPercentageStr((double) usageValue / values[1]));
                text.submitText();
            }

            renderingPos.set(0.0f, fixedTextHeight + barHeight);
            text.render(renderingPos, 0.0f, false, null);

            text = this.hoverText[ordinal];
            if (refreshText) {
                text.getTextDataList().get(0).setText(String.format(Locale.US, "%,d", usageValue));
                text.getTextDataList().get(2).setText(String.format(Locale.US, "%,d", values[1]));
                text.submitText();
            }

            if (notShowHover && CalculateUtil.isPointWithinAABB(mousePos, aabb)) {
                notShowHover = false;
                aabb[0].x = 0.0f;
                aabb[0].y = (hoverTextHeight + barHeight) * 0.5f;
                aabb[1].x = 1.5f;
                aabb[1].y = aabb[0].y - 1.5f;

                text.render(aabb[1], 0.0f, false, Color.BLACK);
                text.render(aabb[0], 0.0f, false, null);
            }

            drawOffset = -(fixedTextHeight + barHeight + widgetSpace);
            totalDrawOffset += drawOffset;
            if (notShowHover) {
                aabb[0].y += drawOffset;
                aabb[1].y += drawOffset;
            }
        }
        GL11.glPopMatrix();
        return totalDrawOffset;
    }
}
