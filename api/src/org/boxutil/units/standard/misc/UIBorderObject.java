package org.boxutil.units.standard.misc;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * For rendering vanilla ui border "ui_border1_XX" or "panel00_XX" serial.<p>
 * Vanilla supported.
 */
public class UIBorderObject {
    protected final static byte _S_BL = 0;
    protected final static byte _S_B = 1;
    protected final static byte _S_BR = 2;
    protected final static byte _S_L = 3;
    protected final static byte _S_R = 4;
    protected final static byte _S_TL = 5;
    protected final static byte _S_T = 6;
    protected final static byte _S_TR = 7;
    protected final static byte _S_CENTER = 8;
    protected final SpriteAPI[] _sprites = new SpriteAPI[9];
    protected final Vector2f size = new Vector2f();
    protected final int _sizeStyle;
    protected final boolean isValid;
    protected float expend = 0.0f;
    protected Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    private UIBorderObject() {
        this._sizeStyle = 0;
        this.isValid = false;
    }

    public UIBorderObject(boolean useLargerBorder, boolean translucentBackground) {
        if (useLargerBorder) {
            this._sprites[_S_BL] = Global.getSettings().getSprite("graphics/ui/bgs/panel00_bot_left.png");
            this._sprites[_S_B] = Global.getSettings().getSprite("graphics/ui/bgs/panel00_bot.png");
            this._sprites[_S_BR] = Global.getSettings().getSprite("graphics/ui/bgs/panel00_bot_right.png");
            this._sprites[_S_L] = Global.getSettings().getSprite("graphics/ui/bgs/panel00_left.png");
            this._sprites[_S_R] = Global.getSettings().getSprite("graphics/ui/bgs/panel00_right.png");
            this._sprites[_S_TL] = Global.getSettings().getSprite("graphics/ui/bgs/panel00_top_left.png");
            this._sprites[_S_T] = Global.getSettings().getSprite("graphics/ui/bgs/panel00_top.png");
            this._sprites[_S_TR] = Global.getSettings().getSprite("graphics/ui/bgs/panel00_top_right.png");

        } else {
            this._sprites[_S_BL] = Global.getSettings().getSprite("graphics/ui/bgs/ui_border1_sw.png");
            this._sprites[_S_B] = Global.getSettings().getSprite("graphics/ui/bgs/ui_border1_s.png");
            this._sprites[_S_BR] = Global.getSettings().getSprite("graphics/ui/bgs/ui_border1_se.png");
            this._sprites[_S_L] = Global.getSettings().getSprite("graphics/ui/bgs/ui_border1_w.png");
            this._sprites[_S_R] = Global.getSettings().getSprite("graphics/ui/bgs/ui_border1_e.png");
            this._sprites[_S_TL] = Global.getSettings().getSprite("graphics/ui/bgs/ui_border1_nw.png");
            this._sprites[_S_T] = Global.getSettings().getSprite("graphics/ui/bgs/ui_border1_n.png");
            this._sprites[_S_TR] = Global.getSettings().getSprite("graphics/ui/bgs/ui_border1_ne.png");
        }
        if (translucentBackground) this._sprites[_S_CENTER] = Global.getSettings().getSprite("ui", "BUtil_panel00_center");
        else this._sprites[_S_CENTER] = Global.getSettings().getSprite("graphics/ui/bgs/panel00_center.png");
        if (this._sprites[_S_BL].getTextureId() != 0 && this._sprites[_S_CENTER].getTextureId() != 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this._sprites[_S_BL].getTextureId());
            this._sizeStyle = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            this.isValid = this._sizeStyle > 0;
        } else {
            this._sizeStyle = 0;
            this.isValid = false;
        }
    }

    /**
     * The size of texture shouldn't be too larger, and all sprites should have the same size.
     *
     * @param sprites total 9 sprites: BL, B, BR, L, R, TL, T, TR, Center.
     */
    public UIBorderObject(SpriteAPI[] sprites) {
        for (int i = 0; i < this._sprites.length; i++) {
            this._sprites[i] = sprites[i];
        }
        if (this._sprites[_S_BL].getTextureId() != 0 && this._sprites[_S_CENTER].getTextureId() != 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this._sprites[_S_BL].getTextureId());
            this._sizeStyle = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            this.isValid = this._sizeStyle > 0;
        } else {
            this._sizeStyle = 0;
            this.isValid = false;
        }
    }

    /**
     * General rendering method.<p>
     * Anchor at bottom-left.
     */
    public void render(float x, float y) {
        if (!this.isSizeValid() || !this.isValid) return;
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.glDraw();
        GL11.glPopMatrix();
    }

    /**
     * General rendering method.
     */
    public void renderAtCenter(float x, float y) {
        if (!this.isSizeValid() || !this.isValid) return;
        GL11.glPushMatrix();
        GL11.glTranslatef(x - this.size.x, y - this.size.y, 0.0f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.glDraw();
        GL11.glPopMatrix();
    }

    public void glDraw() {
        final float[] verticesValue = new float[]{this.size.x - this._sizeStyle, this.size.y - this._sizeStyle};
        final float[] uvCut = new float[]{this.size.x - this._sizeStyle - this._sizeStyle, this.size.y - this._sizeStyle - this._sizeStyle};
        final float[] uvScroll = new float[]{uvCut[0] / this._sizeStyle, uvCut[1] / this._sizeStyle};
        GL11.glColor4f(this.color.x, color.y, this.color.z, this.color.w);

        this._sprites[_S_BL].bindTexture();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(0.0f, this._sizeStyle);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(this._sizeStyle, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(this._sizeStyle, this._sizeStyle);
        GL11.glEnd();

        this._sprites[_S_B].bindTexture();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(this._sizeStyle, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(this._sizeStyle, this._sizeStyle);
        GL11.glTexCoord2f(uvScroll[0], 0.0f);
        GL11.glVertex2f(verticesValue[0], 0.0f);
        GL11.glTexCoord2f(uvScroll[0], 1.0f);
        GL11.glVertex2f(verticesValue[0], this._sizeStyle);
        GL11.glEnd();

        this._sprites[_S_BR].bindTexture();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(verticesValue[0], 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(verticesValue[0], this._sizeStyle);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(this.size.x, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(this.size.x, this._sizeStyle);
        GL11.glEnd();

        this._sprites[_S_L].bindTexture();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(0.0f, this._sizeStyle);
        GL11.glTexCoord2f(0.0f, uvScroll[1]);
        GL11.glVertex2f(0.0f, verticesValue[1]);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(this._sizeStyle, this._sizeStyle);
        GL11.glTexCoord2f(1.0f, uvScroll[1]);
        GL11.glVertex2f(this._sizeStyle, verticesValue[1]);
        GL11.glEnd();

        this._sprites[_S_R].bindTexture();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(verticesValue[0], this._sizeStyle);
        GL11.glTexCoord2f(0.0f, uvScroll[1]);
        GL11.glVertex2f(verticesValue[0], verticesValue[1]);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(this.size.x, this._sizeStyle);
        GL11.glTexCoord2f(1.0f, uvScroll[1]);
        GL11.glVertex2f(this.size.x, verticesValue[1]);
        GL11.glEnd();

        this._sprites[_S_TL].bindTexture();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(0.0f, verticesValue[1]);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(0.0f, this.size.y);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(this._sizeStyle, verticesValue[1]);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(this._sizeStyle, this.size.y);
        GL11.glEnd();

        this._sprites[_S_T].bindTexture();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(this._sizeStyle, verticesValue[1]);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(this._sizeStyle, this.size.y);
        GL11.glTexCoord2f(uvScroll[0], 0.0f);
        GL11.glVertex2f(verticesValue[0], verticesValue[1]);
        GL11.glTexCoord2f(uvScroll[0], 1.0f);
        GL11.glVertex2f(verticesValue[0], this.size.y);
        GL11.glEnd();

        this._sprites[_S_TR].bindTexture();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(verticesValue[0], verticesValue[1]);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(verticesValue[0], this.size.y);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(this.size.x, verticesValue[1]);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(this.size.x, this.size.y);
        GL11.glEnd();

        final float halfCutA = this._sizeStyle - this.expend;
        final float halfCutB = verticesValue[0] + this.expend;
        final float halfCutC = verticesValue[1] + this.expend;
        this._sprites[_S_CENTER].bindTexture();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(halfCutA, halfCutA);
        GL11.glTexCoord2f(0.0f, uvScroll[1]);
        GL11.glVertex2f(halfCutA, halfCutC);
        GL11.glTexCoord2f(uvScroll[0], 0.0f);
        GL11.glVertex2f(halfCutB, halfCutA);
        GL11.glTexCoord2f(uvScroll[0], uvScroll[1]);
        GL11.glVertex2f(halfCutB, halfCutC);
        GL11.glEnd();
    }

    public boolean isSizeValid() {
        int sizeTmp = this._sizeStyle * 2;
        return this.getWidth() >= sizeTmp && this.getHeight() >= sizeTmp;
    }

    public Vector2f getSize() {
        return this.size;
    }

    public void setSize(Vector2f size) {
        this.size.set(size);
    }

    public void setSize(float width, float height) {
        this.size.set(width, height);
    }

    public float getWidth() {
        return this.size.x;
    }

    public void setWidth(float width) {
        this.size.x = width;
    }

    public float getHeight() {
        return this.size.y;
    }

    public void setHeight(float height) {
        this.size.y = height;
    }

    public Vector4f getColor() {
        return this.color;
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
    }

    public void setColor(Vector3f color) {
        this.setColor(color.x, color.y, color.z);
    }

    public void setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
    }

    public void setColor(Vector4f color) {
        this.setColor(color.x, color.y, color.z, color.w);
    }

    public float getAlpha() {
        return this.color.w;
    }

    public void setAlpha(float alpha) {
        this.color.w = alpha;
    }

    public float getCenterExpend() {
        return this.expend;
    }

    public void setCenterExpend(float expend) {
        this.expend = expend;
    }

    public boolean isValid() {
        return this.isValid;
    }

    /**
     * @return the value width of bottom-left texture.
     */
    public int getTileSize() {
        return this._sizeStyle;
    }
}
