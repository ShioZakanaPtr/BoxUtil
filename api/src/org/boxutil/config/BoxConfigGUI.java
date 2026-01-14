package org.boxutil.config;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.UIPanelAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import com.fs.starfarer.combat.CombatState;
import com.fs.starfarer.title.TitleScreenState;
import com.fs.state.AppDriver;
import org.boxutil.define.BoxDatabase;
import org.boxutil.manager.KernelCore;
import org.boxutil.manager.ShaderCore;
import org.boxutil.backends.core.BUtil_InstanceDataMemoryPool;
import org.boxutil.backends.gui.BUtil_BaseConfigPanel;
import org.boxutil.backends.reflect.BUtil_RefMethod;
import org.boxutil.units.standard.misc.UIBorderObject;
import org.boxutil.util.CommonUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

public final class BoxConfigGUI extends BaseEveryFrameCombatPlugin {
    private final static byte _TOTAL_TEX = 3;
    private final static byte _CONFIG_ICON = 0;
    private final static byte _BASE_ICON = 1;
    private final static byte _FULL_ICON = 2;
    private final static float _SCREEN_SCALE = Global.getSettings().getScreenScaleMult();
    private final static float _BORDER_WIDTH = 8.0f;
    private final static float _PADDING_SIMPLE = 5.0f;
    private final static float _ICON_PADDING_SIMPLE = 4.0f;
    private final static float _ICON_SIZE = 96.0f;
    private final static float _ICON_SMALL_SIZE = 24.0f;
    private final static float _ICON_SMALL_SPACE = 8.0f;
    private final static float[] _SIMPLE_FULL_SIZE = new float[]{_ICON_SIZE + _BORDER_WIDTH * 2.0f};
    private final static float[] _SIMPLE_BLOCK_A = new float[]{_PADDING_SIMPLE + _BORDER_WIDTH, _PADDING_SIMPLE + _BORDER_WIDTH + _ICON_SMALL_SIZE};
    private final static float[] _SIMPLE_BLOCK_B = new float[]{_SIMPLE_BLOCK_A[1] + _ICON_SMALL_SPACE, _SIMPLE_BLOCK_A[1] + _ICON_SMALL_SPACE + _ICON_SMALL_SIZE};
    private final static float[] _SIMPLE_MIN_SIZE = new float[]{_ICON_SMALL_SIZE * 2.0f + _BORDER_WIDTH * 2.0f + _ICON_SMALL_SPACE, _ICON_SMALL_SIZE + _BORDER_WIDTH * 2.0f};
    private final static float[] _SIMPLE_ICON_ANCHOR = new float[]{_SIMPLE_FULL_SIZE[0] * 0.5f - _ICON_SIZE * 0.5f + _PADDING_SIMPLE + _ICON_PADDING_SIMPLE, _SIMPLE_FULL_SIZE[0] * 0.5f + _ICON_SIZE * 0.5f + _PADDING_SIMPLE - _ICON_PADDING_SIMPLE};
    private static float[] _GUI_ICON_COLOR = null;
    private static float[] _GUI_POS_COLOR = null;
    private static float[] _GUI_NEG_COLOR = null;

    private static boolean getScreenFF = true;
    private static boolean globalInit = false;
    private static int _backgroundTex = 0;
    private static GUIState guiState = GUIState.MINIMIZE;
    private final static float[] _backgroundTexUV = new float[2];
    private static int[] _textures = null;

    private boolean _npotTex = false;
    private boolean stateSwitch = false;
    private float _panelCD = 0.0f;
    private CustomUIPanelPlugin mainSettingsPlugin = null;
    private CustomPanelAPI mainSettingsPanel = null;
    private Object _simpleBorder = null;

    public enum GUIState {
        MINIMIZE(),
        SIMPLE(),
        CONFIG()
    }

    public void init(CombatEngineAPI engine) {
        globalInitLater();
        if (_GUI_ICON_COLOR == null) {
            _GUI_ICON_COLOR = CommonUtil.colorNormalization3fArray(Global.getSettings().getColor("standardUIIconColor"), new float[3]);
            _GUI_POS_COLOR = CommonUtil.colorNormalization3fArray(Misc.getPositiveHighlightColor(), new float[3]);
            _GUI_NEG_COLOR = CommonUtil.colorNormalization3fArray(Misc.getNegativeHighlightColor(), new float[3]);
        }
        if (_textures == null) {
            _textures = new int[_TOTAL_TEX];
            IntBuffer ids = BufferUtils.createIntBuffer(_TOTAL_TEX);
            GL11.glGenTextures(ids);
            if (ids.get(_CONFIG_ICON) != 0) {
                _textures[_CONFIG_ICON] = ids.get(_CONFIG_ICON);
                Pair<int[], ByteBuffer> data = CommonUtil.getRawPixels("graphics/ui/BUtil_ConfigButton.png", 4);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, _textures[_CONFIG_ICON]);
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, data.one[0], data.one[1], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.two);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            }
            if (ids.get(_BASE_ICON) != 0) {
                _textures[_BASE_ICON] = ids.get(_BASE_ICON);
                Pair<int[], ByteBuffer> data = CommonUtil.getRawPixels("graphics/ui/BUtil_FeaturesIconBase.png", 4);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, _textures[_BASE_ICON]);
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, data.one[0], data.one[1], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.two);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            }
            if (ids.get(_FULL_ICON) != 0) {
                _textures[_FULL_ICON] = ids.get(_FULL_ICON);
                Pair<int[], ByteBuffer> data = CommonUtil.getRawPixels("graphics/ui/BUtil_FeaturesIconFull.png", 4);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, _textures[_FULL_ICON]);
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, data.one[0], data.one[1], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.two);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
        if (this._simpleBorder == null) {
            this._simpleBorder = new UIBorderObject(false, true);
            ((UIBorderObject) this._simpleBorder).setSize(_SIMPLE_MIN_SIZE[0], _SIMPLE_MIN_SIZE[1]);
        }
        if (Global.getCurrentState() == GameState.TITLE && this.mainSettingsPlugin == null) {
            final float[] mainPanelSize = new float[]{ShaderCore.getScreenWidth() * 0.8f, ShaderCore.getScreenHeight() * 0.8f, ShaderCore.getScreenWidth() * 0.1f, ShaderCore.getScreenHeight() * 0.1f};
            this.mainSettingsPlugin = new BUtil_BaseConfigPanel(this, mainPanelSize[0], mainPanelSize[1], mainPanelSize[2], mainPanelSize[3]);
            this.mainSettingsPanel = Global.getSettings().createCustom(mainPanelSize[0], mainPanelSize[1], this.mainSettingsPlugin);
            this.mainSettingsPanel.getPosition().inBL(mainPanelSize[2], mainPanelSize[3]);
            ((BUtil_BaseConfigPanel) this.mainSettingsPlugin).init(this.mainSettingsPanel);
            ((BUtil_BaseConfigPanel) this.mainSettingsPlugin).initBackgroundTex(_backgroundTex, _backgroundTexUV);
        }
        this._npotTex = GLContext.getCapabilities().GL_ARB_texture_non_power_of_two && GLContext.getCapabilities().OpenGL20;
        if (Global.getCurrentState() != GameState.TITLE || (Global.getCurrentState() == GameState.TITLE && engine.isSimulation())) engine.removePlugin(this);
    }

    public void advance(float amount, List<InputEventAPI> events) {
        if (this._panelCD < 0.5f) this._panelCD += amount;
    }

    /**
     * Make sure OpenGL context has created.
     */
    public static void globalInitLater() {
        if (globalInit) return;
        ShaderCore.init();
        ShaderCore.initMiscShaderPrograms();
        BUtil_InstanceDataMemoryPool.init();
        GL11.glFinish();
        KernelCore.init();
        BoxDatabase.initCLState();
        BoxConfigs.sysCheck();
        BoxConfigs.check();
        BoxConfigs.initShaderPacks();
        initBackgroundTex();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, BoxDatabase.BUtil_ONE.getTextureId());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, BoxDatabase.BUtil_ZERO.getTextureId());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, BoxDatabase.BUtil_NONE.getTextureId());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, BoxDatabase.BUtil_Z.getTextureId());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, BoxDatabase.BUtil_TILES.getTextureId());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        if (BoxConfigs.isGLDebugOutputSupported() && BoxConfigs.isGLDebugEnable()) {
            GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
            GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
            GL43.glDebugMessageCallback(new KHRDebugCallback());
            GL43.glDebugMessageControl(GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, GL11.GL_DONT_CARE, (IntBuffer) null, true);
        }
        globalInit = true;
    }

    public static boolean isGlobalInitialized() {
        return globalInit;
    }

    private static void initBackgroundTex() {
        _backgroundTex = GL11.glGenTextures();
        if (_backgroundTex != 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, _backgroundTex);
            final int[] texData = new int[2];
            if (GLContext.getCapabilities().GL_ARB_texture_non_power_of_two && GLContext.getCapabilities().OpenGL20) {
                texData[0] = ShaderCore.getScreenScaleWidth();
                texData[1] = ShaderCore.getScreenScaleHeight();
                _backgroundTexUV[0] = 1.0f;
                _backgroundTexUV[1] = 1.0f;
            } else {
                texData[0] = ShaderCore.getScreenFixWidth();
                texData[1] = ShaderCore.getScreenFixHeight();
                _backgroundTexUV[0] = ShaderCore.getScreenFixU();;
                _backgroundTexUV[1] = ShaderCore.getScreenFixV();;
            }
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, texData[0], texData[1], 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
    }

    public static GUIState getGuiState() {
        return guiState;
    }

    public void renderInUICoords(ViewportAPI viewport) {
        if (Global.getCurrentState() != GameState.TITLE || this.mainSettingsPlugin == null || this.mainSettingsPanel == null) return;
        if (!isTitlePanelEmpty()) return;
        if (guiState == GUIState.CONFIG || ((BUtil_BaseConfigPanel) this.mainSettingsPlugin).isOn()) {
            if (_backgroundTex == 0) return;
            final int[] texData = new int[2];
            if (this._npotTex) {
                texData[0] = ShaderCore.getScreenScaleWidth();
                texData[1] = ShaderCore.getScreenScaleHeight();
            } else {
                texData[0] = ShaderCore.getScreenFixWidth();
                texData[1] = ShaderCore.getScreenFixHeight();
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, _backgroundTex);
            if (getScreenFF) {
                GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 0, 0, texData[0], texData[1], 0);
                getScreenFF = false;
            } else {
                GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, texData[0], texData[1]);
            }
            return;
        }
        UIBorderObject border = (UIBorderObject) this._simpleBorder;
        this.inputCheck(border);
        this.simpleSettingsGUI(border);
    }

    public static boolean isTitlePanelEmpty() {
        Object dialogType = null;

        if (BUtil_RefMethod.VALID) {
            TitleScreenState title = (TitleScreenState) AppDriver.getInstance().getCurrentState();
            String name;
            try {
                CombatState combatTitle = (CombatState) AppDriver.getInstance().getCurrentState();
            } catch (Throwable ignored) {
                try {
                    for (Object field : ((Object) title).getClass().getDeclaredFields()) {
                        BUtil_RefMethod.java_lang_reflect_Field_setAccessible(field, true);
                        name = BUtil_RefMethod.java_lang_reflect_Field_getName(field);
                        if (name.contentEquals("dialogType")) {
                            dialogType = BUtil_RefMethod.java_lang_reflect_Field_get(field, (Object) title);
                            break;
                        }
                    }
                } catch (Throwable e) {
                    CommonUtil.printThrowable(BoxConfigGUI.class, "'BoxUtil' title panel check method error: ", e);
                }
            }
        }
        return dialogType == null;
    }

    private static UIPanelAPI getScreenPanel() {
        if (!BUtil_RefMethod.VALID) return null;
        TitleScreenState title = (TitleScreenState) AppDriver.getInstance().getCurrentState();
        Object panel = null;
        try {
            Object getScreenPanel = null;
            for (Object method : title.getClass().getMethods()) {
                if (BUtil_RefMethod.java_lang_reflect_Method_getName(method).equals("getScreenPanel")) {
                    getScreenPanel = method;
                    break;
                }
            }
            panel = BUtil_RefMethod.java_lang_reflect_Method_invoke().invoke(getScreenPanel, title);
        } catch (Throwable e) {
            CommonUtil.printThrowable(BoxConfigGUI.class, "'BoxUtil' title panel getter error: ", e);
        }
        return (UIPanelAPI) panel;
    }

    private void inputCheck(UIBorderObject border) {
        float mouseX = Mouse.getX() / _SCREEN_SCALE;
        float mouseY = Mouse.getY() / _SCREEN_SCALE;
        if (mouseX <= ((guiState == GUIState.SIMPLE ? _SIMPLE_FULL_SIZE[0] : _SIMPLE_MIN_SIZE[0]) + _PADDING_SIMPLE) &&
                mouseY <= ((guiState == GUIState.SIMPLE ? _SIMPLE_FULL_SIZE[0] : _SIMPLE_MIN_SIZE[1]) + _PADDING_SIMPLE)) {
            if (!this.stateSwitch) {
                guiState = GUIState.SIMPLE;
                if (border != null) border.setSize(_SIMPLE_FULL_SIZE[0], _SIMPLE_FULL_SIZE[0]);
                Global.getSoundPlayer().playUISound("BUtil_button_in", 1.0f, 1.0f);
                this.stateSwitch = true;
            }
            if (this._panelCD > 0.1f && Mouse.isButtonDown(0)) {
                guiState = GUIState.CONFIG;
                this._panelCD = 0.0f;
                ((BUtil_BaseConfigPanel) this.mainSettingsPlugin).stateSwitch(true);
                Global.getSoundPlayer().playUISound("BUtil_button_down", 1.0f, 1.0f);
                UIPanelAPI panel = getScreenPanel();
                if (panel != null) panel.addComponent(this.mainSettingsPanel);
            }
        } else {
            if (this.stateSwitch) {
                guiState = GUIState.MINIMIZE;
                if (border != null) border.setSize(_SIMPLE_MIN_SIZE[0], _SIMPLE_MIN_SIZE[1]);
                this.stateSwitch = false;
            }
        }
    }

    public void closePanel() {
        UIPanelAPI panel = getScreenPanel();
        if (panel != null) panel.removeComponent(this.mainSettingsPanel);
        this._panelCD = 0.0f;
        guiState = GUIState.SIMPLE;
    }

    private void simpleSettingsGUI(UIBorderObject border) {
        if (border != null) border.render(_PADDING_SIMPLE, _PADDING_SIMPLE);
        if (guiState == GUIState.SIMPLE) {
            if (_textures != null && _textures[_CONFIG_ICON] != 0) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, _textures[_CONFIG_ICON]);
                GL11.glColor4f(_GUI_ICON_COLOR[0], _GUI_ICON_COLOR[1], _GUI_ICON_COLOR[2], 1.0f);
                GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex2f(_SIMPLE_ICON_ANCHOR[0], _SIMPLE_ICON_ANCHOR[0]);
                GL11.glTexCoord2f(0.0f, 1.0f);
                GL11.glVertex2f(_SIMPLE_ICON_ANCHOR[0], _SIMPLE_ICON_ANCHOR[1]);
                GL11.glTexCoord2f(1.0f, 0.0f);
                GL11.glVertex2f(_SIMPLE_ICON_ANCHOR[1], _SIMPLE_ICON_ANCHOR[0]);
                GL11.glTexCoord2f(1.0f, 1.0f);
                GL11.glVertex2f(_SIMPLE_ICON_ANCHOR[1], _SIMPLE_ICON_ANCHOR[1]);
                GL11.glEnd();
            }
        } else {
            if (BoxConfigs.isBaseGL42Supported()) GL11.glColor4f(_GUI_POS_COLOR[0], _GUI_POS_COLOR[1], _GUI_POS_COLOR[2], 1.0f);
            else GL11.glColor4f(_GUI_NEG_COLOR[0], _GUI_NEG_COLOR[1], _GUI_NEG_COLOR[2], 1.0f);
            if (_textures != null && _textures[_BASE_ICON] != 0) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, _textures[_BASE_ICON]);
                GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex2f(_SIMPLE_BLOCK_A[0], _SIMPLE_BLOCK_A[0]);
                GL11.glTexCoord2f(0.0f, 1.0f);
                GL11.glVertex2f(_SIMPLE_BLOCK_A[0], _SIMPLE_BLOCK_A[1]);
                GL11.glTexCoord2f(1.0f, 0.0f);
                GL11.glVertex2f(_SIMPLE_BLOCK_A[1], _SIMPLE_BLOCK_A[0]);
                GL11.glTexCoord2f(1.0f, 1.0f);
                GL11.glVertex2f(_SIMPLE_BLOCK_A[1], _SIMPLE_BLOCK_A[1]);
                GL11.glEnd();
            }

            if (BoxConfigs.isGLParallelSupported()) GL11.glColor4f(_GUI_POS_COLOR[0], _GUI_POS_COLOR[1], _GUI_POS_COLOR[2], 1.0f);
            else GL11.glColor4f(_GUI_NEG_COLOR[0], _GUI_NEG_COLOR[1], _GUI_NEG_COLOR[2], 1.0f);
            if (_textures != null && _textures[_FULL_ICON] != 0) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, _textures[_FULL_ICON]);
                GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex2f(_SIMPLE_BLOCK_B[0], _SIMPLE_BLOCK_A[0]);
                GL11.glTexCoord2f(0.0f, 1.0f);
                GL11.glVertex2f(_SIMPLE_BLOCK_B[0], _SIMPLE_BLOCK_A[1]);
                GL11.glTexCoord2f(1.0f, 0.0f);
                GL11.glVertex2f(_SIMPLE_BLOCK_B[1], _SIMPLE_BLOCK_A[0]);
                GL11.glTexCoord2f(1.0f, 1.0f);
                GL11.glVertex2f(_SIMPLE_BLOCK_B[1], _SIMPLE_BLOCK_A[1]);
                GL11.glEnd();
            }
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
