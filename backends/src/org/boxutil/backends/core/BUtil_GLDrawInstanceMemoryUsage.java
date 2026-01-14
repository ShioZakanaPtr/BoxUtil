package org.boxutil.backends.core;

import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.InstanceType;
import org.boxutil.manager.ShaderCore;
import org.boxutil.units.standard.attribute.FontMapData;
import org.boxutil.units.standard.entity.TextFieldEntity;
import org.boxutil.units.standard.misc.TextFieldObject;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.TransformUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import java.nio.FloatBuffer;

public final class BUtil_GLDrawInstanceMemoryUsage {
    private final static TextFieldObject _TEXT = new TextFieldObject();
    private final static InstanceType[] _TYPES = InstanceType.values();
    private final static float _BAR_WIDTH = 512.0f, _BAR_HEIGHT = 32.0f, _SPACE = 10.0f;
    private final static byte[][] _COLOR;
    private static FloatBuffer _MATRIX = null;

    static {
        _COLOR = new byte[][]{CommonUtil.colorToByteArray(Misc.getNegativeHighlightColor()), CommonUtil.colorToByteArray(Misc.getPositiveHighlightColor())};
    }

    private static String getNum(long size) {
        if (size < 1) return "    0 Byte";
        final boolean isByte = size < 1024;
        final String[] unit = new String[]{"Byte", " KiB", " MiB", " GiB", " TiB", " PiB", " EiB"};

        byte pick = 0;
        byte decimal = 0;
        double result = size;
        if (!isByte) {
            result = Math.log10(size);
            pick = (byte) (result / 3.010299956639812d);
            result = (pick < 4) ? (1 << (pick * 10)) : Math.pow(1024.0d, pick);
            result = (double) size / result;
            if (result >= 1.0d) decimal = (byte) (4 - (byte) Math.log10(result));
        }

        String resultStr;
        if (isByte) {
            resultStr = String.format("%5s", size) + ' ';
        } else {
            String formatStr = "%." + Math.max(decimal, 0) + "f";
            resultStr = String.format(formatStr, result);
        }
        return resultStr + unit[pick];
    }

    private static String getUsageP(double div) {
        double result = div * 100.0d;
        byte decimal = 2;
        if (result > 1.0d) decimal = (byte) (2 - (byte) Math.log10(result));
        String formatStr = "%4." + Math.max(decimal, 0) + 'f';
        return String.format(formatStr, result) + '%';
    }

    public static void showGUI() {
        if (!BoxConfigs.isShowInstanceMemoryUsage() || BUtil_InstanceDataMemoryPool.isNotSupported()) return;
        if (_TEXT.getFontMap() == null) {
            _TEXT.setFontMap(new FontMapData("graphics/fonts/FiraCodeModified/Fira_Code_SemiBold_14.fnt"));
            _TEXT.mallocTextData(50);
            _TEXT.addText("DYNAMIC_2D ", true);
            _TEXT.addText("▼ ");
            _TEXT.addText("9999.9 KiB", 0.0f, Misc.getHighlightColor());
            _TEXT.addText(" / ");
            _TEXT.addText("0.9999 MiB", 0.0f, Misc.getHighlightColor());
            _TEXT.addText(" ≈ ");
            _TEXT.addText("99.9%", 0.0f, Misc.getHighlightColor());
            _TEXT.addText(" Usage");
            _TEXT.setFieldWidth(512.0f);
            _TEXT.setFieldHeight(32.0f);
            _TEXT.setTextDataRefreshIndex(0);
            _TEXT.setTextDataRefreshAllFromCurrentIndex();
            _TEXT.submitText();
            _TEXT.setShouldRenderingCharCount(50);
            _TEXT.setRefreshRenderingLengthWhenSubmit(false);
            _TEXT.setTextDataRefreshIndex(0);
            _TEXT.setTextDataRefreshSize(7);
        }

        final float fontHeight = _TEXT.getFontMap().getLineHeight();
        final float locX = ShaderCore.getScreenWidth(), locY = ShaderCore.getScreenHeight();
        if (_MATRIX == null) _MATRIX = CommonUtil.createFloatBuffer(TransformUtil.createWindowOrthoMatrix(new Matrix4f()));
        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_ENABLE_BIT | GL11.GL_TRANSFORM_BIT | GL11.GL_POLYGON_BIT);
        GL11.glViewport(0, 0, ShaderCore.getScreenScaleWidth(), ShaderCore.getScreenScaleHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadMatrix(_MATRIX);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        GL11.glTranslatef(locX - _BAR_WIDTH - _SPACE, locY - fontHeight - _BAR_HEIGHT - 32.0f, 0.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        Pair<Long, Long> values;
        TextFieldEntity.TextData textData;
        long usageValue;
        for (var type : _TYPES) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPushMatrix();
            GL11.glScalef(_BAR_WIDTH, _BAR_HEIGHT, 1.0f);
            values = BUtil_InstanceDataMemoryPool.glDrawMemoryUsage(type);
            GL11.glPopMatrix();

            usageValue = values.two - values.one;
            textData = _TEXT.getTextDataList().get(0);
            textData.text = String.format("%-11s", type.name());
            System.arraycopy(_COLOR[values.two < 1 ? 0 : 1], 0, textData.byteState, 2, 4);
            _TEXT.getTextDataList().get(2).text = getNum(usageValue);
            _TEXT.getTextDataList().get(4).text = getNum(values.two);
            _TEXT.getTextDataList().get(6).text = getUsageP((double) usageValue / values.two);
            _TEXT.submitText();
            _TEXT.render(new Vector2f(0, fontHeight + _BAR_HEIGHT), 0, false, null);

            GL11.glTranslatef(0.0f, -(fontHeight + _BAR_HEIGHT + _SPACE), 0.0f);
        }

        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }
}
