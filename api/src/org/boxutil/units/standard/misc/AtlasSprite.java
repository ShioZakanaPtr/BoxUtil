package org.boxutil.units.standard.misc;

import com.fs.starfarer.api.graphics.SpriteAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.util.TrigUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * Always available, even if {@link BoxConfigs#isShaderEnable()} at false.
 */
public class AtlasSprite implements SpriteAPI {
    protected final static byte _COLOR = 0;
    protected final static byte _AVG_COLOR = 1;
    protected final static byte _AVG_GRAY_COLOR = 2;
    protected final int textureID;
    protected final float[] current = new float[8];
    protected final float[] vertices = new float[16];
    protected final int[] blendFunc = new int[]{GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA};
    protected float alpha = 1.0f;
    protected float[] facing = new float[2];
    protected final byte[][] color = new byte[][]{new byte[]{127, 127, 127, 127}, new byte[]{127, 127, 127, 127}, new byte[]{127, 127, 127, 127}};

    /**
     * @param anchorX based bottom-left.
     * @param anchorY based bottom-left.
     * @param centerX center of ship.
     * @param centerY center of ship.
     */
    public AtlasSprite(int atlasTextureID, float atlasWidth, float atlasHeight, float rawSizeWidth, float rawSizeHeight, float anchorX, float anchorY, float centerX, float centerY, float compensationAngle) {
        this.textureID = atlasTextureID;
        this.current[0] = rawSizeWidth;
        this.current[1] = rawSizeHeight;
        this.current[2] = centerX;
        this.current[3] = centerY;

        this.vertices[0] = -centerX;
        this.vertices[1] = -centerY;
        this.vertices[2] = rawSizeWidth - centerX;
        this.vertices[3] = -centerY;
        this.vertices[4] = -centerX;
        this.vertices[5] = rawSizeHeight - centerY;
        this.vertices[6] = rawSizeWidth - centerX;
        this.vertices[7] = rawSizeHeight - centerY;

        float texX = anchorX / atlasWidth;
        float texY = anchorY / atlasHeight;
        float texWidth = rawSizeWidth / atlasWidth;
        float texHeight = rawSizeHeight / atlasHeight;
        this.current[4] = texX;
        this.current[5] = texY;
        this.current[6] = texWidth;
        this.current[7] = texHeight;

        this.vertices[8] = texX;
        this.vertices[9] = texY;
        this.vertices[10] = texX + texWidth;
        this.vertices[11] = texY;
        this.vertices[12] = texX;
        this.vertices[13] = texY + texHeight;
        this.vertices[14] = texX + texWidth;
        this.vertices[15] = texY + texHeight;
    }

    public float[] getCurrent() {
        return this.current;
    }

    public void loadModelMatrix(float x, float y, boolean offset) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        float angle = (this.facing[0] + this.facing[1]) * 0.5f;
        angle = (angle + 360.0f) % 360.0f;
        angle = (float) Math.toRadians(angle);
        float w = (float) Math.cos(angle);
        float z = TrigUtil.sinFormCosRadiansF(w, angle);
        float dqz = z + z;
        float q22 = dqz * z;
        float q23 = dqz * w;
        float q22OM = 1.0f - q22;
        float offsetX = x;
        float offsetY = y;
        if (offset) {
            offsetX += this.getCenterX();
            offsetY += this.getCenterY();
        }
        buffer.put(0, q22OM);
        buffer.put(1, q23);
        buffer.put(4, -q23);
        buffer.put(5, q22OM);
        buffer.put(10, 1.0f);
        buffer.put(12, offsetX);
        buffer.put(13, offsetY);
        buffer.put(15, 1.0f);
        GL11.glMultMatrix(buffer);
    }

    public float getWidth() {
        return this.current[0];
    }

    public float getHeight() {
        return this.current[1];
    }

    public void setSize(float width, float height) {
        this.current[0] = width;
        this.current[1] = height;
        this.vertices[0] = -this.getCenterX();
        this.vertices[1] = -this.getCenterY();
        this.vertices[2] = width - this.getCenterX();
        this.vertices[3] = -this.getCenterY();
        this.vertices[4] = -this.getCenterX();
        this.vertices[5] = height - this.getCenterY();
        this.vertices[6] = width - this.getCenterX();
        this.vertices[7] = height - this.getCenterY();
    }

    public void setWidth(float width) {
        this.current[0] = width;
        this.vertices[0] = -this.getCenterX();
        this.vertices[2] = width - this.getCenterX();
        this.vertices[4] = -this.getCenterX();
        this.vertices[6] = width - this.getCenterX();
    }

    public void setHeight(float height) {
        this.current[1] = height;
        this.vertices[1] = -this.getCenterY();
        this.vertices[3] = -this.getCenterY();
        this.vertices[5] = height - this.getCenterY();
        this.vertices[7] = height - this.getCenterY();
    }

    public float getCenterX() {
        return this.current[2];
    }

    public float getCenterY() {
        return this.current[3];
    }

    public void setCenter(float x, float y) {
        this.current[2] = x;
        this.current[3] = y;
    }

    public void setCenterX(float cx) {
        this.current[2] = cx;
        this.vertices[0] = -cx;
        this.vertices[2] = this.getWidth() - cx;
        this.vertices[4] = -cx;
        this.vertices[6] = this.getWidth() - cx;
    }

    public void setCenterY(float cy) {
        this.current[3] = cy;
        this.vertices[1] = -cy;
        this.vertices[3] = -cy;
        this.vertices[5] = this.getHeight() - cy;
        this.vertices[7] = this.getHeight() - cy;
    }

    public float getTexX() {
        return this.current[4];
    }

    public float getTexY() {
        return this.current[5];
    }

    public float getTexWidth() {
        return this.current[6];
    }

    public float getTexHeight() {
        return this.current[7];
    }

    public void setTexX(float texX) {
        this.current[4] = texX;
        this.vertices[8] = texX;
        this.vertices[12] = texX;
    }

    public void setTexY(float texY) {
        this.current[5] = texY;
        this.vertices[9] = texY;
        this.vertices[11] = texY;
    }

    public void setTexWidth(float texWidth) {
        this.current[6] = texWidth;
        this.vertices[10] = this.getTexX() + texWidth;
        this.vertices[14] = this.getTexX() + texWidth;
    }

    public void setTexHeight(float texHeight) {
        this.current[7] = texHeight;
        this.vertices[13] = this.getTexY() + texHeight;
        this.vertices[15] = this.getTexY() + texHeight;
    }

    public float getTextureWidth() {
        return this.current[6];
    }

    public float getTextureHeight() {
        return this.current[7];
    }

    public void bindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureID);
    }

    public void releaseBind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public int getTextureId() {
        return this.textureID;
    }

    private void render(float x, float y, boolean offset, boolean withBind) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(this.blendFunc[0], this.blendFunc[1]);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (withBind) this.bindTexture();
        this.loadModelMatrix(x, y, offset);
        GL11.glColor4ub(this.color[_COLOR][0], this.color[_COLOR][1], this.color[_COLOR][2], (byte) ((this.color[_COLOR][3] & 0xFF) * this.alpha));
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(this.vertices[8], this.vertices[9]);
        GL11.glVertex2f(this.vertices[0], this.vertices[1]);
        GL11.glTexCoord2f(this.vertices[10], this.vertices[11]);
        GL11.glVertex2f(this.vertices[2], this.vertices[3]);
        GL11.glTexCoord2f(this.vertices[12], this.vertices[13]);
        GL11.glVertex2f(this.vertices[4], this.vertices[5]);
        GL11.glTexCoord2f(this.vertices[14], this.vertices[15]);
        GL11.glVertex2f(this.vertices[6], this.vertices[7]);
        GL11.glEnd();
        this.releaseBind();
        GL11.glPopMatrix();
    }

    public void renderAtCenter(float x, float y) {
        this.render(x, y, false, true);
    }

    public void renderAtCenterNoBind(float x, float y) {
        this.render(x, y, false, false);
    }

    public void render(float x, float y) {
        this.render(x, y, true, true);
    }

    public void renderNoBind(float x, float y) {
        this.render(x, y, true, false);
    }

    private void renderRegion(float x, float y, float tx, float ty, float tw, float th, boolean offset) {
        final float[] factor = new float[]{this.getTextureWidth(), this.getTextureHeight()};
        final float[] uvSize = new float[]{factor[0] * tx, factor[1] * ty, factor[0] * tw, factor[1] * th};
        final float[] uvCorners = new float[]{uvSize[0] + uvSize[2], uvSize[1] + uvSize[3]};
        final float[] uv = new float[]{uvSize[0], uvSize[1], uvCorners[0], uvSize[1], uvSize[0], uvCorners[1], uvCorners[0], uvCorners[1]};
        final float[] sizeRaw = new float[]{this.getWidth(), this.getHeight()};
        final float[] size = new float[]{sizeRaw[0] * tx, sizeRaw[1] * ty, sizeRaw[0] * tw, sizeRaw[1] * th};
        final float[] loc = new float[]{size[0], size[1], size[2], size[1], size[0], size[3], size[2], size[3]};
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(this.blendFunc[0], this.blendFunc[1]);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.bindTexture();
        this.loadModelMatrix(x, y, offset);
        GL11.glColor4ub(this.color[_COLOR][0], this.color[_COLOR][1], this.color[_COLOR][2], (byte) ((this.color[_COLOR][3] & 0xFF) * this.alpha));
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(uv[0], uv[1]);
        GL11.glVertex2f(loc[0], loc[1]);
        GL11.glTexCoord2f(uv[2], uv[3]);
        GL11.glVertex2f(loc[2], loc[3]);
        GL11.glTexCoord2f(uv[4], uv[5]);
        GL11.glVertex2f(loc[4], loc[5]);
        GL11.glTexCoord2f(uv[6], uv[7]);
        GL11.glVertex2f(loc[6], loc[7]);
        GL11.glEnd();
        this.releaseBind();
        GL11.glPopMatrix();
    }

    public void renderRegionAtCenter(float x, float y, float tx, float ty, float tw, float th) {
        this.renderRegion(x, y, tx, ty, tw, th, false);
    }

    public void renderRegion(float x, float y, float tx, float ty, float tw, float th) {
        this.renderRegion(x, y, tx, ty, tw, th, true);
    }

    public void renderWithCorners(float blX, float blY, float tlX, float tlY, float trX, float trY, float brX, float brY) {
        final float vCenterX = (blX + tlX + trX + brX) / 4.0f;
        final float vCenterY = (blY + tlY + trY + brY) / 4.0F;
        final float vtCenterX = this.vertices[8] + this.vertices[10] + this.vertices[12] + this.vertices[14] / 4.0f;
        final float vtCenterY = this.vertices[9] + this.vertices[11] + this.vertices[13] + this.vertices[15] / 4.0f;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(this.blendFunc[0], this.blendFunc[1]);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.bindTexture();
        GL11.glColor4ub(this.color[_COLOR][0], this.color[_COLOR][1], this.color[_COLOR][2], (byte) ((this.color[_COLOR][3] & 0xFF) * this.alpha));
        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, 6);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glTexCoord2f(vtCenterX, vtCenterY);
        GL11.glVertex2f(vCenterX, vCenterY);
        GL11.glTexCoord2f(this.vertices[8], this.vertices[9]);
        GL11.glVertex2f(blX, blY);
        GL11.glTexCoord2f(this.vertices[12], this.vertices[13]);
        GL11.glVertex2f(tlX, tlY);
        GL11.glTexCoord2f(this.vertices[14], this.vertices[15]);
        GL11.glVertex2f(trX, trY);
        GL11.glTexCoord2f(this.vertices[10], this.vertices[11]);
        GL11.glVertex2f(brX, brY);
        GL11.glTexCoord2f(this.vertices[8], this.vertices[9]);
        GL11.glVertex2f(blX, blY);
        GL11.glEnd();
        this.releaseBind();
        GL11.glPopMatrix();
    }

    public float getAlphaMult() {
        return this.alpha;
    }

    public void setAlphaMult(float alphaMult) {
        this.alpha = alphaMult;
    }

    public int getBlendSrc() {
        return this.blendFunc[0];
    }

    public int getBlendDest() {
        return this.blendFunc[1];
    }

    public void setBlendFunc(int src, int dest) {
        this.blendFunc[0] = src;
        this.blendFunc[1] = dest;
    }

    public void setNormalBlend() {
        this.blendFunc[0] = GL11.GL_SRC_ALPHA;
        this.blendFunc[1] = GL11.GL_ONE_MINUS_SRC_ALPHA;
    }

    public void setAdditiveBlend() {
        this.blendFunc[0] = GL11.GL_SRC_ALPHA;
        this.blendFunc[1] = GL11.GL_ONE;
    }

    public float getAngle() {
        return this.facing[0];
    }

    public void setAngle(float angle) {
        this.facing[1] = angle;
    }

    public float getCompensationAngle() {
        return this.facing[1];
    }

    /**
     * @param angle value range [0.0, 360.0].
     */
    public void setCompensationAngle(float angle) {
        this.facing[1] = angle;
    }

    public Color getColor() {
        return new Color(this.color[_COLOR][0] & 0xFF, this.color[_COLOR][1] & 0xFF, this.color[_COLOR][2] & 0xFF, this.color[_COLOR][3] & 0xFF);
    }

    public void setColor(Color color) {
        this.color[_COLOR][0] = (byte) color.getRed();
        this.color[_COLOR][1] = (byte) color.getGreen();
        this.color[_COLOR][2] = (byte) color.getBlue();
        this.color[_COLOR][3] = (byte) color.getAlpha();
    }

    public Color getAverageColor() {
        return new Color(this.color[_AVG_COLOR][0] & 0xFF, this.color[_AVG_COLOR][1] & 0xFF, this.color[_AVG_COLOR][2] & 0xFF, this.color[_AVG_COLOR][3] & 0xFF);
    }

    public void setAverageColor(Color color) {
        this.color[_AVG_COLOR][0] = (byte) color.getRed();
        this.color[_AVG_COLOR][1] = (byte) color.getGreen();
        this.color[_AVG_COLOR][2] = (byte) color.getBlue();
        this.color[_AVG_COLOR][3] = (byte) color.getAlpha();
    }

    public Color getAverageBrightColor() {
        return new Color(this.color[_AVG_GRAY_COLOR][0] & 0xFF, this.color[_AVG_GRAY_COLOR][1] & 0xFF, this.color[_AVG_GRAY_COLOR][2] & 0xFF, this.color[_AVG_GRAY_COLOR][3] & 0xFF);
    }

    public void setAverageBrightColor(Color color) {
        this.color[_AVG_GRAY_COLOR][0] = (byte) color.getRed();
        this.color[_AVG_GRAY_COLOR][1] = (byte) color.getGreen();
        this.color[_AVG_GRAY_COLOR][2] = (byte) color.getBlue();
        this.color[_AVG_GRAY_COLOR][3] = (byte) color.getAlpha();
    }
}
