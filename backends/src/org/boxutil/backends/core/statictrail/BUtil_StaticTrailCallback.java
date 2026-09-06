package org.boxutil.backends.core.statictrail;

import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.define.BoxEnum;
import org.boxutil.util.CommonUtil;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;

public class BUtil_StaticTrailCallback implements StaticTrailTracker.Result {
    private boolean triggerPaused = false;
    private boolean triggerDestroy = false;
    private boolean triggerDestroyImmediate = false;
    private float lastUV = 0.0f;
    private final byte[] color = new byte[]{BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR};
    private final Vector2f lastLoc = new Vector2f(Float.NaN, Float.NaN);
    private final Vector2f location = new Vector2f();
    private final Vector2f facing = new Vector2f(1.0f, 0.0f);

    public Vector2f getPreviousRecordsLocation() {
        return this.lastLoc;
    }

    public float getPreviousRecordsDistanceSq(float targetX, float targetY) {
        if (Float.isNaN(this.lastLoc.x) || Float.isNaN(this.lastLoc.y)) return Float.NaN;
        final float x_diff = this.lastLoc.x - targetX, y_diff = this.lastLoc.y - targetY;
        return x_diff * x_diff + y_diff * y_diff;
    }

    public float getPreviousRecordsDistanceSq(final Vector2f targetLocation) {
        if (targetLocation == null) return Float.NaN;
        return this.getPreviousRecordsDistanceSq(targetLocation.x, targetLocation.y);
    }

    public float getPreviousRecordsDistanceSq() {
        return this.getPreviousRecordsDistanceSq(this.location.x, this.location.y);
    }

    public boolean isNotRecommendedRecordsCurrent(float targetX, float targetY) {
        final float preDist = this.getPreviousRecordsDistanceSq(targetX, targetY);
        return !Float.isNaN(preDist) && preDist < StaticTrailTracker.MINIMAL_VALID_LENGTH_SQ;
    }

    public boolean isNotRecommendedRecordsCurrent(final Vector2f targetLocation) {
        if (targetLocation == null) return true;
        return this.isNotRecommendedRecordsCurrent(targetLocation.x, targetLocation.y);
    }

    public boolean isNotRecommendedRecordsCurrent() {
        return false;
    }

    public Vector2f getCurrentLocation() {
        return this.location;
    }

    public void setCurrentLocation(final float x, final float y) {
        this.location.set(x, y);
    }

    public void setCurrentLocation(final Vector2f location) {
        if (location != null) this.setCurrentLocation(location.x, location.y);
    }

    public Vector2f getCurrentFacing() {
        return this.facing;
    }

    public void setCurrentFacing(final float x, final float y) {
        this.facing.set(x, y);
    }

    public void setCurrentFacing(final Vector2f facingVector) {
        if (facingVector != null) this.setCurrentFacing(facingVector.x, facingVector.y);
    }

    public byte[] getCurrentColor() {
        return this.color;
    }

    public Color getCurrentColorC() {
        return new Color(
                this.color[0] & 0xFF,
                this.color[1] & 0xFF,
                this.color[2] & 0xFF,
                this.color[3] & 0xFF);
    }

    public Vector4f getCurrentColorV() {
        return new Vector4f(
                (this.color[0] & 0xFF) / 255.0f,
                (this.color[1] & 0xFF) / 255.0f,
                (this.color[2] & 0xFF) / 255.0f,
                (this.color[3] & 0xFF) / 255.0f);
    }

    public void setCurrentColor(byte r, byte g, byte b, byte a) {
        this.color[0] = r;
        this.color[1] = g;
        this.color[2] = b;
        this.color[3] = a;
    }

    public void setCurrentColor(int r, int g, int b, int a) {
        this.setCurrentColor((byte) r, (byte) g, (byte) b, (byte) a);
    }

    public void setCurrentColor(float r, float g, float b, float a) {
        this.setCurrentColor((byte) (r * 255.0f), (byte) (g * 255.0f), (byte) (b * 255.0f), (byte) (a * 255.0f));
    }

    public void setCurrentColor(@NotNull final Color color) {
        this.setCurrentColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public void setCurrentColor(@NotNull final Vector4f color) {
        this.setCurrentColor(color.x, color.y, color.z, color.w);
    }

    public byte getCurrentAlpha() {
        return this.color[0];
    }

    public int getCurrentAlphaI() {
        return this.color[0] & 0xff;
    }

    public float getCurrentAlphaF() {
        return this.getCurrentAlphaI() / 255.0f;
    }

    public void setCurrentAlpha(byte a) {
        this.color[0] = a;
    }

    public void setCurrentAlpha(int a) {
        this.setCurrentAlpha((byte) a);
    }

    public void setCurrentAlpha(float a) {
        this.setCurrentAlpha((byte) (a * 255.0f));
    }

    public void pauseOnce() {
        this.triggerPaused = true;
    }

    public void destroy() {
        this.triggerDestroy = true;
    }

    public void destroyImmediate() {
        this.triggerDestroyImmediate = true;
    }

    public boolean isExpired() {
        return this.triggerDestroy || this.triggerDestroyImmediate;
    }

    public float getLastUV() {
        return this.lastUV;
    }

    public int pickColor_vec4() {
        return CommonUtil.packingBytesToInt(this.color[3], this.color[2], this.color[1], this.color[0]);
    }

    public void nextFrame(final float uvAdvance) {
        this.lastLoc.set(this.location);
        this.triggerPaused = false;
        this.lastUV += uvAdvance; // not fract
    }

    public void idleReset() {
        this.lastUV = 0.0f;
        this.lastLoc.set(Float.NaN, Float.NaN);
        this.location.set(0.0f, 0.0f);
        this.facing.set(1.0f, 0.0f);
    }

    public void onCutTrail() {
        this.lastUV = 0.0f;
    }

    public boolean isPaused() {
        return this.triggerPaused;
    }

    public boolean shouldDestroy() {
        return this.triggerDestroy;
    }

    public boolean shouldDestroyImmediate() {
        return this.triggerDestroyImmediate;
    }
}
