package org.boxutil.backends.gui;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import org.boxutil.define.BoxEnum;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.CommonUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

public class BUtil_BaseTrackbar extends BaseCustomUIPanelPlugin {
    protected final static float _SCREEN_SCALE_DIV = 1.0f / Global.getSettings().getScreenScaleMult();
    protected static int _GLOBAL_LOCK = -1;
    protected final int _fixedID = this.hashCode();
    private final UIComponentAPI _component;
    private final LabelAPI _value;
    private final Vector4f[] colorState = new Vector4f[]{new Vector4f(), new Vector4f(), new Vector4f(), new Vector4f()};
    private final Vector4f[] colorDisabledState = new Vector4f[]{new Vector4f(), new Vector4f(), new Vector4f(), new Vector4f()};
    private final String[] soundID = new String[]{"BUtil_trackbar_drag", "ui_button_disabled_pressed"};
    private final float[] state = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}; // barWidth, barHeight, totalWidth, perStep, alpha, keyCD, activeAlpha
    private final int[] stateI = new int[]{0, 0, 1}; // currStep, maxStep, keyStep
    private final boolean[] stateB = new boolean[]{true, true, true, true, false, false, true, false}; // showMeter, meterValid, isEnabled, isInteractive, breakAnimation, haveChanged, showGradient, keyboardEvents
    private PositionAPI _pos = null;
    private PositionAPI inputCheck = null;

    public static boolean haveDraggingEvent() {
        return _GLOBAL_LOCK > 0;
    }

    public static void resetEvent() {
        _GLOBAL_LOCK = -1;
    }

    public static int getEventID() {
        return _GLOBAL_LOCK;
    }

    protected void getDisabledColor(byte index) {
        float[] array = CommonUtil.RGBToHSVArray(this.colorState[index].x, this.colorState[index].y, this.colorState[index].z);
        array[1] *= 0.2f;
        array[2] *= 0.8f;
        array = CommonUtil.HSVToRGBArray(array[0], array[1], array[2]);
        this.colorDisabledState[index].x = array[0];
        this.colorDisabledState[index].y = array[1];
        this.colorDisabledState[index].z = array[2];
    }

    public BUtil_BaseTrackbar(TooltipMakerAPI maker, String value, float width, float height, float numSpaces, int step, float pad) {
        this.state[0] = width;
        this.state[1] = height;
        this.state[2] = width + numSpaces;
        CustomPanelAPI itemPanel = Global.getSettings().createCustom(this.state[2], this.state[1], this);
        itemPanel.getPosition().inTL(0.0f, 0.0f);
        itemPanel.getPosition().setSize(this.state[2], this.state[1]);

        TooltipMakerAPI makerFixed = itemPanel.createUIElement(this.state[2], this.state[1], false);
        makerFixed.getPosition().inTL(0.0f, 0.0f);
        makerFixed.setParaOrbitronLarge();
        makerFixed.setParaFontColor(Misc.getTextColor());

        this._value = makerFixed.addPara(value, 0.0f);
        this._value.setAlignment(Alignment.RMID);
        this._value.getPosition().inTR(0.0f, (this.state[1] - this._value.getPosition().getHeight()) * 0.5f);
        this._value.getPosition().setSize(numSpaces, this._value.getPosition().getHeight());
        makerFixed.setHeightSoFar(this.state[1]);
        itemPanel.addUIElement(makerFixed);
        this._pos = itemPanel.getPosition();
        this._component = maker.addCustom(itemPanel, pad);
        this.stateI[1] = Math.max(step, 1);
        this.state[3] = (this.stateI[1] == 1) ? 1.0f : 1.0f / this.stateI[1];
        this.stateB[0] = this.stateB[1] = this.state[0] / this.getMaxStep() >= 8.0f;

        CommonUtil.colorNormalization4f(Global.getSettings().getColor("buttonBg"), this.colorState[2]);
        this.colorState[2].w *= 0.9f;
        CommonUtil.colorNormalization4f(Global.getSettings().getColor("buttonBgDark"), this.colorState[3]);
        CommonUtil.colorNormalization4f(Global.getSettings().getColor("buttonText"), this.colorState[0]);
        this.colorState[1].set(this.colorState[0]);
        this.getDisabledColor((byte) 0);
        this.getDisabledColor((byte) 1);
        this.getDisabledColor((byte) 2);
        this.getDisabledColor((byte) 3);
    }

    public void advance(float amount) {
        if (this.haveChanged()) this.stateB[5] = false;
        if (this.state[5] > 0.0f) this.state[5] -= amount;
        float mouseX = Mouse.getX() * _SCREEN_SCALE_DIV;
        float mouseY = Mouse.getY() * _SCREEN_SCALE_DIV;
        boolean within = mouseX >= this._pos.getX() && mouseY >= this._pos.getY() && mouseX <= this._pos.getX() + this.state[0] && mouseY <= this._pos.getY() + this.state[1];
        boolean active = this.isActive() && this.isEnabled() && this.isInteractive();

        if (!Mouse.isButtonDown(0) && active) {
            active = false;
            if (BUtil_BaseTrackbar.getEventID() == this._fixedID) BUtil_BaseTrackbar.resetEvent();
        }
        if (active) {
            float rightEdge = this._pos.getX() + this.state[0];
            float level = CalculateUtil.inverseLerp(this._pos.getX(), rightEdge, Mouse.getX() * _SCREEN_SCALE_DIV);
            int lastStep = this.getCurrStep();
            this.setCurrStep(Math.min(Math.round(level / this.state[3]), this.stateI[1]));
            if (lastStep != this.getCurrStep()) {
                Global.getSoundPlayer().playUISound(this.soundID[0], 1.0f, 1.0f);
                this.stateB[5] = true;
            }
            this.state[6] = 1.8f;
        } else if (this.state[6] > 1.0f) this.state[6] = Math.max(this.state[6] - amount * 3.0f, 1.0f);

        within &= !this.isBreakAnimation();
        float a5 = amount * 5.0f;
        if ((within || active) && this.state[4] < 1.0f) {
            this.state[4] = Math.min(this.state[4] + a5 + a5, 1.0f);
        } else if (!within && !active && this.state[4] > 0.0f) {
            this.state[4] = Math.max(this.state[4] - a5, 0.0f);
        }

        if (this.isBreakAnimation() && this.isActive()) {
            BUtil_BaseTrackbar.resetEvent();
        }
    }

    protected void glColor(Vector4f color, float alpha) {
        GL11.glColor4f(color.x, color.y, color.z, color.w * alpha);
    }

    public void renderBelow(float alphaMult) {
        float anim = CalculateUtil.smoothstep(0.0f, 1.0f, this.state[4] * this.state[4]);
        float alpha = this.state[6] * alphaMult;
        float barProgress = (float) this.getCurrStep() / this.getMaxStep();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float midX = this._pos.getX() + this.state[0] * 0.5f;
        float midY = this._pos.getY() + this.state[1] * 0.5f;
        float splitX = this._pos.getX() + this.state[0] * barProgress;
        float barHeight = CalculateUtil.mix(this.state[1] * 0.07f, this.state[1] * 0.28f, anim);
        float barYPosBottom = midY - barHeight;
        float barYPosTop = midY + barHeight;
        float pickerHeight = CalculateUtil.mix(this.state[1] * 0.1f, this.state[1] * 0.33f, anim);
        float meterTopYPos = CalculateUtil.mix(midY + this.state[1] * 0.2f, this._pos.getY() + this.state[1], anim);
        float meterBottomYPos = CalculateUtil.mix(midY - this.state[1] * 0.2f, this._pos.getY(), anim);
        float meterHeight = CalculateUtil.mix(this.state[1] * 0.005f, this.state[1] * 0.08f, anim);

        if (barProgress > 0.0f) {
            this.glColor(this.getProgressColor(), alpha);
            GL11.glRectf(this._pos.getX(), barYPosBottom, splitX, barYPosTop);
        }
        if (barProgress < 1.0f) {
            this.glColor(this.getProgressEmptyColor(), alpha);
            GL11.glRectf(splitX, barYPosBottom, this._pos.getX() + this.state[0], barYPosTop);
        }

        if (this.isShowGradient()) {
            GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_SRC_COLOR);
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            GL11.glColor3ub((byte) 85, (byte) 85, (byte) 85);
            GL11.glVertex2f(this._pos.getX(), barYPosBottom);
            GL11.glVertex2f(this._pos.getX(), barYPosTop);
            GL11.glColor3ub(BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR);
            GL11.glVertex2f(midX, barYPosBottom);
            GL11.glVertex2f(midX, barYPosTop);
            GL11.glColor3ub((byte) 85, (byte) 85, (byte) 85);
            GL11.glVertex2f(this._pos.getX() + this.state[0], barYPosBottom);
            GL11.glVertex2f(this._pos.getX() + this.state[0], barYPosTop);
            GL11.glEnd();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        if (anim > 0.0f && this.isEnabled()) {
            this.glColor(this.getPickerColor(), alpha * this.state[4]);
            GL11.glRectf(splitX - 5.0f, midY - pickerHeight, splitX + 5.0f, midY + pickerHeight);
        }

        if (anim > 0.0f && this.isShowMeter()) {
            float meterXPos;
            this.glColor(this.isEnabled() ? this.getMeterColor() : this.colorDisabledState[0], this.state[4] * alphaMult);
            for (int i = 0; i <= this.getMaxStep(); ++i) {
                meterXPos = this._pos.getX() + this.state[0] * ((float) i / this.getMaxStep());
                GL11.glRectf(meterXPos - 1.5f, meterBottomYPos, meterXPos + 1.5f, meterBottomYPos + meterHeight);
                GL11.glRectf(meterXPos - 1.5f, meterTopYPos - meterHeight, meterXPos + 1.5f, meterTopYPos);
            }
        }
    }

    public void processInput(List<InputEventAPI> events) {
        if (!this.isInteractive()) return;
        float mouseX = Mouse.getX() * _SCREEN_SCALE_DIV;
        float mouseY = Mouse.getY() * _SCREEN_SCALE_DIV;
        boolean within = mouseX >= this.inputCheck.getX() && mouseY >= this.inputCheck.getY() && mouseX <= this.inputCheck.getX() + this.inputCheck.getWidth() && mouseY <= this.inputCheck.getY() + this.inputCheck.getHeight();
        boolean barWithin = mouseX >= this._pos.getX() && mouseY >= this._pos.getY() && mouseX <= this._pos.getX() + this.state[0] && mouseY <= this._pos.getY() + this.state[1];

        int lastStep = this.getCurrStep();
        boolean rightKey;
        for (InputEventAPI event : events) {
            if (within) {
                if (!BUtil_BaseTrackbar.haveDraggingEvent() && event.isLMBDownEvent() && barWithin && this.state[5] <= 0.0f) {
                    if (this.isEnabled()) BUtil_BaseTrackbar._GLOBAL_LOCK = this._fixedID; else Global.getSoundPlayer().playUISound(this.soundID[1], 1.0f, 1.0f);
                    this.state[5] = 0.1f;
                    event.consume();
                    break;
                }
                if (this.isEnabledKeyboardEvents() && !BUtil_BaseTrackbar.haveDraggingEvent() && this.state[5] <= 0.0f) {
                    if (event.isKeyDownEvent()) {
                        rightKey = event.getEventValue() == Keyboard.KEY_RIGHT;
                        if (event.getEventValue() == Keyboard.KEY_LEFT || rightKey) {
                            if (this.isEnabled()) {
                                this.setCurrStep(this.getCurrStep() + (rightKey ? this.stateI[2] : -this.stateI[2]));
                                Global.getSoundPlayer().playUISound(this.soundID[0], 1.0f, 1.0f);
                            } else Global.getSoundPlayer().playUISound(this.soundID[1], 1.0f, 0.5f);
                            this.state[5] = 0.1f;
                            event.consume();
                            break;
                        }
                    }
                }
            }
            if (event.isLMBUpEvent() && BUtil_BaseTrackbar.haveDraggingEvent()) {
                BUtil_BaseTrackbar.resetEvent();
                event.consume();
                break;
            }
        }
        if (lastStep != this.getCurrStep()) this.stateB[5] = true;
    }

    public UIComponentAPI getComponent() {
        return this._component;
    }

    public LabelAPI getValue() {
        return this._value;
    }

    public boolean isShowMeter() {
        return this.stateB[0];
    }

    public void setShowMeter(boolean showMeter) {
        this.stateB[0] = this.stateB[1] && showMeter;
    }

    public boolean isEnabled() {
        return this.stateB[2];
    }

    public void setEnabled(boolean enabled) {
        this.stateB[2] = enabled;
    }

    public boolean isInteractive() {
        return this.stateB[3];
    }

    public void setInteractive(boolean interactive) {
        this.stateB[3] = interactive;
    }

    public boolean isBreakAnimation() {
        return this.stateB[4];
    }

    public void setBreakAnimation(boolean breakAnimation) {
        this.stateB[4] = breakAnimation;
    }

    public boolean haveChanged() {
        return this.stateB[5];
    }

    public int getMaxStep() {
        return this.stateI[1];
    }

    public void setMaxStep(int maxStep) {
        this.stateI[1] = Math.max(maxStep, 1);
    }

    public int getCurrStep() {
        return this.stateI[0];
    }

    public int getKeyStep() {
        return this.stateI[2];
    }

    public void setKeyStep(int keyStep) {
        this.stateI[2] = keyStep;
    }

    public void setCurrStep(int currStep) {
        this.stateI[0] = Math.max(Math.min(currStep, this.stateI[1]), 0);
    }

    public Vector4f getMeterColor() {
        return this.colorState[0];
    }

    public void setMeterColor(Vector4f color) {
        this.colorState[0].set(color);
        this.getDisabledColor((byte) 0);
    }

    public Vector4f getPickerColor() {
        return this.colorState[1];
    }

    public void setPickerColor(Vector4f color) {
        this.colorState[1].set(color);
        this.getDisabledColor((byte) 1);
    }

    public Vector4f getProgressColor() {
        return this.colorState[2];
    }

    public void setProgressColor(Vector4f color) {
        this.colorState[2].set(color);
        this.getDisabledColor((byte) 2);
    }

    public Vector4f getProgressEmptyColor() {
        return this.colorState[3];
    }

    public void setProgressEmptyColor(Vector4f color) {
        this.colorState[3].set(color);
        this.getDisabledColor((byte) 3);
    }

    public String getDraggingSoundID() {
        return this.soundID[0];
    }

    public void setDraggingSound(String sound) {
        this.soundID[0] = sound;
    }

    public String getDisabledDraggingSoundID() {
        return this.soundID[1];
    }

    public void setDisabledDraggingSound(String sound) {
        this.soundID[1] = sound;
    }

    public boolean isShowGradient() {
        return this.stateB[6];
    }

    public void setShowGradient(boolean showGradient) {
        this.stateB[6] = showGradient;
    }

    public boolean isEnabledKeyboardEvents() {
        return this.stateB[7];
    }

    public void setEnabledKeyboardEvents(boolean enable) {
        this.stateB[7] = enable;
    }

    public float getAnimationProgress() {
        return this.state[4];
    }

    public boolean isActive() {
        return BUtil_BaseTrackbar._GLOBAL_LOCK == this._fixedID;
    }

    public PositionAPI getPosition() {
        return this._pos;
    }

    public void setInputCheck(PositionAPI inputCheck) {
        this.inputCheck = inputCheck;
    }
}
