package unit2Vol.panelBase;

import Tools.GeoTools;
import unit2Vol.face.Face;
import wblut.geom.*;

import java.util.*;

/**
 * 用于处理转交的PanelBase
 * 由若干个位于转角处的Face组成
 *
 * @auther Alessio
 * @date 2022/12/28
 **/

// TODO: 2022/12/30 未完全解决转角的问题
public class EdgePanelBase extends PanelBase {
    //输入的faces
    private final List<Face> faceList;

    private List<WB_Vector> dirs;

    private WB_Segment edge;

    private HashMap<WB_Vector, List<Face>> dirFaceMap;

    /**
     * 用于创建面板的数据
     * 一般由六个点组成，中间两个点为转角公共边的点
     */
    private WB_Point[] basePts;

    public EdgePanelBase(List<Face> faceList) {
        this.faceList = faceList;
        init();
    }

    @Override
    public void init() {
        initMap();
        initE();
//        initEdge();
        initDir();
        initShape();
    }

    @Override
    public void initDir() {
        dirs = new LinkedList<>(dirFaceMap.keySet());
    }

    @Override
    public void initShape() {
        initBasePts();
    }

    @Override
    public void initInfo() {
        //初始化building信息
        building = faceList.get(0).getUnit().getBuilding();
    }

    /**
     * 初始化公共边
     */
    private void initEdge() {
        //记录出现的次数
        Map<WB_Segment, Integer> recorder = new HashMap<>();

        for (Face f : faceList) {
            WB_Polygon shape = f.getShape();
            List<WB_Segment> segments = shape.toSegments();

            for (WB_Segment s : segments) {
                System.out.println("s   ____ " + s);
                if (recorder.containsKey(s)) {
                    System.out.println("a");
                    recorder.put(s, recorder.get(s) + 1);
                } else recorder.put(s, 1);
            }
        }

        List<WB_Segment> allDup = new LinkedList<>();

        Set<Map.Entry<WB_Segment, Integer>> entries = recorder.entrySet();
        for (var entry : entries) {
            if (entry.getValue() == 2) {
                allDup.add(entry.getKey());
            }
        }

        if (allDup.size() != 1) {
            System.out.printf("The edge num is %s, check input faces%n", allDup.size());
        }

        edge = allDup.get(0);
    }

    private void initE() {
        Face face1 = faceList.get(0);
        Face face2 = faceList.get(1);

        WB_Polygon shape1 = face1.getShape();
        WB_Polygon shape2 = face2.getShape();

        List<WB_Segment> segments1 = shape1.toSegments();
        List<WB_Segment> segments2 = shape2.toSegments();

        for (WB_Segment seg1 : segments1) {
            for (WB_Segment seg2 : segments2) {
                if (segEqual(seg1, seg2, 10)) {
                    edge = seg1;
                    return;
                }
            }
        }
    }

    private boolean segEqual(WB_Segment s1, WB_Segment s2, double threshold) {
        WB_Coord originPoint1 = s1.getOrigin();
        WB_Coord endPoint1 = s1.getEndpoint();

        WB_Coord originPoint2 = s2.getOrigin();
        WB_Coord endPoint2 = s2.getEndpoint();

        return (GeoTools.getDistance3D((WB_Point) originPoint1, (WB_Point) endPoint2) < threshold &&
                GeoTools.getDistance3D((WB_Point) endPoint1, (WB_Point) originPoint2) < threshold) ||
                (GeoTools.getDistance3D((WB_Point) originPoint1, (WB_Point) originPoint2) < threshold &&
                        GeoTools.getDistance3D((WB_Point) endPoint1, (WB_Point) endPoint2) < threshold);
    }

    /**
     * 初始化用于创建面板的基点数组
     */
    private void initBasePts() {
        //只允许六个点（两个面）
        basePts = new WB_Point[6];

        Map<Face, WB_Coord[]> facePtsMap = new HashMap<>();
        List<WB_Coord> allPts = new LinkedList<>();

        for (Face f : faceList) {
            WB_CoordCollection points = f.getShape().getPoints();
            allPts.addAll(points.toList());
            WB_Coord[] wb_coords = points.toArray();
            facePtsMap.put(f, wb_coords);
        }

        Map<Double, LinkedHashSet<WB_Coord>> zCoordMap = new HashMap<>();
        for (WB_Coord c : allPts) {
            if (zCoordMap.containsKey(c.zd())) {
                zCoordMap.get(c.zd()).add(c);
            } else {
                LinkedHashSet<WB_Coord> coords = new LinkedHashSet<>();
                coords.add(c);
                zCoordMap.put(c.zd(), coords);
            }
        }

        Double[] zds = zCoordMap.keySet().toArray(new Double[0]);
        System.out.println("z num : " + zds.length);

        Double z0 = zds[0];
        Double z1 = zds[1];

        double min = Math.min(z0, z1);
        double max = Math.max(z0, z1);
        WB_Coord[] coordsMin = zCoordMap.get(min).toArray(new WB_Coord[0]);
        WB_Coord[] coordsMax = zCoordMap.get(max).toArray(new WB_Coord[0]);

        WB_Coord[] coords = new WB_Coord[6];
        if (!checkSegContainsCoord(edge, coordsMin[0], 10)) {
            coords[0] = coordsMin[0];
            coords[1] = checkSegContainsCoord(edge, coordsMin[1], 10) ? coordsMin[1] : coordsMin[2];
            coords[2] = coords[1].equals(coordsMin[1]) ? coordsMin[2] : coordsMin[1];

            coords[3] = coordsMax[2];
            coords[4] = checkSegContainsCoord(edge, coordsMax[0], 10) ? coordsMax[0] : coordsMax[1];
            coords[5] = coords[4].equals(coordsMax[0]) ? coordsMax[1] : coordsMax[0];
        } else {
            coords[0] = coordsMin[1];
            coords[1] = coordsMin[0];
            coords[2] = coordsMin[2];

            coords[3] = coordsMax[2];
            coords[4] = coordsMax[0];
            coords[5] = coordsMax[1];
        }

        for (int i = 0; i < coords.length; i++) {
            WB_Coord c = coords[i];
            basePts[i] = new WB_Point(c.xd(), c.yd(), c.zd());
        }

//        WB_Point basePt2 = basePts[2];
//        WB_Point basePt3 = basePts[3];
//
//        basePts[2] = new WB_Point(-basePt2.xd(), basePt2.yd(), basePt2.zd());
//        basePts[3] = new WB_Point(-basePt3.xd(), basePt3.yd(), basePt3.zd());

    }

    private boolean checkSegContainsCoord(WB_Segment segment, WB_Coord coord, double threshold) {
        WB_Coord endpoint = segment.getEndpoint();
        WB_Coord origin = segment.getOrigin();

        System.out.println(GeoTools.getDistance3D((WB_Point) coord, (WB_Point) endpoint) < threshold ||
                GeoTools.getDistance3D((WB_Point) coord, (WB_Point) origin) < threshold);
        return GeoTools.getDistance3D((WB_Point) coord, (WB_Point) endpoint) < threshold ||
                GeoTools.getDistance3D((WB_Point) coord, (WB_Point) origin) < threshold;
    }


    /**
     * 初始化dirFaceMap
     */
    private void initMap() {
        dirFaceMap = new HashMap<>();

        for (Face face : faceList) {
            WB_Vector dir = face.getDir();

            //如果vector未出现，则新建一个K,V来记录
            if (!dirFaceMap.containsKey(dir)) {
                dirFaceMap.put(dir, new LinkedList<>());
            }

            dirFaceMap.get(dir).add(face);
        }
    }

    /**
     * 获取若干个朝向的方向
     *
     * @return
     */
    public List<WB_Vector> getDirs() {
        return dirs;
    }

    public WB_Point[] getBasePts() {
        return basePts;
    }
}
