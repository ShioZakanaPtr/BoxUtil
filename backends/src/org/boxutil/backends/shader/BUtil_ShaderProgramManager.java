package org.boxutil.backends.shader;

import com.fs.starfarer.api.Global;
import org.boxutil.base.BaseShaderData;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.GLWrapper;
import org.boxutil.manager.ShaderCore;
import org.boxutil.units.standard.ShaderProgram;
import org.boxutil.util.ShaderUtil;
import org.lwjgl.opengl.GLContext;

import java.io.IOException;

public final class BUtil_ShaderProgramManager {
    private final static byte _SHADER_COUNT = 34;
    public final static byte COMMON = 0;
    public final static byte SPRITE = 1;
    public final static byte CURVE = 2;
    public final static byte SEGMENT = 3;
    public final static byte TRAIL = 4;
    public final static byte FLARE = 5;
    public final static byte TEXT = 6;
    public final static byte DIST = 7;
    public final static byte DIRECT = 8;
    public final static byte MATRIX_2D = 9;
    public final static byte MATRIX_3D = 10;
    public final static byte SDF_INIT = 11;
    public final static byte SDF_PROCESS = 12;
    public final static byte SDF_RESULT = 13;
    public final static byte RADIAL_BLUR = 14;
    public final static byte GAUSSIAN_BLUR = 15;
    public final static byte GAUSSIAN_BLUR_RED = 16;
    public final static byte BILATERAL_FILTER = 17;
    public final static byte BILATERAL_FILTER_RED = 18;
    public final static byte DFT = 19;
    public final static byte DFT_RED = 20;
    public final static byte NORMAL_GEN_INIT = 21;
    public final static byte NORMAL_GEN_RESULT = 22;
    public final static byte SIMPLE_NUMBER = 23;
    public final static byte SIMPLE_ARC = 24;
    public final static byte SIMPLE_TEX_ARC = 25;
    public final static byte MISSION_BG = 26;
    public final static byte FXAA_C = 27;
    public final static byte FXAA_Q = 28;
    public final static byte BLOOM = 29;
    public final static byte AREA_LIGHT_PRE_FILTERING = 30;
    public final static byte LEGACY_NORMAL_BLUR = 31;
    public final static byte LEGACY_NORMAL_RESULT = 32;
    public final static byte STATIC_TRAIL_SYSTEM = 33;
    private final ShaderProgram[] program = new ShaderProgram[_SHADER_COUNT];
    
    private boolean glCoreProgramValid = true;
    private boolean glDirectDrawValid = true;
    private boolean glSDFGenValid = true;
    private boolean glCompGaussianBlurValid = true;
    private boolean glCompBilateralFilterValid = true;
    private boolean glDiscreteFourierValid = true;
    private boolean glNormalMapGenValid = true;
    private boolean glRadialBlurValid = true;
    private boolean glLegacyNormalMapGenValid = true;
    private boolean glFXAACValid = true;
    private boolean glFXAAQValid = true;
    private boolean glFXAABothValid = true;
    private boolean glBloomValid = true;
    private boolean glAreaLightTexValid = true;
    private boolean glStaticTrailShaderValid = true;

    public void initCore() {
        this.loadCoreProgram();
        this.initDirectDrawProgram();
        this.initCoreProgram();
    }

    public void initOptional() {
        this.initToolProgram();
        this.initFXAAProgram();
        this.initBloomProgram();
        this.initAreaLightTexProgram();
        this.initMiscProgram();
    }

    private final static String _SHADER_PATH_ROOT = "data/shaders/";

    private static String _loadLocalFile(final String path) {
        String result;
        try {
            result = Global.getSettings().loadText(_SHADER_PATH_ROOT + path);
        } catch (IOException e) {
            throw new RuntimeException("File load failed in '" + _SHADER_PATH_ROOT + path + "' cause: " + e.getMessage());
        }
        return result;
    }

    private static ShaderProgram _createShaderProgramVF(final String name, final String file) {
        return new ShaderProgram(true, name, _SHADER_PATH_ROOT + file + ".vert", _SHADER_PATH_ROOT + file + ".frag");
    }

//    private static ShaderProgram _createShaderProgramVGF(final String name, final String file) {
//        return new ShaderProgram(true, name, _SHADER_PATH_ROOT + file + ".vert", _SHADER_PATH_ROOT + file + ".geom", _SHADER_PATH_ROOT + file + ".frag");
//    }
    
    private void loadCoreProgram() {
        final boolean vendorCheckRed = BoxDatabase.isGLDeviceAMD();
        final String 
//                gBufferToolH,
                instanceDataH,
                vertCommon, fragCommon,
                vertSprite, fragSprite,
                vertCurve, tescCurve, teseCurve, geomCurve, fragCurve,
                vertSeg, tescSeg, teseSeg,
                vertTrail, geomTrail, fragTrail,
                vertFlare, fragFlare,
                vertText, geomText, fragText,
                vertDist, fragDist,
                vertPost, fragDirect,
                compMatrix2D, compMatrix3D,
                compSDFInit, compSDFProcess, compSDFResult,
                compGaussianBlur, compGaussianBlurRed,
                compBilateralFilter, compBilateralFilterRed,
                compDFT, compDFTRed,
                compNormalInit, compNormalResult,
                vertPostSimple, fragFXAAC, fragFXAAQ, compBloom, compAreaTex,
                
                gl_matrixUBO = Byte.toString(ShaderCore.getMatrixUBOBinding()),
                gl_weightedLuminance = "0.2126729, 0.7151522, 0.0721750",
                gl_screenXStep = String.format("%.7f", 1.0f / (float) ShaderCore.getScreenScaleWidth()),
                gl_screenYStep = String.format("%.7f", 1.0f / (float) ShaderCore.getScreenScaleHeight()),
                gl_bloomRadius = Float.toString(Global.getSettings().getScreenScaleMult()),
                
                gl_localWorkDim = vendorCheckRed ? "2" : "1",
                gl_localWorkDimSDF = vendorCheckRed ? "8" : "4",
                gl_localWorkSize = vendorCheckRed ? "64" : "32",
                
                gl_instanceDataInclude = "#include \"BUtil_InstanceDataSSBO.h\"",
                gl_matrixUBOTitle = "OVERWRITE_MATRIX_UBO",
                gl_weightedLuminanceTitle = "LINEAR_VALUES",
                gl_computeDimTitle = "RESET_VALUE",
                gl_localWorkSizeTitle = "WORKGROUP_SIZE_VALUE";
        
//        gBufferToolH = _loadLocalFile("include/BUtil_GBufferTool.h");
        instanceDataH = _loadLocalFile("include/BUtil_InstanceDataSSBO.h");
        vertCommon = _loadLocalFile("BUtil_CommonShader.vert").replace(gl_matrixUBOTitle, gl_matrixUBO).replace(gl_instanceDataInclude, instanceDataH);
        fragCommon = _loadLocalFile("BUtil_CommonShader.frag").replace(gl_matrixUBOTitle, gl_matrixUBO);
        vertSprite = _loadLocalFile("BUtil_SpriteShader.vert").replace(gl_matrixUBOTitle, gl_matrixUBO).replace(gl_instanceDataInclude, instanceDataH);
        fragSprite = _loadLocalFile("BUtil_SpriteShader.frag").replace(gl_matrixUBOTitle, gl_matrixUBO);
        vertCurve = _loadLocalFile("BUtil_CurveShader.vert").replace(gl_matrixUBOTitle, gl_matrixUBO).replace(gl_instanceDataInclude, instanceDataH);
        tescCurve = _loadLocalFile("BUtil_CurveShader.tesc");
        teseCurve = _loadLocalFile("BUtil_CurveShader.tese");
        geomCurve = _loadLocalFile("BUtil_CurveShader.geom").replace(gl_matrixUBOTitle, gl_matrixUBO);
        fragCurve = _loadLocalFile("BUtil_CurveShader.frag").replace(gl_matrixUBOTitle, gl_matrixUBO);
        vertSeg = _loadLocalFile("BUtil_SegmentShader.vert").replace(gl_matrixUBOTitle, gl_matrixUBO);
        tescSeg = _loadLocalFile("BUtil_SegmentShader.tesc");
        teseSeg = _loadLocalFile("BUtil_SegmentShader.tese");
        vertTrail = _loadLocalFile("BUtil_TrailShader.vert").replace(gl_matrixUBOTitle, gl_matrixUBO);
        geomTrail = _loadLocalFile("BUtil_TrailShader.geom").replace(gl_matrixUBOTitle, gl_matrixUBO);
        fragTrail = _loadLocalFile("BUtil_TrailShader.frag").replace(gl_matrixUBOTitle, gl_matrixUBO);
        vertFlare = _loadLocalFile("BUtil_FlareShader.vert").replace(gl_matrixUBOTitle, gl_matrixUBO).replace(gl_instanceDataInclude, instanceDataH);
        fragFlare = _loadLocalFile("BUtil_FlareShader.frag");
        vertText = _loadLocalFile("BUtil_TextFieldShader.vert").replace(gl_matrixUBOTitle, gl_matrixUBO);
        geomText = _loadLocalFile("BUtil_TextFieldShader.geom");
        fragText = _loadLocalFile("BUtil_TextFieldShader.frag");
        vertDist = _loadLocalFile("BUtil_DistortionShader.vert").replace(gl_matrixUBOTitle, gl_matrixUBO).replace(gl_instanceDataInclude, instanceDataH);
        fragDist = _loadLocalFile("BUtil_DistortionShader.frag");
        vertPost = _loadLocalFile("misc/BUtil_PostShader.vert");
        fragDirect = _loadLocalFile("misc/BUtil_DirectShader.frag");
        compMatrix2D = _loadLocalFile("BUtil_Instance2DMatrix.comp").replace(gl_computeDimTitle, gl_localWorkDim).replace(gl_localWorkSizeTitle, gl_localWorkSize);
        compMatrix3D = _loadLocalFile("BUtil_Instance3DMatrix.comp").replace(gl_computeDimTitle, gl_localWorkDim).replace(gl_localWorkSizeTitle, gl_localWorkSize);
        compSDFInit = _loadLocalFile("sdf/BUtil_SDFGenInit.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF).replace(gl_weightedLuminanceTitle, gl_weightedLuminance);
        compSDFProcess = _loadLocalFile("sdf/BUtil_SDFGenProcess.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF);
        compSDFResult = _loadLocalFile("sdf/BUtil_SDFGenResult.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF);
        compGaussianBlur = _loadLocalFile("filter/BUtil_GaussianBlur.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF);
        compGaussianBlurRed = _loadLocalFile("filter/BUtil_GaussianBlurRed.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF);
        compBilateralFilter = _loadLocalFile("filter/BUtil_BilateralFilter.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF);
        compBilateralFilterRed = _loadLocalFile("filter/BUtil_BilateralFilterRed.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF);
        compDFT = _loadLocalFile("fourier/BUtil_DFT.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF);
        compDFTRed = _loadLocalFile("fourier/BUtil_DFTRed.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF);
        compNormalInit = _loadLocalFile("normalMap/BUtil_NormalMapInit.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF).replace(gl_weightedLuminanceTitle, gl_weightedLuminance);
        compNormalResult = _loadLocalFile("normalMap/BUtil_NormalMapResult.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF);

        vertPostSimple = _loadLocalFile("misc/BUtil_PostSimpleShader.vert");
        fragFXAAC = _loadLocalFile("shaderpacks/BUtil_FXAACShader.frag").replace("OVERWRITE_SCREEN_X", gl_screenXStep).replace("OVERWRITE_SCREEN_Y", gl_screenYStep);
        fragFXAAQ = _loadLocalFile("shaderpacks/BUtil_FXAAQShader.frag").replace("OVERWRITE_SCREEN_X", gl_screenXStep).replace("OVERWRITE_SCREEN_Y", gl_screenYStep);
        compBloom = _loadLocalFile("shaderpacks/BUtil_BloomShader.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF).replace("OVERWRITE_RADIUS_SCALE", gl_bloomRadius);
        compAreaTex = _loadLocalFile("shaderpacks/BUtil_AreaLightPreFilteringShader.comp").replace(gl_computeDimTitle, gl_localWorkDimSDF);
        if (glCoreProgramValid &= BoxConfigs.isShaderEnable()) {
            this.program[COMMON] = new ShaderProgram("BoxUtil-CommonShader", vertCommon, fragCommon);
            this.program[SPRITE] = new ShaderProgram("BoxUtil-SpriteShader", vertSprite, fragSprite);
            this.program[CURVE] = new ShaderProgram("BoxUtil-CurveShader", vertCurve, tescCurve, teseCurve, geomCurve, fragCurve);
            this.program[SEGMENT] = new ShaderProgram("BoxUtil-SegmentShader", vertSeg, tescSeg, teseSeg, geomCurve, fragCurve);
            this.program[TRAIL] = new ShaderProgram("BoxUtil-TrailShader", vertTrail, geomTrail, fragTrail);
            this.program[FLARE] = new ShaderProgram("BoxUtil-FlareShader", vertFlare, fragFlare);
            this.program[TEXT] = new ShaderProgram("BoxUtil-TextShader", vertText, geomText, fragText);
            this.program[DIST] = new ShaderProgram("BoxUtil-DistortionShader", vertDist, fragDist);
            this.program[MATRIX_2D] = new ShaderProgram("BoxUtil-MatrixComputeShader-2D", compMatrix2D);
            this.program[MATRIX_3D] = new ShaderProgram("BoxUtil-MatrixComputeShader-3D", compMatrix3D);
        }

        this.program[DIRECT] = new ShaderProgram("BoxUtil-DirectShader", vertPost, fragDirect);
        this.program[FXAA_C] = new ShaderProgram("BoxUtil-FXAA-ConsoleShader", vertPostSimple, fragFXAAC);
        this.program[FXAA_Q] = new ShaderProgram("BoxUtil-FXAA-QualityShader", vertPostSimple, fragFXAAQ);

        this.program[SDF_INIT] = new ShaderProgram("BoxUtil-SDFGenInitShader", compSDFInit);
        this.program[SDF_PROCESS] = new ShaderProgram("BoxUtil-SDFGenProcessShader", compSDFProcess);
        this.program[SDF_RESULT] = new ShaderProgram("BoxUtil-SDFGenResultShader", compSDFResult);

        this.program[GAUSSIAN_BLUR] = new ShaderProgram("BoxUtil-CompGaussianBlurShader", compGaussianBlur);
        this.program[GAUSSIAN_BLUR_RED] = new ShaderProgram("BoxUtil-CompGaussianBlurRedShader", compGaussianBlurRed);
        this.program[BILATERAL_FILTER] = new ShaderProgram("BoxUtil-CompBilateralFilterShader", compBilateralFilter);
        this.program[BILATERAL_FILTER_RED] = new ShaderProgram("BoxUtil-CompBilateralFilterRedShader", compBilateralFilterRed);
        this.program[DFT] = new ShaderProgram("BoxUtil-DFTShader", compDFT);
        this.program[DFT_RED] = new ShaderProgram("BoxUtil-DFTRedShader", compDFTRed);
        this.program[NORMAL_GEN_INIT] = new ShaderProgram("BoxUtil-NormalMapInitShader", compNormalInit);
        this.program[NORMAL_GEN_RESULT] = new ShaderProgram("BoxUtil-NormalMapResultShader", compNormalResult);

        this.program[BLOOM] = new ShaderProgram("BoxUtil-BloomShader", compBloom);
        this.program[AREA_LIGHT_PRE_FILTERING] = new ShaderProgram("BoxUtil-AreaLightTexPreFiltering", compAreaTex);
    }

    private void initDirectDrawProgram() {
        if (this.glDirectDrawValid &= this.program[DIRECT].isValid()) {
            this.program[DIRECT].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_alphaFix")
                    .loadUniformIndex("u_level")
                    .loadUniformIndex("u_uvStart")
                    .loadUniformIndex("u_uvEnd");
            GLWrapper.Shader.glProgramUniform1f(this.program[DIRECT].getId(), this.program[DIRECT].location[1], 0.0f);
            GLWrapper.Shader.glProgramUniform2f(this.program[DIRECT].getId(), this.program[DIRECT].location[2], 0.0f, 0.0f);
            GLWrapper.Shader.glProgramUniform2f(this.program[DIRECT].getId(), this.program[DIRECT].location[3], 1.0f, 1.0f);
        }
    }
    
    private void initCoreProgram() {
        if (!BoxConfigs.isShaderEnable()) return;
        this.glCoreProgramValid &= this.glDirectDrawValid;
        if (this.glCoreProgramValid &= this.program[COMMON].isValid()) {
            this.program[COMMON].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_baseSize")
                    .loadUniformIndex("u_additionEmissive_DataBit_InstanceOffset")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", ShaderCore.getMatrixUBOBinding())

                    .initSubroutineSize(5, 2)
                    .beginSubroutine(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_noneData")
                    .loadSubroutineIndex("p_haveData2D")
                    .loadSubroutineIndex("p_haveFixedData2D")
                    .loadSubroutineIndex("p_haveData3D")
                    .loadSubroutineIndex("p_haveFixedData3D")
                    .beginSubroutine(1, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER)
                    .loadSubroutineIndex("p_commonMode")
                    .loadSubroutineIndex("p_colorMode")

                    .initSubroutineUniformSize(1, 1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_instanceState")
                    .beginSubroutineUniform(1, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER)
                    .loadSubroutineUniformIndex("f_surfaceState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glCoreProgramValid &= this.program[SPRITE].isValid()) {
            this.program[SPRITE].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_additionEmissive_DataBit_InstanceOffset")
                    .loadUniformIndex("u_globalTimerAlpha")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", ShaderCore.getMatrixUBOBinding())

                    .initSubroutineSize(8)
                    .beginSubroutine(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_commonUV")
                    .loadSubroutineIndex("p_tileUV")
                    .loadSubroutineIndex("p_tileRUV")
                    .loadSubroutineIndex("p_noneData")
                    .loadSubroutineIndex("p_haveData2D")
                    .loadSubroutineIndex("p_haveFixedData2D")
                    .loadSubroutineIndex("p_haveData3D")
                    .loadSubroutineIndex("p_haveFixedData3D")

                    .initSubroutineUniformSize(2)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_uvMapping")
                    .loadSubroutineUniformIndex("f_instanceState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glCoreProgramValid &= this.program[CURVE].isValid()) {
            this.program[CURVE].initUniformSize(6)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_totalNodes")
                    .loadUniformIndex("u_additionEmissive_DataBit")
                    .loadUniformIndex("u_globalTimerAlpha")
                    .loadUniformIndex("u_instanceOffset")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", ShaderCore.getMatrixUBOBinding())

                    .initSubroutineSize(5)
                    .beginSubroutine(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_noneData")
                    .loadSubroutineIndex("p_haveData2D")
                    .loadSubroutineIndex("p_haveFixedData2D")
                    .loadSubroutineIndex("p_haveData3D")
                    .loadSubroutineIndex("p_haveFixedData3D")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_instanceState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glCoreProgramValid &= this.program[SEGMENT].isValid()) {
            this.program[SEGMENT].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_additionEmissive_DataBit")
                    .loadUniformIndex("u_globalTimerAlpha")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", ShaderCore.getMatrixUBOBinding());
        }

        if (this.glCoreProgramValid &= this.program[TRAIL].isValid()) {
            this.program[TRAIL].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_extraData")
                    .loadUniformIndex("u_additionEmissive_DataBit")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", ShaderCore.getMatrixUBOBinding())

                    .initSubroutineSize(2)
                    .beginSubroutine(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_lineStripMode")
                    .loadSubroutineIndex("p_linesMode")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_lineModeState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glCoreProgramValid &= this.program[FLARE].isValid()) {
            this.program[FLARE].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_dataBit")
                    .loadUniformIndex("u_instanceOffset")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", ShaderCore.getMatrixUBOBinding())

                    .initSubroutineSize(5, 4)
                    .beginSubroutine(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_noneData")
                    .loadSubroutineIndex("p_haveData2D")
                    .loadSubroutineIndex("p_haveFixedData2D")
                    .loadSubroutineIndex("p_haveData3D")
                    .loadSubroutineIndex("p_haveFixedData3D")
                    .beginSubroutine(1, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER)
                    .loadSubroutineIndex("p_smoothMode")
                    .loadSubroutineIndex("p_sharpMode")
                    .loadSubroutineIndex("p_smoothDiscMode")
                    .loadSubroutineIndex("p_sharpDiscMode")

                    .initSubroutineUniformSize(1, 1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_instanceState")
                    .beginSubroutineUniform(1, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER)
                    .loadSubroutineUniformIndex("f_flareState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glCoreProgramValid &= this.program[TEXT].isValid()) {
            this.program[TEXT].initUniformSize(9)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_fontMap[0]")
                    .loadUniformIndex("u_fontMap[1]")
                    .loadUniformIndex("u_fontMap[2]")
                    .loadUniformIndex("u_fontMap[3]")
                    .loadUniformIndex("u_italicFactor")
                    .loadUniformIndex("u_globalColor")
                    .loadUniformIndex("u_dataBit")
                    .loadUniformIndex("u_blendBloom_globalTimerAlpha")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", ShaderCore.getMatrixUBOBinding());
            this.program[TEXT].putDefaultTextureUnit(this.program[TEXT].location[1], 0);
            this.program[TEXT].putDefaultTextureUnit(this.program[TEXT].location[2], 1);
            this.program[TEXT].putDefaultTextureUnit(this.program[TEXT].location[3], 2);
            this.program[TEXT].putDefaultTextureUnit(this.program[TEXT].location[4], 3);
        }

        if (this.glCoreProgramValid &= this.program[DIST].isValid()) {
            this.program[DIST].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_modelMatrix")
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_instanceDataOffset")

                    .initUniformBlockSize(1)
                    .beginUniformBlock()
                    .loadAndSetUniformBlockIndex("BUtilGlobalData", ShaderCore.getMatrixUBOBinding())

                    .initSubroutineSize(5)
                    .beginSubroutine(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineIndex("p_noneData")
                    .loadSubroutineIndex("p_haveData2D")
                    .loadSubroutineIndex("p_haveFixedData2D")
                    .loadSubroutineIndex("p_haveData3D")
                    .loadSubroutineIndex("p_haveFixedData3D")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Vert.GL_VERTEX_SHADER)
                    .loadSubroutineUniformIndex("f_instanceState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glCoreProgramValid &= (this.program[MATRIX_2D].isValid() && this.program[MATRIX_3D].isValid())) {
            this.program[MATRIX_2D].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_amount")
                    .loadUniformIndex("u_instanceRange");

            this.program[MATRIX_3D].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_amount")
                    .loadUniformIndex("u_instanceRange");
        }
    }

    private void initToolProgram() {
        if (this.glSDFGenValid &= (this.program[SDF_INIT].isValid() && this.program[SDF_PROCESS].isValid() && this.program[SDF_RESULT].isValid())) {
            this.glSDFGenValid = GLWrapper.Shader.valid_Subroutine() && GLWrapper.Texture.valid_TexInt() && GLWrapper.Operation.Sync.valid_Barrier() && GLWrapper.Texture.valid_TexStorage() && GLWrapper.Texture.valid_NPOT();
            this.program[SDF_INIT].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_sizeState")
                    .loadUniformIndex("u_border_offset")
                    .loadUniformIndex("u_threshold")

                    .initSubroutineSize(5)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_fromRed")
                    .loadSubroutineIndex("p_fromGreen")
                    .loadSubroutineIndex("p_fromBlue")
                    .loadSubroutineIndex("p_fromAlpha")
                    .loadSubroutineIndex("p_fromRGB")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_sampleMethodState")
                    .computeSubroutineUniformRoute();

            this.program[SDF_PROCESS].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_step");

            this.program[SDF_RESULT].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_size_offset")
                    .loadUniformIndex("u_preMultiply")

                    .initSubroutineSize(2)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glCompGaussianBlurValid &= (this.program[GAUSSIAN_BLUR].isValid() && this.program[GAUSSIAN_BLUR_RED].isValid())) {
            this.glCompGaussianBlurValid = GLWrapper.Shader.valid_Subroutine() && GLWrapper.Operation.Sync.valid_Barrier() && GLWrapper.Texture.valid_ImageLoadStore() && GLWrapper.Texture.valid_TexInt();
            this.program[GAUSSIAN_BLUR].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_sizeStep")
                    .loadUniformIndex("u_ioOffset")
                    .loadUniformIndex("u_vertical")
                    .loadUniformIndex("u_perStep")

                    .initSubroutineSize(4)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")
                    .loadSubroutineIndex("p_filterMode")
                    .loadSubroutineIndex("p_copyMode")

                    .initSubroutineUniformSize(2)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .loadSubroutineUniformIndex("f_workModeState")
                    .computeSubroutineUniformRoute();

            this.program[GAUSSIAN_BLUR_RED].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_sizeStep")
                    .loadUniformIndex("u_ioOffset")
                    .loadUniformIndex("u_vertical")
                    .loadUniformIndex("u_perStep")

                    .initSubroutineSize(4)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")
                    .loadSubroutineIndex("p_filterMode")
                    .loadSubroutineIndex("p_copyMode")

                    .initSubroutineUniformSize(2)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .loadSubroutineUniformIndex("f_workModeState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glCompBilateralFilterValid &= (this.program[BILATERAL_FILTER].isValid() && this.program[BILATERAL_FILTER_RED].isValid())) {
            this.glCompBilateralFilterValid = GLWrapper.Shader.valid_Subroutine() && GLWrapper.Operation.Sync.valid_Barrier() && GLWrapper.Texture.valid_ImageLoadStore() && GLWrapper.Texture.valid_TexInt();
            this.program[BILATERAL_FILTER].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_sizeStep")
                    .loadUniformIndex("u_ioOffset")
                    .loadUniformIndex("u_vertical")
                    .loadUniformIndex("u_gSigmaSRInv")

                    .initSubroutineSize(2)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .computeSubroutineUniformRoute();

            this.program[BILATERAL_FILTER_RED].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_sizeStep")
                    .loadUniformIndex("u_ioOffset")
                    .loadUniformIndex("u_vertical")
                    .loadUniformIndex("u_gSigmaSRInv")

                    .initSubroutineSize(2)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glDiscreteFourierValid &= (this.program[DFT].isValid() && this.program[DFT_RED].isValid())) {
            this.glDiscreteFourierValid = GLWrapper.Shader.valid_Subroutine() && GLWrapper.Operation.Sync.valid_Barrier() && GLWrapper.Texture.valid_ImageLoadStore() && GLWrapper.Texture.valid_TexInt() && GLWrapper.Texture.valid_TexFloat();
            this.program[DFT].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_ioOffset")
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_state")
                    .loadUniformIndex("u_sizeDiv")

                    .initSubroutineSize(3)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")
                    .loadSubroutineIndex("p_bit32Store")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .computeSubroutineUniformRoute();

            this.program[DFT_RED].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_ioOffset")
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_state")
                    .loadUniformIndex("u_sizeDiv")

                    .initSubroutineSize(3)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_bit8Store")
                    .loadSubroutineIndex("p_bit16Store")
                    .loadSubroutineIndex("p_bit32Store")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_formatPickerStoreState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glNormalMapGenValid &= (this.program[NORMAL_GEN_INIT].isValid() && this.program[NORMAL_GEN_RESULT].isValid())) {
            this.glNormalMapGenValid = GLWrapper.Shader.valid_Subroutine() && GLWrapper.Operation.Sync.valid_Barrier();
            this.program[NORMAL_GEN_INIT].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_state")
                    .loadUniformIndex("u_rampMix")

                    .initSubroutineSize(4)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_texOnly")
                    .loadSubroutineIndex("p_withVolume")
                    .loadSubroutineIndex("p_withDetails")
                    .loadSubroutineIndex("p_withBoth")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_texInputState")
                    .computeSubroutineUniformRoute();

            this.program[NORMAL_GEN_RESULT].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_sizeState")
                    .loadUniformIndex("u_ioOffset")
                    .loadUniformIndex("u_normalStrength");
        }

        this.program[RADIAL_BLUR] = _createShaderProgramVF("BoxUtil-RadialBlurShader", "filter/BUtil_RadialBlurShader");
        if (this.glRadialBlurValid &= this.program[RADIAL_BLUR].isValid()) {
            this.program[RADIAL_BLUR].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_alphaStrength")
                    .loadUniformIndex("u_tex");
        }

        this.program[LEGACY_NORMAL_BLUR] = _createShaderProgramVF("BoxUtil-LegacyNormalMapBlurShader", "normalMap/BUtil_LegacyNormalMapBlur");
        this.program[LEGACY_NORMAL_RESULT] = _createShaderProgramVF("BoxUtil-LegacyNormalMapResultShader", "normalMap/BUtil_LegacyNormalMapResult");
        if (this.glLegacyNormalMapGenValid &= (this.program[LEGACY_NORMAL_BLUR].isValid() && this.program[LEGACY_NORMAL_RESULT].isValid())) {
            this.program[LEGACY_NORMAL_BLUR].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_uvRegion")
                    .loadUniformIndex("u_state")
                    .loadUniformIndex("u_stepUV_srcUVDiv")
                    .loadUniformIndex("u_vertical");

            this.program[LEGACY_NORMAL_RESULT].initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_uvRegion")
                    .loadUniformIndex("u_state")
                    .loadUniformIndex("u_stepUV");
        }
    }

    private void initFXAAProgram() {
        if (this.glFXAACValid &= this.program[FXAA_C].isValid()) {
            this.glFXAACValid = this.glDirectDrawValid && GLWrapper.FBO.valid() && GLWrapper.FBO.valid_Blit() && GLWrapper.Shader.valid_Subroutine();
            this.program[FXAA_C].initSubroutineSize(4)
                    .beginSubroutine(0, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER)
                    .loadSubroutineIndex("p_fromRaw")
                    .loadSubroutineIndex("p_fromDepth")
                    .loadSubroutineIndex("p_commonDisplay")
                    .loadSubroutineIndex("p_edgeDisplay")

                    .initSubroutineUniformSize(2)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER)
                    .loadSubroutineUniformIndex("f_sampleMethodState")
                    .loadSubroutineUniformIndex("f_displayMethodState")
                    .computeSubroutineUniformRoute();
        }

        if (this.glFXAAQValid &= this.program[FXAA_Q].isValid()) {
            this.glFXAAQValid = this.glDirectDrawValid && GLWrapper.FBO.valid() && GLWrapper.FBO.valid_Blit() && GLWrapper.Shader.valid_Subroutine();
            this.program[FXAA_Q].initSubroutineSize(4)
                    .beginSubroutine(0, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER)
                    .loadSubroutineIndex("p_fromRaw")
                    .loadSubroutineIndex("p_fromDepth")
                    .loadSubroutineIndex("p_commonDisplay")
                    .loadSubroutineIndex("p_edgeDisplay")

                    .initSubroutineUniformSize(2)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Frag.GL_FRAGMENT_SHADER)
                    .loadSubroutineUniformIndex("f_sampleMethodState")
                    .loadSubroutineUniformIndex("f_displayMethodState")
                    .computeSubroutineUniformRoute();
        }
        this.glFXAABothValid = this.glFXAACValid && this.isFXAAQValid();
    }

    private void initBloomProgram() {
        if (this.glBloomValid &= (this.program[BLOOM] != null && this.program[BLOOM].isValid())) {
            this.glBloomValid = this.glDirectDrawValid && GLWrapper.Operation.valid_BlendIndexed() && GLWrapper.Operation.Sync.valid_Barrier() && GLWrapper.Texture.valid_ImageLoadStore();
            this.program[BLOOM].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_size")
                    .loadUniformIndex("u_targetUVStepDiv")
                    .loadUniformIndex("u_initPass")

                    .initSubroutineSize(4)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_sampleInit")
                    .loadSubroutineIndex("p_downSampleFirst")
                    .loadSubroutineIndex("p_downSample")
                    .loadSubroutineIndex("p_upSample")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_sampleModeState")
                    .computeSubroutineUniformRoute();
        }
    }

    private void initAreaLightTexProgram() {
        if (this.glAreaLightTexValid &= (this.program[AREA_LIGHT_PRE_FILTERING] != null && this.program[AREA_LIGHT_PRE_FILTERING].isValid())) {
            this.glAreaLightTexValid = GLWrapper.Operation.Sync.valid_Barrier() && GLWrapper.Texture.valid_ImageLoadStore();
            this.program[AREA_LIGHT_PRE_FILTERING].initUniformSize(3)
                    .beginUniform()
                    .loadUniformIndex("u_sizeStep")
                    .loadUniformIndex("u_vertical")
                    .loadUniformIndex("u_uvStepDiv_Lod_ADiv")

                    .initSubroutineSize(2)
                    .beginSubroutine(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineIndex("p_normalMode")
                    .loadSubroutineIndex("p_copyMode")

                    .initSubroutineUniformSize(1)
                    .beginSubroutineUniform(0, GLWrapper.Shader.Comp.GL_COMPUTE_SHADER)
                    .loadSubroutineUniformIndex("f_filteringModeState")
                    .computeSubroutineUniformRoute();
        }
    }

    private void initMiscProgram() {
        this.program[SIMPLE_NUMBER] = _createShaderProgramVF("BoxUtil-NumberShader", "BUtil_NumberShader");
        this.program[SIMPLE_ARC] = _createShaderProgramVF("BoxUtil-ArcShader", "BUtil_ArcShader");
        this.program[SIMPLE_TEX_ARC] = new ShaderProgram(true, "BoxUtil-TexArcShader", _SHADER_PATH_ROOT + "BUtil_ArcShader.vert", _SHADER_PATH_ROOT + "BUtil_TexArcShader.frag");
        this.program[MISSION_BG] = _createShaderProgramVF("BoxUtil-TestMissionShader", "misc/BUtil_TestMissionShader");
        if (this.program[SIMPLE_NUMBER].isValid()) {
            this.program[SIMPLE_NUMBER].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_charLength");
        }

        if (this.program[SIMPLE_ARC].isValid()) {
            this.program[SIMPLE_ARC].initUniformSize(2)
                    .beginUniform()
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_arcValue");
        }

        if (this.program[SIMPLE_TEX_ARC].isValid()) {
            this.program[SIMPLE_TEX_ARC].initUniformSize(1)
                    .beginUniform()
                    .loadUniformIndex("u_statePackage");
        }

        if (this.program[MISSION_BG].isValid()) {
            this.program[MISSION_BG].initUniformSize(1)
                    .beginUniform()
                    .loadUniformIndex("u_time");
        }
    }
    
    public void initStaticTrailSystemProgram(boolean useFullFeatures) {
        final var cap = GLContext.getCapabilities();
        final String bitEncodeEXTName, uboEXTName, geomShaderEXTName;
        if (cap.GL_ARB_shader_bit_encoding) {
            bitEncodeEXTName = "GL_ARB_shader_bit_encoding";
        } else if (cap.GL_ARB_gpu_shader5) {
            bitEncodeEXTName = "GL_ARB_gpu_shader5";
        } else if (useFullFeatures) {
            bitEncodeEXTName = "GL_ARB_shader_bit_encoding";
        } else return;
        if (cap.GL_ARB_uniform_buffer_object || useFullFeatures) {
            uboEXTName = "GL_ARB_uniform_buffer_object";
        } else return;
        if (cap.GL_ARB_geometry_shader4) {
            geomShaderEXTName = "GL_ARB_geometry_shader4";
        } else if (cap.GL_EXT_geometry_shader4) {
            geomShaderEXTName = "GL_EXT_geometry_shader4";
        } else if (useFullFeatures) {
            geomShaderEXTName = "GL_ARB_geometry_shader4";
        } else return;

        final String gl_glslVersionTitle = "OVERWRITE_VERSION_TITLE",
                gl_bitEncodeEXTTitle = "BIT_ENCODE_EXT_TITLE",
                gl_uboEXTTitle = "UBO_EXT_TITLE",
                gl_geomShaderEXTTitle = "GEOM_SHADER_EXT_TITLE",
                gl_trailVersionTitle = "TRAIL_VERSION_TITLE",
                gl_matrixUBOTitle = "OVERWRITE_MATRIX_UBO",
                gl_glslVersion = useFullFeatures ? "430" : "130",
                gl_trailVersion = useFullFeatures ? "MODERN_TRAIL_MODE" : "LEGACY_TRAIL_MODE",
                gl_matrixUBO = Byte.toString(ShaderCore.getMatrixUBOBinding()),
                programName = useFullFeatures ? "BoxUtil-StaticTrailSystemShader" : "BoxUtil-LegacyStaticTrailSystemShader";

        final String vert = _loadLocalFile("trailSystem/BUtil_StaticTrail.vert").replace(gl_glslVersionTitle, gl_glslVersion)
                .replace(gl_bitEncodeEXTTitle, bitEncodeEXTName)
                .replace(gl_trailVersionTitle, gl_trailVersion),

                geom = _loadLocalFile("trailSystem/BUtil_StaticTrail.geom").replace(gl_glslVersionTitle, gl_glslVersion)
                        .replace(gl_matrixUBOTitle, gl_matrixUBO)
                        .replace(gl_bitEncodeEXTTitle, bitEncodeEXTName)
                        .replace(gl_uboEXTTitle, uboEXTName)
                        .replace(gl_geomShaderEXTTitle, geomShaderEXTName)
                        .replace(gl_trailVersionTitle, gl_trailVersion),

                frag = _loadLocalFile("trailSystem/BUtil_StaticTrail.frag").replace(gl_glslVersionTitle, gl_glslVersion)
                        .replace(gl_trailVersionTitle, gl_trailVersion);

        if (useFullFeatures) {
            this.program[STATIC_TRAIL_SYSTEM] = new ShaderProgram(programName, vert, geom, frag);
        } else {
            final var makeBinding = ShaderUtil.makeShaderBindingLocation(
                    new ShaderUtil.BindingLocationStruct(ShaderUtil.BIND_VERTEX_ATTRIB, 0, "a_position"),
                    new ShaderUtil.BindingLocationStruct(ShaderUtil.BIND_VERTEX_ATTRIB, 1, "a_facingVector"),
                    new ShaderUtil.BindingLocationStruct(ShaderUtil.BIND_VERTEX_ATTRIB, 2, "a_nodeColor"),
                    new ShaderUtil.BindingLocationStruct(ShaderUtil.BIND_VERTEX_ATTRIB, 3, "a_timeStampRaw"),
                    new ShaderUtil.BindingLocationStruct(ShaderUtil.BIND_VERTEX_ATTRIB, 4, "a_uv")
            ).next(l_program -> {
                GLWrapper.Shader.glProgramParameteri(l_program, GLWrapper.Shader.Geom.GL_GEOMETRY_INPUT_TYPE, GLWrapper.Drawcall.GL_LINES_ADJACENCY);
                GLWrapper.Shader.glProgramParameteri(l_program, GLWrapper.Shader.Geom.GL_GEOMETRY_OUTPUT_TYPE, GLWrapper.Drawcall.GL_TRIANGLE_STRIP);
                GLWrapper.Shader.glProgramParameteri(l_program, GLWrapper.Shader.Geom.GL_GEOMETRY_VERTICES_OUT, 4);
                return false;
            });

            final var shaderID = ShaderUtil.createShaderVGF(programName, makeBinding, vert, geom, frag);
            this.program[STATIC_TRAIL_SYSTEM] = new ShaderProgram(shaderID);
        }

        final var in_program = this.program[STATIC_TRAIL_SYSTEM];
        if (this.glStaticTrailShaderValid &= in_program.isValid()) {
            this.glStaticTrailShaderValid = GLWrapper.DataType.valid_FP16() && GLWrapper.VAO.valid_IntAttrib() && GLWrapper.Buffer.VBO.valid() && GLWrapper.Drawcall.valid_MultiDraw();
            in_program.initUniformSize(4)
                    .beginUniform()
                    .loadUniformIndex("u_statePackage")
                    .loadUniformIndex("u_time")
                    .loadUniformIndex("u_additionEmissive")
                    .loadUniformIndex("u_dataBit");

            if (!useFullFeatures) {
                final int u_diffuse = GLWrapper.Shader.glGetUniformLocation(in_program.getId(), "u_diffuseMap"),
                        u_complex = GLWrapper.Shader.glGetUniformLocation(in_program.getId(), "u_complexMap"),
                        u_emissive = GLWrapper.Shader.glGetUniformLocation(in_program.getId(), "u_emissiveMap");

                in_program.active();
                GLWrapper.Shader.glUniform1i(u_diffuse, 0);
                GLWrapper.Shader.glUniform1i(u_complex, 2);
                GLWrapper.Shader.glUniform1i(u_emissive, 3);
                in_program.close();

                final int programID = in_program.getId(),
                        blockIndex = GLWrapper.Buffer.UBO.glGetUniformBlockIndex(programID, "BUtilGlobalData");
                GLWrapper.Buffer.UBO.glUniformBlockBinding(programID, blockIndex, ShaderCore.getMatrixUBOBinding());
            }
        }
    }

    public BaseShaderData program(byte target) {
        return this.program[target];
    }
    
    public boolean isCoreValid() {
        return this.glCoreProgramValid;
    }

    public boolean isSDFGenValid() {
        return glSDFGenValid;
    }

    public boolean isCompGaussianBlurValid() {
        return glCompGaussianBlurValid;
    }

    public boolean isCompBilateralFilterValid() {
        return glCompBilateralFilterValid;
    }

    public boolean isDiscreteFourierValid() {
        return glDiscreteFourierValid;
    }

    public boolean isNormalMapGenValid() {
        return glNormalMapGenValid;
    }

    public boolean isRadialBlurValid() {
        return glRadialBlurValid;
    }

    public boolean isLegacyNormalMapGenValid() {
        return glLegacyNormalMapGenValid;
    }

    public boolean isFXAACValid() {
        return glFXAACValid;
    }

    public boolean isFXAAQValid() {
        return glFXAAQValid;
    }

    public boolean isFXAABothValid() {
        return glFXAABothValid;
    }

    public boolean isBloomValid() {
        return glBloomValid;
    }

    public boolean isAreaLightTexValid() {
        return glAreaLightTexValid;
    }

    public boolean isStaticTrailShaderValid() {
        return glStaticTrailShaderValid;
    }
}
