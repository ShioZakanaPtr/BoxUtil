package org.boxutil.base;

import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.base.api.MaterialRenderAPI;
import org.boxutil.units.standard.attribute.MaterialData;
import de.unkrig.commons.nullanalysis.Nullable;

import java.nio.FloatBuffer;

public abstract class BaseMIRenderData extends BaseInstanceRenderData implements MaterialRenderAPI {
    protected MaterialData material = new MaterialData();

    protected int _StatePackageStack() {
        return 4;
    }

    protected void _deleteExc() {
        super._deleteExc();
        this.material = null;
    }

    public @NotNull MaterialData getMaterialData() {
        if (this.hasDelete()) return new MaterialData();
        return this.material;
    }

    public void setMaterialData(@Nullable MaterialData material) {
        this.material = material == null ? new MaterialData() : material;
    }

    public FloatBuffer pickDataPackage_vec4() {
        this._statePackageBuffer.put(0, this.material.getState(), 0, 12);
        this._statePackageBuffer.put(13, this.getGlobalTimerAlpha());
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
        return this._statePackageBuffer;
    }
}
