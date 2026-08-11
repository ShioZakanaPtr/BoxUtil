package org.boxutil.manager;

import org.boxutil.units.standard.attribute.FontMapData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@SuppressWarnings("UnusedReturnValue")
public final class FontDataManager {
    private final static HashMap<String, FontMapData> _PATH_FONT = new HashMap<>(8);

    public static boolean haveFont(final String file) {
        return _PATH_FONT.containsKey(file);
    }

    public static FontMapData getFont(final String file) {
        return _PATH_FONT.get(file);
    }

    public static FontMapData putFont(@NotNull final String file, final FontMapData font) {
        if (file.isBlank()) throw new IllegalArgumentException("Illegal file path: a white space");
        if (font == null || !font.isValid()) return null;
        return _PATH_FONT.put(file, font);
    }

    public static FontMapData deleteFont(final String file) {
        return _PATH_FONT.remove(file);
    }

    public static FontMapData loadFont(@NotNull final String file) {
        if (file.isBlank()) throw new IllegalArgumentException("Illegal file path: a white space");
        FontMapData result = new FontMapData(file);
        return result.isValid() ? _PATH_FONT.put(file, result) : null;
    }

    public static FontMapData tryFont(final String file) {
        if (!haveFont(file)) loadFont(file);
        return _PATH_FONT.get(file);
    }

    private FontDataManager() {}
}
