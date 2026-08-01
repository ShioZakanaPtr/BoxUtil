package org.boxutil.backends.core.statictrail;

import com.fs.starfarer.api.util.Misc;
import org.boxutil.backends.core.dev.BUtil_GLDrawInfo;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.boxutil.units.standard.entity.TextFieldEntity;
import org.boxutil.units.standard.misc.TextFieldObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.util.Locale;

public class BUtil_GLDrawStaticTrailMemoryUsage implements BUtil_GLDrawInfo.Drawable {
    private int last_types = 0;
    private long last_total = 0;
    private long last_space = 0;
    private final TextFieldObject title;
    private final TextFieldObject info_types;
    private final TextFieldObject info_total;
    private final TextFieldObject info_usage;

    public BUtil_GLDrawStaticTrailMemoryUsage() {
        this.title = new TextFieldObject("graphics/fonts/FiraCodeModified/Fira_Code_SemiBold_14.fnt");
        this.title.addText("[", true);
        this.title.addText("Static Trail", 0.0f, Misc.getPositiveHighlightColor(), true);
        this.title.addText("]", true);
        this.title.addText(TextFieldEntity.LINE_FEED_SYMBOL + " - Allocated:", 10.0f);
        this.title.setFieldWidth(512.0f);
        this.title.setFieldHeight(64.0f);
        this.title.setTextDataRefreshIndex(0);
        this.title.setTextDataRefreshAllFromCurrentIndex();
        this.title.submitText();

        this.info_types = new TextFieldObject("graphics/fonts/FiraCodeModified/Fira_Code_SemiBold_14.fnt");
        this.info_types.mallocTextData(32);
        this.info_types.addText("Types: ");
        this.info_types.addText("0", 0.0f, Misc.getHighlightColor());
        this.info_types.setFieldWidth(512.0f);
        this.info_types.setFieldHeight(32.0f);
        this.info_types.setTextDataRefreshIndex(0);
        this.info_types.setTextDataRefreshAllFromCurrentIndex();
        this.info_types.submitText();
        this.info_types.setTextDataRefreshIndex(1);
        this.info_types.setTextDataRefreshSize(1);

        this.info_total = new TextFieldObject("graphics/fonts/FiraCodeModified/Fira_Code_SemiBold_14.fnt");
        this.info_total.mallocTextData(32);
        this.info_total.addText("Total: ");
        this.info_total.addText("0", 0.0f, Misc.getHighlightColor());
        this.info_total.addText(" Byte");
        this.info_total.setFieldWidth(512.0f);
        this.info_total.setFieldHeight(32.0f);
        this.info_total.setTextDataRefreshIndex(0);
        this.info_total.setTextDataRefreshAllFromCurrentIndex();
        this.info_total.submitText();
        this.info_total.setTextDataRefreshIndex(1);
        this.info_total.setTextDataRefreshSize(1);

        this.info_usage = new TextFieldObject("graphics/fonts/FiraCodeModified/Fira_Code_SemiBold_14.fnt");
        this.info_usage.mallocTextData(32);
        this.info_usage.addText("Usage: ");
        this.info_usage.addText("0", 0.0f, Misc.getNegativeHighlightColor());
        this.info_usage.addText(" Byte");
        this.info_usage.setFieldWidth(512.0f);
        this.info_usage.setFieldHeight(32.0f);
        this.info_usage.setTextDataRefreshIndex(0);
        this.info_usage.setTextDataRefreshAllFromCurrentIndex();
        this.info_usage.submitText();
        this.info_usage.setTextDataRefreshIndex(1);
        this.info_usage.setTextDataRefreshSize(1);
    }

    public boolean isShown() {
        return BoxConfigs.isShowStaticTrailMemoryUsage() && !BUtil_StaticTrailMemoryPool.isNotSupported();
    }

    private static void refreshValue(final TextFieldObject text, long value) {
        text.getTextDataList().get(1).setText(String.format(Locale.US, "%,d", value));
        text.submitText();
    }

    public float glDraw(float totalYOffset) {
        final float widgetWidth = 450.0f, topPadding = 24.0f, widgetSpace = 10.0f, border = 5.0f,
                fixedTextWidth = (float) Math.ceil(this.title.getCurrentVisualWidth()),
                fixedTextHeight = this.title.getFontMap().getLineHeight(), widgetHeight = 3.0f * (fixedTextHeight + widgetSpace);
        final var renderingPos = new Vector2f(border, -border);

        GL11.glPushMatrix();
        GL11.glTranslatef(-widgetSpace - widgetWidth, -topPadding, 0.0f);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4ub(BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ZERO, (byte) 100);
        GL11.glRectf(0.0f, -widgetHeight, widgetWidth, 0.0f);

        this.title.render(renderingPos, 0.0f, false, null);
        renderingPos.x += fixedTextWidth + 32.0f;

        final int currTypes = BUtil_StaticTrailMemoryPool.getTrailTypes();
        if (currTypes != this.last_types) {
            this.last_types = currTypes;
            refreshValue(this.info_types, currTypes);
        }
        this.info_types.render(renderingPos, 0.0f, false, null);
        renderingPos.y -= fixedTextHeight + widgetSpace;

        final long currTotal = BUtil_StaticTrailMemoryPool.getTotalAllocatedMemory();
        if (currTotal != this.last_total) {
            this.last_total = currTotal;
            refreshValue(this.info_total, currTotal);
        }
        this.info_total.render(renderingPos, 0.0f, false, null);
        renderingPos.y -= fixedTextHeight + widgetSpace;

        final long currSpace = BUtil_StaticTrailMemoryPool.getTotalSpace();
        if (currSpace != this.last_space) {
            this.last_space = currSpace;
            refreshValue(this.info_usage, currTotal - currSpace);
        }
        this.info_usage.render(renderingPos, 0.0f, false, null);
        renderingPos.y -= fixedTextHeight + widgetSpace;

        GL11.glPopMatrix();
        return renderingPos.y - border - topPadding;
    }
}
