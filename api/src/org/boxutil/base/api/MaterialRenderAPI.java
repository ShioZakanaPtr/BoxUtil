package org.boxutil.base.api;

import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.define.BoxEnum;
import org.boxutil.units.standard.attribute.MaterialData;
import de.unkrig.commons.nullanalysis.Nullable;

public interface MaterialRenderAPI {
    @NotNull MaterialData getMaterialData();

    void setMaterialData(@Nullable MaterialData material);
}
