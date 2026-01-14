package org.boxutil.manager;

import org.boxutil.units.standard.attribute.FontMapData;

import java.util.HashMap;

public final class FontDataManager {
    private final static HashMap<String, FontMapData> _PATH_FONT = new HashMap<>(8);

    public static boolean haveFont(String file) {
        return _PATH_FONT.containsKey(file);
    }

    public static FontMapData getFont(String file) {
        return _PATH_FONT.get(file);
    }

    public static FontMapData putFont(String file, FontMapData font) {
        if (file == null || file.isEmpty() || font == null || !font.isValid()) return null;
        return _PATH_FONT.put(file, font);
    }

    public static FontMapData deleteFont(String file) {
        return _PATH_FONT.remove(file);
    }

    public static FontMapData loadFont(String file) {
        if (file == null || file.isEmpty()) return null;
        FontMapData result = new FontMapData(file);
        return result.isValid() ? _PATH_FONT.put(file, result) : null;
    }

    public static FontMapData tryFont(String file) {
        if (!haveFont(file)) loadFont(file);
        return _PATH_FONT.get(file);
    }

    private FontDataManager() {}
}
