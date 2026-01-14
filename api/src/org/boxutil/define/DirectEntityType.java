package org.boxutil.define;

public enum DirectEntityType {
    DISTORTION(false),
    INFINITE_LIGHT(true),
    POINT_LIGHT(true),
    SPOT_LIGHT(true),
    LINEAR_LIGHT(true),
    AREA_LIGHT(true);

    private final boolean isIlluminant;

    DirectEntityType(boolean isIlluminant) {
        this.isIlluminant = isIlluminant;
    }

    public boolean isIlluminant() {
        return this.isIlluminant;
    }
}
