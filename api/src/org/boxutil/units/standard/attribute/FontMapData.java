package org.boxutil.units.standard.attribute;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.apache.log4j.Level;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.TextureManager;
import org.boxutil.units.standard.entity.TextFieldEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Only supported one page loading now.
 * Do not use large texture font-map;
 * The font height is 255 the maximum, and 0 is the minimum.
 */
public class FontMapData {
    protected final static FontData _RESERVED_FONT = new FontData(BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ZERO, (byte) 8);
    static {
        _RESERVED_FONT.uv[0] = -512.0f;
        _RESERVED_FONT.uv[1] = -512.0f;
        _RESERVED_FONT.uv[2] = -512.0f;
        _RESERVED_FONT.uv[3] = -512.0f;
    }
    protected String name = "";
    protected SpriteAPI fontMap = null;
    protected int fontMapID = 0;
    protected HashMap<Character, FontData> fonts = new HashMap<>(128);
    protected HashMap<Character, HashMap<Character, Byte>> kerning = null;

    protected final byte fontSize;
    protected final byte stretchH;
    protected final byte aaLevel;
    protected final byte spacingX;
    protected final byte spacingY;
    protected final boolean isBold;
    protected final boolean isItalic;
    protected final boolean isUnicode;
    protected final boolean isSmooth;
    protected final int charCount;
    protected final byte lineHeight;
    protected final byte baseHeight;
    protected final short mapWidth;
    protected final short mapHeight;
    protected boolean isValid = true;

    private FontMapData() {
        this.fontSize = 0;
        this.stretchH = 0;
        this.aaLevel = 0;
        this.spacingX = 0;
        this.spacingY = 0;
        this.isBold = false;
        this.isItalic = false;
        this.isUnicode = false;
        this.isSmooth = false;
        this.charCount = 0;
        this.lineHeight = 0;
        this.baseHeight = 0;
        this.mapWidth = 0;
        this.mapHeight = 0;
    }

    /**
     * @param fontPath should register the texture of font in <strong>settings.json</strong>, or set handel after by manual.
     */
    public FontMapData(String fontPath) {
        StringBuilder mapPath = new StringBuilder();
        String[] fontFileFound = fontPath.split("/");
        Set<Character> kerningSpace = new HashSet<>(4);
        for (int i = 0; i < fontFileFound.length - 1; i++) {
            mapPath.append(fontFileFound[i]).append("/");
        }
        String fontFile;
        final byte[] stateInteger = new byte[7];
        final boolean[] stateBool = new boolean[4];
        final short[] mapSize = new short[2];
        int charCountIn = 0;
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
                    this.fonts.put(id, new FontData(mapSize[0], mapSize[1], x, y, width, height, xOffset, yOffset, xAdvance, channel));
                }
                if (lineLow.startsWith("kerning")) {
                    if (this.kerning == null) this.kerning = new HashMap<>();
                    fieldFound = lineLow.split(" ");
                    char first = 0, second = 0;
                    byte amount = 0;
                    for (String check : fieldFound) {
                        if (check.startsWith("first=")) first = (char) Integer.parseInt(check.replace("first=", ""));
                        if (check.startsWith("second=")) second = (char) Integer.parseInt(check.replace("second=", ""));
                        if (check.startsWith("amount=")) amount = Byte.parseByte(check.replace("amount=", ""));
                    }
                    if (!this.fonts.containsKey(first)) {
                        this.isValid = false;
                        break;
                    }
                    if (!kerningSpace.contains(first) && second == ' ') kerningSpace.add(first);
                    if (!this.kerning.containsKey(first)) this.kerning.put(first, new HashMap<Character, Byte>());
                    HashMap<Character, Byte> thisMap = this.kerning.get(first);
                    thisMap.put(second, amount);
                }
            }

            FontData tabChar = this.getFont('\t'), spaceChar = this.getFont(' ');
            HashMap<Character, Byte> spaceKerningMap;
            if (spaceChar == null) {
                spaceChar = new FontData((short) 0, (short) 0, (short) 0, (short) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) (stateInteger[0] / 2), (byte) 0b000_000_00);
                this.fonts.put(' ', spaceChar);
            }
            if (tabChar == null) {
                tabChar = new FontData((short) 0, (short) 0, (short) 0, (short) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
                System.arraycopy(spaceChar.byteState, 0, tabChar.byteState, 0, tabChar.byteState.length);
                System.arraycopy(spaceChar.uv, 0, tabChar.uv, 0, tabChar.uv.length);
                tabChar.byteState[0] <<= 2;
                tabChar.byteState[4] <<= 2;
                this.fonts.put('\t', tabChar);
                if (this.kerning != null) {
                    spaceKerningMap = this.kerning.get(' ');
                    if (spaceKerningMap != null) this.kerning.put('\t', spaceKerningMap);
                    if (!kerningSpace.isEmpty()) {
                        for (Character character : kerningSpace) {
                            spaceKerningMap = this.kerning.get(character);
                            spaceKerningMap.put('\t', spaceKerningMap.get(' '));
                        }
                    }
                }
            }
            this.checkValidWhenChangedFontMap();
            Global.getLogger(FontMapData.class).info("'BoxUtil' loaded font at path: '" + fontPath + "'.");
        } catch (IOException e) {
            this.isValid = false;
            Global.getLogger(FontMapData.class).error("'BoxUtil' loading font file failed at path: '" + fontPath + "'. " + e.getMessage());
        } finally {
            this.fonts.put(TextFieldEntity.RESERVED_SYMBOL, _RESERVED_FONT);
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
        }
    }

    protected void checkValidWhenChangedFontMap() {
        this.isValid = this.fontMapID != 0 && this.fonts.size() > 3;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public String getName() {
        return this.name;
    }

    public SpriteAPI getMap() {
        return this.fontMap;
    }

    public void setMap(SpriteAPI fontMap) {
        this.fontMap = fontMap;
        if (this.fontMap == null) {
            this.fontMapID = 0;
        } else this.fontMapID = this.fontMap.getTextureId();
        this.checkValidWhenChangedFontMap();
    }

    public int getMapID() {
        return this.fontMapID;
    }

    public void setMapID(int fontMapID) {
        this.fontMapID = fontMapID;
        this.checkValidWhenChangedFontMap();
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

    public int getCharCount() {
        return this.charCount;
    }

    public boolean containsFont(char character) {
        return this.fonts.containsKey(character);
    }

    public FontData getFont(char character) {
        return this.fonts.get(character);
    }

    public boolean haveKerning() {
        return this.kerning != null && !this.kerning.isEmpty();
    }

    public boolean containsKerning(char character) {
        return this.kerning.containsKey(character);
    }

    public HashMap<Character, Byte> getKerningMap(char character) {
        return this.kerning.get(character);
    }

    public static class FontData{
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
}
