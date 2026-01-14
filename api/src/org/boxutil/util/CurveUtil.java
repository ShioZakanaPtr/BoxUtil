package org.boxutil.util;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.CombatRenderingManager;
import org.boxutil.units.standard.attribute.NodeData;
import org.boxutil.units.standard.entity.SegmentEntity;
import org.boxutil.units.standard.entity.TrailEntity;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.util.vector.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class CurveUtil {
    private final static V3Sort _V3_COMPARATOR = new V3Sort();
    private final static V4Sort _V4_COMPARATOR = new V4Sort();
    private final static DealtDataSort _DD_COMPARATOR = new DealtDataSort();
    private final static CurvePackageSort _CP_COMPARATOR = new CurvePackageSort();

    private final static class ConvexHullSort implements Comparator<Vector2f> {
        public int compare(Vector2f a, Vector2f b) {
            return Float.compare(a.x, b.x) != 0 ? Float.compare(a.x, b.x) : Float.compare(a.y, b.y);
        }
    }

    private final static class V4Sort implements Comparator<Vector4f> {
        public int compare(Vector4f a, Vector4f b) {
            return Float.compare(a.w, b.w);
        }
    }

    private final static class V3Sort implements Comparator<Vector3f> {
        public int compare(Vector3f a, Vector3f b) {
            return Float.compare(a.z, b.z);
        }
    }

    private final static class DealtDataSort implements Comparator<DealtData> {
        public int compare(DealtData o1, DealtData o2) {
            return Float.compare(o1.curveT, o2.curveT);
        }
    }

    private final static class CurvePackageSort implements Comparator<CurvePackage> {
        public int compare(CurvePackage o1, CurvePackage o2) {
            return Float.compare(o1.tStart, o2.tStart);
        }
    }

    public static List<Vector2f> getPointsConvexHull(Vector2f... points) {
        if (points.length < 3) return Arrays.asList(points);
        List<Vector2f> hull = new ArrayList<>();
        Arrays.sort(points, new ConvexHullSort());

        for (Vector2f point : points) {
            while (hull.size() >= 2 && CalculateUtil.cross(hull.get(hull.size() - 2), hull.get(hull.size() - 1), point) <= 0) hull.remove(hull.size() - 1);
            hull.add(point);
        }

        final int upper = hull.size() + 1;
        for (int i = points.length - 2; i >= 0; --i) {
            Vector2f point = points[i];
            while (hull.size() >= upper && CalculateUtil.cross(hull.get(hull.size() - 2), hull.get(hull.size() - 1), point) <= 0) hull.remove(hull.size() - 1);
            hull.add(point);
        }
        if (hull.size() > 1) hull.remove(hull.size() - 1);

        return hull;
    }

    public static List<Vector2f> getCurveConvexHull(NodeData curveStart, NodeData curveEnd) {
        Vector2f[] points = new Vector2f[4];
        points[0] = curveStart.getLocation();
        points[1] = curveStart.getTangentRight();
        points[2] = curveEnd.getTangentLeft();
        points[3] = curveEnd.getLocation();
        points[1].x += points[0].x;
        points[1].y += points[0].y;
        points[2].x += points[3].x;
        points[2].y += points[3].y;
        return getPointsConvexHull(points);
    }

    public static boolean isPointWithinCurveConvexHull(Vector2f point, List<Vector2f> hull) {
        int nextIndex, size = hull.size();
        if (size < 3) return false;
        for (int i = 0; i < size; i++) {
            nextIndex = i + 1;
            if (nextIndex >= size) nextIndex = 0;
            if (CalculateUtil.cross(hull.get(i), hull.get(nextIndex), point) < 0.0f) {
                return false;
            }
        }
        return true;
    }

    private static float getAxisExtreme(float p0, float p1, float p2, float p3, float factor) {
        float factor2 = factor * factor;
        float factor3 = factor * factor2;
        float oneMinusF1 = 1.0f - factor;
        return (p0 * oneMinusF1 * oneMinusF1 * oneMinusF1) + (p1 * (factor3 - factor2 - factor2 + factor) * 3.0f) + (p2 * (factor2 - factor3) * 3.0f) + (p3 * factor3);
    }

    private static float[] getABC(float p0, float p1, float p2, float p3) {
        float[] result = new float[3];
        result[0] = 3.0f * (p3 - 3.0f * (p2 - p1) - p0); // a
        result[1] = 6.0f * (p0 - 2.0f * p1 + p2);        // b
        result[2] = 3.0f * (p1 - p0);                    // c
        return result;
    }

    /**
     * @return Vector2f[] = {BL, TR}
     */
    public static Vector2f[] getCurveAABB(NodeData curveStart, NodeData curveEnd) {
        float[][] curvePointArray = new float[][]{
                curveStart.getLocationArray(),
                curveStart.getTangentRightArray(),
                curveEnd.getTangentLeftArray(),
                curveEnd.getLocationArray()
        };
        curvePointArray[1][0] += curvePointArray[0][0];
        curvePointArray[1][1] += curvePointArray[0][1];
        curvePointArray[2][0] += curvePointArray[3][0];
        curvePointArray[2][1] += curvePointArray[3][1];

        float[][] ABC = new float[][]{
                getABC(curvePointArray[0][0], curvePointArray[1][0], curvePointArray[2][0], curvePointArray[3][0]),
                getABC(curvePointArray[0][1], curvePointArray[1][1], curvePointArray[2][1], curvePointArray[3][1])
        };
        float[][] hullArray = new float[2][2];
        hullArray[0][0] = Math.min(curvePointArray[0][0], curvePointArray[3][0]);
        hullArray[0][1] = Math.min(curvePointArray[0][1], curvePointArray[3][1]);
        hullArray[1][0] = Math.max(curvePointArray[0][0], curvePointArray[3][0]);
        hullArray[1][1] = Math.max(curvePointArray[0][1], curvePointArray[3][1]);

        List<Float> roots = new ArrayList<>(4);
        float delta, t, a2div, dSQ, currAxisPoint;

        for (byte i = 0; i < 2; ++i) {
            if (ABC[i][0] == 0.0f) {
                if (ABC[i][1] != 0.0f) {
                    t = -ABC[i][2] / ABC[i][1];
                    if (t > 0.0f && t < 1.0f) roots.add(t);
                }
            } else {
                 delta = ABC[i][1] * ABC[i][1] - 4.0f * ABC[i][0] * ABC[i][2];
                 if (delta >= 0.0f) {
                     dSQ = (float) Math.sqrt(delta);
                     a2div = 1.0f / (2.0f * ABC[i][0]);
                     t = (dSQ - ABC[i][1]) * a2div;
                     if (t > 0.0f && t < 1.0f) roots.add(t);
                     t = -(ABC[i][1] + dSQ) * a2div;
                     if (t > 0.0f && t < 1.0f) roots.add(t);
                 }
            }

            for (float root : roots) {
                currAxisPoint = getAxisExtreme(curvePointArray[0][i], curvePointArray[1][i], curvePointArray[2][i], curvePointArray[3][i], root);
                if (currAxisPoint < hullArray[0][i]) hullArray[0][i] = currAxisPoint;
                if (currAxisPoint > hullArray[1][i]) hullArray[1][i] = currAxisPoint;
            }
            roots.clear();
        }
        return new Vector2f[]{new Vector2f(hullArray[0][0], hullArray[0][1]), new Vector2f(hullArray[1][0], hullArray[1][1])};
    }

    @Deprecated
    public static boolean isPointWithinAABB(Vector2f point, Vector2f bl, Vector2f tr) {
        return point.x >= bl.x && point.x <= tr.x && point.y >= bl.y && point.y <= tr.y;
    }

    /**
     * @param aabb Vector2f[] = {bottomLeft, topRight}
     */
    @Deprecated
    public static boolean isPointWithinAABB(Vector2f point, Vector2f[] aabb) {
        return isPointWithinAABB(point, aabb[0], aabb[1]);
    }

    @Deprecated
    public static boolean isPointWithinAABB(Vector2f point, float range, Vector2f bl, Vector2f tr) {
        return point.x + range >= bl.x && point.x - range <= tr.x && point.y + range >= bl.y && point.y - range <= tr.y;
    }

    /**
     * @param aabb Vector2f[] = {bottomLeft, topRight}
     */
    @Deprecated
    public static boolean isPointWithinAABB(Vector2f point, float range, Vector2f[] aabb) {
        return isPointWithinAABB(point, range, aabb[0], aabb[1]);
    }

    // From java.awt.geom.Line2D.relativeCCW()
    @Deprecated
    private static byte relativeCCW_F(float x1, float y1, float x2, float y2, float px, float py) {
        x2 -= x1;
        y2 -= y1;
        px -= x1;
        py -= y1;
        float ccw = px * y2 - py * x2;
        if (ccw == 0.0f) {
            ccw = px * x2 + py * y2;
            if (ccw > 0.0f) {
                ccw = (px - x2) * x2 + (py - y2) * y2;
                if (ccw < 0.0f) ccw = 0.0f;
            }
        }
        return ccw < 0.0f ? BoxEnum.NEG_ONE : (ccw > 0.0f ? BoxEnum.ONE : BoxEnum.ZERO);
    }

    @Deprecated
    private static boolean linesIntersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        return (relativeCCW_F(x1, y1, x2, y2, x3, y3) * relativeCCW_F(x1, y1, x2, y2, x4, y4) <= 0) &&
                (relativeCCW_F(x3, y3, x4, y4, x1, y1) * relativeCCW_F(x3, y3, x4, y4, x2, y2) <= 0);
    }

    @Deprecated
    public static boolean isSegmentWithinOrIntersectAABB(Vector2f start, Vector2f end, Vector2f bl, Vector2f tr) {
        boolean result = isPointWithinAABB(start, bl, tr) || isPointWithinAABB(end, bl, tr);
        result |= linesIntersect(start.x, start.y, end.x, end.y, bl.x, bl.y, tr.x, tr.y) || linesIntersect(start.x, start.y, end.x, end.y, bl.x, tr.y, tr.x, bl.y);
        return result;
    }

    @Deprecated
    public static boolean isSegmentWithinOrIntersectAABB(BoundsAPI.SegmentAPI segment, Vector2f bl, Vector2f tr) {
        return isSegmentWithinOrIntersectAABB(segment.getP1(), segment.getP2(), bl, tr);
    }

    /**
     * @param aabb Vector2f[] = {bottomLeft, topRight}
     */
    @Deprecated
    public static boolean isSegmentWithinOrIntersectAABB(Vector2f start, Vector2f end, Vector2f[] aabb) {
        return isSegmentWithinOrIntersectAABB(start, end, aabb[0], aabb[1]);
    }

    /**
     * @param aabb Vector2f[] = {bottomLeft, topRight}
     */
    @Deprecated
    public static boolean isSegmentWithinOrIntersectAABB(BoundsAPI.SegmentAPI segment, Vector2f[] aabb) {
        return isSegmentWithinOrIntersectAABB(segment.getP1(), segment.getP2(), aabb);
    }

    @Deprecated
    public static boolean isAABBOverlap(Vector2f blSRC, Vector2f trSRC, Vector2f blDST, Vector2f trDST) {
        return trSRC.x >= blDST.x && trSRC.y >= blDST.y && blSRC.x <= trDST.x && blSRC.y <= trDST.y;
    }

    /**
     * @param aabbSRC Vector2f[] = {bottomLeft, topRight}
     * @param aabbDST Vector2f[] = {bottomLeft, topRight}
     */
    @Deprecated
    public static boolean isAABBOverlap(Vector2f[] aabbSRC, Vector2f[] aabbDST) {
        return isAABBOverlap(aabbSRC[0], aabbSRC[1], aabbDST[0], aabbDST[1]);
    }

    public static float getCurveLength(NodeData curveStart, NodeData curveEnd, int step) {
        if (step <= 1) return Misc.getDistance(curveStart.getLocation(), curveEnd.getLocation());
        Vector2f last = getPointOnCurve(curveStart, curveEnd, 1.0f / step), curr, tmp = new Vector2f();
        float length = 0.0f;
        for (int i = 1; i <= step; ++i) {
            curr = getPointOnCurve(curveStart, curveEnd, (float) i / step);
            Vector2f.sub(curr, last, tmp);
            length += tmp.length();
            last.set(curr);
        }
        return length;
    }

    /**
     * @return float[] = {cos, sin, offsetX, offsetY, segmentLength}
     */
    public static Pair<NodeData[], float[]> aligningNodeSegmentBased(NodeData curveStart, NodeData curveEnd, Vector2f segmentStart, Vector2f segmentEnd, @Nullable Pair<NodeData[], float[]> result) {
        if (result == null || result.one.length < 2 || result.two.length < 5) result = new Pair<>(new NodeData[]{new NodeData(curveStart), new NodeData(curveEnd)}, new float[5]);
        if (result.one[0] == null) result.one[0] = new NodeData(curveStart);
        if (result.one[1] == null) result.one[1] = new NodeData(curveEnd);

        Matrix2f rotate = new Matrix2f();
        Vector2f cosSin = Vector2f.sub(segmentEnd, segmentStart, new Vector2f());
        result.two[4] = cosSin.length();
        if (result.two[4] > 0.0f) {
            cosSin.scale(1.0f / result.two[4]);
            rotate.m00 = cosSin.x;
            rotate.m10 = cosSin.y;
            rotate.m01 = -cosSin.y;
            rotate.m11 = cosSin.x;
        } else {
            result.one[0] = new NodeData(curveStart);
            result.one[1] = new NodeData(curveEnd);
            result.two[0] = 1.0f;
            result.two[1] = result.two[2] = result.two[3] = 0.0f;
            return result;
        }

        result.two[0] = cosSin.x;
        result.two[1] = cosSin.y;
        result.two[2] = segmentStart.x;
        result.two[3] = segmentStart.y;
        Vector2f tmp = curveStart.getLocation();
        tmp.x -= result.two[2];
        tmp.y -= result.two[3];
        result.one[0].setLocation(Matrix2f.transform(rotate, tmp, tmp));
        result.one[0].setTangentLeft(Matrix2f.transform(rotate, curveStart.getTangentLeft(), tmp));
        result.one[0].setTangentRight(Matrix2f.transform(rotate, curveStart.getTangentRight(), tmp));
        tmp = curveEnd.getLocation();
        tmp.x -= result.two[2];
        tmp.y -= result.two[3];
        result.one[1].setLocation(Matrix2f.transform(rotate, tmp, tmp));
        result.one[1].setTangentLeft(Matrix2f.transform(rotate, curveEnd.getTangentLeft(), tmp));
        result.one[1].setTangentRight(Matrix2f.transform(rotate, curveEnd.getTangentRight(), tmp));
        return result;
    }

    /**
     * @return float[] = {cos, sin, offsetX, offsetY, segmentLength}
     */
    public static Pair<NodeData[], float[]> aligningNode(NodeData curveStart, NodeData curveEnd, @Nullable Pair<NodeData[], float[]> result) {
        return aligningNodeSegmentBased(curveStart, curveEnd, curveStart.getLocation(), curveEnd.getLocation(), result);
    }

    /**
     * @return float[] = {cos, sin, offsetX, offsetY, segmentLength}
     */
    public static Pair<NodeData[], float[]> aligningNodeSegmentBased(NodeData curveStart, NodeData curveEnd, BoundsAPI.SegmentAPI segment, @Nullable Pair<NodeData[], float[]> result) {
        return aligningNodeSegmentBased(curveStart, curveEnd, segment.getP1(), segment.getP2(), result);
    }

    /**
     * @param angle angle value recommend to <code>[0, 180]</code> for rough, <code>[0, 90]</code> for more accurate fitting.
     * @return <code>NodeData</code> is the end point of curve, use left tangent; float[] = {cos, sin, tangentLength};<p>For the start point of curve: location = {radius, 0.0f}, tangentRight = {0.0f, tangentLength}.
     */
    public static Pair<NodeData, float[]> getCurveCircleFitting(float angle, float radius, @Nullable Pair<NodeData, float[]> result) {
        if (result == null || result.two.length < 3) result = new Pair<>(new NodeData(), new float[3]);
        if (result.one == null) result.one = new NodeData();
        Vector2f endPoint = new Vector2f();
        float halfAngle = angle * 0.5f;
        result.two[2] = (float) Math.cos(Math.toRadians(halfAngle));
        halfAngle = TrigUtil.sinFormCosF(result.two[2], halfAngle);
        result.two[2] = (4.0f * (1.0f - result.two[2])) / (3.0f * halfAngle) * radius;
        result.two[0] = endPoint.x = (float) Math.cos(Math.toRadians(angle));
        result.two[1] = endPoint.y = TrigUtil.sinFormCosF(endPoint.x, angle);
        endPoint.scale(radius);
        result.one.setLocation(endPoint);
        result.one.setTangentLeft(result.two[2] * result.two[1], -result.two[2] * result.two[0]);
        return result;
    }

    /**
     * @param stepFirstSearch minimum step is 3, first searched points is <code>stepPerSearch + 1</code>.
     * @param stepPerSearch minimum step is 3, each round searched points is <code>stepPerSearch + 1</code>.
     * @param thresholdSquared search accuracy, lower is accurate but slower.
     * @return {loc.xy, t, distanceSquared}
     */
    public static Vector4f getProjectivePointOnCurve(NodeData curveStart, NodeData curveEnd, Vector2f point, int stepFirstSearch, int stepPerSearch, float thresholdSquared) {
        final int stepIFirst = Math.max(stepFirstSearch, 3), stepI = Math.max(stepPerSearch, 3);
        float[] range = new float[]{0.0f, 1.0f / stepIFirst, 0.0f}; // start, stepPer, currT
        float tmpX, tmpY, currSearchDistanceSquared = Float.MAX_VALUE;
        float[] currPoint;
        Vector4f tmp0, tmp1;
        List<Vector4f> findList = new ArrayList<>(Math.max(stepIFirst, stepI));

        for (int i = 0; i <= stepIFirst; ++i) {
            range[2] = Math.min(range[0] + i * range[1], 1.0f);
            currPoint = getPointOnCurveArray(curveStart, curveEnd, range[2]);
            tmpX = currPoint[0] - point.x;
            tmpY = currPoint[1] - point.y;
            findList.add(new Vector4f(currPoint[0], currPoint[1], range[2], tmpX * tmpX + tmpY * tmpY));
        }
        findList.sort(_V4_COMPARATOR);
        tmp0 = findList.get(0);
        range[0] = Math.max(tmp0.z - range[1], 0.0f);
        range[1] = (Math.min(tmp0.z + range[1], 1.0f) - range[0]) / stepI;
        tmp1 = tmp0;

        while (currSearchDistanceSquared > thresholdSquared) {
            findList.clear();
            for (int i = 0; i <= stepI; ++i) {
                range[2] = Math.min(range[0] + i * range[1], 1.0f);
                currPoint = getPointOnCurveArray(curveStart, curveEnd, range[2]);
                tmpX = currPoint[0] - point.x;
                tmpY = currPoint[1] - point.y;
                findList.add(new Vector4f(currPoint[0], currPoint[1], range[2], tmpX * tmpX + tmpY * tmpY));
            }
            findList.sort(_V4_COMPARATOR);
            tmp0 = findList.get(0);
            range[0] = Math.max(tmp0.z - range[1], 0.0f);
            range[1] = (Math.min(tmp0.z + range[1], 1.0f) - range[0]) / stepI;
            tmpX = tmp1.x - tmp0.x;
            tmpY = tmp1.y - tmp0.y;
            currSearchDistanceSquared = tmpX * tmpX + tmpY * tmpY;
            tmp1 = tmp0;
        }
        return findList.get(0);
    }

    /**
     * @return {loc.xy, t}, empty list when no intersection.
     */
    public static List<Vector3f> getIntersectionCurveSegment(NodeData curveStart, NodeData curveEnd, Vector2f segmentStart, Vector2f segmentEnd) {
        List<Vector3f> result = new ArrayList<>(9);
        Pair<NodeData[], float[]> aligned = aligningNodeSegmentBased(curveStart, curveEnd, segmentStart, segmentEnd, null);
        Vector2f[] alignedCurvePointArray = new Vector2f[]{
                aligned.one[0].getLocation(),
                aligned.one[0].getTangentRight(),
                aligned.one[1].getTangentLeft(),
                aligned.one[1].getLocation()
        };
        alignedCurvePointArray[1].y += alignedCurvePointArray[0].y;
        alignedCurvePointArray[2].y += alignedCurvePointArray[3].y;

        float w = 3 * (alignedCurvePointArray[1].y - alignedCurvePointArray[2].y) + alignedCurvePointArray[3].y - alignedCurvePointArray[0].y;
        float a = 3.0f * (alignedCurvePointArray[0].y - 2.0f * alignedCurvePointArray[1].y + alignedCurvePointArray[2].y);
        float b = 3 * (alignedCurvePointArray[1].y - alignedCurvePointArray[0].y);
        float c = alignedCurvePointArray[0].y;
        float[] roots = new float[3];
        float delta;

        if (w != 0.0f) {
            a /= w;
            b /= w;
            c /= w;
            float a2 = a * a;
            float p = (3.0f * b - a2) / 3.0f;
            float q = (2.0f * a2 * a - 9 * a * b + 27.0f * c) / 27.0f;
            delta = (q * q) / 4.0f + (p * p * p) / 27.0f;

            if (delta == 0.0f) {
                a /= 3.0f;
                q /= -2.0f;
                q = (float) Math.cbrt(q);
                a = q - a;
                roots[0] = a + q;
                roots[1] = a;
            } else if (delta > 0.0f) {
                q /= 2.0f;
                delta = (float) Math.sqrt(delta);
                roots[0] = (float) Math.cbrt(delta - q) - (float) Math.cbrt(delta + q) - a / 3.0f;
            } else {
                a /= 3.0f;
                p = p / -3.0f;
                p = (float) Math.sqrt(p * p * p);
                q = Math.max(Math.min(q / (-2.0f * p), 1.0f), -1.0f);
                q = (float) Math.acos(q) / 3.0f;

                p = (float) Math.cbrt(p);
                p = p + p;
                roots[0] = (float) Math.cos(q) * p - a;
                roots[1] = (float) Math.cos(q + 2.0943951023931953f) * p - a; // q + PI * 2 / 3
                roots[2] = (float) Math.cos(q + 4.1887902047863905f) * p - a; // q + PI * 4 / 3
            }
        } else {
            if (a != 0.0f) {
                delta = b * b - 4.0f * a * c;
                a = a + a;
                if (delta > 0.0f) {
                    delta = (float) Math.sqrt(delta);
                    roots[0] = (delta - b) / a;
                    roots[1] = -(delta + b) / a;
                } else if (delta == 0.0f) roots[0] = -b / a;
            } else if (b != 0.0f) {
                roots[0] = -c / b;
            }
        }

        float[] checkPointTMP;
        Vector2f[] segAABB = new Vector2f[]{new Vector2f(), new Vector2f()};
        CalculateUtil.min(segmentStart, segmentEnd, segAABB[0]);
        CalculateUtil.max(segmentStart, segmentEnd, segAABB[1]);
        for (float checkRoot : roots) {
            if (checkRoot < 0.0f || checkRoot > 1.0f) continue;
            checkPointTMP = getPointOnCurveArray(curveStart, curveEnd, checkRoot);
            if (CalculateUtil.isPointWithinAABB(new Vector2f(checkPointTMP[0], checkPointTMP[1]), 0.05f, segAABB)) {
                result.add(new Vector3f(checkPointTMP[0], checkPointTMP[1], checkRoot));
            }
        }
        result.sort(_V3_COMPARATOR);
        return result;
    }

    /**
     * @return {loc.xy, t}, empty list when no intersection.
     */
    public static List<Vector3f> getIntersectionCurveSegment(NodeData curveStart, NodeData curveEnd, BoundsAPI.SegmentAPI segment) {
        return getIntersectionCurveSegment(curveStart, curveEnd, segment.getP1(), segment.getP2());
    }

    /**
     * @return {loc.xy, t}, null when no intersection.
     */
    public static @Nullable Vector3f getNearestIntersectionCurveSegment(NodeData curveStart, NodeData curveEnd, Vector2f segmentStart, Vector2f segmentEnd) {
        List<Vector3f> intersections = getIntersectionCurveSegment(curveStart, curveEnd, segmentStart, segmentEnd);
        return intersections.isEmpty() ? null : intersections.get(0);
    }

    /**
     * @return {loc.xy, t}, null when no intersection.
     */
    public static @Nullable Vector3f getNearestIntersectionCurveSegment(NodeData curveStart, NodeData curveEnd, BoundsAPI.SegmentAPI segment) {
        return getNearestIntersectionCurveSegment(curveStart, curveEnd, segment.getP1(), segment.getP2());
    }

    private final static class CurvePackage {
        float tStart = 0.0f; // at origin
        float tEnd = 1.0f; // at origin
        NodeData[] curve = new NodeData[2];
        Vector2f[] aabb;
    }

    private final static class CurvePackageDst {
        NodeData[] curve = new NodeData[2];
        Vector2f[] aabb;
    }

    private static float getAABBsLengthSquared(Vector2f[] src, Vector2f[] dst) {
        float x = Math.max(src[1].x, dst[1].x) - Math.min(src[0].x, dst[0].x);
        float y = Math.max(src[1].y, dst[1].y) - Math.min(src[0].y, dst[0].y);
        return x * x + y * y;
    }

    private static void _curveIntersection(CurvePackage src, CurvePackageDst dst, float searchThresholdSquared, float splitFactorEachRound, List<CurvePackage> result) {
        if (!CalculateUtil.isAABBOverlap(src.aabb, dst.aabb)) return;
        if (getAABBsLengthSquared(src.aabb, dst.aabb) <= searchThresholdSquared) {
            result.add(src);
            return;
        }
        NodeData[] splitNode;
        CurvePackage src2 = new CurvePackage(), srcSub = new CurvePackage(), srcSub2 = new CurvePackage();
        CurvePackageDst dst2 = new CurvePackageDst(), dstSub = new CurvePackageDst(), dstSub2 = new CurvePackageDst();
        srcSub.tEnd = src.tEnd;
        src.tEnd = src.tStart + (src.tEnd - src.tStart) * splitFactorEachRound;
        srcSub.tStart = src.tEnd;
        src2.tStart = src.tStart;
        src2.tEnd = src.tEnd;
        srcSub2.tStart = srcSub.tStart;
        srcSub2.tEnd = srcSub.tEnd;
        splitNode = curveSplit(src.curve[0], src.curve[1], splitFactorEachRound);
        src.curve[0] = src2.curve[0] = splitNode[0];
        src.curve[1] = src2.curve[1] = splitNode[1];
        srcSub.curve[0] = srcSub2.curve[0] = splitNode[1];
        srcSub.curve[1] = srcSub2.curve[1] = splitNode[2];
        splitNode = curveSplit(dst.curve[0], dst.curve[1], splitFactorEachRound);
        dst.curve[0] = dst2.curve[0] = splitNode[0];
        dst.curve[1] = dst2.curve[1] = splitNode[1];
        dstSub.curve[0] = dstSub2.curve[0] = splitNode[1];
        dstSub.curve[1] = dstSub2.curve[1] = splitNode[2];
        src.aabb = getCurveAABB(src.curve[0], src.curve[1]);
        src2.aabb = src.aabb;
        srcSub.aabb = getCurveAABB(srcSub.curve[0], srcSub.curve[1]);
        srcSub2.aabb = srcSub.aabb;
        dst.aabb = getCurveAABB(dst.curve[0], dst.curve[1]);
        dst2.aabb = dst.aabb;
        dstSub.aabb = getCurveAABB(dstSub.curve[0], dstSub.curve[1]);
        dstSub2.aabb = dstSub.aabb;
        _curveIntersection(src, dst, searchThresholdSquared, splitFactorEachRound, result);
        _curveIntersection(src2, dstSub, searchThresholdSquared, splitFactorEachRound, result);
        _curveIntersection(srcSub, dst2, searchThresholdSquared, splitFactorEachRound, result);
        _curveIntersection(srcSub2, dstSub2, searchThresholdSquared, splitFactorEachRound, result);
    }

    /**
     * @param similarityThresholdSquared check two curves is similar then return p0 of start curve.
     * @param searchThresholdSquared search accuracy, lower is accurate but slower.
     * @param mergeThresholdSquared merge curve intersection points with distance less than this threshold.
     * @param splitFactorEachRound range must be <strong>(0.0, 1.0)</strong>.
     * @return {loc.xy, t}, <strong>t</strong> is average value from two curves, empty list when no intersection.
     */
    public static List<Vector3f> getIntersectionCurveCurve(NodeData curveStartSRC, NodeData curveEndSRC, NodeData curveStartDST, NodeData curveEndDST, float similarityThresholdSquared, float searchThresholdSquared, float mergeThresholdSquared, float splitFactorEachRound) {
        List<Vector3f> result = new ArrayList<>();
        Vector2f similarityCheckTMP = new Vector2f();
        if (Vector2f.sub(curveStartSRC.getLocation(), curveStartDST.getLocation(), similarityCheckTMP).lengthSquared() < similarityThresholdSquared
                && Vector2f.sub(curveStartSRC.getTangentRight(), curveStartDST.getTangentRight(), similarityCheckTMP).lengthSquared() < similarityThresholdSquared
                && Vector2f.sub(curveEndSRC.getTangentLeft(), curveEndDST.getTangentLeft(), similarityCheckTMP).lengthSquared() < similarityThresholdSquared
                && Vector2f.sub(curveEndSRC.getLocation(), curveEndDST.getLocation(), similarityCheckTMP).lengthSquared() < similarityThresholdSquared) {
            float[] loc = curveStartSRC.getLocationArray();
            result.add(new Vector3f(loc[0], loc[1], 0.0f));
            return result;
        }
        List<CurvePackage> checkList = new ArrayList<>(16);
        CurvePackage checkA = new CurvePackage();
        CurvePackageDst checkB = new CurvePackageDst();
        checkA.aabb = getCurveAABB(curveStartSRC, curveEndSRC);
        checkB.aabb = getCurveAABB(curveStartDST, curveEndDST);
        checkA.curve[0] = curveStartSRC;
        checkA.curve[1] = curveEndSRC;
        checkB.curve[0] = curveStartDST;
        checkB.curve[1] = curveEndDST;

        _curveIntersection(checkA, checkB, searchThresholdSquared, splitFactorEachRound, checkList);

        if (checkList.isEmpty()) return result;
        float[] pointArray;
        checkA = checkList.get(0);
        if (checkList.size() < 2) {
            checkA.tStart += checkA.tEnd;
            checkA.tStart *= 0.5f;
            pointArray = getPointOnCurveArray(curveStartSRC, curveEndSRC, checkA.tStart);
            result.add(new Vector3f(pointArray[0], pointArray[1], checkA.tStart));
            return result;
        }
        checkList.sort(_CP_COMPARATOR);

        List<Float> roots = new ArrayList<>(16);
        int count = 1;
        float lastX = (checkA.aabb[0].x + checkA.aabb[1].x) * 0.5f, lastY = (checkA.aabb[0].y + checkA.aabb[1].y) * 0.5f, avgT = (checkA.tStart + checkA.tEnd) * 0.5f, currX, currY, currT, checkX, checkY;
        checkList.remove(0);
        for (CurvePackage curr : checkList) {
            currX = (curr.aabb[0].x + curr.aabb[1].x) * 0.5f;
            currY = (curr.aabb[0].y + curr.aabb[1].y) * 0.5f;
            checkX = lastX - currX;
            checkY = lastY - currY;
            currT = (curr.tStart + curr.tEnd) * 0.5f;
            if (checkX * checkX + checkY * checkY > mergeThresholdSquared) {
                roots.add(avgT / count);
                avgT = currT;
                count = 1;
            } else {
                avgT += currT;
                ++count;
            }
            lastX = currX;
            lastY = currY;
        }
        roots.add(count > 1 ? avgT / count : avgT);

        for (float root : roots) {
            pointArray = getPointOnCurveArray(curveStartSRC, curveEndSRC, root);
            result.add(new Vector3f(pointArray[0], pointArray[1], root));
        }
        result.sort(_V3_COMPARATOR);
        return result;
    }

    /**
     * @param similarityThresholdSquared check two curves is similar then return p0 of start curve.
     * @param searchThresholdSquared search accuracy, lower is accurate but slower.
     * @param mergeThresholdSquared merge curve intersection points with distance less than this threshold.
     * @return {loc.xy, t}, <strong>t</strong> is average value from two curves, null when no intersection.
     */
    public static @Nullable Vector3f getNearestIntersectionCurveCurve(NodeData curveStartSRC, NodeData curveEndSRC, NodeData curveStartDST, NodeData curveEndDST, float similarityThresholdSquared, float searchThresholdSquared, float mergeThresholdSquared, float splitFactorEachRound) {
        List<Vector3f> intersections = getIntersectionCurveCurve(curveStartSRC, curveEndSRC, curveStartDST, curveEndDST, similarityThresholdSquared, searchThresholdSquared, mergeThresholdSquared, splitFactorEachRound);
        return intersections.isEmpty() ? null : intersections.get(0);
    }

    private static float[] getPointOnCurveArray(NodeData left, NodeData right, float factor) {
        if (factor <= 0.0f) return left.getLocationArray();
        if (factor >= 1.0f) return right.getLocationArray();
        float factor2 = factor * factor;
        float factor3 = factor * factor2;
        float oneMinusF1 = 1.0f - factor;
        float[] tmp0 = CalculateUtil.mul(left.getLocationArray(), oneMinusF1 * oneMinusF1 * oneMinusF1);
        float[] tmp1 = CalculateUtil.mul(CalculateUtil.add(left.getLocationArray(), left.getTangentRightArray()), (factor3 - factor2 - factor2 + factor) * 3.0f);
        float[] tmp2 = CalculateUtil.mul(CalculateUtil.add(right.getLocationArray(), right.getTangentLeftArray()), (factor2 - factor3) * 3.0f);
        float[] tmp3 = CalculateUtil.mul(right.getLocationArray(), factor3);
        return CalculateUtil.addAll(tmp0, tmp1, tmp2, tmp3);
    }

    private static float[] getCurveDerivativeArray(NodeData left, NodeData right, float factor) {
        float factor1 = Math.min(Math.max(factor, 0.0f), 1.0f);
        float factor1P2 = factor1 + factor1;
        float factor2 = factor1 * factor1;
        float factor2P2 = factor2 + factor2;
        float oneMinusF1 = 1.0f - factor1;
        float oneMinusF1M2 = oneMinusF1 * oneMinusF1;
        float[] tmp0 = CalculateUtil.sub(CalculateUtil.mul(left.getLocationArray(), oneMinusF1M2), 3.0f);
        float[] tmp1 = CalculateUtil.mul(CalculateUtil.add(left.getLocationArray(), left.getTangentRightArray()), (oneMinusF1M2 - factor1P2 + factor2P2) * 3.0f);
        float[] tmp2 = CalculateUtil.mul(CalculateUtil.add(right.getLocationArray(), right.getTangentLeftArray()), (factor1P2 - factor2 - factor2P2) * 3.0f);
        float[] tmp3 = CalculateUtil.mul(right.getLocationArray(), factor2 * 3.0f);
        return CalculateUtil.addAll(tmp0, tmp1, tmp2, tmp3);
    }

    private static float[] getCurveSplitMatrix(float factor) {
        float[] matrix = new float[9];
        matrix[0] = factor;                       // t
        matrix[1] = matrix[0] * matrix[0];        // t^2
        matrix[2] = matrix[1] * matrix[0];        // t^3
        matrix[3] = matrix[0] - 1.0f;             // -(t-1)
        matrix[4] = matrix[3] * matrix[3];        // (t-1)^2
        matrix[5] = matrix[4] * matrix[3];        // -(t-1)^3
        matrix[5] = -matrix[5];
        matrix[3] = -matrix[3];
        matrix[6] = 2.0f * matrix[0] * matrix[3]; // -2t(t-1)
        matrix[7] = 3.0f * matrix[0] * matrix[4]; // 3t(t-1)^2
        matrix[8] = 3.0f * matrix[3] * matrix[1]; // -3(t-1)t^2
        return matrix;
    }

    private static NodeData[] curveSplitControlOnlyWithoutCopy(NodeData left, NodeData right, float factor) {
        NodeData[] nodes = new NodeData[]{new NodeData(left), null, new NodeData(right)};
        if (factor <= 0.0f) {
            nodes[1] = new NodeData(left);
            return nodes;
        }
        if (factor >= 1.0f) {
            nodes[1] = new NodeData(right);
            return nodes;
        }
        nodes[1] = new NodeData();

        float[] matrix = getCurveSplitMatrix(factor);
        float[][] result = new float[5][2];
        Vector2f[] raw = new Vector2f[4];
        raw[0] = left.getLocation();
        raw[3] = right.getLocation();
        raw[1] = Vector2f.add(raw[0], left.getTangentRight(), new Vector2f());
        raw[2] = Vector2f.add(raw[3], right.getTangentLeft(), new Vector2f());
        result[0][0] = matrix[0] * raw[1].x + matrix[3] * raw[0].x;
        result[0][1] = matrix[0] * raw[1].y + matrix[3] * raw[0].y;
        result[0][0] -= raw[0].x;
        result[0][1] -= raw[0].y;
        result[1][0] = matrix[1] * raw[2].x + matrix[6] * raw[1].x + matrix[4] * raw[0].x;
        result[1][1] = matrix[1] * raw[2].y + matrix[6] * raw[1].y + matrix[4] * raw[0].y;
        result[2][0] = matrix[2] * raw[3].x + matrix[8] * raw[2].x + matrix[7] * raw[1].x + matrix[5] * raw[0].x;
        result[2][1] = matrix[2] * raw[3].y + matrix[8] * raw[2].y + matrix[7] * raw[1].y + matrix[5] * raw[0].y;
        result[1][0] -= result[2][0];
        result[1][1] -= result[2][1];
        result[3][0] = matrix[1] * raw[3].x + matrix[6] * raw[2].x + matrix[4] * raw[1].x;
        result[3][1] = matrix[1] * raw[3].y + matrix[6] * raw[2].y + matrix[4] * raw[1].y;
        result[3][0] -= result[2][0];
        result[3][1] -= result[2][1];
        result[4][0] = matrix[0] * raw[3].x + matrix[3] * raw[2].x;
        result[4][1] = matrix[0] * raw[3].y + matrix[3] * raw[2].y;
        result[4][0] -= raw[3].x;
        result[4][1] -= raw[3].y;
        nodes[1].setLocation(result[2][0], result[2][1]);
        nodes[1].setTangentLeft(result[1][0], result[1][1]);
        nodes[1].setTangentRight(result[3][0], result[3][1]);
        nodes[0].setTangentRight(result[0][0], result[0][1]);
        nodes[2].setTangentLeft(result[4][0], result[4][1]);
        return nodes;
    }

    public static Vector2f getPointOnCurve(NodeData left, NodeData right, float factor) {
        float[] location = getPointOnCurveArray(left, right, factor);
        return new Vector2f(location[0], location[1]);
    }

    public static Vector2f getCurveDerivative(NodeData left, NodeData right, float factor) {
        float[] location = getCurveDerivativeArray(left, right, factor);
        return new Vector2f(location[0], location[1]);
    }

    /**
     * Pick a node between two nodeList.
     * @return {start, mid, end}
     */
    public static NodeData[] curveSplit(NodeData left, NodeData right, float factor) {
        NodeData[] out = curveSplitControlOnlyWithoutCopy(left, right, factor);
        byte[] colorState = CalculateUtil.mix(left.getColorState(), right.getColorState(), factor);
        out[1].setColor(colorState[0], colorState[1], colorState[2], colorState[3]);
        out[1].setEmissiveColor(colorState[4], colorState[5], colorState[6], colorState[7]);
        out[1].setWidth(CalculateUtil.mix(left.getWidth(), right.getWidth(), factor));
        out[1].setMixFactor(CalculateUtil.mix(left.getMixFactor(), right.getMixFactor(), factor));
        return out;
    }

    /**
     * @param left for result, <strong>color/emissive color/width/mix</strong> factor all use it.
     * @return {start, mid, end}
     */
    public static NodeData[] curveSplitControlPointOnly(NodeData left, NodeData right, float factor) {
        NodeData[] out = curveSplitControlOnlyWithoutCopy(left, right, factor);
        System.arraycopy(left.getColorArray(), 0, out[1].getColorArray(), 0, left.getColorArray().length);
        System.arraycopy(left.getEmissiveColorArray(), 0, out[1].getEmissiveColorArray(), 0, left.getEmissiveColorArray().length);
        out[1].setWidth(left.getWidth());
        out[1].setMixFactor(left.getMixFactor());
        return out;
    }

    private final static class DealtData {
        CombatEntityAPI target;
        Vector2f point;
        float curveT;
        boolean isShieldHit;
    }

    /**
     * Execution order: {@link DealtController#isIgnore(CombatEntityAPI)} -> {@link DealtController#isPierceShield(ShipAPI)} -> {@link DealtController#applyEffect(CombatEntityAPI, Vector2f, float, boolean)} -> {@link DealtController#isPierce(CombatEntityAPI, Vector2f, float, boolean)}
     */
    public interface DealtController {
        /**
         * @param beamT returns <code>distanceSquared</code> to start when use {@link CurveUtil#spawnDirectBeam(CombatEngineAPI, Vector2f, Vector2f, float, DealtController)}
         */
        void applyEffect(CombatEntityAPI target, Vector2f point, float beamT, boolean isShieldHit);

        boolean isIgnore(CombatEntityAPI target);

        boolean isPierceShield(ShipAPI target);

        /**
         * @param beamT returns <code>distanceSquared</code> to start when use {@link CurveUtil#spawnDirectBeam(CombatEngineAPI, Vector2f, Vector2f, float, DealtController)}
         */
        boolean isPierce(CombatEntityAPI target, Vector2f point, float beamT, boolean isShieldHit);
    }

    public static class SimpleDealtController implements DealtController {
        private final CombatEngineAPI engine;
        private final float damageAmount;
        private final DamageType damageType;
        private final float empDamageAmount;
        private final CombatEntityAPI source;
        private final boolean dealsSoftFlux;
        private final Color core;
        private final Color fringe;
        private final float dur;
        private final float width;
        private final float width2;

        public SimpleDealtController(CombatEngineAPI engine, float damageAmount, DamageType damageType, float empDamageAmount, CombatEntityAPI source, boolean dealsSoftFlux, Color core, Color fringe, float dur, float width) {
            this.engine = engine;
            this.damageAmount = damageAmount;
            this.damageType = damageType;
            this.empDamageAmount = empDamageAmount;
            this.source = source;
            this.dealsSoftFlux = dealsSoftFlux;
            this.core = core;
            this.fringe = new Color(fringe.getRed(), fringe.getGreen(), fringe.getBlue(), (int) (fringe.getAlpha() * 0.666667f));
            this.dur = dur * 1.5f;
            this.width = width * 4.0f;
            this.width2 = this.width + this.width;
        }

        public void applyEffect(CombatEntityAPI target, Vector2f point, float beamT, boolean isShieldHit) {
            this.engine.applyDamage(target, point, this.damageAmount, this.damageType, this.empDamageAmount, false, this.dealsSoftFlux, source, true);
            if (!RenderingUtil.VanillaFX.addHitParticle(false, point, new Vector2f(), this.width2, 1.0f, this.dur, this.fringe)) {
                this.engine.addHitParticle(point, new Vector2f(), this.width2, 1.0f, this.dur, this.fringe);
            }
            if (!RenderingUtil.VanillaFX.addHitParticle(false, point, new Vector2f(), this.width, 1.0f, this.dur, this.core)) {
                this.engine.addHitParticle(point, new Vector2f(), this.width, 1.0f, this.dur, this.core);
            }
        }

        public boolean isIgnore(CombatEntityAPI target) {
            boolean isFighter = false;
            boolean parentIsSource = false;
            boolean inPhased = false;
            if (target instanceof ShipAPI shipTarget) {
                isFighter = shipTarget.isFighter();
                parentIsSource = shipTarget.getParentStation() == this.source;
                inPhased = shipTarget.isPhased();
            }
            return target == this.source || target.getCollisionClass() == CollisionClass.NONE || inPhased || (target instanceof MissileAPI || isFighter || parentIsSource) && target.getOwner() == source.getOwner();
        }

        public boolean isPierceShield(ShipAPI target) {
            return false;
        }

        public boolean isPierce(CombatEntityAPI target, Vector2f point, float beamT, boolean isShieldHit) {
            return false;
        }
    }

    private static boolean entityCrossLine(Vector2f point, float radius, float length) {
        return Math.abs(point.y) <= radius && point.x >= -radius && point.x <= (length + radius);
    }

    /**
     * @return <code>Vector3f</code> returns absolute location the end of beam and distance squared, returns null when no intersection; <p><code>Matrix2f</code> is the rotation matrix of beam.
     */
    public static Pair<Vector3f, Matrix2f> spawnDirectBeam(CombatEngineAPI engine, Vector2f start, Vector2f end, float maxCheckRangeOffset, DealtController dealtController) {
        Pair<Vector3f, Matrix2f> result = new Pair<>(null, new Matrix2f());
        List<DealtData> dealtList = new ArrayList<>();
        Vector2f[] cullAABB = new Vector2f[]{new Vector2f(), new Vector2f()};
        Matrix2f _transform = new Matrix2f();
        Vector2f _firstCullCenter = new Vector2f(), beamNormal = new Vector2f();
        float cosValue, sinValue, beamLength;

        Vector2f.sub(end, start, _firstCullCenter);
        cullAABB[1].set(_firstCullCenter);
        beamNormal.set(-_firstCullCenter.y, _firstCullCenter.x);
        beamLength = _firstCullCenter.length();
        _firstCullCenter.scale(1.0f / beamLength);
        cosValue = _firstCullCenter.x;
        sinValue = _firstCullCenter.y;

        Vector2f.add(start, end, _firstCullCenter);
        _firstCullCenter.x *= 0.5f;
        _firstCullCenter.y *= 0.5f;
        _transform.m00 = _transform.m11 = result.two.m00 = result.two.m11 = cosValue;
        _transform.m01 = result.two.m10 = -sinValue;
        _transform.m10 = result.two.m01 = sinValue;

        List<CombatEntityAPI> targetList = new ArrayList<>();
        Object targetICheck;
        CombatEntityAPI targetI;
        Vector2f firstCullLoc = new Vector2f();
        float _firstCullRange = beamLength + maxCheckRangeOffset;
        for (Iterator<Object> iterator = engine.getAllObjectGrid().getCheckIterator(_firstCullCenter, _firstCullRange, _firstCullRange); iterator.hasNext();) {
            targetICheck = iterator.next();
            if (targetICheck instanceof CombatEntityAPI) {
                targetI = (CombatEntityAPI) targetICheck;
                if (targetI instanceof BattleObjectiveAPI || targetI instanceof EmpArcEntityAPI || targetI instanceof DamagingProjectileAPI && !(targetI instanceof MissileAPI)) continue;
                if (dealtController.isIgnore(targetI) || !entityCrossLine(Matrix2f.transform(_transform, Vector2f.sub(targetI.getLocation(), start, firstCullLoc), firstCullLoc), targetI.getCollisionRadius() * 1.1f + 0.05f, beamLength)) continue;
                targetList.add(targetI);
            }
        }

        CalculateUtil.min(start, end, cullAABB[0]);
        CalculateUtil.max(start, end, cullAABB[1]);
        DealtData currDealtData;
        ShipAPI shipTarget;
        ShieldAPI shipShield;
        BoundsAPI shipBounds;
        Vector2f[] shipHullBoundPair;
        Vector2f[] shipHullBoundAABB = new Vector2f[]{new Vector2f(), new Vector2f()};
        Vector2f shipCenter;
        List<Vector3f> shipHullCheckPoint = new ArrayList<>();
        Vector2f shieldTmp, boundNormal = new Vector2f();
        Vector3f shipShieldV3, shipPointV3;
        float tmpX, tmpY, dN, d1, d2;
        for (CombatEntityAPI target : targetList) {
            if (target instanceof ShipAPI) {
                result.one = null;
                boolean shieldHit = false;
                shipTarget = (ShipAPI) target;
                shipCenter = shipTarget.getLocation();
                shipShield = shipTarget.getShield();
                shipShieldV3 = null;
                shipHullCheckPoint.clear();
                if (shipShield != null && !dealtController.isPierceShield(shipTarget) && shipShield.getType() != ShieldAPI.ShieldType.NONE && shipShield.getType() != ShieldAPI.ShieldType.PHASE && shipShield.isOn() && shipShield.getArc() > 0.0f && shipShield.getActiveArc() > 0.0f && shipShield.getRadius() > 0.0f) {
                    shieldTmp = CalculateUtil.getNearestIntersectionSegmentShield(start, end, shipShield.getLocation(), shipShield.getRadius());

                    if (shieldTmp != null && shipShield.isWithinArc(shieldTmp)) {
                        tmpX = shieldTmp.x - start.x;
                        tmpY = shieldTmp.y - start.y;
                        shipShieldV3 = new Vector3f(shieldTmp.x, shieldTmp.y, tmpX * tmpX + tmpY * tmpY);
                    }
                }

                TransformUtil.createSimpleRotateMatrix(shipTarget.getFacing(), _transform);
                shipBounds = shipTarget.getExactBounds();
                if (shipBounds != null) for (BoundsAPI.SegmentAPI bound : shipBounds.getOrigSegments()) {
                    if (bound == null) continue;
                    shipHullBoundPair = new Vector2f[]{new Vector2f(bound.getP1()), new Vector2f(bound.getP2())};
                    Matrix2f.transform(_transform, shipHullBoundPair[0], shipHullBoundPair[0]);
                    shipHullBoundPair[0].x += shipCenter.x;
                    shipHullBoundPair[0].y += shipCenter.y;
                    Matrix2f.transform(_transform, shipHullBoundPair[1], shipHullBoundPair[1]);
                    shipHullBoundPair[1].x += shipCenter.x;
                    shipHullBoundPair[1].y += shipCenter.y;
                    CalculateUtil.min(shipHullBoundPair[0], shipHullBoundPair[1], shipHullBoundAABB[0]);
                    CalculateUtil.max(shipHullBoundPair[0], shipHullBoundPair[1], shipHullBoundAABB[1]);
                    if (!CalculateUtil.isAABBOverlap(cullAABB, shipHullBoundAABB)) continue;

                    Vector2f.sub(shipHullBoundPair[1], shipHullBoundPair[0], _firstCullCenter);
                    boundNormal.set(-_firstCullCenter.y, _firstCullCenter.x);
                    dN = Vector2f.dot(shipHullBoundPair[1], boundNormal);
                    d1 = Vector2f.dot(start, boundNormal);
                    d2 = Vector2f.dot(end, boundNormal);
                    if ((d1 - dN) * (d2 - dN) >= 0.0f) continue;
                    dN = Vector2f.dot(end, beamNormal);
                    d1 = Vector2f.dot(shipHullBoundPair[0], beamNormal);
                    d2 = Vector2f.dot(shipHullBoundPair[1], beamNormal);
                    if ((d1 - dN) * (d2 - dN) >= 0.0f) continue;
                    d2 = (d1 - dN) / (boundNormal.x * beamNormal.y - boundNormal.y * beamNormal.x);
                    boundNormal.set(shipHullBoundPair[0].x + d2 * boundNormal.y, shipHullBoundPair[0].y - d2 * boundNormal.x);
                    tmpX = boundNormal.x - start.x;
                    tmpY = boundNormal.y - start.y;
                    shipHullCheckPoint.add(new Vector3f(boundNormal.x, boundNormal.y, tmpX * tmpX + tmpY * tmpY));
                }
                shipHullCheckPoint.sort(_V3_COMPARATOR);

                if (shipShieldV3 != null) {
                    if (shipHullCheckPoint.isEmpty()) {
                        result.one = shipShieldV3;
                        shieldHit = true;
                    } else {
                        shipPointV3 = shipHullCheckPoint.get(0);
                        if (shipShieldV3.z <= shipPointV3.z) {
                            result.one = shipShieldV3;
                            shieldHit = true;
                        } else result.one = shipPointV3;
                    }
                } else if (!shipHullCheckPoint.isEmpty()) result.one = shipHullCheckPoint.get(0);

                if (result.one != null) {
                    currDealtData = new DealtData();
                    currDealtData.target = target;
                    currDealtData.point = new Vector2f(result.one.x, result.one.y);
                    currDealtData.curveT = result.one.z;
                    currDealtData.isShieldHit = shieldHit;
                    dealtList.add(currDealtData);
                }
            } else {
                shipCenter = CalculateUtil.getNearestIntersectionSegmentShield(start, end, target.getLocation(), target.getCollisionRadius());
                if (shipCenter != null) {
                    tmpX = shipCenter.x - start.x;
                    tmpY = shipCenter.y - start.y;
                    currDealtData = new DealtData();
                    currDealtData.target = target;
                    currDealtData.point = shipCenter;
                    currDealtData.curveT = tmpX * tmpX + tmpY * tmpY;
                    currDealtData.isShieldHit = false;
                    dealtList.add(currDealtData);
                }
            }
        }

        result.one = null;
        dealtList.sort(_DD_COMPARATOR);
        for (DealtData dealt : dealtList) {
            dealtController.applyEffect(dealt.target, dealt.point, dealt.curveT, dealt.isShieldHit);
            if (!dealtController.isPierce(dealt.target, dealt.point, dealt.curveT, dealt.isShieldHit)) {
                result.one = new Vector3f(dealt.point.x, dealt.point.y, dealt.curveT);
                break;
            }
        }
        return result;
    }

    /**
     * @return <code>TrailEntity</code> has been submitted and added to rendering manager;<p><code>Vector3f</code> is the end location and distance squared of beam, returns null when no damaging.
     */
    public static Pair<TrailEntity, Vector3f> spawnDirectBeam(CombatEngineAPI engine, Vector2f start, Vector2f end, CombatEntityAPI source, float damageAmount, DamageType damageType, float empDamageAmount, boolean dealsSoftFlux, SpriteAPI core, Color coreColor, SpriteAPI fringe, Color fringeColor, float width, float textureSpeed, float fadeIn, float full, float fadeOut, CombatEngineLayers layer) {
        DealtController dealtController = new SimpleDealtController(engine, damageAmount, damageType, empDamageAmount, source, dealsSoftFlux, coreColor, fringeColor, full + fadeOut, width);
        Pair<Vector3f, Matrix2f> origResult = spawnDirectBeam(engine, start, end, 320.0f, dealtController);
        Pair<TrailEntity, Vector3f> result = new Pair<>(new TrailEntity(), origResult.one);
        Vector3f realEnd = new Vector3f();
        Vector2f endNode = new Vector2f();
        result.one.setLocation(start);
        result.one.getModelMatrix().m00 = origResult.two.m00;
        result.one.getModelMatrix().m01 = origResult.two.m01;
        result.one.getModelMatrix().m10 = origResult.two.m10;
        result.one.getModelMatrix().m11 = origResult.two.m11;
        origResult.two.m10 = -origResult.two.m10;
        origResult.two.m01 = -origResult.two.m01;
        if (result.two == null) {
            endNode.set(end.x - start.x, end.y - start.y);
            realEnd.z = endNode.lengthSquared();
        } else {
            realEnd.set(result.two);
            endNode.set(result.two.x - start.x, result.two.y - start.y);
        }
        Matrix2f.transform(origResult.two, endNode, endNode);
        result.one.addNode(endNode);
        result.one.addNode(new Vector2f());
        result.one.setStripLineMode(false);
        result.one.setNodeRefreshAllFromCurrentIndex();
        result.one.submitNodes();
        result.one.setLayer(layer);
        realEnd.z = (float) Math.sqrt(realEnd.z);
        float smoothFactor = Math.max(realEnd.z - 10.0f, 0.0f) / realEnd.z;
        result.one.setFillStartAlpha(0.0f);
        result.one.setFillStartFactor(smoothFactor);
        result.one.setFillEndAlpha(0.0f);
        if (result.two == null) result.one.setFillEndFactor(smoothFactor);
        result.one.setTexturePixels(512.0f);
        result.one.setTextureSpeed(textureSpeed);
        result.one.getMaterialData().setDiffuse(core);
        result.one.getMaterialData().setEmissive(fringe);
        result.one.setStartColor(coreColor);
        result.one.setEndColor(coreColor);
        result.one.setStartEmissive(fringeColor);
        result.one.setEndEmissive(fringeColor);
        result.one.setStartWidth(width);
        result.one.setEndWidth(width);
        result.one.setAdditiveBlend();
        result.one.setGlobalTimer(fadeIn, full, fadeOut);
        CombatRenderingManager.addEntity(result.one);
        return result;
    }

    /**
     * @param startOffset {loc.xy, angle}.<p> if set to <strong>null</strong>: all calculating will be based node data;<p> if set to <strong>not null</strong>: curve must be aligned to <strong>(0, 0) to positive x-axis</strong>, angle value must be <strong>[0, 360]</strong>.
     * @param stepFirstSearch for entity that without shield or bounds. Minimum step is 2, first searched points is <strong>stepPerSearch + 1</strong>.
     * @param stepPerSearch for entity that without shield or bounds. Minimum step is 2, each round searched points is <strong>stepPerSearch + 1</strong>.
     * @param thresholdSquared for entity that without shield or bounds. Search accuracy, lower is accurate but slower.
     * @param similarityThresholdSquared for shield of ship. Check two curves is similar then return p0 of start curve.
     * @param searchThresholdSquared for shield of ship. Search accuracy, lower is accurate but slower.
     * @param mergeThresholdSquared for shield of ship. Merge curve intersection points with distance less than this threshold.
     * @param resultEndNode for set the end node.
     * @param transformResult for turn the result aligned nodes to absolute coordinate.
     * @return NodeData[] = {start, end};<p><code>Vector3f</code> is the end location and t of curve, returns null when no damaging.
     */
    public static Pair<NodeData[], Vector3f> spawnCurveBeam(CombatEngineAPI engine, @Nullable Vector3f startOffset, NodeData start, NodeData end, float maxCheckRange, DealtController dealtController, int stepFirstSearch, int stepPerSearch, float thresholdSquared, float similarityThresholdSquared, float searchThresholdSquared, float mergeThresholdSquared, float splitFactorEachRound, @Nullable NodeData resultEndNode, boolean returnsAlignedNode, @Nullable Matrix3f transformResult) {
        List<DealtData> dealtList = new ArrayList<>();
        Vector3f tmp0, tmp1, resultPoint;
        NodeData[] curve = new NodeData[2];
        NodeData[] curveAligned = new NodeData[2];
        Vector2f[] firstCullAABB;
        Matrix2f _transform = new Matrix2f();
        Vector2f _firstCullCenter = new Vector2f(), curveStart = new Vector2f();
        float cosValue, sinValue;

        if (startOffset == null) {
            curve[0] = start;
            curve[1] = end;
            Pair<NodeData[], float[]> aligningNode = aligningNode(curve[0], curve[1], new Pair<>(new NodeData[2], new float[5]));
            curveAligned[0] = aligningNode.one[0];
            curveAligned[1] = aligningNode.one[1];
            firstCullAABB = getCurveAABB(aligningNode.one[0], aligningNode.one[1]);
            cosValue = aligningNode.two[0];
            sinValue = aligningNode.two[1];
            curveStart.set(aligningNode.two[2], aligningNode.two[3]);
        } else {
            curve[0] = new NodeData(start);
            curve[1] = new NodeData(end);
            curveAligned[0] = start;
            curveAligned[1] = end;
            firstCullAABB = getCurveAABB(curve[0], curve[1]);
            TransformUtil.createSimpleRotateMatrix(startOffset.z, _transform);
            cosValue = _transform.m00;
            sinValue = _transform.m01;
            curve[0].setLocation(startOffset.x, startOffset.y);
            curve[1].setLocation(Vector2f.add(Matrix2f.transform(_transform, end.getLocation(), curveStart), new Vector2f(startOffset.x, startOffset.y), curveStart));
            curve[0].setTangentRight(Matrix2f.transform(_transform, start.getTangentRight(), curveStart));
            curve[1].setTangentLeft(Matrix2f.transform(_transform, end.getTangentLeft(), curveStart));
            curveStart.set(startOffset.x, startOffset.y);
        }
        float[] _fcTMP0, _fcTMP1;
        _fcTMP0 = curve[0].getLocationArray();
        _fcTMP1 = curve[0].getTangentRightArray();
        _firstCullCenter.x += _fcTMP0[0] + _fcTMP0[0] + _fcTMP1[0];
        _firstCullCenter.y += _fcTMP0[1] + _fcTMP0[1] + _fcTMP1[1];
        _fcTMP0 = curve[1].getLocationArray();
        _fcTMP1 = curve[1].getTangentLeftArray();
        _firstCullCenter.x += _fcTMP0[0] + _fcTMP0[0] + _fcTMP1[0];
        _firstCullCenter.y += _fcTMP0[1] + _fcTMP0[1] + _fcTMP1[1];
        _firstCullCenter.x *= 0.25f;
        _firstCullCenter.y *= 0.25f;
        _transform.m00 = _transform.m11 = cosValue;
        _transform.m01 = -sinValue;
        _transform.m10 = sinValue;

        List<CombatEntityAPI> targetList = new ArrayList<>();
        Object targetICheck;
        CombatEntityAPI targetI;
        Vector2f firstCullLoc = new Vector2f();
        for (Iterator<Object> iterator = engine.getAllObjectGrid().getCheckIterator(_firstCullCenter, maxCheckRange, maxCheckRange); iterator.hasNext();) {
            targetICheck = iterator.next();
            if (targetICheck instanceof CombatEntityAPI) {
                targetI = (CombatEntityAPI) targetICheck;
                if (targetI instanceof BattleObjectiveAPI || targetI instanceof EmpArcEntityAPI || targetI instanceof DamagingProjectileAPI && !(targetI instanceof MissileAPI)) continue;
                if (dealtController.isIgnore(targetI) || !CalculateUtil.isPointWithinAABB(Matrix2f.transform(_transform, Vector2f.sub(targetI.getLocation(), curveStart, firstCullLoc), firstCullLoc), targetI.getCollisionRadius() * 1.1f + 0.05f, firstCullAABB)) continue;
                targetList.add(targetI);
            }
        }

        Matrix2f hullBoundInv = new Matrix2f();
        hullBoundInv.load(_transform);
        DealtData currDealtData;
        ShipAPI shipTarget;
        ShieldAPI shipShield;
        BoundsAPI shipBounds;
        Vector2f[] shipHullBoundPair;
        Vector2f[] shipHullBoundPairTMP = new Vector2f[]{new Vector2f(), new Vector2f()};
        List<Vector2f[]> shipHullBoundsList = new ArrayList<>();
        Vector2f shipCenter;
        Vector2f shieldCenter;
        NodeData[] shieldCurve = new NodeData[]{new NodeData(), new NodeData()};
        float shieldTangent, shieldCenterXL, shieldCenterXR, shieldCenterY;
        List<Vector3f> shipHullCheckPoint = new ArrayList<>();
        List<Vector3f> shipShieldCheckPoint = new ArrayList<>();
        for (CombatEntityAPI target : targetList) {
            resultPoint = null;
            if (target instanceof ShipAPI) {
                boolean shieldHit = false;
                shipTarget = (ShipAPI) target;
                shipCenter = shipTarget.getLocation();
                shipShield = shipTarget.getShield();
                shipShieldCheckPoint.clear();
                shipHullCheckPoint.clear();
                shipHullBoundsList.clear();
                if (shipShield != null && !dealtController.isPierceShield(shipTarget) && shipShield.getType() != ShieldAPI.ShieldType.NONE && shipShield.getType() != ShieldAPI.ShieldType.PHASE && shipShield.isOn() && shipShield.getArc() > 0.0f && shipShield.getActiveArc() > 0.0f && shipShield.getRadius() > 0.0f) {
                    shieldCenter = shipShield.getLocation();
                    shieldTangent = (shipShield.getRadius() + 0.75f) * 1.3333334f;

                    shieldCenterXL = shieldCenter.x - shipShield.getRadius();
                    shieldCenterXR = shieldCenter.x + shipShield.getRadius();
                    shieldCenterY = shieldCenter.y - 1.5f;
                    shieldCurve[0].setLocation(shieldCenterXL, shieldCenterY);
                    shieldCurve[0].setTangentLeft(0.0f, -shieldTangent);
                    shieldCurve[0].setTangentRight(0.0f, shieldTangent);
                    shieldCurve[1].setLocation(shieldCenterXR, shieldCenterY);
                    shieldCurve[1].setTangentLeft(0.0f, shieldTangent);
                    shieldCurve[1].setTangentRight(0.0f, -shieldTangent);

                    tmp0 = getNearestIntersectionCurveCurve(curve[0], curve[1], shieldCurve[0], shieldCurve[1], similarityThresholdSquared, searchThresholdSquared, mergeThresholdSquared, splitFactorEachRound);
                    if (tmp0 != null && shipShield.isWithinArc(new Vector2f(tmp0.x, tmp0.y))) shipShieldCheckPoint.add(tmp0);
                    shieldCenterY = shieldCenter.y + 1.5f;
                    shieldCurve[0].setLocation(shieldCenterXL, shieldCenterY);
                    shieldCurve[1].setLocation(shieldCenterXR, shieldCenterY);
                    tmp0 = getNearestIntersectionCurveCurve(curve[0], curve[1], shieldCurve[1], shieldCurve[0], similarityThresholdSquared, searchThresholdSquared, mergeThresholdSquared, splitFactorEachRound);
                    if (tmp0 != null && shipShield.isWithinArc(new Vector2f(tmp0.x, tmp0.y))) shipShieldCheckPoint.add(tmp0);
                    shipShieldCheckPoint.sort(_V3_COMPARATOR);
                }

                TransformUtil.createSimpleRotateMatrix(shipTarget.getFacing(), _transform);
                shipBounds = shipTarget.getExactBounds();
                float hullOffsetX = shipCenter.x - curveStart.x, hullOffsetY = shipCenter.y - curveStart.y;

                if (shipBounds != null) for (BoundsAPI.SegmentAPI bound : shipBounds.getOrigSegments()) {
                    if (bound == null) continue;
                    shipHullBoundPair = new Vector2f[]{new Vector2f(bound.getP1()), new Vector2f(bound.getP2())};
                    Matrix2f.transform(_transform, shipHullBoundPair[0], shipHullBoundPair[0]);
                    shipHullBoundPairTMP[0].set(shipHullBoundPair[0]);
                    shipHullBoundPair[0].x += hullOffsetX;
                    shipHullBoundPair[0].y += hullOffsetY;
                    Matrix2f.transform(hullBoundInv, shipHullBoundPair[0], shipHullBoundPair[0]);
                    Matrix2f.transform(_transform, shipHullBoundPair[1], shipHullBoundPair[1]);
                    shipHullBoundPairTMP[1].set(shipHullBoundPair[1]);
                    shipHullBoundPair[1].x += hullOffsetX;
                    shipHullBoundPair[1].y += hullOffsetY;
                    Matrix2f.transform(hullBoundInv, shipHullBoundPair[1], shipHullBoundPair[1]);
                    if (CalculateUtil.isSegmentWithinOrIntersectAABB(shipHullBoundPair[0], shipHullBoundPair[1], firstCullAABB)) {
                        shipHullBoundPair[0].set(shipHullBoundPairTMP[0]);
                        Vector2f.add(shipHullBoundPair[0], shipCenter, shipHullBoundPair[0]);
                        shipHullBoundPair[1].set(shipHullBoundPairTMP[1]);
                        Vector2f.add(shipHullBoundPair[1], shipCenter, shipHullBoundPair[1]);
                        shipHullBoundsList.add(shipHullBoundPair);
                    }
                }
                for (Vector2f[] bound : shipHullBoundsList) {
                    tmp1 = getNearestIntersectionCurveSegment(curve[0], curve[1], bound[0], bound[1]);
                    if (tmp1 != null) shipHullCheckPoint.add(tmp1);
                }
                shipHullCheckPoint.sort(_V3_COMPARATOR);

                if (!shipShieldCheckPoint.isEmpty()) {
                    tmp0 = shipShieldCheckPoint.get(0);
                    if (shipHullCheckPoint.isEmpty()) {
                        resultPoint = tmp0;
                        shieldHit = true;
                    } else {
                        tmp1 = shipHullCheckPoint.get(0);
                        if (tmp0.z <= tmp1.z) {
                            resultPoint = tmp0;
                            shieldHit = true;
                        } else resultPoint = tmp1;
                    }
                } else if (!shipHullCheckPoint.isEmpty()) resultPoint = shipHullCheckPoint.get(0);

                if (resultPoint != null) {
                    currDealtData = new DealtData();
                    currDealtData.target = target;
                    currDealtData.point = new Vector2f(resultPoint.x, resultPoint.y);
                    currDealtData.curveT = resultPoint.z;
                    currDealtData.isShieldHit = shieldHit;
                    dealtList.add(currDealtData);
                }
            } else {
                Vector4f projectivePoint = getProjectivePointOnCurve(curve[0], curve[1], target.getLocation(), stepFirstSearch, stepPerSearch, thresholdSquared);
                if (projectivePoint.w <= target.getCollisionRadius() * target.getCollisionRadius()) resultPoint = new Vector3f(projectivePoint.x, projectivePoint.y, projectivePoint.z);
                if (resultPoint != null) {
                    currDealtData = new DealtData();
                    currDealtData.target = target;
                    currDealtData.point = new Vector2f(resultPoint.x, resultPoint.y);
                    currDealtData.curveT = resultPoint.z;
                    currDealtData.isShieldHit = false;
                    dealtList.add(currDealtData);
                }
            }
        }

        resultPoint = null;
        dealtList.sort(_DD_COMPARATOR);
        for (DealtData dealt : dealtList) {
            dealtController.applyEffect(dealt.target, dealt.point, dealt.curveT, dealt.isShieldHit);
            if (!dealtController.isPierce(dealt.target, dealt.point, dealt.curveT, dealt.isShieldHit)) {
                resultPoint = new Vector3f(dealt.point.x, dealt.point.y, dealt.curveT);
                break;
            }
        }

        if (resultPoint != null && resultPoint.z < 1.0f) {
            NodeData[] nodeData = curveSplit(curveAligned[0], curveAligned[1], resultPoint.z);
            curveAligned[1] = nodeData[1];
        } else resultPoint = null;

        if (transformResult != null) {
            transformResult.m20 = curveStart.x;
            transformResult.m21 = curveStart.y;
            transformResult.m00 = cosValue;
            transformResult.m10 = -sinValue;
            transformResult.m01 = sinValue;
            transformResult.m11 = cosValue;
        }
        if (resultEndNode != null) {
            resultEndNode.setLocation(returnsAlignedNode ? curveAligned[1].getLocation() : curve[1].getLocation());
            resultEndNode.setTangentLeft(returnsAlignedNode ? curveAligned[1].getTangentLeft() : curve[1].getTangentLeft());
        }

        return new Pair<>(returnsAlignedNode ? curveAligned : curve, resultPoint);
    }

    /**
     * @param resultEndNode for set the end node.
     * @param transformResult for turn the result aligned nodes to absolute coordinate.
     * @return NodeData[] = {start, end};<p><code>Vector3f</code> is the end location and t of curve, returns null when no damaging.
     */
    public static Pair<NodeData[], Vector3f> spawnCurveBeam(CombatEngineAPI engine, @Nullable Vector3f startOffset, NodeData start, NodeData end, float maxCheckRange, DealtController dealtController, @Nullable NodeData resultEndNode, boolean returnsAlignedNode, @Nullable Matrix3f transformResult) {
        return spawnCurveBeam(engine, startOffset, start, end, maxCheckRange, dealtController, 11, 7, 1.0f, 1.0f, 1.0f, 0.25f, 0.5f, resultEndNode, returnsAlignedNode, transformResult);
    }

    /**
     * @param startOffset {loc.xy, angle}.<p> if set to <strong>null</strong>: all calculating will be based node data;<p> if set to <strong>not null</strong>: curve must be aligned to <strong>(0, 0) to positive x-axis</strong>, angle value must be <strong>[0, 360]</strong>.
     * @param stepFirstSearch for entity that without shield or bounds. Minimum step is 2, first searched points is <strong>stepPerSearch + 1</strong>.
     * @param stepPerSearch for entity that without shield or bounds. Minimum step is 2, each round searched points is <strong>stepPerSearch + 1</strong>.
     * @param thresholdSquared for entity that without shield or bounds. Search accuracy, lower is accurate but slower.
     * @param similarityThresholdSquared for shield of ship. Check two curves is similar then return p0 of start curve.
     * @param searchThresholdSquared for shield of ship. Search accuracy, lower is accurate but slower.
     * @param mergeThresholdSquared for shield of ship. Merge curve intersection points with distance less than this threshold.
     * @return <code>SegmentEntity</code> is without submit or add to rendering manager;<p><code>Vector3f</code> is the end location and t of curve, returns null when no damaging.
     */
    public static Pair<SegmentEntity, Vector3f> spawnCurveBeam(CombatEngineAPI engine, @Nullable Vector3f startOffset, NodeData start, NodeData end, float maxCheckRange, DealtController dealtController, int stepFirstSearch, int stepPerSearch, float thresholdSquared, float similarityThresholdSquared, float searchThresholdSquared, float mergeThresholdSquared, float splitFactorEachRound) {
        Matrix3f resultMat = new Matrix3f();
        Pair<NodeData[], Vector3f> result = spawnCurveBeam(engine, startOffset, start, end, maxCheckRange, dealtController, stepFirstSearch, stepPerSearch, thresholdSquared, similarityThresholdSquared, searchThresholdSquared, mergeThresholdSquared, splitFactorEachRound, null, true, resultMat);
        SegmentEntity resultEntity = new SegmentEntity();
        Matrix4f entityMatrix = new Matrix4f();
        entityMatrix.m30 = resultMat.m20;
        entityMatrix.m31 = resultMat.m21;
        entityMatrix.m00 = resultMat.m00;
        entityMatrix.m10 = resultMat.m10;
        entityMatrix.m01 = resultMat.m01;
        entityMatrix.m11 = resultMat.m11;
        resultEntity.addNode(result.one[0]);
        resultEntity.addNode(result.one[1]);
        resultEntity.setModelMatrix(entityMatrix);
        return new Pair<>(resultEntity, result.two);
    }

    /**
     * @param startOffset {loc.xy, angle}.<p> if set to <strong>null</strong>: all calculating will be based node data;<p> if set to <strong>not null</strong>: curve must be aligned to <strong>(0, 0) to positive x-axis</strong>, angle value must be <strong>[0, 360]</strong>.
     * @return <strong>SegmentEntity</strong> is without submit or add to rendering manager; <strong>Vector3f</strong> is the end location and t of curve, returns null when no damaging.
     */
    public static Pair<SegmentEntity, Vector3f> spawnCurveBeam(CombatEngineAPI engine, @Nullable Vector3f startOffset, NodeData start, NodeData end, float maxCheckRange, DealtController dealtController) {
        return spawnCurveBeam(engine, startOffset, start, end, maxCheckRange, dealtController, 11, 7, 1.0f, 1.0f, 1.0f, 0.25f, 0.5f);
    }

    /**
     * @param startOffset {loc.xy, angle}.<p> if set to <strong>null</strong>: all calculating will be based node data;<p> if set to <strong>not null</strong>: curve must be aligned to <strong>(0, 0) to positive x-axis</strong>, angle value must be <strong>[0, 360]</strong>.
     * @return <strong>SegmentEntity</strong> has been submitted and added to rendering manager; <strong>Vector3f</strong> is the end location and t of curve, returns null when no damaging.
     */
    public static Pair<SegmentEntity, Vector3f> spawnCurveBeam(CombatEngineAPI engine, @Nullable Vector3f startOffset, NodeData start, NodeData end, CombatEntityAPI source, float damageAmount, DamageType damageType, float empDamageAmount, boolean dealsSoftFlux, SpriteAPI core, Color coreColor, SpriteAPI fringe, Color fringeColor, float width, float textureSpeed, float fadeIn, float full, float fadeOut, CombatEngineLayers layer) {
        DealtController dealtController = new SimpleDealtController(engine, damageAmount, damageType, empDamageAmount, source, dealsSoftFlux, coreColor, fringeColor, full + fadeOut, width);
        Vector2f[] tmp = new Vector2f[]{start.getTangentRight(), end.getLocation(), end.getTangentLeft()};
        Vector2f minLoc = start.getLocation(), maxLoc = new Vector2f(minLoc);
        Vector2f.add(minLoc, tmp[0], tmp[0]);
        Vector2f.add(tmp[1], tmp[2], tmp[2]);
        for (Vector2f v : tmp) {
            CalculateUtil.min(minLoc, v, minLoc);
            CalculateUtil.max(maxLoc, v, maxLoc);
        }
        float maxRange = Vector2f.sub(maxLoc, minLoc, minLoc).length() * 0.66667f + 320.0f;
        Pair<SegmentEntity, Vector3f> result = spawnCurveBeam(engine, startOffset, start, end, maxRange, dealtController);
        result.one.setInterpolation((short) 32);
        result.one.setTexturePixels(512.0f);
        result.one.setTextureSpeed(textureSpeed);
        result.one.getMaterialData().setDiffuse(core);
        result.one.getMaterialData().setEmissive(fringe);
        result.one.setAdditiveBlend();
        for (NodeData node : result.one.getNodes()) {
            node.setColor(coreColor);
            node.setEmissiveColor(fringeColor);
            node.setWidth(width);
        }
        result.one.setNodeRefreshAllFromCurrentIndex();
        result.one.submitNodes();
        result.one.setLayer(layer);
        float smoothFactor = getCurveLength(result.one.getNodes().get(0), result.one.getNodes().get(1), 8);
        smoothFactor = Math.max(smoothFactor - 4.0f, 0.0f) / smoothFactor;
        result.one.setFillStartAlpha(0.0f);
        result.one.setFillStartFactor(smoothFactor);
        result.one.setFillEndAlpha(0.0f);
        if (result.two == null) result.one.setFillEndFactor(smoothFactor);
        result.one.setGlobalTimer(fadeIn, full, fadeOut);
        CombatRenderingManager.addEntity(result.one);
        return result;
    }

    private CurveUtil() {}
}
