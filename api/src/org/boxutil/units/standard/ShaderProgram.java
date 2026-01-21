package org.boxutil.units.standard;

import de.unkrig.commons.nullanalysis.Nullable;
import org.boxutil.base.BaseShaderData;
import org.boxutil.util.ShaderUtil;

import java.util.HashMap;

/**
 * For example how use it:
 * <pre>
 * {@code
 * // program init
 * final String vertSrc, fragSrc;
 * final var program = new ShaderProgram("YourShaderProgramTag-TheCommonDraw", vertSrc, fragSrc);
 * if (program.isValid()) {
 *     // If have uniform
 *     program.initUniformSize(2)
 *             .beginUniform()
 *             .loadUniformIndex("u00")
 *             .loadUniformIndex("u01")
 *
 *             // if have UBO
 *             .initUniformBlockSize(1)
 *             .beginUniformBlock()
 *             .loadAndSetUniformBlockIndex("BUtilGlobalData", ShaderCore.getMatrixUBOBinding())
 *
 *             // if have subroutine
 *             .initSubroutineSize(5, 2)
 *             .beginSubroutine(0, GL20.GL_VERTEX_SHADER)
 *             .loadSubroutineIndex("funA_v00") // of "funA_vu"
 *             .loadSubroutineIndex("funA_v01") // of "funA_vu"
 *             .loadSubroutineIndex("funA_v02") // of "funA_vu"
 *             .loadSubroutineIndex("funB_v00") // of "funB_vu"
 *             .loadSubroutineIndex("funB_v01") // of "funB_vu"
 *             .beginSubroutine(1, GL20.GL_FRAGMENT_SHADER)
 *             .loadSubroutineIndex("fun_f00")
 *             .loadSubroutineIndex("fun_f01")
 *
 *             .initSubroutineUniformSize(2, 1)
 *             .beginSubroutineUniform(0, GL20.GL_VERTEX_SHADER)
 *             .loadSubroutineUniformIndex("funA_vu") // index 0
 *             .loadSubroutineUniformIndex("funB_vu") // index 1
 *             .beginSubroutineUniform(1, GL20.GL_FRAGMENT_SHADER)
 *             .loadSubroutineUniformIndex("fun_fu")
 *             .computeSubroutineUniformRoute();
 * }
 *
 *
 *
 * // When running
 * // set uniform
 * GL20.glUniform1i(program.uniform("u00"), 2); // put 2 to "u00" that use GL13.GL_TEXTURE2, if "u00" is a 'sampler2D' type uniform
 * //GL20.glUniform1i(program.location[0], 2); // or use index
 * final int texID;
 * program.bindTexture2D(2, texID); // bind to "u00"
 *
 * GL20.glUniform2f(program.uniform("u01"), 1.0f, 0.0f); // put vec2(1.0f, 0.0f) to "u01", if "u01" is a 'vec2' type uniform
 * //GL20.glUniform2f(program.location[1], 1.0f, 0.0f); // or use index
 *
 *
 * // put "funA_v00" to "funA_vu" and put "funB_v00" to "funB_vu"
 * final int[] vertexSub = new int[2];
 * vertexSub[0] = program.subroutine(0, "funA_v00"); // "funA_vu" at index-0
 * //vertexSub[0] = program.subroutineLocation[0][0]; // or use index
 * vertexSub[1] = program.subroutine(0, "funB_v00"); // "funB_vu" at index-1
 * //vertexSub[1] = program.subroutineLocation[0][3]; // or use index
 * program.putUniformSubroutines(GL20.GL_VERTEX_SHADER, 0, vertexSub);
 *
 *
 * // put "fun_f00" to "fun_fu"
 * program.putUniformSubroutine(GL20.GL_VERTEX_SHADER, 1, 0);
 * }
 * </pre>
 */
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
