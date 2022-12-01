package Tools;


import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.polygonize.Polygonizer;
import wblut.geom.*;

import java.util.*;

/**
 * @auther Alessio
 * @date 2020/12/24
 **/

public class GeoTools {
    private static GeometryFactory gf = new GeometryFactory();
    private static WB_GeometryFactory wbgf = new WB_GeometryFactory();

    /**
     * 2D计算两点距离
     *
     * @param a
     * @param b
     * @return
     */
    public static double getDistance2D(WB_Point a, WB_Point b) {
        return Math.sqrt((a.xd() - b.xd()) * (a.xd() - b.xd()) + (a.yd() - b.yd()) * (a.yd() - b.yd()));
    }

    /**
     * 3D计算两点距离
     *
     * @param a
     * @param b
     * @return
     */
    public static double getDistance3D(WB_Point a, WB_Point b) {
        return Math.sqrt((a.xd() - b.xd()) * (a.xd() - b.xd()) + (a.yd() - b.yd()) * (a.yd() - b.yd()) + (a.zd() - b.zd()) * (a.zd() - b.zd()));
    }

    /**
     * 以基点及长宽得到矩形
     *
     * @param pt
     * @param width
     * @param height
     * @return
     */
    public static WB_Polygon getRecByWidthHeight(WB_Point pt, double width, double height) {
        WB_Vector vWidth = new WB_Vector(width, 0);
        WB_Vector vHeight = new WB_Vector(0, height);
        WB_Point p1 = pt.add(vWidth);
        WB_Point p2 = pt.add(vHeight).add(vWidth);
        WB_Point p3 = pt.add(vHeight);
        return new WB_Polygon(pt, p1, p2, p3);
    }

    /**
     * 由基准线、偏移宽度以及偏移方向得到矩形
     *
     * @param segment
     * @param width
     * @param dir
     * @return
     */
    public static WB_Polygon getRecBySegAndWidth(WB_Segment segment, double width, WB_Vector dir) {
        WB_Point p0 = (WB_Point) segment.getOrigin();
        WB_Point p1 = (WB_Point) segment.getEndpoint();
        WB_Point p2 = p1.add(dir.mul(width));
        WB_Point p3 = p0.add(dir.mul(width));

        return new WB_Polygon(p0, p1, p2, p3);
    }

    /**
     * 计算长宽比
     *
     * @param polygon
     * @return
     */
    public static double getProportion(WB_Polygon polygon) {
        WB_AABB aabb = polygon.getAABB();
        return aabb.getWidth() / aabb.getHeight();
    }

    //p2指向p1的单位向量
    static public WB_Vector getUnitVector(WB_Point p1, WB_Point p2) {
        return p1.sub(p2).div(p1.getDistance(p2));
    }

    //polygon判断是否相交（相交是false，相交是true）
    public static boolean checkIntersection(WB_Polygon a, WB_Polygon b) {
        List<WB_Segment> as = a.toSegments();
        List<WB_Segment> bs = b.toSegments();
        for (WB_Segment s : as) {
            for (WB_Segment s1 : bs) {
                if (WB_GeometryOp2D.getIntersection2D(s, s1).intersection) return false;
            }
        }
        return true;
    }

    public static WB_Polygon getBuffer(WB_Polygon boundary, double dis) {
        List<WB_Polygon> list = wbgf.createBufferedPolygons(boundary, dis, 0);
        return list.get(0);
    }

    //数组翻转
    public static WB_Coord[] reserve(WB_Coord[] arr) {
        WB_Coord[] arr1 = new WB_Coord[arr.length];
        for (int x = 0; x < arr.length; x++) {
            arr1[x] = arr[arr.length - x - 1];
        }
        return arr1;
    }

    //Jts与Hemesh转化工具
    public static LineString WB_SegmentToJtsLineString(final WB_Segment seg) {
        Coordinate[] coords = new Coordinate[2];
        coords[0] = new Coordinate(seg.getOrigin().xd(), seg.getOrigin().yd(), seg.getOrigin().zd());
        coords[1] = new Coordinate(seg.getEndpoint().xd(), seg.getEndpoint().yd(), seg.getEndpoint().zd());
        return gf.createLineString(coords);
    }

    public static WB_PolyLine JtsLineStringToWB_PolyLine(final LineString p) {
        WB_Coord[] points = new WB_Point[p.getNumPoints()];
        for (int i = 0; i < p.getNumPoints(); i++) {
            points[i] = new WB_Point(p.getCoordinates()[i].x, p.getCoordinates()[i].y, p.getCoordinates()[i].z);
        }
        return new WB_PolyLine(points);
    }

    public static WB_Polygon JtsPolygonToWB_Polygon(final Polygon p) {
        WB_Coord[] points = new WB_Point[p.getNumPoints()];
        for (int i = 0; i < p.getNumPoints(); i++) {
            points[i] = new WB_Point(p.getCoordinates()[i].x, p.getCoordinates()[i].y, p.getCoordinates()[i].z);
        }
        return new WB_Polygon(points).getSimplePolygon();
    }

    public static LineString WB_polylineToJtsLinestring(WB_PolyLine wb_polyLine) {
        int num = wb_polyLine.getNumberOfPoints();
        var pts = wb_polyLine.getPoints().toList();
        Coordinate[] coordinates = new Coordinate[num];
        for (int i = 0; i < num; i++) {
            coordinates[i] = new Coordinate(pts.get(i).xd(), pts.get(i).yd());
        }
        return gf.createLineString(coordinates);
    }

    public static WB_Point wb_point2Coord(WB_Coord coord) {
        return new WB_Point(coord.xd(), coord.yd());
    }


    public static List<WB_Point> wb_pointsList2CoordList(List<WB_Coord> coords) {
        List<WB_Point> pts = new ArrayList<>();
        for (WB_Coord c : coords) {
            pts.add(wb_point2Coord(c));
        }
        return pts;
    }


    public static Polygon WB_PolygonToJtsPolygon(final WB_Polygon wbp) {
        if (wbp.getNumberOfHoles() == 0) {
            if (wbp.getPoint(0).equals(wbp.getPoint(wbp.getNumberOfPoints() - 1))) {
                Coordinate[] coords = new Coordinate[wbp.getNumberOfPoints()];
                for (int i = 0; i < wbp.getNumberOfPoints(); i++) {
                    coords[i] = new Coordinate(wbp.getPoint(i).xd(), wbp.getPoint(i).yd(), wbp.getPoint(i).zd());
                }
                return gf.createPolygon(coords);
            } else {
                Coordinate[] coords = new Coordinate[wbp.getNumberOfPoints() + 1];
                for (int i = 0; i < wbp.getNumberOfPoints(); i++) {
                    coords[i] = new Coordinate(wbp.getPoint(i).xd(), wbp.getPoint(i).yd(), wbp.getPoint(i).zd());
                }
                coords[wbp.getNumberOfPoints()] = coords[0];
                return gf.createPolygon(coords);
            }
        } else {
            // exterior
            List<Coordinate> exteriorCoords = new ArrayList<>();
            for (int i = 0; i < wbp.getNumberOfShellPoints(); i++) {
                exteriorCoords.add(new Coordinate(wbp.getPoint(i).xd(), wbp.getPoint(i).yd(), wbp.getPoint(i).zd()));
            }
            if (exteriorCoords.get(0).equals3D(exteriorCoords.get(exteriorCoords.size() - 1))) {
                exteriorCoords.add(exteriorCoords.get(0));
            }
            LinearRing exteriorLinearRing = gf.createLinearRing(exteriorCoords.toArray(new Coordinate[0]));

            // interior
            final int[] npc = wbp.getNumberOfPointsPerContour();
            int index = npc[0];
            LinearRing[] interiorLinearRings = new LinearRing[wbp.getNumberOfHoles()];
            for (int i = 0; i < wbp.getNumberOfHoles(); i++) {
                List<Coordinate> contour = new ArrayList<>();
                for (int j = 0; j < npc[i + 1]; j++) {
                    contour.add(new Coordinate(wbp.getPoint(index).xd(), wbp.getPoint(index).yd(), wbp.getPoint(index).zd()));
                    index++;
                }
                if (!contour.get(0).equals3D(contour.get(contour.size() - 1))) {
                    contour.add(contour.get(0));
                }
                interiorLinearRings[i] = gf.createLinearRing(contour.toArray(new Coordinate[0]));
            }

            return gf.createPolygon(exteriorLinearRing, interiorLinearRings);
        }
    }

    /**
     * 将Polygon转换为WB_Polygon（支持带洞）
     *
     * @param p input Polygon
     * @return wblut.geom.WB_Polygon
     */
    public static WB_Polygon jtsPolygonToWB_Polygon(final Polygon p) {
        if (p.getNumInteriorRing() == 0) {
            WB_Coord[] points = new WB_Point[p.getNumPoints()];
            for (int i = 0; i < p.getNumPoints(); i++) {
                points[i] = new WB_Point(p.getCoordinates()[i].x, p.getCoordinates()[i].y, p.getCoordinates()[i].z);
            }
            return new WB_Polygon(points).getSimplePolygon();
        } else {
            // exterior
            WB_Coord[] exteriorPoints = new WB_Point[p.getExteriorRing().getNumPoints()];
            for (int i = 0; i < p.getExteriorRing().getNumPoints(); i++) {
                exteriorPoints[i] = new WB_Point(p.getCoordinates()[i].x, p.getCoordinates()[i].y, p.getCoordinates()[i].z);
            }
            // interior
            int index = p.getExteriorRing().getNumPoints();
            WB_Coord[][] interiorHoles = new WB_Point[p.getNumInteriorRing()][];
            for (int i = 0; i < p.getNumInteriorRing(); i++) {
                LineString curr = p.getInteriorRingN(i);
                WB_Coord[] holePoints = new WB_Point[curr.getNumPoints()];
                for (int j = 0; j < curr.getNumPoints(); j++) {
                    WB_Point point = new WB_Point(curr.getCoordinates()[j].x, curr.getCoordinates()[j].y, curr.getCoordinates()[j].z);
                    holePoints[j] = point;
                }
                interiorHoles[i] = holePoints;
            }
            return new WB_Polygon(exteriorPoints, interiorHoles);
        }
    }

    /**
     * 缩短polyline
     *
     * @param lines
     * @param distance
     * @return
     */
    public static List<WB_PolyLine> getShortedPolylines(List<WB_PolyLine> lines, double distance) {
        List<WB_PolyLine> out = new ArrayList<>();
        for (WB_PolyLine l : lines) {
            WB_Vector v1 = getUnitVector(l.getPoint(0), l.getPoint(1));
            WB_Vector v2 = getUnitVector(l.getPoint(1), l.getPoint(0));
            WB_Point p1 = l.getPoint(0).add((v2).mul(distance));
            WB_Point p2 = l.getPoint(1).add((v1).mul(distance));
            WB_PolyLine newL = new WB_PolyLine(p1, p2);
            out.add(newL);
        }
        return out;
    }

    /**
     * 创建带洞多边形
     *
     * @param polygon
     * @param depth
     * @return
     */
    public static WB_Polygon getPolygonWithHoles(WB_Polygon polygon, double depth) {
        WB_Coord[] shell = polygonFaceDown(polygon).getPoints().toArray();   //边缘
        WB_Coord[][] holeCoordsList = null;
        List<WB_Polygon> holeList = wbgf.createBufferedPolygons(polygon, depth * (-1));
        for (int i = 0; i < holeList.size(); i++) {
            holeCoordsList = new WB_Coord[holeList.size()][holeList.get(i).getPoints().size()];    //创建洞的点集
            for (int j = 0; j < holeList.size(); j++) {
                WB_Coord[] hole = polygonFaceUp(holeList.get(j)).getPoints().toArray();
                holeCoordsList[j] = hole;
            }
        }
        return new WB_Polygon(wbgf.createPolygonWithHoles(shell, holeCoordsList));
    }

    /**
     * 创建带有多个洞的多边形
     *
     * @param base
     * @param holes
     * @return
     */
    public static WB_Polygon getPolygonWithHoles(WB_Polygon base, List<WB_Polygon> holes) {
        if (holes.isEmpty()) {
            return base;
        }

        WB_Coord[] shell = polygonFaceDown(base).getPoints().toArray();
        WB_Coord[][] holesList = new WB_Coord[holes.size()][];
        int i = 0;
        for (WB_Polygon hole : holes) {
            holesList[i] = polygonFaceUp(hole).getPoints().toArray();
            i++;
        }

        return new WB_Polygon(wbgf.createPolygonWithHoles(shell, holesList));
    }


    //线切割polygon，得到切割后的List<polygon>
    public static List<WB_Polygon> splitPolygonWithPolylineList(List<WB_Polygon> back, List<WB_PolyLine> cutters) {
        List<LineString> lines = new ArrayList<>();
        List<WB_Segment> allSegs = new ArrayList<>();
        List<WB_Polygon> output = new ArrayList<>();
        for (WB_PolyLine cutter : cutters) {
            for (int i = 0; i < cutter.getNumberSegments(); i++) {
                WB_Segment segment = cutter.getSegment(i);
                allSegs.add(segment);
            }
        }
        for (WB_Polygon p : back) {
            allSegs.addAll(p.toSegments());
        }
//        System.out.println("segNum : " + allSegs.size());
        for (WB_Segment segment : allSegs) {
            LineString lineString = WB_SegmentToJtsLineString(segment);
            lines.add(lineString);
        }
        Geometry noded = (LineString) lines.get(0);
        for (int i = 1; i < lines.size(); i++) {
            noded = noded.union((LineString) lines.get(i));
        }
//        System.out.println("lineNum : " + lines.size());
        Polygonizer polygonizer = new Polygonizer();
        polygonizer.add(noded);
        Collection polys = polygonizer.getPolygons();
//        System.out.println("polyNum : " + polys.size());
        for (Object poly : polys) {
            WB_Polygon wb_polygon = jtsPolygonToWB_Polygon((Polygon) poly);
            output.add(wb_polygon);
        }


        return output;
    }


    /**
     * WB_Polygon 点序反向（支持带洞）
     *
     * @param original input polygon
     * @return wblut.geom.WB_Polygon
     */
    public static WB_Polygon reversePolygon(final WB_Polygon original) {
        if (original.getNumberOfHoles() == 0) {
            WB_Point[] newPoints = new WB_Point[original.getNumberOfPoints()];
            for (int i = 0; i < newPoints.length; i++) {
                newPoints[i] = original.getPoint(newPoints.length - 1 - i);
            }
            return new WB_Polygon(newPoints);
        } else {
            WB_Point[] newExteriorPoints = new WB_Point[original.getNumberOfShellPoints()];
            for (int i = 0; i < original.getNumberOfShellPoints(); i++) {
                newExteriorPoints[i] = original.getPoint(original.getNumberOfShellPoints() - 1 - i);
            }

            final int[] npc = original.getNumberOfPointsPerContour();
            int index = npc[0];
            WB_Point[][] newInteriorPoints = new WB_Point[original.getNumberOfHoles()][];

            for (int i = 0; i < original.getNumberOfHoles(); i++) {
                WB_Point[] newHole = new WB_Point[npc[i + 1]];
                for (int j = 0; j < newHole.length; j++) {
                    newHole[j] = new WB_Point(original.getPoint(newHole.length - 1 - j + index));
                }
                newInteriorPoints[i] = newHole;
                index = index + npc[i + 1];
            }

            return new WB_Polygon(newExteriorPoints, newInteriorPoints);
        }
    }


    /**
     * 检查两个WB_Polygon是否同向
     *
     * @param p1 polygon1
     * @param p2 polygon2
     * @return boolean
     */
    public static boolean isNormalEquals(final WB_Polygon p1, final WB_Polygon p2) {
        return p1.getNormal().equals(p2.getNormal());
    }

    /**
     * 让WB_Polygon法向量朝向Z轴正向（支持带洞）
     *
     * @param polygon input polygon
     * @return wblut.geom.WB_Polygon
     */
    public static WB_Polygon polygonFaceUp(final WB_Polygon polygon) {
        if (polygon.getNormal().zd() < 0) {
            return reversePolygon(polygon);
        } else {
            return polygon;
        }
    }

    /**
     * 让WB_Polygon法向量朝向Z轴负向（支持带洞）
     *
     * @param polygon input polygon
     * @return wblut.geom.WB_Polygon
     */
    public static WB_Polygon polygonFaceDown(final WB_Polygon polygon) {
        if (polygon.getNormal().zd() > 0) {
            return reversePolygon(polygon);
        } else {
            return polygon;
        }
    }


    //选择polygons在多边形外
    public static List<WB_Polygon> selPolygonsInRingByCenter(WB_Polygon ring, List<WB_Polygon> polygons) {
        List<WB_Polygon> selPolygons = new ArrayList<>();
        for (WB_Polygon polygon : polygons) {
            WB_Point point = polygon.getCenter();
            if (!WB_GeometryOp.contains2D(point, ring)) {
                selPolygons.add(polygon);
            }
        }
        return selPolygons;
    }

    //选择polygons在多边形外
    public static List<WB_Polygon> selPolygonsInRingByPoint0(WB_Polygon ring, List<WB_Polygon> polygons) {
        List<WB_Polygon> selPolygons = new ArrayList<>();
        for (WB_Polygon polygon : polygons) {
            if (polygon.getNumberSegments() > 2) {
//                System.out.println("pointInPolygon : " + polygon.getPoint(1));
//                System.out.println("pointsNumInPolygon : " + polygon.getNumberOfPoints());
                WB_Polygon poly = wbgf.createBufferedPolygons(polygon, -1).get(0);
                WB_Point point = poly.getPoint(0);
                if (!WB_GeometryOp.contains2D(point, ring)) {
                    selPolygons.add(polygon);
                }
            }
        }
        return selPolygons;
    }

    /**
     * 比较两个点是否是在同一个位置
     *
     * @param p1
     * @param p2
     * @param tol
     * @return
     */
    public static boolean comparePts(WB_Point p1, WB_Point p2, double tol) {
        double p1x = p1.xd();
        double p1y = p1.yd();
        double p2x = p2.xd();
        double p2y = p2.yd();
        boolean xEqu = Math.abs(p1x - p2x) <= tol;
        boolean yEqu = Math.abs(p1y - p2y) <= tol;
        return xEqu && yEqu;
    }

    /**
     * 比较两个点是否是在同一个位置
     *
     * @param p1
     * @param p2
     * @param tol
     * @return
     */
    public static boolean comparePts(WB_Coord p1, WB_Coord p2, double tol) {
        double p1x = p1.xd();
        double p1y = p1.yd();
        double p2x = p2.xd();
        double p2y = p2.yd();
        boolean xEqu = Math.abs(p1x - p2x) <= tol;
        boolean yEqu = Math.abs(p1y - p2y) <= tol;
        return xEqu && yEqu;
    }


    /**
     * 比较两个点是否是在同一个位置
     *
     * @param p1
     * @param p2
     * @return
     */
    public static boolean comparePts(WB_Coord p1, WB_Coord p2) {
        double p1x = p1.xd();
        double p1y = p1.yd();
        double p2x = p2.xd();
        double p2y = p2.yd();
        boolean xEqu = Math.abs(p1x - p2x) <= 0.01;
        boolean yEqu = Math.abs(p1y - p2y) <= 0.01;
        return xEqu && yEqu;
    }

    /**
     * 找指定segment的下一根segment
     * 下一根segment的方向与初始尽量一致
     *
     * @param origin
     * @param segments
     * @return
     */
    public static WB_Segment selNextLine(WB_Segment origin, List<WB_Segment> segments) {
        WB_Vector oriVec = new WB_Vector(origin.getOrigin(), origin.getEndpoint());

        WB_Segment tempSeg = segments.get(0);
        WB_Vector tempVec = new WB_Vector(tempSeg.getOrigin(), tempSeg.getEndpoint());
        double tempAngle = calAngle(oriVec, tempVec);

        for (WB_Segment segment : segments) {
            WB_Vector vec = (WB_Vector) segment.getDirection();
            double angle = calAngle(oriVec, vec);
            if (angle < tempAngle) {
                tempSeg = segment;
            }
        }

        return tempSeg;
    }

    public static WB_Coord[] selNextLineCoords(WB_Coord[] originCoord, List<WB_Coord[]> segmentCoords) {
        WB_Segment origin = new WB_Segment(originCoord[0], originCoord[1]);
        List<WB_Segment> segments = new ArrayList<>();

        for (WB_Coord[] seg : segmentCoords) {
            WB_Segment segment = new WB_Segment(seg[0], seg[1]);
            segments.add(segment);
        }

        WB_Segment target = selNextLine(origin, segments);

        return new WB_Coord[]{target.getOrigin(), target.getEndpoint()};
    }


    /**
     * 计算两个向量之间的角度，取0-Pi， 去掉向量之间的顺序的影响
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double calAngle(WB_Vector v1, WB_Vector v2) {
        double x1 = v1.xd();
        double y1 = v1.yd();
        double x2 = v2.xd();
        double y2 = v2.yd();
        double value = (x1 * x2 + y1 * y2) / (Math.sqrt(x1 * x1 + y1 * y1) * Math.sqrt(x2 * x2 + y2 * y2)); // 余弦值
        return Math.acos(value);
    }


    /**
     * 从一大组线段中挑出来连续的polyline
     *
     * @param coordsList
     * @return
     */
    public static List<List<WB_Coord>> selPolylineFromCoordList(List<WB_Coord[]> coordsList) {
        List<List<WB_Coord>> target = new ArrayList<>();

        List<WB_Coord> polyline = new ArrayList<>();
        polyline.add(coordsList.get(0)[0]);
        polyline.add(coordsList.get(0)[1]);
        target.add(polyline);

        for (int i = 0; i < coordsList.size() - 1; i++) {
            WB_Coord[] originLine = coordsList.get(i);
            WB_Coord formerEnd = originLine[1];
            //备选的与上一根线首尾相连的line
            List<WB_Coord[]> nextLines = new ArrayList<>();

            for (WB_Coord[] line : coordsList) {
                WB_Coord start = line[0];
                if (GeoTools.comparePts(formerEnd, start, 0.1)) {
                    nextLines.add(line);
                }
            }

            if (nextLines.size() > 0) {
                WB_Coord[] nextSelLine = GeoTools.selNextLineCoords(originLine, nextLines);
                target.get(target.size() - 1).add(nextSelLine[1]);
                System.out.println("add point");
            } else {
                target.add(new ArrayList<>());
                target.get(target.size() - 1).add(coordsList.get(i + 1)[0]);
                target.get(target.size() - 1).add(coordsList.get(i + 1)[1]);
                System.out.println("new polyline");
            }
        }
        return target;
    }

    /**
     * 创建四边形的二维wb_polygon
     * 基点在左下角，基点为（0,0）
     *
     * @param width
     * @param height
     * @return
     */
    public static WB_Polygon createRecPolygon(double width, double height) {
        WB_Point p0 = new WB_Point(0, 0);
        WB_Point p1 = new WB_Point(width, 0);
        WB_Point p2 = new WB_Point(width, height);
        WB_Point p3 = new WB_Point(0, height);
        WB_Point p4 = new WB_Point(0, 0);
        return new WB_Polygon(p0, p1, p2, p3, p4);
    }


    /**
     * 检测两个二维的wb_polygon是否相连
     * 涉及较为复杂几何运算，有时会出现浮点误差与计算时间过长等问题
     * 若输入的list中仅含有一个元素，输出为true，但会生成提示
     *
     * @param polygons
     * @return
     */
    public static boolean checkIfNeighbor2D(List<WB_Polygon> polygons) {
        if (polygons.size() < 2) {
            System.out.println("仅存在一个图元");
            return true;
        }

        WB_Polygon origin = polygons.get(0);
        List<WB_Polygon> result;
        for (int i = 1; i < polygons.size(); i++) {
            result = wbgf.unionPolygons2D(origin, polygons.get(i));
            if (result.size() > 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检测两个二维的wb_polygon是否相连
     * 涉及较为复杂几何运算，有时会出现浮点误差与计算时间过长等问题
     * 若输入的list中仅含有一个元素，输出为true，但会生成提示
     *
     * @return
     */
    public static boolean checkIfNeighbor2D(WB_Polygon p1, WB_Polygon p2) {
        List<WB_Polygon> result = wbgf.unionPolygons2D(p1, p2);
        return result.size() == 1;
    }

    public static WB_Polygon unionPolygons(WB_Polygon p1, WB_Polygon p2) {
        if (checkIfNeighbor2D(p1, p2)) {
            System.out.println("多边形不相邻，无法合并");
            return null;
        }
        List<WB_Polygon> result = wbgf.unionPolygons2D(p1, p2);
        return result.get(0);
    }

    /**
     * 将wb_polygon进行合并
     *
     * @param ps
     * @return
     */
    public static List<WB_Polygon> unionPolygons(WB_Polygon... ps) {
        if (ps.length == 1) {
            return Collections.singletonList(ps[0]);
        }

        List<WB_Polygon> result = new LinkedList<>();
        Iterator<WB_Polygon> it = Arrays.stream(ps).iterator();
        it.next();
        while (it.hasNext()) {
            result = wbgf.unionPolygons2D(ps[0], it.next());
        }

        return result;
    }

    public static WB_Polygon movePolygonNew(WB_Polygon origin, WB_Point pos) {
        WB_Transform2D transform2D = new WB_Transform2D();
        transform2D.addTranslate2D(pos);
        return (WB_Polygon) origin.apply2D(transform2D);
    }

    public static WB_Polygon movePolygon(WB_Polygon origin, WB_Point pos) {
        WB_Transform2D transform2D = new WB_Transform2D();
        transform2D.addTranslate2D(pos);
        return (WB_Polygon) origin.apply2D(transform2D);
    }

    public static WB_Polygon movePolygon3D(WB_Polygon origin, WB_Point pos) {
        WB_Transform3D transform3D = new WB_Transform3D();
        transform3D.addTranslate(pos);
        return origin.apply(transform3D);
    }

    public static WB_PolyLine movePolyline(WB_PolyLine origin, WB_Point pos) {
        WB_Transform2D transform2D = new WB_Transform2D();
        transform2D.addTranslate2D(pos);
        return origin.apply2D(transform2D);
    }

    /**
     * 将基准图元三维变换,矢量方向以Z轴位基准
     *
     * @param origin
     * @param pos
     * @param dir
     * @return
     */
    public static WB_Polygon transferPolygon3DByZ(WB_Polygon origin, WB_Point pos, WB_Vector dir) {
        WB_Transform3D transform3D = new WB_Transform3D();
        WB_CoordinateSystem system = new WB_CoordinateSystem();
        system.setOrigin(pos);
        system.setZ(dir);
        transform3D.addFromWorldToCS(system);
        return origin.apply(transform3D);
    }

    /**
     * 将基准图元三维变换,矢量方向以X轴位基准
     *
     * @param origin
     * @param pos
     * @param dir
     * @return
     */
    public static WB_Polygon transferPolygon3DByX(WB_Polygon origin, WB_Point pos, WB_Vector dir) {
        WB_Transform3D transform3D = new WB_Transform3D();
        WB_CoordinateSystem system = new WB_CoordinateSystem();
        system.setOrigin(pos.mul(-1));
        system.setX(dir);
        transform3D.addFromWorldToCS(system);
        return origin.apply(transform3D);
    }

    /**
     * 将基准图元三维变换,以Z轴为基准
     *
     * @param origin
     * @param pos
     * @param dir
     * @return
     */
    public static WB_PolyLine transferPolyline3DByZ(WB_PolyLine origin, WB_Point pos, WB_Vector dir) {
        WB_Transform3D transform3D = new WB_Transform3D();
        WB_CoordinateSystem system = new WB_CoordinateSystem();
        system.setOrigin(pos);
        system.setZ(dir);
        transform3D.addFromWorldToCS(system);
        return origin.apply(transform3D);
    }

    /**
     * 判断geo是否在shell内部
     * 只适用于二维
     *
     * @param shell
     * @param geo
     * @return
     */
    public static boolean ifPolyCoverPoly2D(WB_Polygon shell, WB_PolyLine geo) {
        Polygon jtsShell = WB_PolygonToJtsPolygon(shell);
        LineString lineString = WB_polylineToJtsLinestring(geo);
        return jtsShell.covers(lineString);
    }

    /**
     * 检测点在不在polygon内
     * 只适用于二维
     *
     * @param shell
     * @param pt
     * @return
     */
    public static boolean ifPolyCoverPt2D(WB_Polygon shell, WB_Point pt) {
        Polygon jtsShell = WB_PolygonToJtsPolygon(shell);
        Point jtsPoint = gf.createPoint(new Coordinate(pt.xd(), pt.yd(), pt.zd()));
        return jtsShell.covers(jtsPoint);
    }


    /**
     * 将多个polyLine沿着一个方向移动一定的长度
     *
     * @param polygons
     * @param v
     * @return
     */
    public static List<? extends WB_PolyLine> moveMultiPolys(List<? extends WB_PolyLine> polygons, WB_Vector v) {
        List<WB_PolyLine> result = new LinkedList<>();
        for (WB_PolyLine p : polygons) {
            WB_Transform2D transform2D = new WB_Transform2D();
            transform2D.addTranslate2D(v.mul(-1));

            p = p instanceof WB_Polygon ? ((WB_Polygon) p) : p;
            var poly = p.apply2D(transform2D);
            result.add(poly);
        }
        return result;
    }

}



















