package org.boxutil.define;

public enum InstanceType {
    DYNAMIC_2D((byte) 20, (byte) 18),
    FIXED_2D((byte) 8, (byte) 8),
    DYNAMIC_3D((byte) 36, (byte) 27),
    FIXED_3D((byte) 12, (byte) 12);

    private final byte component;
    private final byte compactComponent;
    private final byte compactOffset;

    InstanceType(byte component, byte compactComponent) {
        this.component = component;
        this.compactComponent = compactComponent;
        this.compactOffset = (byte) (component - compactComponent);
    }

    /**
     * The float[] length.
     */
    public byte getComponent() {
        return this.component;
    }

    /**
     * The float[] length.
     */
    public byte getCompactComponent() {
        return this.compactComponent;
    }

    public byte getCompactOffset() {
        return this.compactOffset;
    }

    /**
     * The float[] byte size.
     */
    public int getSize() {
        return this.component << 2;
    }

    /**
     * The float[] byte size.
     */
    public int getCompactSize() {
        return this.compactComponent << 2;
    }

    public int getCompactOffsetSize() {
        return this.compactOffset << 2;
    }
}
