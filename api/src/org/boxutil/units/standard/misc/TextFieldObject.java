package org.boxutil.units.standard.misc;

import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.base.api.resource.TextSubmitFeedbackAPI;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.FontDataManager;
import org.boxutil.units.standard.attribute.FontMapData;
import org.boxutil.units.standard.entity.TextFieldEntity;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.concurrent.SpinLock;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Easy way for rendering text anywhere.<p>
 * The compatibility simple rendering object in lower version, a substitute for {@link org.boxutil.units.standard.entity.TextFieldEntity}.<p>
 * Vanilla supported.
 */
public class TextFieldObject {
    protected final static byte _DEFAULT_PAD = 0;
    protected FontMapData fontMap = null;
    protected final int[] textDataRefreshState = new int[4]; // lastCharSize, index, size, shouldDrawCharSize
    protected final float[] textStateAfterSubmit = new float[]{0.0f, 0.0f}; // width, height
    protected final float[] state = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.2f}; // widthSpace, HeightSpace, fieldWidth, fieldHeight, italicValue
    protected final SpinLock _sync_lock = new SpinLock();
    protected boolean isRefreshRenderingLengthWhenSubmit = true;
    protected FloatBuffer vertexBuffer = null;
    protected FloatBuffer uvBuffer = null;
    protected ByteBuffer colorBuffer = null;
    protected List<TextFieldEntity.TextData> _lastTextDataList = null;
    protected List<Vector3f> _lastTextDataState = null;
    protected TextSubmitFeedbackAPI _submitFeedback = null;
    protected TextFieldEntity.Alignment alignment = TextFieldEntity.Alignment.LEFT;

    public TextFieldObject() {}

    public TextFieldObject(@NotNull FontMapData map) {
        this.fontMap = map;
    }

    public TextFieldObject(@NotNull String fontPath) {
        this(FontDataManager.tryFont(fontPath));
    }

    public FontMapData getFontMap() {
        return this.fontMap;
    }

    public void setFontMap(@NotNull FontMapData map) {
        this._sync_lock.lock();
        this.fontMap = map;
        this._sync_lock.unlock();
    }

    public void setFontMap(@NotNull String fontPath) {
        this.setFontMap(new FontMapData(fontPath));
    }

    public void reset() {
        this.state[0] = 0.0f;
        this.state[1] = 0.0f;
        this.state[2] = 0.0f;
        this.state[3] = 0.0f;
        this.state[4] = 0.2f;
        this.alignment = TextFieldEntity.Alignment.LEFT;
        this._submitFeedback = null;
    }

    public void resetText() {
        this.textStateAfterSubmit[0] = 0.0f;
        this.textStateAfterSubmit[1] = 0.0f;
        this.textDataRefreshState[0] = 0;
        this.textDataRefreshState[1] = 0;
        this.textDataRefreshState[2] = 0;
        this.textDataRefreshState[3] = 0;
        this._lastTextDataList = null;
        this._lastTextDataState = null;
        this.isRefreshRenderingLengthWhenSubmit = true;
    }

    /**
     * General rendering method.<p>
     * Anchor at top-left.
     */
    public void render(Vector2f location, float facing, boolean isAdditiveBlend, @Nullable Color globalColor) {
        if (this.isValidRenderingTextField()) {
            final boolean useVertexColor = globalColor == null;
            GL11.glPushMatrix();
            GL11.glTranslatef(location.x, location.y, 0.0f);
            if (facing != 0.0f) GL11.glRotatef(facing, 0.0f, 0.0f, 1.0f);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, isAdditiveBlend ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
            if (!useVertexColor) Misc.setColor(globalColor);
            this.glDraw(useVertexColor);
            GL11.glPopMatrix();
        }
    }

    public void glDraw(boolean useVertexColor) {
        if (this.isValidRenderingTextField()) {
            GL11.glPushClientAttrib(GL11.GL_CLIENT_VERTEX_ARRAY_BIT);
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            if (useVertexColor) GL11.glEnableClientState(GL11.GL_COLOR_ARRAY); else GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontMap.getMapID());
            this._sync_lock.lock();
            this.vertexBuffer.position(0);
            this.vertexBuffer.limit(this.vertexBuffer.capacity());
            this.uvBuffer.position(0);
            this.uvBuffer.limit(this.uvBuffer.capacity());
            GL11.glVertexPointer(2, 0, this.vertexBuffer);
            GL11.glTexCoordPointer(2, 0, this.uvBuffer);
            if (useVertexColor) {
                this.colorBuffer.position(0);
                this.colorBuffer.limit(this.colorBuffer.capacity());
                GL11.glColorPointer(4, true, 0, this.colorBuffer);
            }
            GL11.glDrawArrays(GL11.GL_QUADS, 0, this.getValidCharLength() << 2);
            this._sync_lock.unlock();
            GL11.glPopClientAttrib();
        }
    }

    protected TextFieldEntity.TextData createTextData(String text, float padding, Color color, boolean italic) {
        TextFieldEntity.TextData para = new TextFieldEntity.TextData();
        para.text = text == null ? "null" : text;
        para.pad = padding;
        byte[] colorArray = CommonUtil.colorToByteArray(color);
        if (italic) para.byteState[1] |= 0b0100;
        para.byteState[2] = colorArray[0];
        para.byteState[3] = colorArray[1];
        para.byteState[4] = colorArray[2];
        para.byteState[5] = colorArray[3];
        return para;
    }

    public TextFieldEntity.TextData addText(String text, float padding, Color color, boolean italic) {
        this._sync_lock.lock();
        if (this._lastTextDataList == null) this._lastTextDataList = new ArrayList<>();
        TextFieldEntity.TextData para = createTextData(text, padding, color, italic);
        this._lastTextDataList.add(para);
        this._sync_lock.unlock();
        return para;
    }

    public TextFieldEntity.TextData addText(String text, float padding, Color color) {
        return this.addText(text, padding, color, false);
    }

    public TextFieldEntity.TextData addText(String text, float padding) {
        return this.addText(text, padding, Misc.getTextColor());
    }

    public TextFieldEntity.TextData addText(String text) {
        return this.addText(text, _DEFAULT_PAD, Misc.getTextColor());
    }

    public TextFieldEntity.TextData addText(String text, boolean italic) {
        return this.addText(text, _DEFAULT_PAD, Misc.getTextColor(), italic);
    }

    /**
     * @param padding offset when line feed.
     *
     * @return the text data previously at the specified position.
     */
    public TextFieldEntity.TextData replaceTextAtParagraph(@NotNull String text, float padding, Color color, boolean italic, int paragraphIndex) {
        this._sync_lock.lock();
        if (this._lastTextDataList == null || paragraphIndex >= this._lastTextDataList.size()) {
            this._sync_lock.unlock();
            return null;
        }
        final var para = this._lastTextDataList.set(paragraphIndex, createTextData(text, padding, color, italic));
        this._sync_lock.unlock();
        return para;
    }

    /**
     * @param padding offset when line feed.
     *
     * @return the text data previously at the specified position.
     */
    public TextFieldEntity.TextData replaceTextAtParagraph(@NotNull String text, float padding, int paragraphIndex) {
        return replaceTextAtParagraph(text, padding, Misc.getTextColor(), false, paragraphIndex);
    }

    /**
     * @return the text data previously at the specified position.
     */
    public TextFieldEntity.TextData replaceTextAtParagraph(@NotNull String text, int paragraphIndex) {
        return replaceTextAtParagraph(text, _DEFAULT_PAD, paragraphIndex);
    }

    protected void reallocateBuffer(int num) {
        this.vertexBuffer = BufferUtils.createFloatBuffer(num << 3);
        this.uvBuffer = BufferUtils.createFloatBuffer(num << 3);
        this.colorBuffer = BufferUtils.createByteBuffer(num << 4);
    }

    /**
     * If insufficient space exists on allocated buffer space, will lose data what the text data is not include in current submit.
     *
     * @return return {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when an empty text data list or refresh count is zero.<p> return {@link BoxEnum#STATE_FAILED_OTHER} when happened another error.
     */
    public byte submitText() {
        this._sync_lock.lock();
        if (this.fontMap == null) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED_OTHER;
        }
        if (this._lastTextDataList == null || this._lastTextDataList.isEmpty()) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        final int textDataRefreshIndex = this.textDataRefreshState[1];
        final int textDataRefreshCount = this.textDataRefreshState[2];
        final int textDataRefreshLimit = textDataRefreshIndex + textDataRefreshCount;
        if (textDataRefreshCount < 1) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }
        if (this._lastTextDataState == null) this._lastTextDataState = new ArrayList<>(textDataRefreshCount);

        Vector3f refreshDataTmp;
        int charLength, lastCharLength;
        charLength = lastCharLength = 0;
        final int preDataIndex = textDataRefreshIndex - 1;
        float currentLine, currentStep, currentDrawStep, lastLineVisualWidth = 0.0f, maxLineVisualWidth = 0.0f;
        for (int i = 0; i < Math.min(textDataRefreshIndex, this._lastTextDataState.size()); i++) {
            lastCharLength += (int) this._lastTextDataState.get(i).getZ();
        }
        if (textDataRefreshIndex >= this._lastTextDataState.size()) {
            currentLine = currentStep = 0.0f;
        } else {
            if (preDataIndex > -1) {
                refreshDataTmp = this._lastTextDataState.get(preDataIndex);
                currentLine = refreshDataTmp.getX();
                currentStep = refreshDataTmp.getY();
            } else currentLine = currentStep = 0.0f;
        }
        char[] charArray;
        final byte offset = (byte) (this.getAlignment() == TextFieldEntity.Alignment.MID ? 2 : (this.getAlignment() == TextFieldEntity.Alignment.RIGHT ? 1 : 0));
        final boolean notDefaultAlignment = offset > 0, haveSubmitFeedback = this._submitFeedback != null;
        int charLimit, currentTextSize, lineValidChar = 0;
        char lastCharacter;
        boolean putWidthDataCheck = false, hasCharSubmit = false;
        List<FloatBuffer> tmpVertexList = new ArrayList<>(textDataRefreshCount);
        List<FloatBuffer> tmpUVList = new ArrayList<>(textDataRefreshCount);
        List<FloatBuffer> tmpColorList = new ArrayList<>(textDataRefreshCount);
        FloatBuffer tmpVertexBuffer, tmpUVBuffer, tmpColorBuffer;
        Pair<Integer, Float> lastLineAlignmentData = new Pair<Integer, Float>(0, 0.0f);
        List<Pair<Integer, Float>> currLineAlignmentData = new ArrayList<>(8); // lineCharCount, currWidth
        TextFieldEntity.TextData textData;
        FontMapData.FontData fontData, notFoundSymbol;
        HashMap<Character, Byte> kerningMap;
        Byte kerningCharPackage;
        final float[] currCharUV = new float[4];
        byte[] currCharSize;
        float currYPos, currRightPos, currBottomPos, bottomItalicOffset, topItalicOffset;
        for (int i = textDataRefreshIndex; i < textDataRefreshLimit; i++) {
            textData = this._lastTextDataList.get(i);
            if (textData == null) continue;
            String text = textData.text;
            final byte fontLineHeight = this.fontMap.getLineHeight(), fontBaseHeight = this.fontMap.getLineBase();
            lastCharacter = 0;
            char character;
            notFoundSymbol = this.fontMap.getFont(TextFieldEntity.NOT_FOUND_SYMBOL);
            boolean isLineFeed, currNotFound, haveNotFoundSymbol = notFoundSymbol != null;
            if (Math.abs(currentLine - fontLineHeight) > this.getTextFieldHeight()) {
                if (haveSubmitFeedback) this._submitFeedback.processBreak(BoxEnum.ZERO, i, true, '\0', 0, currentStep, currentLine);
                break;
            }
            charArray = text.toCharArray();
            currentTextSize = charArray.length;
            charLimit = currentTextSize - 1;
            tmpVertexBuffer = BufferUtils.createFloatBuffer(currentTextSize << 3);
            tmpUVBuffer = BufferUtils.createFloatBuffer(currentTextSize << 3);
            tmpColorBuffer = BufferUtils.createFloatBuffer(currentTextSize << 2);
            charLength += currentTextSize;
            currentLine -= textData.getPadding();
            final boolean isItalic = (textData.byteState[1] & 0b0100) > 0;
            final float colorBit = Float.intBitsToFloat(textData.pickColorPackage_int32());

            for (int j = 0; j < charArray.length; j++) {
                character = charArray[j];
                isLineFeed = character == TextFieldEntity.LINE_FEED_SYMBOL;
                fontData = this.fontMap.getFont(character);
                currNotFound = fontData == null;
                if (currNotFound && haveNotFoundSymbol) {
                    currNotFound = false;
                    character = TextFieldEntity.NOT_FOUND_SYMBOL;
                    fontData = notFoundSymbol;
                }

                if (currNotFound || isLineFeed) {
                    if (haveSubmitFeedback) this._submitFeedback.processText(BoxEnum.ZERO, i, character, !currNotFound, isLineFeed, j, currentStep, currentLine, textData.getPadding(), textData.byteState[1], textData.byteState[2], textData.byteState[3], textData.byteState[4], textData.byteState[5]);
                    if (isLineFeed) {
                        currentLine -= fontLineHeight + this.getFontHeightSpace();
                        currentStep = 0.0f;
                    }
                    charLength--;
                    currentTextSize--;
                    lastCharacter = character;
                    if (j >= charLimit) {
                        refreshDataTmp = new Vector3f(currentLine, currentStep, currentTextSize);
                        if (i >= this._lastTextDataState.size()) this._lastTextDataState.add(refreshDataTmp); else this._lastTextDataState.set(i, refreshDataTmp);
                    }
                    if (notDefaultAlignment && lineValidChar > 0) {
                        putWidthDataCheck = false;
                        currLineAlignmentData.add(new Pair<>(lineValidChar, lastLineVisualWidth));
                        lineValidChar = 0;
                    }
                    continue;
                }

                if (this.getFontMap().haveKerning()) {
                    kerningMap = this.getFontMap().getKerningMap(lastCharacter);
                    if (kerningMap != null) {
                        kerningCharPackage = kerningMap.get(character);
                        if (kerningCharPackage != null) currentStep += kerningCharPackage;
                    }
                }

                currentDrawStep = currentStep + fontData.getXOffset();
                isLineFeed = currentDrawStep + fontData.getSize()[0] > this.getTextFieldWidth();
                if (isLineFeed) {
                    currentStep = 0.0f;
                    currentDrawStep = fontData.getXOffset();
                    currentLine -= fontLineHeight + this.getFontHeightSpace();

                    putWidthDataCheck = false;
                    currLineAlignmentData.add(new Pair<>(lineValidChar, lastLineVisualWidth));
                    lineValidChar = 0;
                }
                lastLineVisualWidth = currentDrawStep + fontData.getSize()[0];
                maxLineVisualWidth = Math.max(maxLineVisualWidth, lastLineVisualWidth);

                if (Math.abs(currentLine - fontLineHeight) > this.getTextFieldHeight()) {
                    if (haveSubmitFeedback) this._submitFeedback.processBreak(BoxEnum.ZERO, i, false, character, j, currentStep, currentLine);
                    break;
                }
                if (haveSubmitFeedback) this._submitFeedback.processText(BoxEnum.ZERO, i, character, true, isLineFeed, j, currentStep, currentLine, textData.getPadding(), textData.byteState[1], textData.byteState[2], textData.byteState[3], textData.byteState[4], textData.byteState[5]);

                currCharUV[0] = fontData.getUVs()[0] + 0.5f;
                currCharUV[1] = fontData.getUVs()[1] + 0.5f;
                currCharUV[2] = fontData.getUVs()[2] + 0.5f;
                currCharUV[3] = fontData.getUVs()[3] + 0.5f;
                currCharSize = fontData.getSize();
                currYPos = currentLine - fontData.getYOffset(); // top
                currRightPos = currentDrawStep + currCharSize[0];
                currBottomPos = currYPos - currCharSize[1];
                if (isItalic) {
                    final byte bottomEdge = (byte) (Math.max(fontLineHeight - fontData.getYOffset() - currCharSize[1], 0) - fontBaseHeight);
                    bottomItalicOffset = this.getCurrentItalicFactor() * bottomEdge;
                    topItalicOffset = this.getCurrentItalicFactor() * (currCharSize[1] + bottomEdge);
                } else bottomItalicOffset = topItalicOffset = 0.0f;
                // bl
                tmpVertexBuffer.put(currentDrawStep + bottomItalicOffset);
                tmpVertexBuffer.put(currBottomPos);
                tmpUVBuffer.put(currCharUV[0]);
                tmpUVBuffer.put(currCharUV[1]);
                // br
                tmpVertexBuffer.put(currRightPos + bottomItalicOffset);
                tmpVertexBuffer.put(currBottomPos);
                tmpUVBuffer.put(currCharUV[2]);
                tmpUVBuffer.put(currCharUV[1]);
                // tr
                tmpVertexBuffer.put(currRightPos + topItalicOffset);
                tmpVertexBuffer.put(currYPos);
                tmpUVBuffer.put(currCharUV[2]);
                tmpUVBuffer.put(currCharUV[3]);
                // tl
                tmpVertexBuffer.put(currentDrawStep + topItalicOffset);
                tmpVertexBuffer.put(currYPos);
                tmpUVBuffer.put(currCharUV[0]);
                tmpUVBuffer.put(currCharUV[3]);
                for (int n = 0; n < 4; ++n) tmpColorBuffer.put(colorBit);

                if (notDefaultAlignment) {
                    if (!putWidthDataCheck) putWidthDataCheck = true;
                    lineValidChar++;
                }
                lastCharacter = character;
                currentStep += fontData.getXAdvance() + this.getFontWidthSpace();
                if (character == '\t') currentStep += this.getFontWidthSpace() * 3.0f;
                if (!hasCharSubmit) {
                    hasCharSubmit = true;
                    this.textStateAfterSubmit[0] = 0.0f;
                }
            }
            if (notDefaultAlignment) {
                lastLineAlignmentData.one = lineValidChar;
                lastLineAlignmentData.two = lastLineVisualWidth;
            }
            tmpVertexBuffer.position(0);
            tmpVertexBuffer.limit(currentTextSize << 3);
            tmpUVBuffer.position(0);
            tmpUVBuffer.limit(currentTextSize << 3);
            tmpColorBuffer.position(0);
            tmpColorBuffer.limit(currentTextSize << 2);
            tmpVertexList.add(tmpVertexBuffer);
            tmpUVList.add(tmpUVBuffer);
            tmpColorList.add(tmpColorBuffer);
            if (i >= this._lastTextDataState.size()) {
                this._lastTextDataState.add(new Vector3f(currentLine, currentStep, currentTextSize));
            } else {
                this._lastTextDataState.set(i, new Vector3f(currentLine, currentStep, currentTextSize));
            }
            if (hasCharSubmit) this.textStateAfterSubmit[0] = Math.max(this.textStateAfterSubmit[0], maxLineVisualWidth);
            this.textStateAfterSubmit[1] = Math.abs(currentLine - fontLineHeight);
        }
        if (putWidthDataCheck && lastLineAlignmentData.one > 0) currLineAlignmentData.add(lastLineAlignmentData);

        if (charLength < 1) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }

        final int newCharLength = charLength + lastCharLength;
        if (newCharLength > this.textDataRefreshState[0]) {
            this.reallocateBuffer(newCharLength);
            this.textDataRefreshState[0] = newCharLength;
        }

        final int subVec2Index = lastCharLength << 3;
        final int subColorIndex = lastCharLength << 2;
        this.vertexBuffer.position(subVec2Index);
        this.uvBuffer.position(subVec2Index);
        FloatBuffer colorBufferF = this.colorBuffer.asFloatBuffer();
        colorBufferF.position(subColorIndex);
        if (this.isRefreshRenderingLengthWhenSubmit()) this.textDataRefreshState[3] = newCharLength;

        FloatBuffer tmpPutBuffer;
        int tmpBufferAddIndexA = subVec2Index, tmpBufferAddIndexB = subColorIndex;
        for (int i = 0; i < tmpVertexList.size(); i++) {
            tmpPutBuffer = tmpVertexList.get(i);
            tmpBufferAddIndexA += tmpPutBuffer.limit();
            this.vertexBuffer.put(tmpPutBuffer);
            this.vertexBuffer.position(tmpBufferAddIndexA);
            tmpPutBuffer = tmpUVList.get(i);
            this.uvBuffer.put(tmpPutBuffer);
            this.uvBuffer.position(tmpBufferAddIndexA);

            tmpPutBuffer = tmpColorList.get(i);
            tmpBufferAddIndexB += tmpPutBuffer.limit();
            colorBufferF.put(tmpPutBuffer);
            colorBufferF.position(tmpBufferAddIndexB);
        }
        if (notDefaultAlignment) {
            int lineBufferIndexA = subVec2Index, lineBufferIndexB = subVec2Index + 2, lineBufferIndexC = subVec2Index + 4, lineBufferIndexD = subVec2Index + 6;
            float currOffsetStep;
            for (Pair<Integer, Float> line : currLineAlignmentData) {
                currOffsetStep = Math.max(this.getTextFieldWidth() - line.two, 0.0f) / offset;
                for (int n = 0; n < line.one; ++n) {
                    this.vertexBuffer.put(lineBufferIndexA, this.vertexBuffer.get(lineBufferIndexA) + currOffsetStep);
                    this.vertexBuffer.put(lineBufferIndexB, this.vertexBuffer.get(lineBufferIndexB) + currOffsetStep);
                    this.vertexBuffer.put(lineBufferIndexC, this.vertexBuffer.get(lineBufferIndexC) + currOffsetStep);
                    this.vertexBuffer.put(lineBufferIndexD, this.vertexBuffer.get(lineBufferIndexD) + currOffsetStep);
                    lineBufferIndexA += 8;
                    lineBufferIndexB += 8;
                    lineBufferIndexC += 8;
                    lineBufferIndexD += 8;
                }
            }
        }

        this.vertexBuffer.position(0);
        this.vertexBuffer.limit(this.vertexBuffer.capacity());
        this.uvBuffer.position(0);
        this.uvBuffer.limit(this.uvBuffer.capacity());
        this.colorBuffer.position(0);
        this.colorBuffer.limit(this.colorBuffer.capacity());
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
        if (this.fontMap == null) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED_OTHER;
        }
        if (charNum < 1) {
            this._sync_lock.unlock();
            return BoxEnum.STATE_FAILED;
        }

        this.reallocateBuffer(charNum);
        this.textDataRefreshState[0] = charNum;
        this.textDataRefreshState[3] = 0;
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
        return this.isRefreshRenderingLengthWhenSubmit;
    }

    public void setRefreshRenderingLengthWhenSubmit(boolean refresh) {
        this._sync_lock.lock();
        this.isRefreshRenderingLengthWhenSubmit = refresh;
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

    public List<TextFieldEntity.TextData> getTextDataList() {
        return this._lastTextDataList;
    }

    public int getAllocatedValidCharLength() {
        return this.textDataRefreshState[0];
    }

    public boolean haveAllocatedValidCharLength() {
        return this.textDataRefreshState[0] > 0;
    }

    public int getValidCharLength() {
        return this.textDataRefreshState[3];
    }

    public boolean isValidRenderingTextField() {
        return this.textDataRefreshState[3] > 0;
    }

    public void setShouldRenderingCharCount(int charNum) {
        this.textDataRefreshState[3] = Math.max(Math.min(this.textDataRefreshState[0], charNum), 0);
    }

    public TextFieldEntity.Alignment getAlignment() {
        return this.alignment;
    }

    public void setAlignment(TextFieldEntity.Alignment alignment) {
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
        return this.state[4];
    }

    /**
     * @param angle range at <strong>[-90, 90]</strong>.
     */
    public void setItalicFactor(float angle) {
        this.state[4] = (float) Math.sin(Math.toRadians(angle));
    }

    /**
     * @param value value of sin, and angle range at <strong>[-90, 90]</strong>.
     */
    public void setItalicFactorDirect(float value) {
        this.state[4] = value;
    }

    public void setDefaultItalicFactor() {
        this.state[4] = 0.2f;
    }
}
