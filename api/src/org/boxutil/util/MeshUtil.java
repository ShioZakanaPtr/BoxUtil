package org.boxutil.util;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.backends.array.BUtil_Stack2i;
import de.unkrig.commons.nullanalysis.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL43;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.*;

public final class MeshUtil {
    /**
     * Return tangent and bi-tangent, array size is 2.
     */
    public static Vector3f[] tangentMaker(@NotNull Vector3f v1, @NotNull Vector3f v2, @NotNull Vector3f v3, @NotNull Vector2f u1, @NotNull Vector2f u2, @NotNull Vector2f u3) {
        Vector3f edge1 = Vector3f.sub(v2, v1, new Vector3f());
        Vector3f edge2 = Vector3f.sub(v3, v1, new Vector3f());
        Vector2f deltaUV1 = Vector2f.sub(u2, u1, new Vector2f());
        Vector2f deltaUV2 = Vector2f.sub(u3, u1, new Vector2f());
        float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

        deltaUV2.scale(f);
        deltaUV1.scale(f);
        Vector3f tangent = new Vector3f(
                deltaUV2.y * edge1.x - deltaUV1.y * edge2.x,
                deltaUV2.y * edge1.y - deltaUV1.y * edge2.y,
                deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);

        Vector3f bitangent = new Vector3f(
                -deltaUV2.x * edge1.x + deltaUV1.x * edge2.x,
                -deltaUV2.x * edge1.y + deltaUV1.x * edge2.y,
                -deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);

        return new Vector3f[]{tangent.normalise(tangent), bitangent.normalise(bitangent)};
    }

    public static Vector3f tangentOnlyMaker(@NotNull Vector3f v1, @NotNull Vector3f v2, @NotNull Vector3f v3, @NotNull Vector2f u1, @NotNull Vector2f u2, @NotNull Vector2f u3) {
        Vector3f edge1 = Vector3f.sub(v2, v1, new Vector3f());
        Vector3f edge2 = Vector3f.sub(v3, v1, new Vector3f());
        Vector2f deltaUV1 = Vector2f.sub(u2, u1, new Vector2f());
        Vector2f deltaUV2 = Vector2f.sub(u3, u1, new Vector2f());
        float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

        deltaUV2.y *= f;
        deltaUV1.y *= f;
        Vector3f tangent = new Vector3f(
                deltaUV2.y * edge1.x - deltaUV1.y * edge2.x,
                deltaUV2.y * edge1.y - deltaUV1.y * edge2.y,
                deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
        return tangent.normalise(tangent);
    }

    public static Vector3f surfaceNormal(@NotNull Vector3f v1, @NotNull Vector3f v2, @NotNull Vector3f v3) {
        Vector3f normal = Vector3f.cross(new Vector3f(v1.x - v3.x, v1.y - v3.y, v1.z - v3.z), new Vector3f(v2.x - v3.x, v2.y - v3.y, v2.z - v3.z), new Vector3f());
        return normal.normalise(normal);
    }

    private final static class LinkedPoint {
        private final Vector2f loc = new Vector2f();
        private LinkedPoint pre = null;
        private LinkedPoint nex = null;
        private boolean isConcave = false;

        public LinkedPoint(final Vector2f loc) {
            this.loc.set(loc);
        }

        public void compute() {
            this.isConcave = CalculateUtil.cross(this.loc, this.nex.loc, this.pre.loc) <= 0.0f;
        }

        public void reverse() {
            final LinkedPoint tmp = this.pre;
            this.pre = this.nex;
            this.nex = tmp;
            this.compute();
        }
    }

    private static float _pointDistSq(Vector2f a, Vector2f b) {
        final float x = b.x - a.x, y = b.y - a.y;
        return x * x + y * y;
    }

    private static boolean _PointInsideClose(Vector2f a, Vector2f b, Vector2f c, Vector2f point) {
        return !(_pointDistSq(a, point) <= 0.01f ||
                _pointDistSq(b, point) <= 0.01f ||
                _pointDistSq(c, point) <= 0.01f ||
                !CalculateUtil.isPointInside(a, b, c, point));
    }

    private static void _putBuffer(Buffer buf, float v, boolean f32) {
        if (f32) ((FloatBuffer) buf).put(v); else ((ShortBuffer) buf).put(CommonUtil.float16ToShort(v));
    }

    private static Buffer _convertBoundsTriangleFanCore(BoundsAPI bounds, boolean f32, List<BUtil_Stack2i> __glDrawParam) {
        final List<BoundsAPI.SegmentAPI> segments = new ArrayList<>(bounds.getOrigSegments());
        if (segments.size() < 3) return null;

        final List<LinkedPoint> __rawBounds = new ArrayList<>();
        LinkedPoint foundOneConcave = null;
        float areaDouble = 0.0f;

        {
            LinkedPoint _curr, _pre;
            final int rawBoundsSize, rawBoundsSizeLimit;
            byte concaveCount = 0;

            int i_pre = -2;
            for (BoundsAPI.SegmentAPI segment : segments) {
                if (segment == null || segment.getP1() == null) continue;

                _curr = new LinkedPoint(segment.getP1());
                __rawBounds.add(_curr);
                ++i_pre;
                if (i_pre < 0) continue;

                _pre = __rawBounds.get(i_pre);
                _curr.pre = _pre;
                _pre.nex = _curr;

                if (i_pre > 0) _pre.compute();
                if (_pre.isConcave && concaveCount < 2) {
                    if (foundOneConcave == null) foundOneConcave = _pre;
                    ++concaveCount;
                }

                areaDouble += _pre.loc.x * _curr.loc.y - _pre.loc.y * _curr.loc.x;
            }
            rawBoundsSize = __rawBounds.size();
            if (rawBoundsSize < 3) return null;

            rawBoundsSizeLimit = rawBoundsSize - 1;
            _pre = __rawBounds.get(rawBoundsSizeLimit);
            _curr = __rawBounds.get(0);

            _curr.pre = _pre;
            _pre.nex = _curr;

            _curr.compute();
            _pre.compute();
            areaDouble += _pre.loc.x * _curr.loc.y - _pre.loc.y * _curr.loc.x;

            final boolean isCCW = areaDouble > 0.0f;
            if (isCCW) {
                if (_curr.isConcave && concaveCount < 2) {
                    if (foundOneConcave == null) foundOneConcave = _curr;
                    ++concaveCount;
                }
                if (_pre.isConcave && concaveCount < 2) {
                    if (foundOneConcave == null) foundOneConcave = _pre;
                    ++concaveCount;
                }
            } else { // convert to CCW
                foundOneConcave = null;
                concaveCount = 0;

                ListIterator<LinkedPoint> fwd = __rawBounds.listIterator();
                ListIterator<LinkedPoint> rev = __rawBounds.listIterator(rawBoundsSize);
                final int mid = rawBoundsSize >>> 1;
                for (int i = 0; i < mid; ++i) {
                    _curr = fwd.next();
                    _pre = rev.previous();

                    _curr.reverse();
                    _pre.reverse();

                    fwd.set(_pre);
                    rev.set(_curr);

                    if (_curr.isConcave && concaveCount < 2) {
                        if (foundOneConcave == null) foundOneConcave = _curr;
                        ++concaveCount;
                    }
                    if (_pre.isConcave && concaveCount < 2) {
                        if (foundOneConcave == null) foundOneConcave = _pre;
                        ++concaveCount;
                    }
                }
                if ((rawBoundsSize & 0b1) > 0) {
                    _curr = __rawBounds.get(mid);
                    _curr.reverse();
                    if (_curr.isConcave && concaveCount < 2) {
                        if (foundOneConcave == null) foundOneConcave = _curr;
                        ++concaveCount;
                    }
                }
            }

            if (concaveCount < 2) { // unique concave or all convex
                final int _bufferLength = rawBoundsSize << 1;
                final Buffer result = f32 ? BufferUtils.createFloatBuffer(_bufferLength) : BufferUtils.createShortBuffer(_bufferLength);
                if (foundOneConcave == null) foundOneConcave = __rawBounds.get(0);
                _putBuffer(result, foundOneConcave.loc.x, f32);
                _putBuffer(result, foundOneConcave.loc.y, f32);

                LinkedPoint itPoint = foundOneConcave.nex;
                while (itPoint != foundOneConcave) {
                    _putBuffer(result, itPoint.loc.x, f32);
                    _putBuffer(result, itPoint.loc.y, f32);
                    itPoint = itPoint.nex;
                }
                __glDrawParam.add(new BUtil_Stack2i(0, rawBoundsSize));
                result.position(0);
                result.limit(result.capacity());
                return result;
            }
        }

        final List<Vector2f> resultPoints = new ArrayList<>();
        final Deque<LinkedPoint> tmpPrePoints = new ArrayDeque<>(), tmpNexPoints = new ArrayDeque<>();
        LinkedPoint lastAnchorPoint = foundOneConcave, checkA, checkB, checkC;
        int currVertexNum, currPreVertexNum, totalVertexNum = 0;
        short loopBreak = 0;

        while (__rawBounds.size() > 3) {
            if (loopBreak > 1023) break;
            ++loopBreak;
            checkA = lastAnchorPoint;

            currVertexNum = currPreVertexNum = 0;
            boolean pullRemaining = true;
            do {
                if (checkA.isConcave) {
                    lastAnchorPoint = checkA;
                    pullRemaining = false;
                    break;
                }
                checkA = checkA.pre;
            } while (checkA != lastAnchorPoint);

            if (pullRemaining) {
                do {
                    ++currVertexNum;
                    resultPoints.add(checkA.loc);
                    checkA = checkA.nex;
                } while (checkA != lastAnchorPoint);
                __glDrawParam.add(new BUtil_Stack2i(totalVertexNum, currVertexNum));
                totalVertexNum += currVertexNum;
                __rawBounds.clear();
                break;
            }

            tmpPrePoints.clear();
            tmpNexPoints.clear();
            LinkedPoint forEachInside;
            boolean notPush;

            checkC = checkA.nex;
            if (checkC == checkA) break;
            if (checkC.isConcave) {
                checkC = checkA;
            } else {
                tmpNexPoints.add(checkC);
                currVertexNum = 1;
            }
            while (checkC != checkA) {
                checkB = checkC;
                checkC = checkC.nex;

                forEachInside = checkC.nex;

                areaDouble = checkA.loc.x * checkB.loc.y - checkA.loc.y * checkB.loc.x
                        + checkB.loc.x * checkC.loc.y - checkB.loc.y * checkC.loc.x
                        + checkC.loc.x * checkA.loc.y - checkC.loc.y * checkA.loc.x;
                notPush = areaDouble < 0.0f;

                if (forEachInside == checkA) {
                    if (!notPush) {
                        tmpNexPoints.add(checkC);
                        ++currVertexNum;
                    }
                    break;
                }

                if (!notPush) {
                    while (forEachInside != checkC) {
                        if (forEachInside != checkA && forEachInside != checkB) notPush = _PointInsideClose(checkA.loc, checkB.loc, checkC.loc, forEachInside.loc);
                        if (notPush) break;
                        forEachInside = forEachInside.nex;
                    }
                }

                if (notPush) {
                    if (currVertexNum < 2) {
                        tmpNexPoints.clear();
                        currVertexNum = 0;
                    }
                    break;
                } else {
                    tmpNexPoints.add(checkC);
                    ++currVertexNum;
                }
            }

            final LinkedPoint tmpCheckB = tmpNexPoints.peekLast();
            final boolean haveNexPoints = tmpCheckB != null;
            final float fixedAreaPart = haveNexPoints ? (checkA.loc.x * tmpCheckB.loc.y - checkA.loc.y * tmpCheckB.loc.x) : 0.0f;
            checkC = checkA.pre;
            if (checkC == checkA) break;
            if (!haveNexPoints && checkC.isConcave) {
                lastAnchorPoint = lastAnchorPoint.pre;
                continue;
            } else {
                if (checkC.isConcave || checkC == tmpCheckB) {
                    checkC = checkA;
                } else {
                    tmpPrePoints.push(checkC);
                    currPreVertexNum = 1;
                }
            }
            while (checkC != checkA && checkC != tmpCheckB) {
                checkB = checkC;
                checkC = checkC.pre;

                forEachInside = checkC.pre;

                areaDouble = checkA.loc.x * checkC.loc.y - checkA.loc.y * checkC.loc.x
                        + checkC.loc.x * checkB.loc.y - checkC.loc.y * checkB.loc.x
                        + checkB.loc.x * checkA.loc.y - checkB.loc.y * checkA.loc.x;
                notPush = areaDouble < 0.0f;
                if (!notPush && haveNexPoints) {
                    areaDouble = fixedAreaPart
                            + tmpCheckB.loc.x * checkC.loc.y - tmpCheckB.loc.y * checkC.loc.x
                            + checkC.loc.x * checkA.loc.y - checkC.loc.y * checkA.loc.x;
                    notPush = areaDouble < 0.0f;
                }

                if (forEachInside == checkA || forEachInside == tmpCheckB) {
                    if (!notPush) {
                        tmpPrePoints.push(checkC);
                        ++currPreVertexNum;
                    }
                    if (currPreVertexNum < 2) {
                        tmpPrePoints.clear();
                        currPreVertexNum = 0;
                    }
                    break;
                }

                if (!notPush) {
                    while (forEachInside != checkC) {
                        if (forEachInside != checkA && forEachInside != checkB) {
                            notPush = _PointInsideClose(checkA.loc, checkC.loc, checkB.loc, forEachInside.loc);
                            if (haveNexPoints) notPush |= _PointInsideClose(checkA.loc, tmpCheckB.loc, checkC.loc, forEachInside.loc);
                        }
                        if (notPush) break;
                        forEachInside = forEachInside.pre;
                    }
                }

                if (notPush) {
                    if (currPreVertexNum < 2) {
                        tmpPrePoints.clear();
                        currPreVertexNum = 0;
                    }
                    break;
                } else {
                    tmpPrePoints.push(checkC);
                    ++currPreVertexNum;
                }
            }
            currVertexNum += currPreVertexNum;

            if (currVertexNum > 1) {
                checkB = tmpNexPoints.pollLast();
                checkC = tmpPrePoints.pollFirst();

                if (checkB == null && checkC == null) break;
                if (!tmpNexPoints.isEmpty()) __rawBounds.removeAll(tmpNexPoints);
                if (!tmpPrePoints.isEmpty()) __rawBounds.removeAll(tmpPrePoints);
                if (checkB != null) tmpNexPoints.add(checkB);
                if (checkC != null) tmpPrePoints.push(checkC);
                if (checkB != null && checkC != null) __rawBounds.remove(checkA);

                if (checkB == null) checkB = checkA;
                if (checkC == null) checkC = checkA;
                lastAnchorPoint = checkC;
                checkC.nex = checkB;
                checkB.pre = checkC;
                checkB.compute();
                checkC.compute();

                resultPoints.add(checkA.loc);
                ++currVertexNum;
                for (LinkedPoint point : tmpNexPoints) resultPoints.add(point.loc);
                for (LinkedPoint point : tmpPrePoints) resultPoints.add(point.loc);
                __glDrawParam.add(new BUtil_Stack2i(totalVertexNum, currVertexNum));
                totalVertexNum += currVertexNum;

                if (checkC.nex == checkB.pre) break;
            } else lastAnchorPoint = lastAnchorPoint.pre;
        }
        if (__rawBounds.size() == 3) {
            checkA = __rawBounds.get(0);
            checkB = __rawBounds.get(1);
            checkC = __rawBounds.get(2);
            if (checkA.nex == checkB && !checkA.isConcave && checkB.nex == checkC && !checkB.isConcave && checkC.nex == checkA && !checkC.isConcave) {
                resultPoints.add(checkA.loc);
                resultPoints.add(checkB.loc);
                resultPoints.add(checkC.loc);
                __glDrawParam.add(new BUtil_Stack2i(totalVertexNum, 3));
                totalVertexNum += 3;
            }
        }

        totalVertexNum <<= 1;
        final Buffer result = f32 ? BufferUtils.createFloatBuffer(totalVertexNum) : BufferUtils.createShortBuffer(totalVertexNum);
        for (Vector2f point : resultPoints) {
            _putBuffer(result, point.x, f32);
            _putBuffer(result, point.y, f32);
        }
        result.position(0);
        result.limit(result.capacity());
        return result;
    }

    private static IntBuffer[] _putMultiDrawParam(List<BUtil_Stack2i> __glDrawParam) {
        final int drawCount = __glDrawParam.size();
        final IntBuffer[] result = new IntBuffer[]{BufferUtils.createIntBuffer(drawCount), BufferUtils.createIntBuffer(drawCount)};
        for (BUtil_Stack2i stack : __glDrawParam) {
            result[0].put(stack.x());
            result[1].put(stack.y());
        }
        result[0].position(0);
        result[0].limit(result[0].capacity());
        result[1].position(0);
        result[1].limit(result[1].capacity());
        return result;
    }

    /**
     * Recommend to use {@link GL14#glMultiDrawArrays(int, IntBuffer, IntBuffer)} or {@link GL43#glMultiDrawArraysIndirect(int, long, int, int)} with {@link GL11#GL_TRIANGLE_FAN} to draw it.<p>
     * Vertex layout: <code>vec2(location.xy)</code><p>
     * <strong>NOTE: cannot process the self intersection bounds like <em><u>wasp</u></em>!</strong>
     *
     * @return <code>pair.two = {piFirst, piCount};</code>
     */
    public static Pair<FloatBuffer, IntBuffer[]> convertBoundsTriangleFanFP32(BoundsAPI bounds) {
        List<BUtil_Stack2i> __glDrawParam = new ArrayList<>();
        FloatBuffer buffer = (FloatBuffer) _convertBoundsTriangleFanCore(bounds, true, __glDrawParam);
        return new Pair<>(buffer, _putMultiDrawParam(__glDrawParam));
    }

    /**
     * Recommend to use {@link GL14#glMultiDrawArrays(int, IntBuffer, IntBuffer)} or {@link GL43#glMultiDrawArraysIndirect(int, long, int, int)} with {@link GL11#GL_TRIANGLE_FAN} to draw it.<p>
     * Vertex layout: <code>vec2(location.xy)</code><p>
     * <strong>NOTE: cannot process the self intersection bounds like <em><u>wasp</u></em>!</strong>
     *
     * @return <code>pair.two = {piFirst, piCount};</code>
     */
    public static Pair<ShortBuffer, IntBuffer[]> convertBoundsTriangleFanFP16(BoundsAPI bounds) {
        List<BUtil_Stack2i> __glDrawParam = new ArrayList<>();
        ShortBuffer buffer = (ShortBuffer) _convertBoundsTriangleFanCore(bounds, false, __glDrawParam);
        return new Pair<>(buffer, _putMultiDrawParam(__glDrawParam));
    }

    private MeshUtil() {}
}
