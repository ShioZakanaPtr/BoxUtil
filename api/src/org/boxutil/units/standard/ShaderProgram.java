package org.boxutil.units.standard;

import de.unkrig.commons.nullanalysis.Nullable;
import org.boxutil.base.BaseShaderData;
import org.boxutil.util.ShaderUtil;

import java.util.HashMap;

// For
public class ShaderProgram extends BaseShaderData {
    protected final HashMap<String, Integer> uniformMap = new HashMap<>(8);
    protected final HashMap<String, Integer> uniformBlockMap = new HashMap<>(4);
    protected HashMap<String, Integer>[] subroutineMap = null;
    protected HashMap<String, Integer>[] subroutineUniformMap = null;
    protected final int[] _tmpIndex = new int[3];

    public ShaderProgram(int id) {
        super(id);
    }

    public ShaderProgram(@Nullable String loggerTag, String vert, String frag) {
        this(ShaderUtil.createShaderVF(loggerTag, vert, frag));
    }

    public ShaderProgram(@Nullable String loggerTag, String vert, String geom, String frag) {
        this(ShaderUtil.createShaderVGF(loggerTag, vert, geom, frag));
    }

    public ShaderProgram(@Nullable String loggerTag, String vert, String tessC, String tessE, String frag) {
        this(ShaderUtil.createShaderVTF(loggerTag, vert, tessC, tessE, frag));
    }

    public ShaderProgram(@Nullable String loggerTag, String vert, String tessC, String tessE, String geom, String frag) {
        this(ShaderUtil.createShaderVTGF(loggerTag, vert, tessC, tessE, geom, frag));
    }

    public ShaderProgram(@Nullable String loggerTag, String... source) {
        this(ShaderUtil.createComputeShaders(loggerTag, source));
    }

    public ShaderProgram initUniformSize(int size) {
        this.location = new int[size];
        return this;
    }

    public ShaderProgram beginUniform() {
        this._tmpIndex[0] = 0;
        return this;
    }

    public ShaderProgram loadUniformIndex(String name) {
        this.location[this._tmpIndex[0]] = this.getUniformIndex(name);
        this.uniformMap.put(name, this.location[this._tmpIndex[0]]);
        this._tmpIndex[0]++;
        return this;
    }

    public ShaderProgram loadUniformIndex(int storeIndex, String name) {
        this.location[storeIndex] = this.getUniformIndex(name);
        this.uniformMap.put(name, this.location[storeIndex]);
        return this;
    }

    public int uniform(final String name) {
        return this.uniformMap.getOrDefault(name, -1);
    }

    public ShaderProgram initUniformBlockSize(int size) {
        this.uboLocation = new int[size];
        return this;
    }

    public ShaderProgram beginUniformBlock() {
        this._tmpIndex[0] = 0;
        return this;
    }

    public ShaderProgram loadUniformBlockIndex(String name) {
        this.uboLocation[this._tmpIndex[0]] = this.getStructUniformIndex(name);
        this.uniformBlockMap.put(name, this.uboLocation[this._tmpIndex[0]]);
        this._tmpIndex[0]++;
        return this;
    }

    public ShaderProgram loadUniformBlockIndex(int storeIndex, String name) {
        this.uboLocation[storeIndex] = this.getStructUniformIndex(name);
        this.uniformBlockMap.put(name, this.uboLocation[storeIndex]);
        return this;
    }

    public ShaderProgram loadAndSetUniformBlockIndex(String name, int bindingIndex) {
        this.uboLocation[this._tmpIndex[0]] = this.getUBOIndex(name, bindingIndex);
        this.uniformBlockMap.put(name, this.uboLocation[this._tmpIndex[0]]);
        this._tmpIndex[0]++;
        return this;
    }

    public ShaderProgram loadAndSetUniformBlockIndex(int storeIndex, String name, int bindingIndex) {
        this.uboLocation[storeIndex] = this.getUBOIndex(name, bindingIndex);
        this.uniformBlockMap.put(name, this.uboLocation[storeIndex]);
        return this;
    }

    public int uniform_block(final String name) {
        return this.uniformBlockMap.getOrDefault(name, -1);
    }

    public ShaderProgram initSubroutineSize(int... categorySize) {
        final int size = categorySize.length;
        this.subroutineLocation = new int[size][];
        this.subroutineMap = new HashMap[size];
        for (int i = 0; i < size; i++) {
            this.subroutineLocation[i] = new int[categorySize[i]];
            this.subroutineMap[i] = new HashMap<>(4);
        }
        return this;
    }

    public ShaderProgram beginSubroutine(int storeCategory, int shaderType) {
        this._tmpIndex[0] = 0;
        this._tmpIndex[1] = storeCategory;
        this._tmpIndex[2] = shaderType;
        return this;
    }

    public ShaderProgram loadSubroutineIndex(String name) {
        this.subroutineLocation[this._tmpIndex[1]][this._tmpIndex[0]] = this.getSubroutineIndex(this._tmpIndex[2], name);
        this.subroutineMap[this._tmpIndex[1]].put(name, this.subroutineLocation[this._tmpIndex[1]][this._tmpIndex[0]]);
        this._tmpIndex[0]++;
        return this;
    }

    public ShaderProgram loadSubroutineIndex(int storeCategory, int storeIndex, int shaderType, String name) {
        this.subroutineLocation[storeCategory][storeIndex] = this.getSubroutineIndex(shaderType, name);
        this.subroutineMap[storeCategory].put(name, this.subroutineLocation[storeCategory][storeIndex]);
        return this;
    }

    public int subroutine(int category, final String name) {
        return this.subroutineMap[category].getOrDefault(name, -1);
    }

    public ShaderProgram initSubroutineUniformSize(int... categorySize) {
        final int size = categorySize.length;
        this.subroutineUniformLocation = new int[size][];
        this.subroutineUniformMap = new HashMap[size];
        for (int i = 0; i < size; i++) {
            this.subroutineUniformLocation[i] = new int[categorySize[i]];
            this.subroutineUniformMap[i] = new HashMap<>(2);
        }
        return this;
    }

    public ShaderProgram beginSubroutineUniform(int storeCategory, int shaderType) {
        this._tmpIndex[0] = 0;
        this._tmpIndex[1] = storeCategory;
        this._tmpIndex[2] = shaderType;
        return this;
    }

    public ShaderProgram loadSubroutineUniformIndex(String name) {
        this.subroutineUniformLocation[this._tmpIndex[1]][this._tmpIndex[0]] = this.getSubroutineUniformLocation(this._tmpIndex[2], name);
        this.subroutineUniformMap[this._tmpIndex[1]].put(name, this.subroutineLocation[this._tmpIndex[1]][this._tmpIndex[0]]);
        this._tmpIndex[0]++;
        return this;
    }

    public ShaderProgram loadSubroutineUniformIndex(int storeCategory, int storeIndex, int shaderType, String name) {
        this.subroutineUniformLocation[storeCategory][storeIndex] = this.getSubroutineUniformLocation(shaderType, name);
        this.subroutineUniformMap[storeCategory].put(name, this.subroutineLocation[storeCategory][storeIndex]);
        return this;
    }

    public int subroutine_uniform(int category, final String name) {
        return this.subroutineUniformMap[category].getOrDefault(name, -1);
    }

    public ShaderProgram computeSubroutineUniformRoute() {
        this.initMaxSubroutineUniformLocation();
        return this;
    }
}
