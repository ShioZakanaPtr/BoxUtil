package org.boxutil.units.standard;

import org.boxutil.backends.util.BUtil_BoundedIntMap;
import org.boxutil.base.BaseShaderData;
import org.boxutil.util.ShaderUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * For example how use it:
 * <pre>
 * {@code
 * // program init
 * final String vertSrc, fragSrc;
 * final var program = new ShaderProgram("YourShaderProgramTag-TheCommonDraw", vertSrc, fragSrc);
 * if (program.isValid()) {
 *     // location setup
 *     // If have uniform
 *     program.initUniformSize(2)
 *             .beginUniform()
 *             .loadUniformIndex("u_u00")
 *             .loadUniformIndex("u_u01")
 *
 *             // if have UBO
 *             .initUniformBlockSize(1)
 *             .beginUniformBlock()
 *             .loadAndSetUniformBlockIndex("BUtilGlobalData", ShaderCore.getMatrixUBOBinding())
 *
 *             // if have subroutine
 *             .initSubroutineSize(5, 2)
 *             .beginSubroutine(0, GL20.GL_VERTEX_SHADER)
 *             .loadSubroutineIndex("p_funA_v00") // of "f_funA_vu"
 *             .loadSubroutineIndex("p_funA_v01") // of "f_funA_vu"
 *             .loadSubroutineIndex("p_funA_v02") // of "f_funA_vu"
 *             .loadSubroutineIndex("p_funB_v00") // of "f_funB_vu"
 *             .loadSubroutineIndex("p_funB_v01") // of "f_funB_vu"
 *             .beginSubroutine(1, GL20.GL_FRAGMENT_SHADER)
 *             .loadSubroutineIndex("p_fun_f00")
 *             .loadSubroutineIndex("p_fun_f01")
 *
 *             .initSubroutineUniformSize(2, 1)
 *             .beginSubroutineUniform(0, GL20.GL_VERTEX_SHADER)
 *             .loadSubroutineUniformIndex("f_funA_vu") // index 0
 *             .loadSubroutineUniformIndex("f_funB_vu") // index 1
 *             .beginSubroutineUniform(1, GL20.GL_FRAGMENT_SHADER)
 *             .loadSubroutineUniformIndex("f_fun_fu")
 *             .computeSubroutineUniformRoute();
 * }
 *
 *
 *
 * // When running
 * // set uniform
 * GL20.glUniform1i(program.uniform("u_u00"), 2); // put 2 to "u_u00" that use GL13.GL_TEXTURE2, if "u_u00" is a 'sampler2D' type uniform
 * //GL20.glUniform1i(program.location[0], 2); // or use index
 * final int texID;
 * program.bindTexture2D(2, texID); // bind to "u_u00"
 *
 * GL20.glUniform2f(program.uniform("u_u01"), 1.0f, 0.0f); // put vec2(1.0f, 0.0f) to "u_u01", if "u_u01" is a 'vec2' type uniform
 * //GL20.glUniform2f(program.location[1], 1.0f, 0.0f); // or use index
 *
 *
 * // put "p_funA_v00" to "f_funA_vu" and put "p_funB_v00" to "f_funB_vu"
 * final int[] vertexSub = new int[2];
 * vertexSub[0] = program.subroutine(0, "p_funA_v00"); // "f_funA_vu" at index-0
 * //vertexSub[0] = program.subroutineLocation[0][0]; // or use index
 * vertexSub[1] = program.subroutine(0, "p_funB_v00"); // "f_funB_vu" at index-1
 * //vertexSub[1] = program.subroutineLocation[0][3]; // or use index
 * program.putUniformSubroutines(GL20.GL_VERTEX_SHADER, 0, vertexSub);
 *
 *
 * // put "p_fun_f00" to "f_fun_fu"
 * program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 1, 0);
 * }
 * </pre>
 */
@SuppressWarnings("unchecked")
public class ShaderProgram extends BaseShaderData {
    protected BUtil_BoundedIntMap<String> uniformMap = null;
    protected BUtil_BoundedIntMap<String> uniformBlockMap = null;
    protected BUtil_BoundedIntMap<String>[] subroutineMap = null;
    protected BUtil_BoundedIntMap<String>[] subroutineUniformMap = null;
    protected final int[] _tmpIndex = new int[3];

    public ShaderProgram(final int id) {
        super(id);
    }

    public ShaderProgram(final boolean fromPath, @Nullable String loggerTag, String vert, String frag) {
        this(fromPath ? ShaderUtil.createShaderVFFormPath(loggerTag, vert, frag) : ShaderUtil.createShaderVF(loggerTag, vert, frag));
    }

    public ShaderProgram(@Nullable String loggerTag, String vert, String frag) {
        this(false, loggerTag, vert, frag);
    }

    public ShaderProgram(final boolean fromPath, @Nullable String loggerTag, String vert, String geom, String frag) {
        this(fromPath ? ShaderUtil.createShaderVGFFromPath(loggerTag, vert, geom, frag) : ShaderUtil.createShaderVGF(loggerTag, vert, geom, frag));
    }

    public ShaderProgram(@Nullable String loggerTag, String vert, String geom, String frag) {
        this(false, loggerTag, vert, geom, frag);
    }

    public ShaderProgram(final boolean fromPath, @Nullable String loggerTag, String vert, String tessC, String tessE, String frag) {
        this(fromPath ? ShaderUtil.createShaderVTFFromPath(loggerTag, vert, tessC, tessE, frag) : ShaderUtil.createShaderVTF(loggerTag, vert, tessC, tessE, frag));
    }

    public ShaderProgram(@Nullable String loggerTag, String vert, String tessC, String tessE, String frag) {
        this(false, loggerTag, vert, tessC, tessE, frag);
    }

    public ShaderProgram(final boolean fromPath, @Nullable String loggerTag, String vert, String tessC, String tessE, String geom, String frag) {
        this(fromPath ? ShaderUtil.createShaderVTGFFromPath(loggerTag, vert, tessC, tessE, geom, frag) : ShaderUtil.createShaderVTGF(loggerTag, vert, tessC, tessE, geom, frag));
    }

    public ShaderProgram(@Nullable String loggerTag, String vert, String tessC, String tessE, String geom, String frag) {
        this(false, loggerTag, vert, tessC, tessE, geom, frag);
    }

    public ShaderProgram(final boolean fromPath, @Nullable String loggerTag, String... source) {
        this(fromPath ? ShaderUtil.createComputeShadersFormPath(loggerTag, source) : ShaderUtil.createComputeShaders(loggerTag, source));
    }

    public ShaderProgram(@Nullable String loggerTag, String... source) {
        this(false, loggerTag, source);
    }

    public ShaderProgram initUniformSize(int size) {
        this.location = new int[size];
        this.uniformMap = new BUtil_BoundedIntMap<>(size, 0.8f);
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

    /**
     * Quickly queries the cached uniform location value after it has been set up via <code>loadUniformIndex</code>.
     *
     * @return <code>-1</code> when does not contain this <code>name</code>.
     */
    public int uniform(final String name) {
        return this.uniformMap.getOrDefault(name, -1);
    }

    public ShaderProgram initUniformBlockSize(int size) {
        this.uboLocation = new int[size];
        this.uniformBlockMap = new BUtil_BoundedIntMap<>(size, 0.8f);
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

    /**
     * Quickly queries the cached uniform block location value after it has been set up via <code>loadUniformBlockIndex</code> or <code>loadAndSetUniformBlockIndex</code>.
     *
     * @return <code>-1</code> when does not contain this <code>name</code>.
     */
    public int uniform_block(final String name) {
        return this.uniformBlockMap.getOrDefault(name, -1);
    }

    public ShaderProgram initSubroutineSize(int... categorySize) {
        final int size = categorySize.length;
        this.subroutineLocation = new int[size][];
        this.subroutineMap = new BUtil_BoundedIntMap[size];
        for (int i = 0; i < size; i++) {
            this.subroutineLocation[i] = new int[categorySize[i]];
            this.subroutineMap[i] = new BUtil_BoundedIntMap<>(categorySize[i], 0.8f);
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

    /**
     * Quickly queries the cached subroutine function location value after it has been set up via <code>loadSubroutineIndex</code>.
     *
     * @return <code>-1</code> when does not contain this <code>name</code>.
     */
    public int subroutine(int category, final String name) {
        return this.subroutineMap[category].getOrDefault(name, -1);
    }

    public ShaderProgram initSubroutineUniformSize(int... categorySize) {
        final int size = categorySize.length;
        this.subroutineUniformLocation = new int[size][];
        this.subroutineUniformMap = new BUtil_BoundedIntMap[size];
        for (int i = 0; i < size; i++) {
            this.subroutineUniformLocation[i] = new int[categorySize[i]];
            this.subroutineUniformMap[i] = new BUtil_BoundedIntMap<>(categorySize[i], 0.8f);
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

    /**
     * Quickly queries the cached subroutine uniform location value after it has been set up via <code>loadSubroutineUniformIndex</code>.
     *
     * @return <code>-1</code> when does not contain this <code>name</code>.
     */
    public int subroutine_uniform(int category, final String name) {
        return this.subroutineUniformMap[category].getOrDefault(name, -1);
    }

    public ShaderProgram computeSubroutineUniformRoute() {
        this.initMaxSubroutineUniformLocation();
        return this;
    }
}
