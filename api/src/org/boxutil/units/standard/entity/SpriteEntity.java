package org.boxutil.units.standard.entity;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.boxutil.base.BaseMIRenderData;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.LayeredEntityType;
import org.lwjgl.util.vector.Vector2f;

import java.nio.FloatBuffer;

/**
 * Advanced sprite rendering.<p>
 * Sprite entity will not apply AA if depth based AA is enabled.
 */
public class SpriteEntity extends BaseMIRenderData {
    // vec2(tile), startIndex, randomIndexEachInstance, vec2(start), vec2(end), randomSeed
    protected final float[] spriteState = new float[]{1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, this.hashCode() * 0.00066667f};
    protected final float[] baseSize = new float[]{1.0f, 1.0f};
    protected int currentTileCount = 1;
    protected boolean isRandomTile = false;

    protected int _StatePackageStack() {
        return 6;
    }

    public SpriteEntity() {
        this.getMaterialData().setDisableCullFace();
        this.getMaterialData().setIgnoreIllumination(true);
    }

    /**
     * @param sprite will apply to diffuse layer.
     * @param syncSpriteUVMapping for the sprite that it local-size is not POT size.
     */
    public SpriteEntity(SpriteAPI sprite, boolean syncSpriteUVMapping) {
        this();
        this.material.setDiffuse(sprite);
        if (syncSpriteUVMapping) {
            this.setUVStart(sprite.getTexX(), sprite.getTexY());
            this.setUVEnd(sprite.getTexX() + sprite.getTexWidth(), sprite.getTexY() + sprite.getTexHeight());
        }
    }

    public SpriteEntity(SpriteAPI sprite) {
        this(sprite, true);
    }

    public SpriteEntity(String category, String key, boolean syncSpriteUVMapping) {
        this(Global.getSettings().getSprite(category, key), syncSpriteUVMapping);
    }

    public SpriteEntity(String category, String key) {
        this(category, key, true);
    }

    public SpriteEntity(String filename, boolean syncSpriteUVMapping) {
        this(Global.getSettings().getSprite(filename), syncSpriteUVMapping);
    }

    public SpriteEntity(String filename) {
        this(filename, true);
    }

    protected void _resetExc() {
        super._resetExc();
        this.spriteState[0] = 1.0f;
        this.spriteState[1] = 1.0f;
        this.spriteState[2] = 0.0f;
        this.spriteState[3] = 0.0f;
        this.spriteState[8] = this.hashCode() * 0.00066667f;
        this.isRandomTile = true;
        this.resetUV();
    }

    public void resetUV() {
        this.spriteState[4] = 0.0f;
        this.spriteState[5] = 0.0f;
        this.spriteState[6] = 1.0f;
        this.spriteState[7] = 1.0f;
    }

    public float[] getBaseSizePerTiles() {
        return this.baseSize;
    }

    public float getBaseWidthPerTiles() {
        return this.baseSize[0];
    }

    public float getBaseHeightPerTiles() {
        return this.baseSize[1];
    }

    public void setBaseWidthPerTiles(float width) {
        this.baseSize[0] = width;
    }

    public void setBaseHeightPerTiles(float height) {
        this.baseSize[1] = height;
    }

    public void setBaseSizePerTiles(float width, float height) {
        this.setBaseWidthPerTiles(width);
        this.setBaseHeightPerTiles(height);
    }

    public int getMaxTileCount() {
        return (int) (this.spriteState[0] * this.spriteState[1]);
    }

    public boolean isTilesRendering() {
        return this.getMaxTileCount() > 1;
    }

    public int getCurrentTileCount() {
        return this.currentTileCount;
    }

    public void setCurrentTileCount(int count) {
        this.currentTileCount = Math.min(count, this.getMaxTileCount());
    }

    public int getCountPerRow() {
        return (int) this.spriteState[0];
    }

    public int getCountPerColumn() {
        return (int) this.spriteState[1];
    }

    public int getStartingIndex() {
        return (int) this.spriteState[2];
    }

    public boolean isRandomTile() {
        return this.isRandomTile;
    }

    public boolean isRandomTileEachInstance() {
        return this.spriteState[3] == 1.0f;
    }

    /**
     * @param countPerRow value greater than or equal 1.
     * @param countPerColumn value greater than or equal 1.
     */
    public void setTileSize(int countPerRow, int countPerColumn) {
        this.spriteState[0] = Math.max(countPerRow, 1);
        this.spriteState[1] = Math.max(countPerColumn, 1);
        this.currentTileCount = this.getMaxTileCount();
    }

    /**
     * Texture parameter should be {@link org.lwjgl.opengl.GL11#GL_REPEAT}.
     */
    public void setRandomTile(boolean isRandom) {
        this.isRandomTile = isRandom;
    }

    /**
     * Texture parameter should be {@link org.lwjgl.opengl.GL11#GL_REPEAT}.
     */
    public void setRandomTileEachInstance(boolean random) {
        this.spriteState[3] = random ? 1.0f : 0.0f;
    }

    /**
     * From bottom-left, line by line.
     */
    public void setStartingFormIndex(int index) {
        this.spriteState[2] = Math.min(Math.max(index, 0), this.currentTileCount - 1);
    }

    public void nextTileIndex() {
        if (this.currentTileCount == 1) return;
        this.spriteState[2]++;
        if (this.spriteState[2] >= this.currentTileCount) this.spriteState[2] = 0.0f;
    }

    public void setDiffuseSprite(SpriteAPI sprite) {
        this.getMaterialData().setDiffuse(sprite);
    }

    public void setEmissiveSprite(SpriteAPI sprite) {
        this.getMaterialData().setEmissive(sprite);
    }

    public void setDiffuseSprite(String category, String key) {
        this.getMaterialData().setDiffuse(Global.getSettings().getSprite(category, key));
    }

    public void setEmissiveSprite(String category, String key) {
        this.getMaterialData().setEmissive(Global.getSettings().getSprite(category, key));
    }

    public void setDiffuseSprite(String filename) {
        this.getMaterialData().setDiffuse(Global.getSettings().getSprite(filename));
    }

    public void setEmissiveSprite(String filename) {
        this.getMaterialData().setEmissive(Global.getSettings().getSprite(filename));
    }

    public void resetDiffuseSprite() {
        this.getMaterialData().setDiffuse(BoxDatabase.BUtil_ONE);
    }

    public void resetEmissiveSprite() {
        this.getMaterialData().setEmissive(BoxDatabase.BUtil_NONE);
    }

    public float[] getUVStartArray() {
        return new float[]{this.spriteState[4], spriteState[5]};
    }

    public Vector2f getUVStart() {
        return new Vector2f(this.spriteState[4], spriteState[5]);
    }

    public void setUVStart(float x, float y) {
        this.spriteState[4] = x;
        this.spriteState[5] = y;
    }

    public void setUVStart(Vector2f location) {
        this.spriteState[4] = location.x;
        this.spriteState[5] = location.y;
    }

    public float[] getUVEndArray() {
        return new float[]{this.spriteState[6], spriteState[7]};
    }

    public Vector2f getUVEnd() {
        return new Vector2f(this.spriteState[7], spriteState[6]);
    }

    public void setUVEnd(float x, float y) {
        this.spriteState[6] = x;
        this.spriteState[7] = y;
    }

    public void setUVEnd(Vector2f location) {
        this.spriteState[4] = location.x;
        this.spriteState[7] = location.y;
    }

    public float getCurrentRandomSeedValue() {
        return this.spriteState[8];
    }

    /**
     * @param code java instance {@link #hashCode()} * 0.00066667f for default.
     */
    public void setRandomSeedCode(int code) {
        this.spriteState[8] = code;
    }

    public FloatBuffer pickDataPackage_vec4() {
        this._statePackageBuffer.put(0, this.material.getState(), 0, 12);
        this._statePackageBuffer.put(12, this.spriteState, 0, 9);
        this._statePackageBuffer.put(21, this.getCurrentTileCount() - 1);
        this._statePackageBuffer.put(22, this.baseSize[0]);
        this._statePackageBuffer.put(23, this.baseSize[1]);
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
        return this._statePackageBuffer;
    }

    public Object entityType() {
        return LayeredEntityType.SPRITE;
    }
}
