package org.boxutil.backends.core.instancedrendering;

import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.MemoryBlock;
import org.boxutil.units.standard.GPUMemoryPool;

public class BUtil_InstanceMemory extends GPUMemoryPool.InternalMemory<InstanceType> implements MemoryBlock {
    private final boolean isType2D;
    private final boolean isTypeFixed;
    private int instanceAddress = 0;
    private int instanceCount = 0;
    private final InstanceType _type;

    public BUtil_InstanceMemory(InstanceType meta, long address, long size, int index, boolean isFree) {
        super(meta, address, size, index, isFree);
        if (meta == null) throw new IllegalArgumentException("Illegal instance memory type: null");
        this.isType2D = meta == InstanceType.DYNAMIC_2D || meta == InstanceType.FIXED_2D;
        this.isTypeFixed = meta == InstanceType.FIXED_2D || meta == InstanceType.FIXED_3D;
        this._type = meta;
    }

    public InstanceType type() {
        return this._type;
    }

    public InstanceType meta() {
        return this._type;
    }

    public int address_instance() {
        return this.instanceAddress;
    }

    public int instance_count() {
        return this.instanceCount;
    }

    public boolean is_type_2D() {
        return this.isType2D;
    }

    public boolean is_type_fixed() {
        return this.isTypeFixed;
    }

    public void afterAddressChanged() {
        this.instanceAddress = Math.toIntExact(this.address / this._type.getSize());
    }

    public void afterSizeChanged() {
        this.instanceCount = Math.toIntExact(this.size / this._type.getSize());
    }

    public String toString() {
        return "'BoxUtil' Instance memory: Type = '" + this._type.name() + "' Reference = '" + this.ref.get() + "' Address = '0x" + Long.toHexString(this.address).toUpperCase() + "' Size = '" + this.size + "'";
    }

    public int hashCode() {
        int result = 31 + this._type.hashCode();
        result = 31 * result + Long.hashCode(this.address);
        return 31 * result + Long.hashCode(this.size);
    }

    public boolean equals(Object obj) {
        if (obj instanceof BUtil_InstanceMemory) return hashCode() == obj.hashCode();
        else return false;
    }
}
