package org.boxutil.units.standard.attribute;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.boxutil.backends.util.BUtil_GlyphKerningMap;
import org.boxutil.backends.util.BUtil_GlyphSet;
import org.boxutil.backends.util.BUtil_Glyph;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.FontDataManager;
import org.boxutil.manager.TextureManager;
import org.boxutil.units.standard.entity.TextFieldEntity;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Only supported one page loading now.
 * Do not use large texture font-map;
 * The font height or width is 255 the maximum, and 0 is the minimum.
 */
public class FontMapData {
    protected final static BitMapGlyph _RESERVED_FONT = new BitMapGlyph();

    protected final boolean isBold;
    protected final boolean isItalic;
    protected final boolean isUnicode;
    protected final boolean isSmooth;
    protected final byte fontSize;
    protected final byte stretchH;
    protected final byte aaLevel;
    protected final byte spacingX;
    protected final byte spacingY;
    protected final byte lineHeight;
    protected final byte baseHeight;
    protected boolean isValid = true;
    protected final short mapWidth;
    protected final short mapHeight;
    protected final int charCount;
    protected int fontMapID = 0;
    protected String name = "";
    protected SpriteAPI fontMap = null;
    protected final BUtil_GlyphSet<BitMapGlyph> glyphs;
    protected final BUtil_GlyphKerningMap kerning;

    /**
     * Recommend to use {@link FontDataManager#tryFont(String)} to make it.
     *
     * @param fontPath should register the texture of font in <strong>settings.json</strong>, or set handel after by manual; otherwise system will try for loading the texture by {@link TextureManager}.
     */
    public FontMapData(String fontPath) {
        final StringBuilder mapPath = new StringBuilder();
        final String[] fontFileFound = fontPath.split("/");
        final Set<Character> kerningSpace = new HashSet<>(4);
        for (int i = 0; i < fontFileFound.length - 1; i++) {
            mapPath.append(fontFileFound[i]).append("/");
        }
        String fontFile;
        final boolean[] stateBool = new boolean[4];
        final byte[] stateInteger = new byte[7];
        final short[] mapSize = new short[2];
        final Set<BitMapGlyph> tmpFonts = new HashSet<>(256);
        final HashMap<Integer, Byte> tmpKerning = new HashMap<>(128);
        BitMapGlyph tabGlyph = null, spaceGlyph = null;
        int charCountIn = 0, maxCharValue = Math.max('\t' & 0xffff, ' ' & 0xffff);
        try {
            fontFile = Global.getSettings().loadText(fontPath);
            BufferedReader reader = new BufferedReader(new StringReader(fontFile));
            String[] fieldFound;
            String line;
            String lineLow;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                lineLow = line.toLowerCase();
                if (lineLow.startsWith("info")) {
                    fieldFound = lineLow.split(" ");
                    for (String check : fieldFound) {
                        if (check.startsWith("face=")) this.name = check.replace("face=", "").replace("\"", "");
                        if (check.startsWith("size=")) stateInteger[0] = Byte.parseByte(check.replace("size=", ""));
                        if (check.startsWith("stretchH=")) stateInteger[1] = Byte.parseByte(check.replace("stretchH=", ""));
                        if (check.startsWith("aa=")) stateInteger[2] = Byte.parseByte(check.replace("aa=", ""));
                        if (check.startsWith("spacing=")) {
                            String[] spacingStr = check.replace("spacing=", "").split(",");
                            stateInteger[3] = Byte.parseByte(spacingStr[0]);
                            stateInteger[4] = Byte.parseByte(spacingStr[1]);
                        }

                        if (check.startsWith("bold=")) stateBool[0] = "1".contentEquals(check.replace("bold=", ""));
                        if (check.startsWith("italic=")) stateBool[1] = "1".contentEquals(check.replace("italic=", ""));
                        if (check.startsWith("unicode=")) stateBool[2] = "1".contentEquals(check.replace("unicode=", ""));
                        if (check.startsWith("smooth=")) stateBool[3] = "1".contentEquals(check.replace("smooth=", ""));
                    }
                }
                if (lineLow.startsWith("common")) {
                    fieldFound = lineLow.split(" ");
                    for (String check : fieldFound) {
                        if (check.startsWith("lineheight=")) stateInteger[5] = Byte.parseByte(check.replace("lineheight=", ""));
                        if (check.startsWith("base=")) stateInteger[6] = Byte.parseByte(check.replace("base=", ""));
                        if (check.startsWith("scalew=")) mapSize[0] = Short.parseShort(check.replace("scalew=", ""));
                        if (check.startsWith("scaleh=")) mapSize[1] = Short.parseShort(check.replace("scaleh=", ""));
                    }
                    stateInteger[6] = (byte) (stateInteger[5] - stateInteger[6]);
                }
                if (lineLow.startsWith("page") && this.fontMap == null) {
                    fieldFound = line.split(" ");
                    String imageFile;
                    for (String check : fieldFound) {
                        if (check.toLowerCase().startsWith("file=")) {
                            imageFile = check.substring(5).replace("\"", "");
                            String format = imageFile.toLowerCase();
                            if (format.endsWith(".bmp") || format.endsWith(".jpg") || format.endsWith(".jpeg") || format.endsWith(".png")) {
                                imageFile = mapPath + imageFile;
                                this.fontMap = Global.getSettings().getSprite(imageFile);
                                if (this.fontMap != null && this.fontMap.getTextureId() > 0) this.fontMapID = this.fontMap.getTextureId();
                                else this.fontMapID = TextureManager.tryTexture(imageFile);
                                if (this.fontMapID > 0) Global.getLogger(FontMapData.class).info("'BoxUtil' font applied texture with path: '" + imageFile + "'.");
                                else Global.getLogger(FontMapData.class).error("'BoxUtil' loading font texture failed at path: '" + imageFile + "'.");
                            }
                        }
                    }
                }
                if (lineLow.startsWith("chars")) {
                    fieldFound = lineLow.split(" ");
                    for (String check : fieldFound) {
                        if (check.startsWith("count=")) {
                            charCountIn = Integer.parseInt(check.replace("count=", ""));
                            break;
                        }
                    }
                }
                if (lineLow.startsWith("char")) {
                    fieldFound = lineLow.split(" ");
                    char id;
                    short x, y;
                    byte width, height, xOffset, yOffset, xAdvance, page, channel;
                    id = 0;
                    page = 0;
                    x = y = 0;
                    width = height = xOffset = yOffset = xAdvance = page;
                    // R(4u), G(8u), B(12u), A(16u), RGBA(0u)
                    channel = 0b000_000_00;
                    for (String check : fieldFound) {
                        if (check.startsWith("id=")) id = (char) Integer.parseInt(check.replace("id=", ""));
                        if (check.startsWith("x=")) x = Short.parseShort(check.replace("x=", ""));
                        if (check.startsWith("y=")) y = Short.parseShort(check.replace("y=", ""));
                        if (check.startsWith("width=")) width = Byte.parseByte(check.replace("width=", ""));
                        if (check.startsWith("height=")) height = Byte.parseByte(check.replace("height=", ""));
                        if (check.startsWith("xoffset=")) xOffset = Byte.parseByte(check.replace("xoffset=", ""));
                        if (check.startsWith("yoffset=")) yOffset = Byte.parseByte(check.replace("yoffset=", ""));
                        if (check.startsWith("xadvance=")) xAdvance = Byte.parseByte(check.replace("xadvance=", ""));
                        if (check.startsWith("chnl=")) {
                            byte channelTmp = Byte.parseByte(check.replace("chnl=", ""));
                            if (channelTmp == 4) channel = 0b000_001_00;
                            if (channelTmp == 2) channel = 0b000_010_00;
                            if (channelTmp == 1) channel = 0b000_011_00;
                            if (channelTmp == 8) channel = 0b000_100_00;
                        }
                    }
                    final var toAddGlyph = new BitMapGlyph(id, mapSize[0], mapSize[1], x, y, width, height, xOffset, yOffset, xAdvance, channel);
                    if (id == ' ' && spaceGlyph == null) spaceGlyph = toAddGlyph;
                    if (id == '\t' && tabGlyph == null) tabGlyph = toAddGlyph;
                    maxCharValue = Math.max(maxCharValue, id & 0xffff);
                    tmpFonts.add(toAddGlyph);
                }
                if (lineLow.startsWith("kerning")) {
                    fieldFound = lineLow.split(" ");
                    char first = 0, second = 0;
                    byte amount = 0;
                    for (String check : fieldFound) {
                        if (check.startsWith("first=")) first = (char) Integer.parseInt(check.replace("first=", ""));
                        if (check.startsWith("second=")) second = (char) Integer.parseInt(check.replace("second=", ""));
                        if (check.startsWith("amount=")) amount = Byte.parseByte(check.replace("amount=", ""));
                    }
                    final int fetchKerningKey = BUtil_GlyphKerningMap.fetchKey(first, second);
                    if (!kerningSpace.contains(first) && second == ' ') kerningSpace.add(first);
                    final byte finalAmount = amount;
                    tmpKerning.computeIfAbsent(fetchKerningKey, k -> finalAmount);
                }
            }

            if (spaceGlyph == null) {
                spaceGlyph = new BitMapGlyph(' ', (short) 0, (short) 0, (short) 0, (short) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) (stateInteger[0] / 2), (byte) 0b000_000_00);
                tmpFonts.add(spaceGlyph);
            }
            if (tabGlyph == null) {
                tabGlyph = new BitMapGlyph('\t');
                tabGlyph.copyFrom(spaceGlyph);
                tabGlyph.width <<= 2;
                tabGlyph.xOffset <<= 2;
                tmpFonts.add(tabGlyph);
                if (!kerningSpace.isEmpty()) {
                    for (final Character character : kerningSpace) {
                        final Byte kerningValue = tmpKerning.get(BUtil_GlyphKerningMap.fetchKey(character, ' '));
                        if (kerningValue != null && kerningValue != 0) {
                            tmpKerning.put(BUtil_GlyphKerningMap.fetchKey(character, '\t'), kerningValue);
                        }
                    }
                }
            }
            Global.getLogger(FontMapData.class).info("'BoxUtil' loaded font at path: '" + fontPath + "'.");
        } catch (IOException e) {
            this.isValid = false;
            Global.getLogger(FontMapData.class).error("'BoxUtil' loading font file failed at path: '" + fontPath + "'. " + e.getMessage());
        } finally {
            tmpFonts.add(_RESERVED_FONT);
            this.glyphs = new BUtil_GlyphSet<>(tmpFonts, maxCharValue, (byte) 3);
            this.kerning = new BUtil_GlyphKerningMap(tmpKerning, 0.8f);
            this.fontSize = stateInteger[0];
            this.stretchH = stateInteger[1];
            this.aaLevel = stateInteger[2];
            this.spacingX = stateInteger[3];
            this.spacingY = stateInteger[4];
            this.isBold = stateBool[0];
            this.isItalic = stateBool[1];
            this.isUnicode = stateBool[2];
            this.isSmooth = stateBool[3];
            this.charCount = charCountIn;
            this.lineHeight = stateInteger[5];
            this.baseHeight = stateInteger[6];
            this.mapWidth = mapSize[0];
            this.mapHeight = mapSize[1];
            this.checkValidWhenChangedFontMap();
        }
    }

    protected void checkValidWhenChangedFontMap() {
        this.isValid = this.fontMapID != 0 && this.glyphs.size() > 3;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public String getName() {
        return this.name;
    }

    public SpriteAPI getTexture() {
        return this.fontMap;
    }

    @Deprecated
    public SpriteAPI getMap() {
        return this.fontMap;
    }

    public void setTexture(SpriteAPI texture) {
        this.fontMap = texture;
        if (this.fontMap == null) {
            this.fontMapID = 0;
        } else this.fontMapID = this.fontMap.getTextureId();
        this.checkValidWhenChangedFontMap();
    }

    @Deprecated
    public void setMap(SpriteAPI fontMap) {
        this.setTexture(fontMap);
    }

    public int getTextureID() {
        return this.fontMapID;
    }

    @Deprecated
    public int getMapID() {
        return this.getTextureID();
    }

    public void setTexture(int texture) {
        this.fontMapID = texture;
        this.checkValidWhenChangedFontMap();
    }

    @Deprecated
    public void setMapID(int fontMapID) {
        this.setTexture(fontMapID);
    }

    public byte getFontSize() {
        return fontSize;
    }

    public boolean isBold() {
        return isBold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public boolean isUnicode() {
        return isUnicode;
    }

    public byte getStretchH() {
        return stretchH;
    }

    public boolean isSmooth() {
        return isSmooth;
    }

    public byte getAALevel() {
        return aaLevel;
    }

    public byte getSpacingX() {
        return spacingX;
    }

    public byte getSpacingY() {
        return spacingY;
    }

    public byte getLineHeight() {
        return this.lineHeight;
    }

    /**
     * From chars bottom to baseline.
     */
    public byte getLineBase() {
        return this.baseHeight;
    }

    public short getMapWidth() {
        return this.mapWidth;
    }

    public short getMapHeight() {
        return this.mapHeight;
    }

    public int getGlyphNum() {
        return this.glyphs.size();
    }

    @Deprecated
    public int getCharCount() {
        return this.getGlyphNum();
    }

    public boolean containsGlyph(char character) {
        return this.glyphs.contains(character);
    }

    @Deprecated
    public boolean containsFont(char character) {
        return this.containsGlyph(character);
    }

    public @Nullable BitMapGlyph getGlyph(char character) {
        final BitMapGlyph result = new BitMapGlyph(character);
        return this.glyphs.get(character, result) ? result : null;
    }

    public boolean loadGlyph(char character, final BitMapGlyph result) {
        return this.glyphs.get(character, result);
    }

    @Deprecated
    public FontData getFont(char character) {
        return null;
    }

    public boolean haveKerning() {
        return this.kerning.size() > 0;
    }

    public byte getKerning(char first, char second) {
        return this.kerning.get(first, second);
    }

    @Deprecated
    public boolean containsKerning(char character) {
        return false;
    }

    @Deprecated
    public HashMap<Character, Byte> getKerningMap(char character) {
        return null;
    }

    @Deprecated
    public static class FontData {
        public final float[] uv = new float[]{0.0f, 0.0f, 1.0f, 1.0f}; // uvBLx, uvBLy, uvTRx, uvTRy
        public final byte[] byteState = new byte[6]; // vec2(size), xOffset, yOffset, xAdvance, page, channel

        public FontData(short rawX, short rawY, short x, short y, byte width, byte height, byte xOffset, byte yOffset, byte xAdvance, byte channel) {
            int uvXOffset = rawX / 2, uvYOffset = rawY / 2;
            this.uv[0] = (float) (x - uvXOffset) / (float) rawX;
            this.uv[1] = (float) (rawY - y - height - uvYOffset) / (float) rawY;
            this.uv[2] = (float) (x + width - uvXOffset) / (float) rawX;
            this.uv[3] = (float) (rawY - y - uvYOffset) / (float) rawY;
            this.byteState[0] = width;
            this.byteState[1] = height;
            this.byteState[2] = xOffset;
            this.byteState[3] = yOffset;
            this.byteState[4] = xAdvance;
            this.byteState[5] = channel;
        }

        /**
         * Plus 0.5f before use.
         */
        public float[] getUVs() {
            return this.uv;
        }

        public byte[] getSize() {
            return new byte[]{this.byteState[0], this.byteState[1]};
        }

        public byte getXOffset() {
            return this.byteState[2];
        }

        public byte getYOffset() {
            return this.byteState[3];
        }

        public byte getXAdvance() {
            return this.byteState[4];
        }

        /**
         * R(4u), G(8u), B(12u), A(16u), RGBA(0u)
         */
        public byte getChannel() {
            return this.byteState[5];
        }
    }

    public static class BitMapGlyph implements BUtil_Glyph { // only single texture, so page always is 0
        public byte width = 0;
        public byte height = 0;
        public byte xOffset = 0;
        public byte yOffset = 0;
        public byte xAdvance = 0;
        public byte channel = 8;
        private final char character;
        public float uvBLx = 0.0f;
        public float uvBLy = 0.0f;
        public float uvTRx = 1.0f;
        public float uvTRy = 1.0f;

        private BitMapGlyph(char character, short rawX, short rawY, short x, short y, byte width, byte height, byte xOffset, byte yOffset, byte xAdvance, byte channel) {
            final int uvXOffset = rawX / 2, uvYOffset = rawY / 2;
            this.width = width;
            this.height = height;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.xAdvance = xAdvance;
            this.channel = channel;
            this.character = character;
            this.uvBLx = (float) (x - uvXOffset) / (float) rawX;
            this.uvBLy = (float) (rawY - y - height - uvYOffset) / (float) rawY;
            this.uvTRx = (float) (x + width - uvXOffset) / (float) rawX;
            this.uvTRy = (float) (rawY - y - uvYOffset) / (float) rawY;
        }

        private BitMapGlyph(char character) {
            this.character = character;
        }

        public BitMapGlyph() {
            this.character = TextFieldEntity.RESERVED_SYMBOL;
            this.uvBLx = this.uvBLy = this.uvTRx = this.uvTRy = -512.0f;
        }

        public char character() {
            return this.character;
        }

        public void fetch(int offset, final long[] raw) {
            final long raw0 = raw[offset], raw1 = raw[offset + 1], raw2 = raw[offset + 2];

            this.uvBLx = Float.intBitsToFloat((int) (raw0 & 0xffffffffL));
            this.uvBLy = Float.intBitsToFloat((int) ((raw0 >> 32) & 0xffffffffL));

            this.uvTRx = Float.intBitsToFloat((int) (raw1 & 0xffffffffL));
            this.uvTRy = Float.intBitsToFloat((int) ((raw1 >> 32) & 0xffffffffL));

            this.width = (byte) (raw2 & 0xffL);
            this.height = (byte) ((raw2 >> 8) & 0xffL);
            this.xOffset = (byte) ((raw2 >> 16) & 0xffL);
            this.yOffset = (byte) ((raw2 >> 24) & 0xffL);
            this.xAdvance = (byte) ((raw2 >> 32) & 0xffL);
            this.channel = (byte) ((raw2 >> 40) & 0xffL);
        }

        public void store(int offset, final long[] raw) {
            raw[offset] = Float.floatToRawIntBits(this.uvBLx) & 0xffffffffL |
                    (long) Float.floatToRawIntBits(this.uvBLy) << 32 & 0xffffffff00000000L;

            raw[offset + 1] = Float.floatToRawIntBits(this.uvTRx) & 0xffffffffL |
                    (long) Float.floatToRawIntBits(this.uvTRy) << 32 & 0xffffffff00000000L;

            raw[offset + 2] = this.width & 0xffL |
                    this.height << 8 & 0xff_00L |
                    this.xOffset << 16 & 0xff_00_00L |
                    this.yOffset << 24 & 0xff_00_00_00L |
                    (long) this.xAdvance << 32 & 0xff_00_00_00_00L |
                    (long) this.channel << 40 & 0xff_00_00_00_00_00L;
        }

        public void copyFrom(final BitMapGlyph src) {
            this.width = src.width;
            this.height = src.height;
            this.xOffset = src.xOffset;
            this.yOffset = src.yOffset;
            this.xAdvance = src.xAdvance;
            this.channel = src.channel;
            this.uvBLx = src.uvBLx;
            this.uvBLy = src.uvBLy;
            this.uvTRx = src.uvTRx;
            this.uvTRy = src.uvTRy;
        }

        public int hashCode() {
            return Character.hashCode(this.character);
        }

        public boolean equals(Object obj) {
            if (obj instanceof BitMapGlyph cast) return cast.hashCode() == this.hashCode();
            return false;
        }
    }
}
