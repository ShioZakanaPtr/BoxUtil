package org.boxutil.units.standard.entity;

import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.base.api.resource.TextSubmitFeedbackAPI;
import org.boxutil.define.LayeredEntityType;
import org.boxutil.manager.*;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.util.concurrent.SpinLock;
import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.base.BaseRenderData;
import org.boxutil.base.BaseShaderData;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.units.standard.attribute.FontMapData;
import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Put in to rendering manage is not required, can direct creating and use it.<p>
 * Recommend put in to the highest layer if you use rendering manage.<p>
 * Only draws color and emissive to framebuffer color attachment.<p>
 * Loading font info or submit string will both cost some time, usual should to preload it.<p>
 * Anchor at top-left of text field.<p>
 * <strong>Remember to register your font texture at <code>settings.json</code>, or load texture by {@link TextureManager} and set it later.</strong><p>
 * Use {@link TextFieldEntity#RESERVED_SYMBOL} for create empty character (will not display anything), if you will display a dynamic value or text such as flux value.
 */
public class TextFieldEntity extends BaseRenderData {
    public final static char RESERVED_SYMBOL = '\u0007';
    public final static char LINE_FEED_SYMBOL = '\n';
    public final static char NOT_FOUND_SYMBOL = '?';
    protected final static byte _DEFAULT_PAD = 0;
    protected final static byte _VBO_SHORT_COUNT = 13;
    protected final int _textFieldID;
    // for each char: vec4(uvBL, uvTR), vec4(x, y, topStyleUV, bottomStyleUV), float(handelIndex + (invert + channel) + italic + underline + strikeout)), vec4(color), vec4(size, edge)
    protected final int _textFieldVBO;
    protected final boolean _isValid;
    protected FontMapData[] fontMapList = null;
    protected final int[] textDataRefreshState = new int[5]; // lastCharSize, index, size, drawMode, shouldDrawCharSize
    protected final float[] textStateAfterSubmit = new float[]{0.0f, 0.0f}; // width, height
    protected List<TextData> _lastTextDataList = null;
    protected List<Vector4f> _lastTextDataState = null;
    protected TextSubmitFeedbackAPI _submitFeedback = null;
    protected final float[] state = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.2f}; // widthSpace, HeightSpace, fieldWidth, fieldHeight, vec4(globalColor), vec4(bloomColor), italicValue
    protected final boolean[] stateB = new boolean[]{true, true, false, true}; // isBlendBloomColor, refreshRenderingLengthWhenSubmit, synchronousSubmit, mappingMode
    protected Alignment alignment = Alignment.LEFT;

    protected int _StatePackageStack() {
        return 2;
    }

    /**
     * Default value {@link Alignment#LEFT}.
     */
    public enum Alignment {
        LEFT,
        MID,
        RIGHT
    }

    protected void initResourceLayout() {
        final int sizeH = BoxDatabase.HALF_FLOAT_SIZE * _VBO_SHORT_COUNT;
        GL30.glBindVertexArray(this.getFontFieldID());
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.getFontFieldVBO());
        GL20.glVertexAttribPointer(0, 4, GL30.GL_HALF_FLOAT, false, sizeH, 0);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1, 2, GL30.GL_HALF_FLOAT, false, sizeH, 4 * BoxDatabase.HALF_FLOAT_SIZE);
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(2, 2, GL11.GL_UNSIGNED_BYTE, false, sizeH, 6 * BoxDatabase.HALF_FLOAT_SIZE);
        GL20.glEnableVertexAttribArray(2);
        GL30.glVertexAttribIPointer(3, 1, GL11.GL_UNSIGNED_SHORT, sizeH, 7 * BoxDatabase.SHORT_SIZE);
        GL20.glEnableVertexAttribArray(3);
        GL20.glVertexAttribPointer(4, 4, GL11.GL_UNSIGNED_BYTE, true, sizeH, 8 * BoxDatabase.HALF_FLOAT_SIZE);
        GL20.glEnableVertexAttribArray(4);
        GL20.glVertexAttribPointer(5, 2, GL11.GL_UNSIGNED_BYTE, false, sizeH, 10 * BoxDatabase.HALF_FLOAT_SIZE);
        GL20.glEnableVertexAttribArray(5);
        GL20.glVertexAttribPointer(6, 2, GL11.GL_BYTE, false, sizeH, 11 * BoxDatabase.HALF_FLOAT_SIZE);
        GL20.glEnableVertexAttribArray(6);
        GL20.glVertexAttribPointer(7, 1, GL30.GL_HALF_FLOAT, false, sizeH, 12 * BoxDatabase.HALF_FLOAT_SIZE);
        GL20.glEnableVertexAttribArray(7);
        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public TextFieldEntity(boolean dynamicRefresh) {
        this._textFieldID = BoxConfigs.isVAOSupported() ? GL30.glGenVertexArrays() : 0;
        this._textFieldVBO = BoxConfigs.isVAOSupported() ? GL15.glGenBuffers() : 0;
        this._isValid = this.getFontFieldID() > 0 && this.getFontFieldVBO() > 0;
        this.textDataRefreshState[3] = dynamicRefresh ? GL15.GL_DYNAMIC_DRAW : GL15.GL_STATIC_DRAW;

        if (this.isValid()) this.initResourceLayout();
    }

    public TextFieldEntity() {
        this(false);
    }

    public TextFieldEntity(@NotNull FontMapData map, boolean dynamicRefresh) {
        this(dynamicRefresh);
        this.setFontMap(map);
    }

    public TextFieldEntity(@NotNull FontMapData map) {
        this(map, false);
    }

    public TextFieldEntity(@NotNull String fontPath, boolean dynamicRefresh) {
        this(FontDataManager.tryFont(fontPath), dynamicRefresh);
    }

    public TextFieldEntity(@NotNull String fontPath) {
        this(fontPath, false);
    }

    public int getFontFieldID() {
        return this._textFieldID;
    }

    public int getFontFieldVBO() {
        return this._textFieldVBO;
    }

    public boolean isValid() {
        return this._isValid;
    }

    public FontMapData[] getFontMapArray() {
        return this.fontMapList;
    }

    /**
     * Apply for the first map.
     * @param map all the font height (or font size) should be near or equal.
     */
    public void setFontMap(@NotNull FontMapData map) {
        this._sync_lock.lock();
        if (this.fontMapList == null) this.fontMapList = new FontMapData[4];
        this.fontMapList[0] = map;
        this._sync_lock.unlock();
    }

    /**
     * Can use at most four maps.
     * @param map all the font height (or font size) should be near or equal.
     */
    public void setFontMap(@NotNull FontMapData map, byte index) {
        if (index > 3) return;
        this._sync_lock.lock();
        if (this.fontMapList == null) this.fontMapList = new FontMapData[4];
        this.fontMapList[index] = map;
        this._sync_lock.unlock();
    }

    /**
     * Can use at most four maps.
     * @param fontPath all the font height (or font size) should be near or equal.
     */
    public void setFontMap(@NotNull String fontPath, byte index) {
        this.setFontMap(new FontMapData(fontPath), index);
    }

    protected void _deleteExc() {
        super._deleteExc();
        this.textDataRefreshState[0] = 0;
        if (this._lastTextDataList != null) this._lastTextDataList.clear();
        this._lastTextDataList = null;
        if (this._lastTextDataState != null) this._lastTextDataState.clear();
        this._lastTextDataState = null;
        this._submitFeedback = null;
        this.fontMapList = null;
        if (this.getFontFieldID() > 0) {
            GL30.glBindVertexArray(0);
            GL30.glDeleteVertexArrays(this.getFontFieldID());
        }
        if (this.getFontFieldVBO() > 0) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL15.glDeleteBuffers(this.getFontFieldVBO());
        }
    }

    public void glDraw() {
        if (this.isValidRenderingTextField()) {
            GL30.glBindVertexArray(this.getFontFieldID());
            GL11.glDrawArrays(GL11.GL_POINTS, 0, this.getValidCharLength());
        }
    }

    /**
     * You can directly call it when need display the text.<p>
     * And must call {@link TextFieldEntity#delete()} if unneeded.
     */
    public void directDraw() {
        final var program = ShaderCore.getTextProgram();
        if (!this.isValidRenderingTextField() || program == null || !program.isValid()) return;
        int fboNow = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL13.GL_MULTISAMPLE);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL40.glBlendFuncSeparatei(0, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GL40.glBlendEquationi(0, GL14.GL_FUNC_ADD);
        program.active();
        GL20.glUniformMatrix4(program.location[0], false, this.pickModelMatrixPackage_mat4());
        GL20.glUniform1f(program.location[5], this.getCurrentItalicFactor());
        GL20.glUniform4(program.location[6], this.pickColorPackage_vec4());
        GL20.glUniform1i(program.location[8], this.isBlendBloomColor() ? 1 : 0);
        for (int i = 0; i < this.fontMapList.length; i++) {
            if (this.fontMapList[i] != null && this.fontMapList[i].isValid()) program.bindTexture2D(i, this.fontMapList[i].getMapID());
        }
        BUtil_GLImpl.Operations.glEntityDraw(this);
        GL30.glBindVertexArray(0);
        program.close();
        GL11.glPopAttrib();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboNow);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    /**
     * For anyone wanna made custom text rendering plugin.
     *
     * @return send to {@link TextFieldEntity#directDrawDetachedEnd(int)}
     */
    public static int directDrawDetachedBegin() {
        final var program = ShaderCore.getTextProgram();
        int fboNow = 0;
        if (program != null && program.isValid()) {
            fboNow = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glDisable(GL11.GL_STENCIL_TEST);
            GL11.glDisable(GL13.GL_MULTISAMPLE);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL40.glBlendFuncSeparatei(0, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
            GL40.glBlendEquationi(0, GL14.GL_FUNC_ADD);
            program.active();
        }
        return fboNow;
    }

    public void directDrawDetachedProcess() {
        final var program = ShaderCore.getTextProgram();
        if (program != null && program.isValid() && this.isValidRenderingTextField()) {
            GL20.glUniformMatrix4(program.location[0], false, this.pickModelMatrixPackage_mat4());
            GL20.glUniform1f(program.location[5], this.getCurrentItalicFactor());
            GL20.glUniform4(program.location[6], this.pickColorPackage_vec4());
            GL20.glUniform1i(program.location[8], this.isBlendBloomColor() ? 1 : 0);
            for (int i = 0; i < this.fontMapList.length; i++) {
                if (this.fontMapList[i] != null && this.fontMapList[i].isValid()) program.bindTexture2D(i, this.fontMapList[i].getMapID());
            }
            BUtil_GLImpl.Operations.glEntityDraw(this);
        }
    }

    public static void directDrawDetachedEnd(int valueFromBeginReturns) {
        final var program = ShaderCore.getTextProgram();
        if (program != null && program.isValid()) {
            GL30.glBindVertexArray(0);
            program.close();
            GL11.glPopAttrib();
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, valueFromBeginReturns);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
        }
    }

    @Deprecated
    public void directDraw(boolean inCampaignOrCombat) {
        this.directDraw();
    }

    protected void _resetExc() {
        super._resetExc();
        this.state[0] = 0.0f;
        this.state[1] = 0.0f;
        this.state[2] = 0.0f;
        this.state[3] = 0.0f;
        this.state[4] = 1.0f;
        this.state[5] = 1.0f;
        this.state[6] = 1.0f;
        this.state[7] = 1.0f;
        this.state[8] = 1.0f;
        this.state[9] = 1.0f;
        this.state[10] = 1.0f;
        this.state[11] = 0.0f;
        this.state[12] = 0.2f;
        this.stateB[0] = true;
        this.alignment = Alignment.LEFT;
        this._submitFeedback = null;
    }

    /**
     * Excludes text data.
     */
    public void reset() {
        super.reset();
    }

    public void resetText() {
        this._sync_lock.lock();
        this.fontMapList = null;
        this.textStateAfterSubmit[0] = 0.0f;
        this.textStateAfterSubmit[1] = 0.0f;
        this.textDataRefreshState[0] = 0;
        this.textDataRefreshState[1] = 0;
        this.textDataRefreshState[2] = 0;
        this.textDataRefreshState[4] = 0;
        this._lastTextDataList = null;
        this._lastTextDataState = null;
        this.stateB[1] = true;
        this.stateB[2] = false;
        this.stateB[3] = true;
        this._sync_lock.unlock();
    }

    protected TextData createTextData(String text, float padding, Color color, boolean invert, boolean italic, boolean underline, boolean strikeout, int fontMapIndex) {
        TextData para = new TextData();
        para.text = text == null ? "null" : text;
        para.byteState[0] = (byte) Math.max(0, Math.min(fontMapIndex, 3));
        para.pad = padding;
        byte[] colorArray = CommonUtil.colorToByteArray(color);
        if (invert) para.byteState[1] = 0b1000;
        if (italic) para.byteState[1] |= 0b0100;
        if (underline) para.byteState[1] |= 0b0010;
        if (strikeout) para.byteState[1] |= 0b0001;
        para.byteState[2] = colorArray[0];
        para.byteState[3] = colorArray[1];
        para.byteState[4] = colorArray[2];
        para.byteState[5] = colorArray[3];
        return para;
    }

    /**
     * @param padding offset when line feed.
     */
    public TextData addText(String text, float padding, Color color, boolean invert, boolean italic, boolean underline, boolean strikeout, int fontMapIndex) {
        if (fontMapIndex < 0) return null;
        this._sync_lock.lock();
        if (this._lastTextDataList == null) this._lastTextDataList = new ArrayList<>();
        TextData para = createTextData(text, padding, color, invert, italic, underline, strikeout, fontMapIndex);
        this._lastTextDataList.add(para);
        this._sync_lock.unlock();
        return para;
    }

    /**
     * @param padding offset when line feed.
     */
    public TextData addText(String text, float padding, int fontMapIndex) {
        return this.addText(text, padding, Misc.getTextColor(), false, false, false, false, fontMapIndex);
    }

    public TextData addText(String text, int fontMapIndex) {
        return this.addText(text, _DEFAULT_PAD, Misc.getTextColor(), false, false, false, false, fontMapIndex);
    }

    /**
     * @param padding offset when line feed.
     *
     * @return the text data previously at the specified position.
     */
    public TextData replaceTextAtParagraph(@NotNull String text, float padding, Color color, boolean invert, boolean italic, boolean underline, boolean strikeout, int fontMapIndex, int paragraphIndex) {
        if (fontMapIndex < 0) return null;
        this._sync_lock.lock();
        if (this._lastTextDataList == null || paragraphIndex >= this._lastTextDataList.size()) {
            this._sync_lock.unlock();
            return null;
        }
        final var para = this._lastTextDataList.set(paragraphIndex, createTextData(text, padding, color, invert, italic, underline, strikeout, fontMapIndex));
        this._sync_lock.unlock();
        return para;
    }

    /**
     * @param padding offset when line feed.
     *
     * @return the text data previously at the specified position.
     */
    public TextData replaceTextAtParagraph(@NotNull String text, float padding, int fontMapIndex, int paragraphIndex) {
        return this.replaceTextAtParagraph(text, padding, Misc.getTextColor(), false, false, false, false, fontMapIndex, paragraphIndex);
    }

    /**
     * @return the text data previously at the specified position.
     */
    public TextData replaceTextAtParagraph(@NotNull String text, int fontMapIndex, int paragraphIndex) {
        return this.replaceTextAtParagraph(text, _DEFAULT_PAD, fontMapIndex, paragraphIndex);
    }

    public static class TextData {
        public String text = "";
        public float pad = 0.0f; // mapIndex, pad
        public final byte[] byteState = new byte[]{0, 0, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR}; // mapIndex, bit4(invert, italic, underline, strikeout), vec4(color)

        public TextData() {}

        public byte getMapIndex() {
            return this.byteState[0];
        }

        public float getPadding() {
            return this.pad;
        }

        public int pickFontStylePart() {
            return this.byteState[1] << 5 & 0b111100000 | this.byteState[0];
        }

        public byte[][] pickColorPackage_vec4() {
            return new byte[][]{new byte[]{this.byteState[3], this.byteState[5], 0, 0}, new byte[]{this.byteState[2], this.byteState[4], 0, 0}};
        }

        public int pickColorPackage_int32() {
            return CommonUtil.packingBytesToInt(this.byteState[5], this.byteState[4], this.byteState[3], this.byteState[2]);
        }
    }

    /**
     * Cost <code>26 Byte</code> of vRAM each character, that if it had <code>32768</code> characters will cost <code>832 KiB</code> of vRAM.<p>
     * If insufficient space exists on allocated buffer space, will lose data what the text data is not include in current submit.
     *
     * @return return {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when an empty text data list or refresh count is zero.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    public byte submitText() {
        this._sync_lock.lock();
        if (this._lastTextDataList == null || this._lastTextDataList.isEmpty()) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        if (!this.isValid()) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED_OTHER;
        }
        final int textDataRefreshIndex = this.textDataRefreshState[1];
        final int textDataRefreshCount = this.textDataRefreshState[2];
        final int textDataRefreshLimit = textDataRefreshIndex + textDataRefreshCount;
        if (textDataRefreshCount < 1) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        if (this._lastTextDataState == null) this._lastTextDataState = new ArrayList<>(textDataRefreshCount);

        List<ShortBuffer> tmpBufferList = new ArrayList<>(textDataRefreshCount);
        Vector4f refreshDataTmp;
        int charLength, lastCharLength;
        charLength = lastCharLength = 0;
        final int preDataIndex = textDataRefreshIndex - 1;
        float currentLine, currentStep, currentDrawStep, currCharFill, lastLineVisualWidth = 0.0f, maxLineVisualWidth = 0.0f;
        for (int i = 0; i < Math.min(textDataRefreshIndex, this._lastTextDataState.size()); i++) {
            lastCharLength += (int) this._lastTextDataState.get(i).getZ();
        }
        if (textDataRefreshIndex >= this._lastTextDataState.size()) {
            currentLine = currentStep = currCharFill = 0.0f;
        } else {
            if (preDataIndex > -1) {
                refreshDataTmp = this._lastTextDataState.get(preDataIndex);
                currentLine = refreshDataTmp.getX();
                currentStep = refreshDataTmp.getY();
                currCharFill = refreshDataTmp.getW();
            } else currentLine = currentStep = currCharFill = 0.0f;
        }
        char[] charArray;
        final byte offset = (byte) (this.getAlignment() == Alignment.MID ? 2 : (this.getAlignment() == Alignment.RIGHT ? 1 : 0));
        final boolean notDefaultAlignment = offset > 0, haveSubmitFeedback = this._submitFeedback != null;
        int charLimit, currentTextSize, lineValidChar = 0;
        char lastCharacter;
        boolean putWidthDataCheck = false, hasCharSubmit = false;
        ShortBuffer tmpBuffer;
        Pair<List<Float>, Float> lastLineAlignmentData = new Pair<List<Float>, Float>(new ArrayList<Float>(32), 0.0f);
        List<Float> lineStepData = new ArrayList<>(32);
        List<Pair<List<Float>, Float>> currLineAlignmentData = new ArrayList<>(8); // lineCharCount, currWidth
        TextData textData;
        FontMapData.FontData fontData, notFoundSymbol;
        HashMap<Character, Byte> kerningMap;
        Byte kerningCharPackage;
        for (int i = textDataRefreshIndex; i < textDataRefreshLimit; i++) {
            textData = this._lastTextDataList.get(i);
            if (textData == null) continue;
            final byte mapIndex = textData.getMapIndex();
            FontMapData currentFontMapData = this.fontMapList[mapIndex];
            String text = textData.text;
            final byte[][] textDataArray = textData.pickColorPackage_vec4();
            final int style = textData.pickFontStylePart();
            final byte fontLineHeight = currentFontMapData.getLineHeight(), fontBaseHeight = currentFontMapData.getLineBase();
            lastCharacter = 0;
            char character;
            byte kerningValue;
            notFoundSymbol = currentFontMapData.getFont(NOT_FOUND_SYMBOL);
            boolean isLineFeed, currNotFound, haveNotFoundSymbol = notFoundSymbol != null;
            if (Math.abs(currentLine - fontLineHeight) > this.getTextFieldHeight()) {
                if (haveSubmitFeedback) this._submitFeedback.processBreak(mapIndex, i, true, '\0', 0, currentStep, currentLine);
                break;
            }
            charArray = text.toCharArray();
            currentTextSize = charArray.length;
            charLimit = currentTextSize - 1;
            tmpBuffer = BufferUtils.createShortBuffer(currentTextSize * _VBO_SHORT_COUNT);
            charLength += currentTextSize;
            currentLine -= textData.getPadding();

            for (int j = 0; j < charArray.length; j++) {
                character = charArray[j];
                isLineFeed = character == LINE_FEED_SYMBOL;
                fontData = currentFontMapData.getFont(character);
                currNotFound = fontData == null;
                if (currNotFound && haveNotFoundSymbol) {
                    currNotFound = false;
                    character = NOT_FOUND_SYMBOL;
                    fontData = notFoundSymbol;
                }

                if (currNotFound || isLineFeed) {
                    if (haveSubmitFeedback) this._submitFeedback.processText(mapIndex, i, character, !currNotFound, isLineFeed, j, currentStep, currentLine, textData.getPadding(), textData.byteState[1], textData.byteState[2], textData.byteState[3], textData.byteState[4], textData.byteState[5]);
                    if (isLineFeed) {
                        currentLine -= fontLineHeight + this.getFontHeightSpace();
                        currentStep = 0.0f;
                        currCharFill = 0.0f;
                    }
                    charLength--;
                    currentTextSize--;
                    lastCharacter = character;
                    if (j >= charLimit) {
                        refreshDataTmp = new Vector4f(currentLine, currentStep, currentTextSize, currCharFill);
                        if (i >= this._lastTextDataState.size()) this._lastTextDataState.add(refreshDataTmp); else this._lastTextDataState.set(i, refreshDataTmp);
                    }
                    if (notDefaultAlignment && lineValidChar > 0) {
                        putWidthDataCheck = false;
                        currLineAlignmentData.add(new Pair<>(lineStepData, lastLineVisualWidth));
                        lineStepData = new ArrayList<>(32);
                        lineValidChar = 0;
                    }
                    continue;
                }

                if (currentFontMapData.haveKerning()) {
                    kerningMap = currentFontMapData.getKerningMap(lastCharacter);
                    if (kerningMap != null) {
                        kerningCharPackage = kerningMap.get(character);
                        if (kerningCharPackage != null) {
                            kerningValue = kerningCharPackage;
                            currentStep += kerningValue;
                            currCharFill += Math.max(kerningValue, 0);
                        }
                    }
                }

                currentDrawStep = currentStep + fontData.getXOffset();
                isLineFeed = currentDrawStep + fontData.getSize()[0] > this.getTextFieldWidth();
                if (isLineFeed) {
                    currentStep = 0.0f;
                    currentDrawStep = fontData.getXOffset();
                    currentLine -= fontLineHeight + this.getFontHeightSpace();
                    currCharFill = 0.0f;

                    putWidthDataCheck = false;
                    currLineAlignmentData.add(new Pair<>(lineStepData, lastLineVisualWidth));
                    lineStepData = new ArrayList<>(32);
                    lineValidChar = 0;
                }
                lastLineVisualWidth = currentDrawStep + fontData.getSize()[0];
                maxLineVisualWidth = Math.max(maxLineVisualWidth, lastLineVisualWidth);

                if (Math.abs(currentLine - fontLineHeight) > this.getTextFieldHeight()) {
                    if (haveSubmitFeedback) this._submitFeedback.processBreak(mapIndex, i, false, character, j, currentStep, currentLine);
                    break;
                }
                currCharFill = Math.max(currCharFill + fontData.getXOffset(), 0.0f);
                if (haveSubmitFeedback) this._submitFeedback.processText(mapIndex, i, character, true, isLineFeed, j, currentStep, currentLine, textData.getPadding(), textData.byteState[1], textData.byteState[2], textData.byteState[3], textData.byteState[4], textData.byteState[5]);

                CommonUtil.putFloat16(tmpBuffer, fontData.getUVs());
                CommonUtil.putFloat16(tmpBuffer, currentDrawStep);
                CommonUtil.putFloat16(tmpBuffer, currentLine - fontData.getYOffset());
                CommonUtil.putPackingBytes(tmpBuffer, fontBaseHeight, fontLineHeight);
                tmpBuffer.put((short) (style | fontData.getChannel()));
                textDataArray[1][2] = fontData.getSize()[0];
                textDataArray[0][2] = fontData.getSize()[1];
                textDataArray[1][3] = fontData.getYOffset();
                textDataArray[0][3] = (byte) Math.max(fontLineHeight - fontData.getYOffset() - fontData.getSize()[1], 0);
                CommonUtil.putPackingBytes(tmpBuffer, textDataArray[0], textDataArray[1]);
                CommonUtil.putFloat16(tmpBuffer, currCharFill);

                if (notDefaultAlignment) {
                    if (!putWidthDataCheck) putWidthDataCheck = true;
                    lineStepData.add(currentDrawStep);
                    lineValidChar++;
                }
                lastCharacter = character;
                currentStep += fontData.getXAdvance() + this.getFontWidthSpace();
                currCharFill = fontData.getXAdvance() - fontData.getSize()[0] + this.getFontWidthSpace() + Math.max(-fontData.getXOffset(), 0.0f);
                if (character == '\t') {
                    float spacingM3 = this.getFontWidthSpace() * 3.0f;
                    currentStep += spacingM3;
                    currCharFill += spacingM3;
                }
                if (!hasCharSubmit) {
                    hasCharSubmit = true;
                    this.textStateAfterSubmit[0] = 0.0f;
                }
            }
            if (notDefaultAlignment) {
                lastLineAlignmentData.one = lineStepData;
                lastLineAlignmentData.two = lastLineVisualWidth;
            }
            tmpBuffer.position(0);
            tmpBuffer.limit(currentTextSize * _VBO_SHORT_COUNT);
            tmpBufferList.add(tmpBuffer);
            if (i >= this._lastTextDataState.size()) {
                this._lastTextDataState.add(new Vector4f(currentLine, currentStep, currentTextSize, currCharFill));
            } else {
                this._lastTextDataState.set(i, new Vector4f(currentLine, currentStep, currentTextSize, currCharFill));
            }
            if (hasCharSubmit) this.textStateAfterSubmit[0] = Math.max(this.textStateAfterSubmit[0], maxLineVisualWidth);
            this.textStateAfterSubmit[1] = Math.abs(currentLine - fontLineHeight);
        }
        if (putWidthDataCheck && !lastLineAlignmentData.one.isEmpty()) currLineAlignmentData.add(lastLineAlignmentData);

        if (charLength < 1) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.getFontFieldVBO());
        final int newCharLength = charLength + lastCharLength;
        if (newCharLength > this.textDataRefreshState[0]) {
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, ((long) newCharLength * _VBO_SHORT_COUNT) << 1, this.textDataRefreshState[3]);
            this.textDataRefreshState[0] = newCharLength;
        }

        final long subBufferIndex = ((long) lastCharLength * _VBO_SHORT_COUNT) << 1, subBufferSize = ((long) charLength * _VBO_SHORT_COUNT) << 1;
        final boolean useMapping = this.isMappingModeSubmitData();
        ByteBuffer buffer;
        if (useMapping) {
            final int _access = this.isSynchronousSubmit() ? GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_INVALIDATE_RANGE_BIT : GL30.GL_MAP_WRITE_BIT | GL30.GL_MAP_UNSYNCHRONIZED_BIT | GL30.GL_MAP_INVALIDATE_RANGE_BIT;
            buffer = GL30.glMapBufferRange(GL15.GL_ARRAY_BUFFER, subBufferIndex, subBufferSize, _access, null);
            if (buffer == null || buffer.capacity() < 1) {
                GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
                if (this.isRefreshRenderingLengthWhenSubmit()) this.textDataRefreshState[4] = 0;
                this._sync_lock.unlock();
                return BoxEnum.STATE_FAILED_OTHER;
            }
        } else {
            buffer = BufferUtils.createByteBuffer((int) subBufferSize);
        }
        if (this.isRefreshRenderingLengthWhenSubmit()) this.textDataRefreshState[4] = newCharLength;

        ShortBuffer vboBuffer = buffer.asShortBuffer();
        int tmpBufferAddIndex = 0;
        for (ShortBuffer shortBuffer : tmpBufferList) {
            vboBuffer.put(shortBuffer);
            tmpBufferAddIndex += shortBuffer.limit();
            vboBuffer.position(tmpBufferAddIndex);
        }
        if (notDefaultAlignment) {
            int lineBufferIndex = 0;
            float currOffsetStep;
            for (Pair<List<Float>, Float> line : currLineAlignmentData) {
                currOffsetStep = Math.max(this.getTextFieldWidth() - line.two, 0.0f) / offset;
                for (float step : line.one) {
                    vboBuffer.put(lineBufferIndex * _VBO_SHORT_COUNT + 4, CommonUtil.float16ToShort(step + currOffsetStep));
                    lineBufferIndex++;
                }
            }
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());
        if (useMapping) GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
        else GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, subBufferIndex, buffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        this._sync_lock.unlock();
        return BoxEnum.STATE_SUCCESS;
    }

    /**
     * Optional.<p>
     * Just <code>malloc()</code> without any submit call.
     *
     * @param charNum must be positive integer.
     *
     * @return return {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when parameter error.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    public byte mallocTextData(int charNum) {
        this._sync_lock.lock();
        if (!this.isValid()) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED_OTHER;
        }
        if (charNum < 1) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.getFontFieldVBO());
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (long) charNum * _VBO_SHORT_COUNT << 1, this.textDataRefreshState[3]);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        this.textDataRefreshState[0] = charNum;
        this.textDataRefreshState[4] = 0;
        this._sync_lock.unlock();
        return BoxEnum.STATE_SUCCESS;
    }

    public TextSubmitFeedbackAPI getSubmitFeedback() {
        return this._submitFeedback;
    }

    public void setSubmitFeedback(TextSubmitFeedbackAPI plugin) {
        this._sync_lock.lock();
        this._submitFeedback = plugin;
        this._sync_lock.unlock();
    }

    public boolean isRefreshRenderingLengthWhenSubmit() {
        return this.stateB[1];
    }

    public void setRefreshRenderingLengthWhenSubmit(boolean refresh) {
        this._sync_lock.lock();
        this.stateB[1] = refresh;
        this._sync_lock.unlock();
    }

    /**
     * Default value is <code>false</code>.
     */
    public boolean isSynchronousSubmit() {
        return this.stateB[2];
    }

    /**
     * <strong>High performance impact when is synchronized.</strong>
     */
    public void setSynchronousSubmit(boolean sync) {
        this._sync_lock.lock();
        this.stateB[2] = sync;
        this._sync_lock.unlock();
    }

    /**
     * Default value is <code>true</code>.
     */
    public boolean isMappingModeSubmitData() {
        return this.stateB[3];
    }

    /**
     * For some very slight data, use <code>glBufferSubData()</code> may faster.<p>
     * Besides, some devices(some ARM SoC) may slower with <code>glMapBufferRange()</code>, decided by the drive how implements it.
     *
     * @param mappingMode to controls whether submit use <code>glMapBufferRange()</code>, else use <code>glBufferSubData()</code>.
     */
    public void setMappingModeSubmitData(boolean mappingMode) {
        this._sync_lock.lock();
        this.stateB[3] = mappingMode;
        this._sync_lock.unlock();
    }

    public int getTextDataRefreshIndex() {
        return this.textDataRefreshState[1];
    }

    /**
     * @param index Will refresh text data start from the index.
     */
    public void setTextDataRefreshIndex(int index) {
        this._sync_lock.lock();
        if (this._lastTextDataList == null) {
            this._sync_lock.unlock();
            return;
        }
        this.textDataRefreshState[1] = Math.min(Math.max(index, 0), Math.max((short) this._lastTextDataList.size() - 1, 0));
        this._sync_lock.unlock();
    }

    /**
     * @param size Will refresh text data count.
     */
    public void setTextDataRefreshSize(int size) {
        this._sync_lock.lock();
        if (this._lastTextDataList == null) {
            this._sync_lock.unlock();
            return;
        }
        final int total = this._lastTextDataList.size();
        this.textDataRefreshState[2] = this.textDataRefreshState[1] + size > total ? total - this.textDataRefreshState[1] : Math.max(size, 0);
        this._sync_lock.unlock();
    }

    public void setTextDataRefreshAllFromCurrentIndex() {
        this._sync_lock.lock();
        if (this._lastTextDataList == null) {
            this._sync_lock.unlock();
            return;
        }
        this.textDataRefreshState[2] = this._lastTextDataList.size() - this.textDataRefreshState[1];
        this._sync_lock.unlock();
    }

    public List<TextData> getTextDataList() {
        return this._lastTextDataList;
    }

    public int getAllocatedValidCharLength() {
        return this.textDataRefreshState[0];
    }

    public boolean haveAllocatedValidCharLength() {
        return this.textDataRefreshState[0] > 0;
    }

    public int getValidCharLength() {
        return this.textDataRefreshState[4];
    }

    public boolean isValidRenderingTextField() {
        return this.textDataRefreshState[4] > 0;
    }

    public void setShouldRenderingCharCount(int charNum) {
        this.textDataRefreshState[4] = Math.max(Math.min(this.textDataRefreshState[0], charNum), 0);
    }

    public Alignment getAlignment() {
        return this.alignment;
    }

    public void setAlignment(Alignment alignment) {
        this._sync_lock.lock();
        this.alignment = alignment;
        this._sync_lock.unlock();
    }

    public float getFontWidthSpace() {
        return this.state[0];
    }

    public float getFontHeightSpace() {
        return this.state[1];
    }

    public float getTextFieldWidth() {
        return this.state[2];
    }

    public float getTextFieldHeight() {
        return this.state[3];
    }

    /**
     * @param space interval to previous font.
     */
    public void setFontWidthSpace(float space) {
        this.state[0] = space;
    }

    /**
     * @param space line interval to previous line of same(only) text data.
     */
    public void setFontHeightSpace(float space) {
        this.state[1] = space;
    }

    public void setFontSpace(float width, float height) {
        this.setFontWidthSpace(width);
        this.setFontHeightSpace(height);
    }

    public void setFieldWidth(float width) {
        this.state[2] = width;
    }

    public void setFieldHeight(float height) {
        this.state[3] = height;
    }

    public void setFieldSize(float width, float height) {
        this.setFieldWidth(width);
        this.setFieldHeight(height);
    }

    /**
     * After called {@link TextFieldEntity#submitText()}.<p>
     * But only counts for last submit call, not for the entity.
     */
    public float getCurrentVisualWidth() {
        return this.textStateAfterSubmit[0];
    }

    /**
     * After called {@link TextFieldEntity#submitText()}.
     */
    public float getCurrentVisualHeight() {
        return this.textStateAfterSubmit[1];
    }

    public float getCurrentItalicFactor() {
        return this.state[12];
    }

    /**
     * @param angle range at <strong>[-90, 90]</strong>.
     */
    public void setItalicFactor(float angle) {
        this.state[12] = (float) Math.sin(Math.toRadians(angle));
    }

    /**
     * @param value value of sin, and angle range at <strong>[-90, 90]</strong>.
     */
    public void setItalicFactorDirect(float value) {
        this.state[12] = value;
    }

    public void setDefaultItalicFactor() {
        this.state[12] = 0.2f;
    }

    public float[] getGlobalColorArray() {
        return new float[]{this.state[4], this.state[5], this.state[6], this.state[7]};
    }

    public Color getGlobalColorC() {
        return CommonUtil.toCommonColor(this.getGlobalColor());
    }

    public Vector4f getGlobalColor() {
        return new Vector4f(this.state[4], this.state[5], this.state[6], this.state[7]);
    }

    public float getGlobalColorAlpha() {
        return this.state[7];
    }

    public int getGlobalColorAlphaI() {
        return Math.max(Math.min(Math.round(this.state[7] * 255.0f), 255), 0);
    }

    public void setGlobalColor(float r, float g, float b, float a) {
        this.state[4] = r;
        this.state[5] = g;
        this.state[6] = b;
        this.state[7] = a;
    }

    public void setGlobalColor(Vector4f color) {
        this.setGlobalColor(color.x, color.y, color.z, color.w);
    }

    public void setGlobalColor(Color color) {
        this.setGlobalColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }

    public float[] getBloomColorArray() {
        return new float[]{this.state[8], this.state[9], this.state[10], this.state[11]};
    }

    public Color getBloomColorC() {
        return CommonUtil.toCommonColor(this.getBloomColor());
    }

    public Vector4f getBloomColor() {
        return new Vector4f(this.state[8], this.state[9], this.state[10], this.state[11]);
    }

    public float getBloomStrength() {
        return this.state[11];
    }

    /**
     * @param a bloom strength
     */
    public void setBloomColor(float r, float g, float b, float a) {
        this.state[8] = r;
        this.state[9] = g;
        this.state[10] = b;
        this.state[11] = a;
    }

    public void setBloomColor(Vector4f color) {
        this.setBloomColor(color.x, color.y, color.z, color.w);
    }

    public void setBloomColor(Color color) {
        this.setBloomColor(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }

    public void setBloomColorStrength(float strength) {
        this.state[11] = strength;
    }

    public boolean isBlendBloomColor() {
        return this.stateB[0];
    }

    /**
     * @param blend the bloom color is bloom color blend in font color when <code>true</code>, use bloom color when <code>false</code>.
     */
    public void setBlendBloomColor(boolean blend) {
        this.stateB[0] = blend;
    }

    public FloatBuffer pickColorPackage_vec4() {
        this._statePackageBuffer.put(0, this.state, 4, 8);
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
        return this._statePackageBuffer;
    }

    public Object entityType() {
        return LayeredEntityType.TEXT;
    }
}
