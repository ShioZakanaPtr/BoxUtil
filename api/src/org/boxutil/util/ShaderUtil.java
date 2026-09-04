package org.boxutil.util;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;
import org.boxutil.base.BaseShaderData;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.GLWrapper;
import org.boxutil.manager.ShaderCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * For all result texture, recommend to use storage texture for performance.<p>
 * In some shader program making method:<table border = "1">
 * <tr><th>Letter</th><th>Means</th></tr>
 * <tr><th>V</th><th>Vertex shader</th></tr>
 * <tr><th>T</th><th>Tessellation (Control and Evaluation) shader</th></tr>
 * <tr><th>G</th><th>Geometry shader</th></tr>
 * <tr><th>F</th><th>Fragment shader</th></tr>
 * <tr><th>C</th><th>Compute shader</th></tr>
 * </table>
 */
@SuppressWarnings("UnusedReturnValue")
public final class ShaderUtil {
    private final static Logger _LOG = Global.getLogger(ShaderUtil.class);

    private static String foundShaderName(int type) {
        if (type == GLWrapper.Shader.Vert.GL_VERTEX_SHADER) return "Vertex";
        if (type == GLWrapper.Shader.Tess.GL_TESS_CONTROL_SHADER) return "Tess-Control";
        if (type == GLWrapper.Shader.Tess.GL_TESS_EVALUATION_SHADER) return "Tess-Evaluation";
        if (type == GLWrapper.Shader.Geom.GL_GEOMETRY_SHADER) return "Geometry";
        if (type == GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER) return "Fragment";
        if (type == GLWrapper.Shader.Comp.GL_COMPUTE_SHADER) return "Compute";
        return null;
    }

    public static String getShaderName(int type) {
        final var result = foundShaderName(type);
        return result == null ? "Shader type '" + type + "' not found." : result;
    }

    public static int getTypeFromPath(@NotNull String path) {
        String[] sp = path.split("\\.");
        String format = sp[sp.length - 1];
        return getTypeFromFormat(format);
    }

    public static int getTypeFromFormat(@NotNull String format) {
        final String lower = format.toLowerCase();
        return switch (lower) {
            case "vert", "vsh" -> GLWrapper.Shader.Vert.GL_VERTEX_SHADER;
            case "tesc" -> GLWrapper.Shader.Tess.GL_TESS_CONTROL_SHADER;
            case "tese" -> GLWrapper.Shader.Tess.GL_TESS_EVALUATION_SHADER;
            case "geom", "gsh" -> GLWrapper.Shader.Geom.GL_GEOMETRY_SHADER;
            case "frag", "fsh" -> GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER;
            case "comp", "csh" -> GLWrapper.Shader.Comp.GL_COMPUTE_SHADER;
            default -> BoxEnum.ZERO;
        };
    }

    public final static byte BIND_VERTEX_ATTRIB = 0;
    public final static byte BIND_FRAGMENT_OUTPUT = 1;

    /**
     * @param target {@link ShaderUtil#BIND_VERTEX_ATTRIB} for vertex attrib location, OpenGL 2.0 required; {@link ShaderUtil#BIND_FRAGMENT_OUTPUT} for fragment output/location, OpenGL 3.0 required.
     * @param location vertex attrib index or color attachment index.
     * @param name the field name that what them in shader source is.
     */
    public record BindingLocationStruct(byte target, int location, String name) {}

    public interface LinkCondition {
        boolean runAndCheck(int program);

        /**
         * Always run and check each condition.
         */
        default LinkCondition next(@NotNull final LinkCondition other) {
            return l_program -> runAndCheck(l_program) | other.runAndCheck(l_program);
        }

        /**
         * Break the <code>other</code> check if this condition has returns <code>true</code>;
         */
        default LinkCondition nextOptional(@NotNull final LinkCondition other) {
            return l_program -> runAndCheck(l_program) || other.runAndCheck(l_program);
        }
    }

    public static LinkCondition makeShaderBindingLocation(final BindingLocationStruct... bindingLocations) {
        return (programID) -> {
            for (BindingLocationStruct bindingLocation : bindingLocations) {
                switch (bindingLocation.target) {
                    case BIND_VERTEX_ATTRIB: GLWrapper.Shader.Vert.glBindAttribLocation(programID, bindingLocation.location, bindingLocation.name); break;
                    case BIND_FRAGMENT_OUTPUT: GLWrapper.Shader.Frag.glBindFragDataLocation(programID, bindingLocation.location, bindingLocation.name); break;
                }
            }
            return false;
        };
    }

    private static String[] _loadShaderFile(final String tag, final String... path) {
        final int size = path.length;
        final String[] result = new String[size];
        try {
            for (int i = 0; i < size; i++) {
                result[i] = Global.getSettings().loadText(path[i]);
            }
        } catch (IOException ex) {
            _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
            _LOG.error("'BoxUtil' shader file(s) loading error." + ex.getMessage());
            return null;
        }
        return result;
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     */
    public static int createShaderVF(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String vert, final String frag) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        if (!GLWrapper.Shader.valid()) {
            _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
            _LOG.warn("'BoxUtil' platform is not supported shader program.");
            return 0;
        }
        return createShaderProgram(tag, beforeLinkExc, new int[]{GLWrapper.Shader.Vert.GL_VERTEX_SHADER, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER}, vert, frag);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     */
    public static int createShaderVF(@Nullable final String loggerTag, final String vert, final String frag) {
        return createShaderVF(loggerTag, null, vert, frag);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     */
    public static int createShaderVFFormPath(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String vertPath, final String fragPath) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        final String[] src = _loadShaderFile(tag, vertPath, fragPath);
        if (src == null) return 0;
        return createShaderVF(tag, beforeLinkExc, src[0], src[1]);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     */
    public static int createShaderVFFormPath(@Nullable final String loggerTag, final String vertPath, final String fragPath) {
        return createShaderVFFormPath(loggerTag, null, vertPath, fragPath);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     */
    public static int createShaderVGF(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String vert, final String geom, final String frag) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        if (!GLWrapper.Shader.Geom.valid() || !GLWrapper.Shader.valid()) {
            _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
            _LOG.warn("'BoxUtil' platform is not supported OpenGL3.2.");
            return 0;
        }
        return createShaderProgram(tag, beforeLinkExc, new int[]{GLWrapper.Shader.Vert.GL_VERTEX_SHADER, GLWrapper.Shader.Geom.GL_GEOMETRY_SHADER, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER}, vert, geom, frag);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     */
    public static int createShaderVGF(@Nullable final String loggerTag, final String vert, final String geom, final String frag) {
        return createShaderVGF(loggerTag, null, vert, geom, frag);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     */
    public static int createShaderVGFFromPath(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String vertPath, final String geomPath, final String fragPath) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        final String[] src = _loadShaderFile(tag, vertPath, geomPath, fragPath);
        if (src == null) return 0;
        return createShaderVGF(tag, beforeLinkExc, src[0], src[1], src[2]);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     */
    public static int createShaderVGFFromPath(@Nullable final String loggerTag, final String vertPath, final String geomPath, final String fragPath) {
        return createShaderVGFFromPath(loggerTag, null, vertPath, geomPath, fragPath);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     */
    public static int createShaderVTF(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String vert, final String tessC, final String tessE, final String frag) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        if (!GLWrapper.Shader.Tess.valid() || !GLWrapper.Shader.valid()) {
            _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
            _LOG.warn("'BoxUtil' platform is not supported OpenGL4.0.");
            return 0;
        }
        return createShaderProgram(tag, beforeLinkExc, new int[]{GLWrapper.Shader.Vert.GL_VERTEX_SHADER, GLWrapper.Shader.Tess.GL_TESS_CONTROL_SHADER, GLWrapper.Shader.Tess.GL_TESS_EVALUATION_SHADER, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER}, vert, tessC, tessE, frag);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     */
    public static int createShaderVTF(@Nullable final String loggerTag, final String vert, final String tessC, final String tessE, final String frag) {
        return createShaderVTF(loggerTag, null, vert, tessC, tessE, frag);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     */
    public static int createShaderVTFFromPath(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String vertPath, final String tessCPath, final String tessEPath, final String fragPath) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        final String[] src = _loadShaderFile(tag, vertPath, tessCPath, tessEPath, fragPath);
        if (src == null) return 0;
        return createShaderVTF(tag, beforeLinkExc, src[0], src[1], src[2], src[3]);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     */
    public static int createShaderVTFFromPath(@Nullable final String loggerTag, final String vertPath, final String tessCPath, final String tessEPath, final String fragPath) {
        return createShaderVTFFromPath(loggerTag, null, vertPath, tessCPath, tessEPath, fragPath);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     */
    public static int createShaderVTGF(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String vert, final String tessC, final String tessE, final String geom, final String frag) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        if (!GLWrapper.Shader.Tess.valid() || !GLWrapper.Shader.Geom.valid() || !GLWrapper.Shader.valid()) {
            _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
            _LOG.warn("'BoxUtil' platform is not supported OpenGL4.0.");
            return 0;
        }
        return createShaderProgram(tag, beforeLinkExc, new int[]{GLWrapper.Shader.Vert.GL_VERTEX_SHADER, GLWrapper.Shader.Tess.GL_TESS_CONTROL_SHADER, GLWrapper.Shader.Tess.GL_TESS_EVALUATION_SHADER, GLWrapper.Shader.Geom.GL_GEOMETRY_SHADER, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER}, vert, tessC, tessE, geom, frag);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     */
    public static int createShaderVTGF(@Nullable final String loggerTag, final String vert, final String tessC, final String tessE, final String geom, final String frag) {
        return createShaderVTGF(loggerTag, null, vert, tessC, tessE, geom, frag);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     */
    public static int createShaderVTGFFromPath(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String vertPath, final String tessCPath, final String tessEPath, final String geomPath, final String fragPath) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        final String[] src = _loadShaderFile(tag, vertPath, tessCPath, tessEPath, geomPath, fragPath);
        if (src == null) return 0;
        return createShaderVTGF(tag, beforeLinkExc, src[0], src[1], src[2], src[3], src[4]);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     */
    public static int createShaderVTGFFromPath(@Nullable final String loggerTag, final String vertPath, final String tessCPath, final String tessEPath, final String geomPath, final String fragPath) {
        return createShaderVTGFFromPath(loggerTag, null, vertPath, tessCPath, tessEPath, geomPath, fragPath);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     * @param source may only have one shader source normally.
     */
    public static int createComputeShaders(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String... source) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        if (!GLWrapper.Shader.Comp.valid()) {
            _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
            _LOG.error("'BoxUtil' platform is not supported OpenGL4.3.");
            return 0;
        }
        int[] types = new int[source.length];
        Arrays.fill(types, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER);
        return createShaderProgram(tag, beforeLinkExc, types, source);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param source may only have one shader source normally.
     */
    public static int createComputeShaders(@Nullable String loggerTag, String... source) {
        return createComputeShaders(loggerTag, null, source);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     * @param shadersPath may only have one shader source normally.
     */
    public static int createComputeShadersFormPath(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String... shadersPath) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        final String[] src = _loadShaderFile(tag, shadersPath);
        if (src == null) return 0;
        return createComputeShaders(tag, beforeLinkExc, src);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param shadersPath may only have one shader source normally.
     */
    public static int createComputeShadersFormPath(@Nullable final String loggerTag, final String... shadersPath) {
        return createComputeShadersFormPath(loggerTag, null, shadersPath);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     */
    public static int createShader(@Nullable String loggerTag, String shader, int shaderType) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        if (!GLWrapper.Shader.valid()) {
            _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
            _LOG.error("'BoxUtil' platform is not supported shader program.");
            return 0;
        }
        String shaderTypeGetter = foundShaderName(shaderType);
        if (shaderTypeGetter == null) shaderTypeGetter = String.valueOf(shaderType);
        int shaderID = GLWrapper.Shader.glCreateShader(shaderType);
        GLWrapper.Shader.glShaderSource(shaderID, shader);
        GLWrapper.Shader.glCompileShader(shaderID);
        if (GLWrapper.Shader.glGetShaderi(shaderID, GLWrapper.Shader.GL_COMPILE_STATUS) != GLWrapper.Shader.GL_TRUE) {
            _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
            _LOG.error("'BoxUtil' shader type '" + shaderTypeGetter + "' compilation failed:\n" + GLWrapper.Shader.glGetShaderInfoLog(shaderID, GLWrapper.Shader.glGetShaderi(shaderID, GLWrapper.Shader.GL_INFO_LOG_LENGTH)));
            GLWrapper.Shader.glDeleteShader(shaderID);
            return 0;
        } else {
            return shaderID;
        }
    }

    private static byte _clearAndDeleteShader(final int programID, final List<Integer> tmpShaders) {
        if (!tmpShaders.isEmpty()) {
            for (int toDelete : tmpShaders) {
                GLWrapper.Shader.glDetachShader(programID, toDelete);
                GLWrapper.Shader.glDeleteShader(toDelete);
            }
        }
        GLWrapper.Shader.glDeleteProgram(programID);
        return 0;
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     */
    public static int createShaderProgram(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final int[] types, final String... shaders) {
        if (!GLWrapper.Shader.valid()) {
            _LOG.warn("'BoxUtil' platform is not supported OpenGL2.0.");
            return 0;
        }
        String tag = loggerTag == null ? "None marked" : loggerTag;
        if (shaders.length != types.length || shaders.length == 0) {
            _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
            _LOG.error("'BoxUtil' shader file's list's length and shader type list's length is mismatching.");
            return 0;
        }
        List<Integer> tmpShaders = new ArrayList<>();
        int programID = GLWrapper.Shader.glCreateProgram();
        for (int i = 0; i < shaders.length; i++) {
            int shaderID = createShader(loggerTag, shaders[i], types[i]);
            if (shaderID == 0) {
                String shaderTypeGetter = foundShaderName(types[i]);
                if (shaderTypeGetter == null) shaderTypeGetter = String.valueOf(types[i]);
                _LOG.error("'BoxUtil' shader file error with type: '" + shaderTypeGetter + "', creating program has canceled.");
                return _clearAndDeleteShader(programID, tmpShaders);
            }
            GLWrapper.Shader.glAttachShader(programID, shaderID);
            tmpShaders.add(shaderID);
        }
        if (beforeLinkExc != null && beforeLinkExc.runAndCheck(programID)) {
            _LOG.error("'BoxUtil' shader has error occurred when executing the before-link function(from 'beforeLinkExc' parameter), creating program has canceled.");
            return _clearAndDeleteShader(programID, tmpShaders);
        }
        GLWrapper.Shader.glLinkProgram(programID);

        if (GLWrapper.Shader.glGetProgrami(programID, GLWrapper.Shader.GL_LINK_STATUS) == GLWrapper.Shader.GL_FALSE) {
            _LOG.info("'BoxUtil' shader program tag: '" + tag + "'.");
            _LOG.error("'BoxUtil' shader program linking failed:\n" + GLWrapper.Shader.glGetProgramInfoLog(programID, GLWrapper.Shader.glGetProgrami(programID, GLWrapper.Shader.GL_INFO_LOG_LENGTH)));
            return _clearAndDeleteShader(programID, tmpShaders);
        } else {
            _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
            _LOG.info("'BoxUtil' shader program has created.");
            return programID;
        }
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     */
    public static int createShaderProgram(@Nullable final String loggerTag, final int[] types, final String... shaders) {
        return createShaderProgram(loggerTag, null, types, shaders);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param beforeLinkExc accept a <code>int</code> type program id, executing something after all of the <code>glAttachShader</code> calls but before <code>glLinkProgram</code> calls, returns <code>true</code> when some error has occurred.
     * @param shadersPath must be under the format:<table border = "1">
     *                    <tr><th>Shader Type</th><th>Legal Format</th></tr>
     *                    <tr><th>Vertex</th><th>*.vert *.vsh</th></tr>
     *                    <tr><th>Tess-Control</th><th>*.tesc</th></tr>
     *                    <tr><th>Tess-Evaluation</th><th>*.tese</th></tr>
     *                    <tr><th>Geometry</th><th>*.geom *.gsh</th></tr>
     *                    <tr><th>Fragment</th><th>*.frag *.fsh</th></tr>
     *                    <tr><th>Compute</th><th>*.comp *.csh</th></tr>
     *                    </table>
     */
    public static int createShaderProgramFromPath(@Nullable final String loggerTag, @Nullable final LinkCondition beforeLinkExc, final String... shadersPath) {
        String tag = loggerTag == null ? "None marked" : loggerTag;
        int length = shadersPath.length;
        int[] types = new int[length];
        String[] sources = new String[length];
        _LOG.info("'BoxUtil' shader creating tag: '" + tag + "'.");
        try {
            for (int i = 0; i < length; i++) {
                String path = shadersPath[i];
                int type = getTypeFromPath(path);
                if (type == 0) {
                    _LOG.error("'BoxUtil' error file format at: '" + path + "'.");
                    return 0;
                }
                sources[i] = Global.getSettings().loadText(path);
                types[i] = type;
            }
        } catch (IOException ex) {
            _LOG.error("'BoxUtil' shader file(s) loading error." + ex.getMessage());
            return 0;
        }
        return createShaderProgram(loggerTag, beforeLinkExc, types, sources);
    }

    /**
     * @param loggerTag for locating in log when created or failed.
     * @param shadersPath should be in the format:<table border = "1">
     *                    <tr><th>Shader Type</th><th>Legal Format</th></tr>
     *                    <tr><th>Vertex</th><th>*.vert *.vsh</th></tr>
     *                    <tr><th>Tess-Control</th><th>*.tesc</th></tr>
     *                    <tr><th>Tess-Evaluation</th><th>*.tese</th></tr>
     *                    <tr><th>Geometry</th><th>*.geom *.gsh</th></tr>
     *                    <tr><th>Fragment</th><th>*.frag *.fsh</th></tr>
     *                    <tr><th>Compute</th><th>*.comp *.csh</th></tr>
     *                    </table>
     */
    public static int createShaderProgramFromPath(@Nullable final String loggerTag, final String... shadersPath) {
        return createShaderProgramFromPath(loggerTag, null, shadersPath);
    }

    public static long createBindlessTexture(int texture) {
        if (texture == 0 || !GLWrapper.Texture.Bindless.valid()) return 0;
        final long id = GLWrapper.Texture.Bindless.glGetTextureHandle(texture);
        if (id != 0 && !GLWrapper.Texture.Bindless.glIsTextureHandleResident(id)) GLWrapper.Texture.Bindless.glMakeTextureHandleResident(id);
        return id;
    }

    public static long createBindlessImage(int texture, int internalFormat) {
        return createBindlessImage(texture, 0, false, 0, GLWrapper.Texture.GL_READ_WRITE, internalFormat);
    }

    public static long createBindlessImage(int texture, int level, boolean layered, int layer, int access, int internalFormat) {
        if (texture == 0 || !GLWrapper.Texture.Bindless.valid()) return 0;
        final long id = GLWrapper.Texture.Bindless.glGetImageHandle(texture, level, layered, layer, internalFormat);
        if (id != 0 && !GLWrapper.Texture.Bindless.glIsImageHandleResident(id)) GLWrapper.Texture.Bindless.glMakeImageHandleResident(id, access);
        return id;
    }

    public static void releaseBindlessTexture(long texture) {
        if (texture == 0 || !GLWrapper.Texture.Bindless.valid()) return;
        if (GLWrapper.Texture.Bindless.glIsTextureHandleResident(texture)) GLWrapper.Texture.Bindless.glMakeTextureHandleNonResident(texture);
    }

    public static void releaseBindlessImage(long image) {
        if (image == 0 || !GLWrapper.Texture.Bindless.valid()) return;
        if (GLWrapper.Texture.Bindless.glIsImageHandleResident(image)) GLWrapper.Texture.Bindless.glMakeImageHandleNonResident(image);
    }

    public static void blitFBO(int read, int draw, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1) {
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_READ_FRAMEBUFFER, read);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_DRAW_FRAMEBUFFER, draw);
        GLWrapper.FBO.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, GLWrapper.Operation.GL_COLOR_BUFFER_BIT, GLWrapper.Texture.GL_LINEAR);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
    }

    public static void blitFBO(int read, int draw, int srcX1, int srcY1, int dstX1, int dstY1) {
        blitFBO(read, draw, 0, 0, srcX1, srcY1, 0, 0, dstX1, dstY1);
    }

    public static void blitFBO(int read, int draw, int width, int height) {
        blitFBO(read, draw, 0, 0, width, height, 0, 0, width, height);
    }

    public static void blitFBO(int read, int draw) {
        final int width = ShaderCore.getScreenScaleWidth();
        final int height = ShaderCore.getScreenScaleHeight();
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_READ_FRAMEBUFFER, read);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_DRAW_FRAMEBUFFER, draw);
        GLWrapper.FBO.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GLWrapper.Operation.GL_COLOR_BUFFER_BIT, GLWrapper.Texture.GL_LINEAR);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
    }

    public static void copyFromScreen(int fbo) {
        final int width = ShaderCore.getScreenScaleWidth();
        final int height = ShaderCore.getScreenScaleHeight();
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_READ_FRAMEBUFFER, 0);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_DRAW_FRAMEBUFFER, fbo);
        GLWrapper.FBO.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GLWrapper.Operation.GL_COLOR_BUFFER_BIT, GLWrapper.Texture.GL_LINEAR);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
    }

    public static void blitToScreen(int fbo) {
        final int width = ShaderCore.getScreenScaleWidth();
        final int height = ShaderCore.getScreenScaleHeight();
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_READ_FRAMEBUFFER, fbo);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_DRAW_FRAMEBUFFER, 0);
        GLWrapper.FBO.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GLWrapper.Operation.GL_COLOR_BUFFER_BIT, GLWrapper.Texture.GL_LINEAR);
        GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
    }

    private static void initGLTex(int tex, int internalFormat, int width, int height, int format, int type) {
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, tex);
        GLWrapper.Texture.glTexImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, (ByteBuffer) null);
        GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, GLWrapper.Texture.GL_LINEAR);
        GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, GLWrapper.Texture.GL_LINEAR);
    }

    private static void initGLStorageTex(int tex, int levels, int internalFormat, int width, int height) {
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, tex);
        GLWrapper.Texture.glTexStorage2D(GLWrapper.Texture.GL_TEXTURE_2D, levels, internalFormat, width, height);
        GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, GLWrapper.Texture.GL_LINEAR);
        GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAG_FILTER, GLWrapper.Texture.GL_LINEAR);
    }

    private static void initGLStorageTex(int tex, int internalFormat, int width, int height) {
        initGLStorageTex(tex, 1, internalFormat, width, height);
    }

    private static int[] genSDFCore(int source, int checkChannel, int sourceOffsetX, int sourceOffsetY, int localWidth, int localHeight, int finalWidth, int finalHeight, int[] border, float outsideThreshold, byte step, float resultInsidePreMultiply, float resultOutsidePreMultiply, int resultTex, int resultOffsetX, int resultOffsetY, boolean genResultTex, boolean bit16OutMode) {
        int[] result = new int[3];
        result[0] = resultTex;
        if (localWidth < 1 || localHeight < 1) return result;
        result[1] = finalWidth;
        result[2] = finalHeight;
        if (!ShaderCore.isSDFGenValid() || source < 1 || (resultTex < 1 && !genResultTex)) return result;

        int tmpTex = GLWrapper.Texture.glGenTextures();
        initGLStorageTex(tmpTex, GLWrapper.Texture.GL_RGBA16UI, result[1], result[2]);
        if (genResultTex) {
            result[0] = GLWrapper.Texture.glGenTextures();
            initGLStorageTex(result[0], bit16OutMode ? GLWrapper.Texture.GL_R16 : GLWrapper.Texture.GL_R8, result[1], result[2]);
        }
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);

        final int itemDimX = (int) Math.ceil(result[1] / (BoxDatabase.isGLDeviceAMD() ? 8.0f : 4.0f));
        final int itemDimY = (int) Math.ceil(result[2] / 8.0f);
        int channelPick = 3;
        switch (checkChannel) {
            case GLWrapper.Texture.GL_RED:
                channelPick = 0;
                break;
            case GLWrapper.Texture.GL_GREEN:
                channelPick = 1;
                break;
            case GLWrapper.Texture.GL_BLUE:
                channelPick = 2;
                break;
            case GLWrapper.Texture.GL_ALPHA:
                break;
            case GLWrapper.Texture.GL_RGB:
                channelPick = 4;
                break;
        }
        BaseShaderData program = ShaderCore.getSDFInitProgram();
        program.active();
        program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, channelPick);
        program.bindTexture2D(0, source);
        program.putBindingImageTextureWriteOnly(0, tmpTex, GLWrapper.Texture.GL_RGBA16I);
        GLWrapper.Shader.glUniform4i(program.location[0], localWidth, localHeight, result[1], result[2]);
        GLWrapper.Shader.glUniform4i(program.location[1], border[0], border[1], sourceOffsetX, sourceOffsetY);
        GLWrapper.Shader.glUniform1f(program.location[2], outsideThreshold);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        program = ShaderCore.getSDFProcessProgram();
        program.active();
        program.bindTexture2D(0, tmpTex);
        program.putBindingImageTextureWriteOnly(0, tmpTex, GLWrapper.Texture.GL_RGBA16I);
        GLWrapper.Shader.glUniform2i(program.location[0], result[1], result[2]);
        for (int i = 1 << Math.min(Math.max(step, 0), 30); i > 0; i = i >>> 1) {
            GLWrapper.Shader.glUniform1i(program.location[1], i);
            GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
            GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        }
        program = ShaderCore.getSDFResultProgram();
        program.active();
        program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, bit16OutMode ? 1 : 0);
        program.putBindingImageTextureWriteOnly(bit16OutMode ? 1 : 0, result[0], bit16OutMode ? GLWrapper.Texture.GL_R16 : GLWrapper.Texture.GL_R8);
        GLWrapper.Shader.glUniform4i(program.location[0], result[1], result[2], resultOffsetX, resultOffsetY);
        GLWrapper.Shader.glUniform2f(program.location[1], resultInsidePreMultiply, resultOutsidePreMultiply);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        program.close();
        GLWrapper.Texture.glDeleteTextures(tmpTex);
        return result;
    }

    /**
     * Fast GPU SDF generation method.<p>
     * <strong>OpenGL 4.3 required, compute shader supported required.</strong>
     *
     * @param source must be RGBA8 texture.
     * @param checkChannel valid value: {@link GL11#GL_RED}, {@link GL11#GL_GREEN}, {@link GL11#GL_BLUE}, {@link GL11#GL_ALPHA}, {@link GL11#GL_RGB}; default is {@link GL11#GL_ALPHA}.
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     * @param extraWidth border width for sdf texture, positive integer value required.
     * @param extraHeight border height for sdf texture, positive integer value required.
     * @param outsideThreshold when pixel check value less than or equal the value, it will be considered as outside.
     * @param step 8 or 9 for general usage, range from 0 to 30; also you can use <code>CalculateUtil.getExponentPOTMin(Math.max(localWidth, localHeight))</code> for automatic step calculation.
     * @param resultInsidePreMultiply 0.01 or (1.0f / required thickness) for general usage.
     * @param resultOutsidePreMultiply 0.01 or (1.0f / max(extraWidth, extraHeight)) for general usage.
     * @param resultTex texture to store result, must be R8 texture and size must be greater than or equal to the final size.
     * @param resultOffsetX the result texture region left-bottom origin x-position.
     * @param resultOffsetY the result texture region left-bottom origin x-position.
     * @param bit16OutMode true for R16 texture, false for R8 texture.
     * @return generated sdf texture with R8/R16 NPOT at [0.0, 1.0], texture return 0 if failed; int[] = {texture, width, height}
     */
    public static int[] genSDF(int source, int checkChannel, int sourceOffsetX, int sourceOffsetY, int localWidth, int localHeight, int extraWidth, int extraHeight, float outsideThreshold, byte step, float resultInsidePreMultiply, float resultOutsidePreMultiply, int resultTex, int resultOffsetX, int resultOffsetY, boolean bit16OutMode) {
        int[] border = new int[]{Math.abs(extraWidth), Math.abs(extraHeight)};
        return genSDFCore(source, checkChannel, sourceOffsetX, sourceOffsetY, localWidth, localHeight, localWidth + border[0] + border[0], localHeight + border[1] + border[1], border, outsideThreshold, step, resultInsidePreMultiply, resultOutsidePreMultiply, resultTex, resultOffsetX, resultOffsetY, false, bit16OutMode);
    }

    /**
     * Fast GPU SDF generation method.<p>
     * <strong>OpenGL 4.3 required, compute shader supported required.</strong>
     *
     * @param source must be RGBA8 texture.
     * @param checkChannel valid value: {@link GL11#GL_RED}, {@link GL11#GL_GREEN}, {@link GL11#GL_BLUE}, {@link GL11#GL_ALPHA}, {@link GL11#GL_RGB}; default is {@link GL11#GL_ALPHA}.
     * @param extraWidth border width for sdf texture, positive integer value required.
     * @param extraHeight border height for sdf texture, positive integer value required.
     * @param outsideThreshold when pixel check value less than or equal the value, it will be considered as outside.
     * @param step 8 or 9 for general usage, range from 0 to 30; also you can use <code>CalculateUtil.getExponentPOTMin(Math.max(localWidth, localHeight))</code> for automatic step calculation.
     * @param resultInsidePreMultiply 0.01 or (1.0f / required thickness) for general usage.
     * @param resultOutsidePreMultiply 0.01 or (1.0f / max(extraWidth, extraHeight)) for general usage.
     * @param resultTex texture to store result, must be R8 texture and size must be greater than or equal to the final size.
     * @param bit16OutMode true for R16 texture, false for R8 texture.
     * @return generated sdf texture with R8/R16 NPOT at [0.0, 1.0], texture return 0 if failed; int[] = {texture, width, height}
     */
    public static int[] genSDF(int source, int checkChannel, int localWidth, int localHeight, int extraWidth, int extraHeight, float outsideThreshold, byte step, float resultInsidePreMultiply, float resultOutsidePreMultiply, int resultTex, boolean bit16OutMode) {
        return genSDF(source, checkChannel, 0, 0, localWidth, localHeight, extraWidth, extraHeight, outsideThreshold, step, resultInsidePreMultiply, resultOutsidePreMultiply, resultTex, 0, 0, bit16OutMode);
    }

    /**
     * Fast GPU SDF generation method.<p>
     * <strong>OpenGL 4.3 required, compute shader supported required.</strong>
     *
     * @param source must be RGBA8 texture.
     * @param checkChannel valid value: {@link GL11#GL_RED}, {@link GL11#GL_GREEN}, {@link GL11#GL_BLUE}, {@link GL11#GL_ALPHA}, {@link GL11#GL_RGB}; default is {@link GL11#GL_ALPHA}.
     * @param extraWidth border width for sdf texture, positive integer value required.
     * @param extraHeight border height for sdf texture, positive integer value required.
     * @param outsideThreshold when pixel check value less than or equal the value, it will be considered as outside.
     * @param step 8 or 9 for general usage, range from 0 to 30; also you can use <code>CalculateUtil.getExponentPOTMin(Math.max(localWidth, localHeight))</code> for automatic step calculation.
     * @param resultInsidePreMultiply 0.01 or (1.0f / required thickness) for general usage.
     * @param resultOutsidePreMultiply 0.01 or (1.0f / max(extraWidth, extraHeight)) for general usage.
     * @param resultTex texture to store result, must be R8 texture and size must be greater than or equal to the final size.
     * @return generated sdf texture with R8 NPOT at [0.0, 1.0], texture return 0 if failed; int[] = {texture, width, height}
     */
    public static int[] genSDF(int source, int checkChannel, int localWidth, int localHeight, int extraWidth, int extraHeight, float outsideThreshold, byte step, float resultInsidePreMultiply, float resultOutsidePreMultiply, int resultTex) {
        return genSDF(source, checkChannel, localWidth, localHeight, extraWidth, extraHeight, outsideThreshold, step, resultInsidePreMultiply, resultOutsidePreMultiply, resultTex, false);
    }

    /**
     * Fast GPU SDF generation method.<p>
     * <strong>OpenGL 4.3 required, compute shader supported required.</strong>
     *
     * @param source must be RGBA8 texture.
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     * @param checkChannel valid value: {@link GL11#GL_RED}, {@link GL11#GL_GREEN}, {@link GL11#GL_BLUE}, {@link GL11#GL_ALPHA}, {@link GL11#GL_RGB}; default is {@link GL11#GL_ALPHA}.
     * @param extraWidth border width for sdf texture, positive integer value required.
     * @param extraHeight border height for sdf texture, positive integer value required.
     * @param outsideThreshold when pixel check value less than or equal the value, it will be considered as outside.
     * @param step 8 or 9 for general usage, range from 0 to 30; also you can use <code>CalculateUtil.getExponentPOTMin(Math.max(localWidth, localHeight))</code> for automatic step calculation.
     * @param resultInsidePreMultiply 0.01 or (1.0f / required thickness) for general usage.
     * @param resultOutsidePreMultiply 0.01 or (1.0f / max(extraWidth, extraHeight)) for general usage.
     * @param bit16OutMode true for R16 texture, false for R8 texture.
     * @return generated sdf texture with R8/R16 NPOT at [0.0, 1.0], texture return 0 if failed; int[] = {texture, width, height}
     */
    public static int[] genSDF(int source, int checkChannel, int sourceOffsetX, int sourceOffsetY, int localWidth, int localHeight, int extraWidth, int extraHeight, float outsideThreshold, byte step, float resultInsidePreMultiply, float resultOutsidePreMultiply, boolean bit16OutMode) {
        int[] border = new int[]{Math.abs(extraWidth), Math.abs(extraHeight)};
        return genSDFCore(source, checkChannel, sourceOffsetX, sourceOffsetY, localWidth, localHeight, localWidth + border[0] + border[0], localHeight + border[1] + border[1], border, outsideThreshold, step, resultInsidePreMultiply, resultOutsidePreMultiply, 0, 0, 0, true, bit16OutMode);
    }

    /**
     * Fast GPU SDF generation method.<p>
     * <strong>OpenGL 4.3 required, compute shader supported required.</strong>
     *
     * @param source must be RGBA8 texture.
     * @param checkChannel valid value: {@link GL11#GL_RED}, {@link GL11#GL_GREEN}, {@link GL11#GL_BLUE}, {@link GL11#GL_ALPHA}, {@link GL11#GL_RGB}; default is {@link GL11#GL_ALPHA}.
     * @param extraWidth border width for sdf texture, positive integer value required.
     * @param extraHeight border height for sdf texture, positive integer value required.
     * @param outsideThreshold when pixel check value less than or equal the value, it will be considered as outside.
     * @param step 8 or 9 for general usage, range from 0 to 30; also you can use <code>CalculateUtil.getExponentPOTMin(Math.max(localWidth, localHeight))</code> for automatic step calculation.
     * @param resultInsidePreMultiply 0.01 or (1.0f / required thickness) for general usage.
     * @param resultOutsidePreMultiply 0.01 or (1.0f / max(extraWidth, extraHeight)) for general usage.
     * @param bit16OutMode true for R16 texture, false for R8 texture.
     * @return generated sdf texture with R8/R16 NPOT at [0.0, 1.0], texture return 0 if failed; int[] = {texture, width, height}
     */
    public static int[] genSDF(int source, int checkChannel, int localWidth, int localHeight, int extraWidth, int extraHeight, float outsideThreshold, byte step, float resultInsidePreMultiply, float resultOutsidePreMultiply, boolean bit16OutMode) {
        int[] border = new int[]{Math.abs(extraWidth), Math.abs(extraHeight)};
        return genSDFCore(source, checkChannel, 0, 0, localWidth, localHeight, localWidth + border[0] + border[0], localHeight + border[1] + border[1], border, outsideThreshold, step, resultInsidePreMultiply, resultOutsidePreMultiply, 0, 0, 0, true, bit16OutMode);
    }

    /**
     * Fast GPU SDF generation method.<p>
     * <strong>OpenGL 4.3 required, compute shader supported required.</strong>
     *
     * @param source must be RGBA8 texture.
     * @param checkChannel valid value: {@link GL11#GL_RED}, {@link GL11#GL_GREEN}, {@link GL11#GL_BLUE}, {@link GL11#GL_ALPHA}, {@link GL11#GL_RGB}; default is {@link GL11#GL_ALPHA}.
     * @param extraWidth border width for sdf texture, positive integer value required.
     * @param extraHeight border height for sdf texture, positive integer value required.
     * @param outsideThreshold when pixel check value less than or equal the value, it will be considered as outside.
     * @param step 8 or 9 for general usage, range from 0 to 30; also you can use <code>CalculateUtil.getExponentPOTMin(Math.max(localWidth, localHeight))</code> for automatic step calculation.
     * @param resultInsidePreMultiply 0.01 or (1.0f / required thickness) for general usage.
     * @param resultOutsidePreMultiply 0.01 or (1.0f / max(extraWidth, extraHeight)) for general usage.
     * @return generated sdf texture with R8 NPOT at [0.0, 1.0], texture return 0 if failed; int[] = {texture, width, height}
     */
    public static int[] genSDF(int source, int checkChannel, int localWidth, int localHeight, int extraWidth, int extraHeight, float outsideThreshold, byte step,float resultInsidePreMultiply, float resultOutsidePreMultiply) {
        return genSDF(source, checkChannel, localWidth, localHeight, extraWidth, extraHeight, outsideThreshold, step, resultInsidePreMultiply, resultOutsidePreMultiply, false);
    }

    /**
     * Classical radial blur effect.<p>
     * Draw blur effect form source to attachment of current framebuffer, source texture size should be consistent with attachment of current framebuffer size.<p>
     * <strong>OpenGL 2.0 required.</strong>
     *
     * @param center center of blur effect, mapping range from 0.0 to 1.0.
     * @param samples blur iteration, minimum value is 1 (No any effect), general value is 32, larger is better but slower.
     * @param radius the blur "strength", general value is 0.1, but higher value will need more samples to get better effect.
     * @param alphaStrength default value is 1.0.
     */
    public static void applyRadialBlur(int source, @NotNull Vector2f center, short samples, float radius, float alphaStrength, boolean isAdditiveBlend) {
        if (!ShaderCore.isRadialBlurValid() || source < 1) return;
        BaseShaderData program = ShaderCore.getRadialBlurProgram();
        GL11.glPushClientAttrib(GL11.GL_CLIENT_VERTEX_ARRAY_BIT);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GLWrapper.Operation.glPushAttrib(GLWrapper.Operation.GL_ENABLE_BIT);
        program.active();
        GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_BLEND);
        GLWrapper.Operation.glBlendFunc(GLWrapper.Operation.GL_SRC_ALPHA, isAdditiveBlend ? GLWrapper.Operation.GL_ONE : GLWrapper.Operation.GL_ONE_MINUS_SRC_ALPHA);
        program.bindTexture2D(0, source);
        float samplesInv = 1.0f / Math.max(samples, 1);
        GLWrapper.Shader.glUniform4f(program.location[0], center.x, center.y, samplesInv * radius, samplesInv);
        GLWrapper.Shader.glUniform1f(program.location[1], alphaStrength);
        GL11.glVertexPointer(2, GLWrapper.DataType.GL_BYTE, 0, CommonUtil.createByteBuffer(BoxEnum.NEG_ONE, BoxEnum.NEG_ONE, BoxEnum.ONE, BoxEnum.NEG_ONE, BoxEnum.NEG_ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE));
        GLWrapper.Drawcall.glDrawArrays(GLWrapper.Drawcall.GL_TRIANGLE_STRIP, 0, 4);
        program.close();
        GLWrapper.Operation.glPopAttrib();
        GL11.glPopClientAttrib();
    }

    /**
     * Classical radial blur effect.<p>
     * Draw blur effect form source to attachment of current framebuffer, source texture size should be consistent with attachment of current framebuffer size.<p>
     * <strong>OpenGL 2.0 required.</strong>
     *
     * @param center center of blur effect, mapping range from 0.0 to 1.0.
     * @param samples blur iteration, minimum value is 1 (No any effect), general value is 32, larger is better but slower.
     * @param radius the blur "strength", general value is 0.1, but higher value will need more samples to get better effect.
     * @param alphaStrength default value is 1.0.
     */
    public static void applyRadialBlur(int source, @NotNull Vector2f center, short samples, float radius, float alphaStrength, int dstX, int dstY, int dstWidth, int dstHeight, boolean isAdditiveBlend) {
        GLWrapper.Operation.glPushAttrib(GLWrapper.Operation.GL_VIEWPORT_BIT);
        GLWrapper.Operation.glViewport(dstX, dstY, dstWidth, dstHeight);
        applyRadialBlur(source, center, samples, radius, alphaStrength, isAdditiveBlend);
        GLWrapper.Operation.glPopAttrib();
    }

    /**
     * Simple gaussian blur computing, only for RGBA and R texture.
     *
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     * @param useRed true for Red texture, false for RGBA texture.
     * @param step copy texture when less than <code>1</code>.
     * @param resultOffsetX the result texture region left-bottom origin x-position.
     * @param resultOffsetY the result texture region left-bottom origin x-position.
     * @param bit16OutMode true for 16bit per channel texture, false for 8bit per channel texture.
     */
    public static void applyImageGaussianBlur(int source, int sourceOffsetX, int sourceOffsetY, boolean useRed, byte step, int texWidth, int texHeight, int result, int resultOffsetX, int resultOffsetY, boolean bit16OutMode) {
        if (!ShaderCore.isCompGaussianBlurValid() || source < 1 || result < 1 || texWidth < 1 || texHeight < 1) return;
        final boolean useFilter = (step > 0);
        if (!useFilter && source == result) return;
        BaseShaderData program = useRed ? ShaderCore.getCompGaussianBlurRedProgram() : ShaderCore.getCompGaussianBlurProgram();
        final int itemDimX = (int) Math.ceil(texWidth / (BoxDatabase.isGLDeviceAMD() ? 8.0f : 4.0f));
        final int itemDimY = (int) Math.ceil(texHeight / 8.0f);
        final int formatOut = useRed ? (bit16OutMode ? GLWrapper.Texture.GL_R16 : GLWrapper.Texture.GL_R8) : (bit16OutMode ? GLWrapper.Texture.GL_RGBA16 : GLWrapper.Texture.GL_RGBA8);
        final byte outIndex = (byte) (bit16OutMode ? 1 : 0);
        final int[] sub = new int[]{
                bit16OutMode ? program.subroutineLocation[0][1] : program.subroutineLocation[0][0],
                useFilter ? program.subroutineLocation[0][2] : program.subroutineLocation[0][3]
        };
        int tmpTex = 0;
        if (useFilter) {
            tmpTex = GLWrapper.Texture.glGenTextures();
            initGLStorageTex(tmpTex, formatOut, texWidth, texHeight);
        }

        program.active();
        program.putUniformSubroutines(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, sub);
        GLWrapper.Shader.glUniform3i(program.location[0], texWidth, texHeight, step);
        program.bindTexture2D(0, source);
        if (useFilter) {
            GLWrapper.Shader.glUniform4i(program.location[1], sourceOffsetX, sourceOffsetY, resultOffsetX, resultOffsetY);
            GLWrapper.Shader.glUniform1i(program.location[2], 0);
            GLWrapper.Shader.glUniform1f(program.location[3], 1.0f / (step * 0.1111111f * TrigUtil.PI_F));
            program.putBindingImageTextureWriteOnly(outIndex, tmpTex, formatOut);
            GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
            GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
            GLWrapper.Shader.glUniform1i(program.location[2], 1);
            program.bindTexture2D(0, tmpTex);
        }
        program.putBindingImageTextureWriteOnly(outIndex, result, formatOut);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        program.close();
        if (useFilter) GLWrapper.Texture.glDeleteTextures(tmpTex);
    }

    /**
     * Simple gaussian blur computing, only for RGBA and R texture.
     *
     * @param useRed true for Red texture, false for RGBA texture.
     * @param step copy texture when less than <code>1</code>.
     * @param bit16OutMode true for 16bit per channel texture, false for 8bit per channel texture.
     */
    public static void applyImageGaussianBlur(int source, boolean useRed, byte step, int texWidth, int texHeight, int result, boolean bit16OutMode) {
        applyImageGaussianBlur(source, 0, 0, useRed, step, texWidth, texHeight, result, 0, 0, bit16OutMode);
    }

    @Deprecated
    public static void applyImageGaussianBlur(int source, boolean useRed, byte step, int texWidth, int texHeight, int result, boolean bit16InMode, boolean bit16OutMode) {
        applyImageGaussianBlur(source, useRed, step, texWidth, texHeight, result, bit16OutMode);
    }

    /**
     * Simple gaussian blur computing, only for RGBA and R texture.
     *
     * @param useRed true for Red texture, false for RGBA texture.
     * @param step copy texture when less than <code>1</code>.
     * @param bit16OutMode true for 16bit per channel texture, false for 8bit per channel texture.
     */
    public static void applyImageGaussianBlur(int source, boolean useRed, byte step, int texWidth, int texHeight, boolean bit16OutMode) {
        applyImageGaussianBlur(source, useRed, step, texWidth, texHeight, source, bit16OutMode);
    }

    @Deprecated
    public static void applyImageGaussianBlur(int source, boolean useRed, byte step, int texWidth, int texHeight, boolean bit16InMode, boolean bit16OutMode) {
        applyImageGaussianBlur(source, useRed, step, texWidth, texHeight, source, bit16OutMode);
    }

    /**
     * Separated GPU bilateral filter.
     *
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     * @param useRed true for Red texture, false for RGBA texture.
     * @param result Not recommended use same result texture as source texture in separated bilateral filter, otherwise will lose some detail and edge.
     * @param resultOffsetX the result texture region left-bottom origin x-position.
     * @param resultOffsetY the result texture region left-bottom origin x-position.
     * @param bit16OutMode true for 16bit per channel texture, false for 8bit per channel texture.
     */
    public static void applyImageBilateralFilter(int source, int sourceOffsetX, int sourceOffsetY, boolean useRed, byte radius, float sigmaSpace, float sigmaRange, int texWidth, int texHeight, int result, int resultOffsetX, int resultOffsetY, boolean bit16OutMode) {
        if (!ShaderCore.isCompBilateralFilterValid() || source < 1 || result < 1 || texWidth < 1 || texHeight < 1) return;
        BaseShaderData program = useRed ? ShaderCore.getCompBilateralFilterRedProgram() : ShaderCore.getCompBilateralFilterProgram();
        final int itemDimX = (int) Math.ceil(texWidth / (BoxDatabase.isGLDeviceAMD() ? 8.0f : 4.0f));
        final int itemDimY = (int) Math.ceil(texHeight / 8.0f);
        final int formatOut = useRed ? (bit16OutMode ? GLWrapper.Texture.GL_R16 : GLWrapper.Texture.GL_R8) : (bit16OutMode ? GLWrapper.Texture.GL_RGBA16 : GLWrapper.Texture.GL_RGBA8);
        final byte outIndex = (byte) (bit16OutMode ? 1 : 0);
        int tmpTex = GLWrapper.Texture.glGenTextures();
        initGLStorageTex(tmpTex, formatOut, texWidth, texHeight);

        float gsInv = sigmaSpace * sigmaSpace;
        gsInv = -1.0f / (gsInv + gsInv);
        float grInv = sigmaRange * sigmaRange;
        grInv = -1.0f / (grInv + grInv);
        program.active();
        program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, bit16OutMode ? 1 : 0);
        program.bindTexture2D(0, source);
        program.bindTexture2D(1, source);
        program.putBindingImageTextureWriteOnly(outIndex, tmpTex, formatOut);
        GLWrapper.Shader.glUniform3i(program.location[0], texWidth, texHeight, radius);
        GLWrapper.Shader.glUniform4i(program.location[1], sourceOffsetX, sourceOffsetY, resultOffsetX, resultOffsetY);
        GLWrapper.Shader.glUniform1i(program.location[2], 0);
        GLWrapper.Shader.glUniform2f(program.location[3], gsInv, grInv);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        program.bindTexture2D(1, tmpTex);
        program.putBindingImageTextureWriteOnly(outIndex, result, formatOut);
        GLWrapper.Shader.glUniform1i(program.location[2], 1);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        program.close();
        GLWrapper.Texture.glDeleteTextures(tmpTex);
        GLWrapper.Drawcall.MultiTex.glActiveTexture(GLWrapper.Drawcall.MultiTex.GL_TEXTURE0);
    }

    /**
     * Separated GPU bilateral filter.<p>
     * Not recommended use this method, it uses same result texture as source texture that will lose some detail and edge.
     *
     * @param useRed true for Red texture, false for RGBA texture.
     * @param bit16OutMode true for 16bit per channel texture, false for 8bit per channel texture.
     */
    public static void applyImageBilateralFilter(int source, boolean useRed, byte radius, float sigmaSpace, float sigmaRange, int texWidth, int texHeight, int result, boolean bit16OutMode) {
        applyImageBilateralFilter(source, 0, 0, useRed, radius, sigmaSpace, sigmaRange, texWidth, texHeight, result, 0, 0, bit16OutMode);
    }

    @Deprecated
    public static void applyImageBilateralFilter(int source, boolean useRed, byte radius, float sigmaSpace, float sigmaRange, int texWidth, int texHeight, int result, boolean bit16InMode, boolean bit16OutMode) {
        applyImageBilateralFilter(source, useRed, radius, sigmaSpace, sigmaRange, texWidth, texHeight, result, bit16OutMode);
    }

    /**
     * Separated GPU bilateral filter.<p>
     * Not recommended use this method, it uses same result texture as source texture that will lose some detail and edge.
     *
     * @param useRed true for Red texture, false for RGBA texture.
     * @param bit16OutMode true for 16bit per channel texture, false for 8bit per channel texture.
     */
    public static void applyImageBilateralFilter(int source, boolean useRed, byte radius, float sigmaSpace, float sigmaRange, int texWidth, int texHeight, boolean bit16OutMode) {
        applyImageBilateralFilter(source, useRed, radius, sigmaSpace, sigmaRange, texWidth, texHeight, source, bit16OutMode);
    }

    @Deprecated
    public static void applyImageBilateralFilter(int source, boolean useRed, byte radius, float sigmaSpace, float sigmaRange, int texWidth, int texHeight, boolean bit16InMode, boolean bit16OutMode) {
        applyImageBilateralFilter(source, useRed, radius, sigmaSpace, sigmaRange, texWidth, texHeight, source, bit16OutMode);
    }

    private static int imageDFTCore(int source, int sourceOffsetX, int sourceOffsetY, boolean useRed, int texWidth, int texHeight, int result, int resultOffsetX, int resultOffsetY, boolean genResultTex, boolean f16OutMode) {
        if (!ShaderCore.isDiscreteFourierValid() || source < 1 || (!genResultTex && result < 1) || texWidth < 1 || texHeight < 1) return 0;
        final int itemDimX = (int) Math.ceil(texWidth / (BoxDatabase.isGLDeviceAMD() ? 8.0f : 4.0f));
        final int itemDimY = (int) Math.ceil(texHeight / 8.0f);
        final int formatOut = useRed ? (f16OutMode ? GLWrapper.Texture.GL_R16F : GLWrapper.Texture.GL_R32F) : (f16OutMode ? GLWrapper.Texture.GL_RGBA16F : GLWrapper.Texture.GL_RGBA32F);
        final byte outIndex = (byte) (f16OutMode ? 1 : 2);

        int tmpTex = GLWrapper.Texture.glGenTextures();
        initGLStorageTex(tmpTex, formatOut, texWidth + texWidth, texHeight);
        if (genResultTex) {
            result = GLWrapper.Texture.glGenTextures();
            initGLStorageTex(result, formatOut, texWidth + texWidth, texHeight);
        }
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);

        BaseShaderData program = useRed ? ShaderCore.getDFTRedProgram() : ShaderCore.getDFTProgram();
        program.active();
        program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, f16OutMode ? 1 : 2);
        program.bindTexture2D(0, source);
        program.putBindingImageTextureWriteOnly(outIndex, tmpTex, formatOut);
        GLWrapper.Shader.glUniform4i(program.location[0], sourceOffsetX, sourceOffsetY, resultOffsetX, resultOffsetY);
        GLWrapper.Shader.glUniform2i(program.location[1], texWidth, texHeight);
        GLWrapper.Shader.glUniform1i(program.location[2], 0b100);
        GLWrapper.Shader.glUniform2f(program.location[3], 1.0f / texWidth, 1.0f / texHeight);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        program.bindTexture2D(0, tmpTex);
        program.putBindingImageTextureWriteOnly(outIndex, result, formatOut);
        GLWrapper.Shader.glUniform1i(program.location[2], 0b10010);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        program.close();
        GLWrapper.Texture.glDeleteTextures(tmpTex);
        return result;
    }

    private static int imageIDFTCore(int source, int sourceOffsetX, int sourceOffsetY, boolean useRed, int texWidth, int texHeight, int result, int resultOffsetX, int resultOffsetY, boolean genResultTex, boolean f16InMode, boolean f16OutMode) {
        if (!ShaderCore.isDiscreteFourierValid() || source < 1 || (!genResultTex && result < 1) || texWidth < 1 || texHeight < 1) return 0;
        final int itemDimX = (int) Math.ceil(texWidth / (BoxDatabase.isGLDeviceAMD() ? 8.0f : 4.0f));
        final int itemDimY = (int) Math.ceil(texHeight / 8.0f);
        final int formatIn = useRed ? (f16InMode ? GLWrapper.Texture.GL_R16F : GLWrapper.Texture.GL_R32F) : (f16InMode ? GLWrapper.Texture.GL_RGBA16F : GLWrapper.Texture.GL_RGBA32F);
        final int formatOut = useRed ? (f16OutMode ? GLWrapper.Texture.GL_R16F : GLWrapper.Texture.GL_R8) : (f16OutMode ? GLWrapper.Texture.GL_RGBA16F : GLWrapper.Texture.GL_RGBA8);

        int tmpTex = GLWrapper.Texture.glGenTextures();
        initGLStorageTex(tmpTex, formatIn, texWidth + texWidth, texHeight);
        if (genResultTex) {
            result = GLWrapper.Texture.glGenTextures();
            initGLStorageTex(result, formatOut, texWidth, texHeight);
        }
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);

        BaseShaderData program = useRed ? ShaderCore.getDFTRedProgram() : ShaderCore.getDFTProgram();
        program.active();
        program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, f16InMode ? 1 : 2);
        program.bindTexture2D(0, source);
        program.putBindingImageTextureWriteOnly(f16InMode ? 1 : 2, tmpTex, formatIn);
        GLWrapper.Shader.glUniform4i(program.location[0], sourceOffsetX, sourceOffsetY, resultOffsetX, resultOffsetY);
        GLWrapper.Shader.glUniform2i(program.location[1], texWidth, texHeight);
        GLWrapper.Shader.glUniform1i(program.location[2], 0b1);
        GLWrapper.Shader.glUniform2f(program.location[3], 1.0f / texWidth, 1.0f / texHeight);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        program.bindTexture2D(0, tmpTex);
        program.putBindingImageTextureWriteOnly(f16OutMode ? 1 : 0, result, formatOut);
        GLWrapper.Shader.glUniform1i(program.location[2], 0b11011);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        program.close();
        GLWrapper.Texture.glDeleteTextures(tmpTex);
        return result;
    }

    /**
     * GPU DFT(Discrete Fourier Transform), slow but still faster more than CPU computing.
     *
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     * @param useRed true for Red texture, false for RGBA texture.
     * @param texWidth spatial domain texture width, <strong>NOT</strong> frequency domain texture width.
     * @param resultOffsetX the result texture region left-bottom origin x-position.
     * @param resultOffsetY the result texture region left-bottom origin x-position.
     * @param f16OutMode true for f16 per channel texture, false for f32 per channel texture.
     *
     * @return generated centered f16/f32 complex spectrum texture;<p>
     *     R with double size(left area R for real part of complex, right area R for imaginary part of complex) for useRed;<p>
     *     RGBA with double size(left area R+iG/B+iA complex, right area R+iG/B+iA complex) for not useRed;<p>
     *     returns 0 if failed.
     */
    public static int imageDFT(int source, int sourceOffsetX, int sourceOffsetY, boolean useRed, int texWidth, int texHeight, int result, int resultOffsetX, int resultOffsetY, boolean f16OutMode) {
        return imageDFTCore(source, sourceOffsetX, sourceOffsetY, useRed, texWidth, texHeight, result, resultOffsetX, resultOffsetY, false, f16OutMode);
    }

    /**
     * GPU DFT(Discrete Fourier Transform), slow but still faster more than CPU computing.
     *
     * @param useRed true for Red texture, false for RGBA texture.
     * @param texWidth spatial domain texture width, <strong>NOT</strong> frequency domain texture width.
     * @param f16OutMode true for f16 per channel texture, false for f32 per channel texture.
     *
     * @return generated centered f16/f32 complex spectrum texture;<p>
     *     R with double size(left area R for real part of complex, right area R for imaginary part of complex) for useRed;<p>
     *     RGBA with double size(left area R+iG/B+iA complex, right area R+iG/B+iA complex) for not useRed;<p>
     *     returns 0 if failed.
     */
    public static int imageDFT(int source, boolean useRed, int texWidth, int texHeight, int result, boolean f16OutMode) {
        return imageDFT(source, 0, 0, useRed, texWidth, texHeight, result, 0, 0, f16OutMode);
    }

    @Deprecated
    public static int imageDFT(int source, boolean useRed, int texWidth, int texHeight, int result, boolean f16InMode, boolean f16OutMode) {
        return imageDFT(source, useRed, texWidth, texHeight, result, f16OutMode);
    }

    /**
     * GPU DFT(Discrete Fourier Transform), slow but still faster more than CPU computing.
     *
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     * @param useRed true for Red texture, false for RGBA texture.
     * @param texWidth spatial domain texture width, <strong>NOT</strong> frequency domain texture width.
     * @param f16OutMode true for f16 per channel texture, false for f32 per channel texture.
     *
     * @return generated centered f16/f32 complex spectrum texture;<p>
     *     R with double size(left area R for real part of complex, right area R for imaginary part of complex) for useRed;<p>
     *     RGBA with double size(left area R+iG/B+iA complex, right area R+iG/B+iA complex) for not useRed;<p>
     *     returns 0 if failed.
     */
    public static int imageDFT(int source, int sourceOffsetX, int sourceOffsetY, boolean useRed, int texWidth, int texHeight, boolean f16OutMode) {
        return imageDFTCore(source, sourceOffsetX, sourceOffsetY, useRed, texWidth, texHeight, 0, 0, 0, true, f16OutMode);
    }

    /**
     * GPU DFT(Discrete Fourier Transform), slow but still faster more than CPU computing.
     *
     * @param useRed true for Red texture, false for RGBA texture.
     * @param texWidth spatial domain texture width, <strong>NOT</strong> frequency domain texture width.
     * @param f16OutMode true for f16 per channel texture, false for f32 per channel texture.
     *
     * @return generated centered f16/f32 complex spectrum texture;<p>
     *     R with double size(left area R for real part of complex, right area R for imaginary part of complex) for useRed;<p>
     *     RGBA with double size(left area R+iG/B+iA complex, right area R+iG/B+iA complex) for not useRed;<p>
     *     returns 0 if failed.
     */
    public static int imageDFT(int source, boolean useRed, int texWidth, int texHeight, boolean f16OutMode) {
        return imageDFT(source, 0, 0, useRed, texWidth, texHeight, f16OutMode);
    }

    @Deprecated
    public static int imageDFT(int source, boolean useRed, int texWidth, int texHeight, boolean f16InMode, boolean f16OutMode) {
        return imageDFT(source, useRed, texWidth, texHeight, f16OutMode);
    }

    /**
     * GPU IDFT(Inverse Discrete Fourier Transform), slow but still faster more than CPU computing.
     *
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     * @param useRed true for double size Red complex, false for double size RGBA complex.
     * @param texWidth spatial domain texture width, <strong>NOT</strong> frequency domain texture width.
     * @param resultOffsetX the result texture region left-bottom origin x-position.
     * @param resultOffsetY the result texture region left-bottom origin x-position.
     * @param f16InMode true for f16 per channel texture, false for f32 per channel texture.
     * @param f16OutMode true for f16 per channel texture, false for 8bit per channel texture.
     *
     * @return generated 8bit/16bit per channel texture from complex spectrum, R for double size R complex, RGBA for double size RGBA complex, if failed return 0.
     */
    public static int imageIDFT(int source, int sourceOffsetX, int sourceOffsetY, boolean useRed, int texWidth, int texHeight, int result, int resultOffsetX, int resultOffsetY, boolean f16InMode, boolean f16OutMode) {
        return imageIDFTCore(source, sourceOffsetX, sourceOffsetY, useRed, texWidth, texHeight, result, resultOffsetX, resultOffsetY, false, f16InMode, f16OutMode);
    }

    /**
     * GPU IDFT(Inverse Discrete Fourier Transform), slow but still faster more than CPU computing.
     *
     * @param useRed true for double size Red complex, false for double size RGBA complex.
     * @param texWidth spatial domain texture width, <strong>NOT</strong> frequency domain texture width.
     * @param f16InMode true for f16 per channel texture, false for f32 per channel texture.
     * @param f16OutMode true for f16 per channel texture, false for 8bit per channel texture.
     *
     * @return generated 8bit/16bit per channel texture from complex spectrum, R for double size R complex, RGBA for double size RGBA complex, if failed return 0.
     */
    public static int imageIDFT(int source, boolean useRed, int texWidth, int texHeight, int result, boolean f16InMode, boolean f16OutMode) {
        return imageIDFT(source, 0, 0, useRed, texWidth, texHeight, result, 0, 0, f16InMode, f16OutMode);
    }

    /**
     * GPU IDFT(Inverse Discrete Fourier Transform), slow but still faster more than CPU computing.
     *
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     * @param useRed true for double size Red complex, false for double size RGBA complex.
     * @param texWidth spatial domain texture width, <strong>NOT</strong> frequency domain texture width.
     * @param f16InMode true for f16 per channel texture, false for f32 per channel texture.
     * @param f16OutMode true for f16 per channel texture, false for 8bit per channel texture.
     *
     * @return generated 8bit/16bit per channel texture from complex spectrum, R for double size R complex, RGBA for double size RGBA complex, if failed return 0.
     */
    public static int imageIDFT(int source, int sourceOffsetX, int sourceOffsetY, boolean useRed, int texWidth, int texHeight, boolean f16InMode, boolean f16OutMode) {
        return imageIDFTCore(source, sourceOffsetX, sourceOffsetY, useRed, texWidth, texHeight, 0, 0, 0, true, f16InMode, f16OutMode);
    }

    /**
     * GPU IDFT(Inverse Discrete Fourier Transform), slow but still faster more than CPU computing.
     *
     * @param useRed true for double size Red complex, false for double size RGBA complex.
     * @param texWidth spatial domain texture width, <strong>NOT</strong> frequency domain texture width.
     * @param f16InMode true for f16 per channel texture, false for f32 per channel texture.
     * @param f16OutMode true for f16 per channel texture, false for 8bit per channel texture.
     *
     * @return generated 8bit/16bit per channel texture from complex spectrum, R for double size R complex, RGBA for double size RGBA complex, if failed return 0.
     */
    public static int imageIDFT(int source, boolean useRed, int texWidth, int texHeight, boolean f16InMode, boolean f16OutMode) {
        return imageIDFT(source, 0, 0, useRed, texWidth, texHeight, f16InMode, f16OutMode);
    }

    public static class NormalMapGenParam {
        /**
         * <code>1.0</code> for no effect and it is general value, and effects details.
         */
        public float srcPowFactor = 1.0f;
        /**
         * The details weight of source texture in normal map.
         */
        public float srcStrength = 0.8f;
        /**
         * Effects source and details.
         */
        public float srcBrightness = 1.0f;
        /**
         * Effects source and details.
         */
        public float srcContrast = 1.0f;
        /**
         * Effects source and details.
         */
        public float srcSmoothstepMix = 0.66667f;
        /**
         * For bilateral filter, set to less than or equal <code>0.0</code> that filter disabled.
         */
        public float filterSigmaSpace = 7.0f;
        /**
         * For bilateral filter, set to less than or equal <code>0.0</code> that filter disabled.
         */
        public float filterSigmaRange = 0.05f;
        /**
         * Set to <code>0.0f</code> turn off.
         */
        public float applyHorizontalRamp = 0.7f;
        /**
         * Set to <code>0.0f</code> turn off.
         */
        public float applyVerticalRamp = 0.25f;
        /**
         * <strong>Nullable</strong>, <code>{scale, blurStep(integer), applyStrength}</code>, set <code>null</code> that disabled; the scale general value is <code>0.01~0.02</code> for generate.
         */
        public Vector3f volume = new Vector3f(0.01f, 7.0f, 0.25f);
        /**
         * Apply edge smooth to the 'bottom' and 'top' of volume map.
         */
        public boolean volumeSmoothMix = true;
        /**
         * <strong>Nullable</strong>, <code>{blurStep(integer), applyMix}</code>, set <code>null</code> that disabled; general <code>12</code> step for chunk surface.
         */
        public Vector2f details = new Vector2f(7.0f, 0.7f);
        /**
         * General value is <code>2.5</code> and recommended <code>5.0</code> for maximum.
         */
        public float normalStrength = 2.5f;
        /**
         * For bilateral filter, set to less than or equal <code>0</code> that filter disabled.
         */
        public byte filterRadius = 13;
        /**
         * The blur 'strength' of source texture.
         */
        public byte srcBlurStep = 2;
        /**
         * True for default.
         */
        public boolean keepSrcAlpha = true;
        /**
         * False for default.
         */
        public boolean flipX = false;
        /**
         * True for default.
         */
        public boolean flipY = true;
        /**
         * Gen POT texture if needed.
         */
        public boolean alignPOT = true;
        /**
         * Gen mip-map chain if needed, only works when {@link NormalMapGenParam#alignPOT} is <code>true</code> and is a new texture.
         */
        public boolean genMipmap = true;
        /**
         * Const texture, faster than other texture.
         */
        public boolean useTextureStorage = true;
    }

    private static int genNormalMapFromRGBCore(int source, int sourceOffsetX, int sourceOffsetY, int srcLocalWidth, int srcLocalHeight, NormalMapGenParam param, int resultTex, int resultOffsetX, int resultOffsetY, boolean genResultTex) {
        int result = resultTex;
        final int resultTmp = GLWrapper.Texture.glGenTextures();
        if (!ShaderCore.isNormalMapGenValid() || !ShaderCore.isCompGaussianBlurValid() || source < 1 || srcLocalWidth < 1 || srcLocalHeight < 1 || (resultTex < 1 && !genResultTex) || resultTmp < 1) return result;
        initGLStorageTex(resultTmp, GLWrapper.Texture.GL_RGBA16, srcLocalWidth, srcLocalHeight);
        final boolean mipValid = param.alignPOT && param.genMipmap && genResultTex && GLWrapper.FBO.valid();
        if (genResultTex) {
            result = GLWrapper.Texture.glGenTextures();
            int resultWidth = srcLocalWidth, resultHeight = srcLocalHeight;
            if (param.alignPOT || !GLWrapper.Texture.valid_NPOT()) {
                resultWidth = CalculateUtil.getPOTMax(resultWidth);
                resultHeight = CalculateUtil.getPOTMax(resultHeight);
            }

            final byte levels = mipValid ? CalculateUtil.getExponentPOTMin(Math.min(resultWidth, resultHeight)) : 1;
            if (param.useTextureStorage && GLWrapper.Texture.valid_ImageLoadStore()) initGLStorageTex(result, levels, GLWrapper.Texture.GL_RGBA8, resultWidth, resultHeight);
            else initGLTex(result, GLWrapper.Texture.GL_RGBA8, resultWidth, resultHeight, GLWrapper.Texture.GL_RGBA, GLWrapper.DataType.GL_UNSIGNED_BYTE);
            if (mipValid) {
                GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_LOD, 0);
                GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_BASE_LEVEL, 0);
                GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAX_LOD, levels - 1);
                GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MAX_LEVEL, levels - 1);
            }
        }
        GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);

        final int[] detailsTex = new int[2];
        if (param.volume != null && param.volume.x > 0.0f && param.volume.z > 0.0f) {
            detailsTex[0] = genSDF(source, GLWrapper.Texture.GL_ALPHA, sourceOffsetX, sourceOffsetY, srcLocalWidth, srcLocalHeight, 0, 0, 0.5f, CalculateUtil.getExponentPOTMin(Math.max(srcLocalWidth, srcLocalHeight)), param.volume.x, 0.0f, true)[0];
            if (param.volume.y > 0.0f && detailsTex[0] > 0) applyImageGaussianBlur(detailsTex[0], true, (byte) Math.min(Math.ceil(param.volume.y), 127), srcLocalWidth, srcLocalHeight, true);
        }
        final boolean haveBF = param.filterRadius > 0 && param.filterSigmaSpace > 0.0f && param.filterSigmaRange > 0.0f;
        if (haveBF) applyImageBilateralFilter(source, sourceOffsetX, sourceOffsetY, false, param.filterRadius, param.filterSigmaSpace, param.filterSigmaRange, srcLocalWidth, srcLocalHeight, resultTmp, 0, 0, true);
        if (param.details != null && param.details.x > 0.0f && param.details.y > 0.0f) {
            detailsTex[1] = GLWrapper.Texture.glGenTextures();
            initGLStorageTex(detailsTex[1], GLWrapper.Texture.GL_RGBA16, srcLocalWidth, srcLocalHeight);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);
            applyImageGaussianBlur(haveBF ? resultTmp : source, haveBF ? 0 : sourceOffsetX, haveBF ? 0 : sourceOffsetY, false, (byte) Math.min(Math.ceil(param.details.x), 127), srcLocalWidth, srcLocalHeight, detailsTex[1], 0, 0, true);
        }
        applyImageGaussianBlur(haveBF ? resultTmp : source, haveBF ? 0 : sourceOffsetX, haveBF ? 0 : sourceOffsetY, false, param.srcBlurStep, srcLocalWidth, srcLocalHeight, resultTmp, 0, 0, true);

        final int itemDimX = (int) Math.ceil(srcLocalWidth / (BoxDatabase.isGLDeviceAMD() ? 8.0f : 4.0f));
        final int itemDimY = (int) Math.ceil(srcLocalHeight / 8.0f);
        final boolean volumeValid = detailsTex[0] > 0, detailsValid = detailsTex[1] > 0;
        int subroutine = 0;
        if (volumeValid) subroutine |= 0b1;
        if (detailsValid) subroutine |= 0b10;
        BaseShaderData program = ShaderCore.getNormalMapGenInitProgram();
        program.active();
        program.putUniformSubroutine(GLWrapper.Shader.Comp.GL_COMPUTE_SHADER, 0, subroutine);
        GLWrapper.Shader.glUniform2i(program.location[0], srcLocalWidth, srcLocalHeight);
        GLWrapper.Shader.glUniform4(program.location[1], CommonUtil.createFloatBuffer(param.srcStrength, param.srcPowFactor, param.volume != null ? param.volume.z : 0.0f, param.details != null ? param.details.y : 0.0f, param.srcBrightness, param.srcContrast, param.srcSmoothstepMix, param.volumeSmoothMix ? 1.0f : 0.0f));
        GLWrapper.Shader.glUniform2f(program.location[2], param.applyHorizontalRamp, param.applyVerticalRamp);
        program.bindTexture2D(0, resultTmp);
        program.putBindingImageTextureWriteOnly(0, resultTmp, GLWrapper.Texture.GL_RGBA16);
        if (volumeValid) program.bindTexture2D(1, detailsTex[0]);
        if (detailsValid) program.bindTexture2D(2, detailsTex[1]);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

        int resultStateBit = param.flipY ? 0b1 : 0b0;
        if (param.flipX) resultStateBit |= 0b10;
        if (param.keepSrcAlpha) resultStateBit |= 0b100;
        program = ShaderCore.getNormalMapGenResultProgram();
        program.active();
        GLWrapper.Shader.glUniform3i(program.location[0], srcLocalWidth, srcLocalHeight, resultStateBit);
        GLWrapper.Shader.glUniform4i(program.location[1], sourceOffsetX, sourceOffsetY, resultOffsetX, resultOffsetY);
        GLWrapper.Shader.glUniform1f(program.location[2], param.normalStrength);
        program.putBindingImageTextureWriteOnly(0, result, GLWrapper.Texture.GL_RGBA8);
        program.bindTexture2D(1, source);
        program.bindTexture2D(0, resultTmp);
        GLWrapper.Shader.Comp.glDispatchCompute(itemDimX, itemDimY, 1);
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
        program.close();
        GLWrapper.Texture.glDeleteTextures(resultTmp);
        if (volumeValid) GLWrapper.Texture.glDeleteTextures(detailsTex[0]);
        if (detailsValid) GLWrapper.Texture.glDeleteTextures(detailsTex[1]);
        if (mipValid) {
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, result);
            GLWrapper.Texture.glTexParameteri(GLWrapper.Texture.GL_TEXTURE_2D, GLWrapper.Texture.GL_TEXTURE_MIN_FILTER, GLWrapper.Texture.GL_LINEAR_MIPMAP_LINEAR);
            GLWrapper.FBO.glGenerateMipmap(GLWrapper.Texture.GL_TEXTURE_2D);
            GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, 0);
        }
        return result;
    }

    /**
     * GPU normal map generation from RGB texture for sprites.<p>
     * <strong>OpenGL 4.3 required, compute shader supported required.</strong>
     *
     * @param source must be RGBA8 2D-texture.
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     * @param resultOffsetX the result texture region left-bottom origin x-position.
     * @param resultOffsetY the result texture region left-bottom origin x-position.
     *
     * @return returns NPOT-texture.
     */
    public static int genNormalMapFromRGB(int source, int sourceOffsetX, int sourceOffsetY, int srcLocalWidth, int srcLocalHeight, NormalMapGenParam param, int resultTex, int resultOffsetX, int resultOffsetY) {
        return genNormalMapFromRGBCore(source, sourceOffsetX, sourceOffsetY, srcLocalWidth, srcLocalHeight, param, resultTex, resultOffsetX, resultOffsetY, false);
    }

    /**
     * GPU normal map generation from RGB texture for sprites.<p>
     * <strong>OpenGL 4.3 required, compute shader supported required.</strong>
     *
     * @param source must be RGBA8 2D-texture.
     *
     * @return returns NPOT-texture.
     */
    public static int genNormalMapFromRGB(int source, int srcLocalWidth, int srcLocalHeight, NormalMapGenParam param, int resultTex) {
        return genNormalMapFromRGB(source, 0, 0, srcLocalWidth, srcLocalHeight, param, resultTex, 0, 0);
    }

    /**
     * GPU normal map generation from RGB texture for sprites.<p>
     * <strong>OpenGL 4.3 required, compute shader supported required.</strong>
     *
     * @param source must be RGBA8 2D-texture.
     * @param sourceOffsetX the source texture region left-bottom origin x-position.
     * @param sourceOffsetY the source texture region left-bottom origin y-position.
     *
     * @return returns NPOT-texture.
     */
    public static int genNormalMapFromRGB(int source, int sourceOffsetX, int sourceOffsetY, int srcLocalWidth, int srcLocalHeight, NormalMapGenParam param) {
        return genNormalMapFromRGBCore(source, sourceOffsetX, sourceOffsetY, srcLocalWidth, srcLocalHeight, param, 0, 0, 0, true);
    }

    /**
     * GPU normal map generation from RGB texture for sprites.<p>
     * <strong>OpenGL 4.3 required, compute shader supported required.</strong>
     *
     * @param source must be RGBA8 2D-texture.
     *
     * @return returns NPOT-texture.
     */
    public static int genNormalMapFromRGB(int source, int srcLocalWidth, int srcLocalHeight, NormalMapGenParam param) {
        return genNormalMapFromRGB(source, 0, 0, srcLocalWidth, srcLocalHeight, param);
    }

    private ShaderUtil() {}
}
