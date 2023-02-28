package unit2Vol.panelBase;

import Tools.GeoTools;
import org.eclipse.collections.impl.bimap.mutable.HashBiMap;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.buffer.BufferOp;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import wblut.geom.*;
import wblut.hemesh.HEC_FromPolygons;
import wblut.hemesh.HE_Halfedge;
import wblut.hemesh.HE_Mesh;
import wblut.math.WB_Epsilon;

import java.util.*;

/**
 * 由若干个基础的Face组成的PanelBase
 *
 * @auther Alessio
 * @date 2022/12/13
 **/
public class MergedPanelBase extends PanelBase {
    private final List<Face> faceList;

    private Map<Face, Unit> faceUnitMap;

    public MergedPanelBase(List<Face> faceList) {
        this.faceList = faceList;
        init();
    }


    @Override
    public void init() {
        initInfo();
        initShape();
        initDir();
    }

    @Override
    public void initDir() {
        dir = faceList.get(0).getDir();
    }

    /**
     * 初始化几何形体
     */
    @Override
    public void initShape() {
        System.out.println("face list size " + faceList.size());
        if (faceList.size() == 1) {
            shape = faceList.get(0).getShape();
        } else {
            List<WB_Polygon> polygons = new LinkedList<>();
            faceList.forEach(e -> polygons.add(e.getShape()));

//------------------------各种合并方法的尝试----------------------------------
//            shape = unionPolygon(polygons);
//            shape = unionByMesh(polygons);
//            shape = union(polygons);

            shape = jtsUnion(polygons);
            System.out.println("area : " + shape.getSignedArea());
        }
    }

    @Deprecated
    private WB_Polygon unionPoly(List<WB_Polygon> polygons) {
        double epsilon = WB_Epsilon.EPSILON;

        System.out.println(epsilon);
        Map<WB_Coord, Integer> map = new HashMap<>();

        for (WB_Polygon polygon : polygons) {
            int size = polygon.getPoints().size();
            List<WB_Coord> wb_coords = polygon.getPoints().subList(0, size - 1);
            for (var coord : wb_coords) {
                if (map.containsKey(coord)) {
                    map.replace(coord, map.get(coord) + 1);
                } else {
                    map.put(coord, 1);
                }
            }
        }

        System.out.println(map.entrySet());
        System.out.println(map.entrySet().size());

        return new WB_Polygon();
    }

    /**
     * 合并相邻的空间几何图形
     * 仅适用于顶点相邻的情况
     *
     * @param polygons
     * @return
     */
    @Deprecated
    private WB_Polygon unionPolygon(List<WB_Polygon> polygons) {
        WB_Polygon base = polygons.get(0);

        List<WB_Coord> basePoints = base.getPoints().subList(0, 3);
        Set<WB_Coord> set = new HashSet<>(basePoints);

        for (int i = 1; i < polygons.size(); i++) {
            WB_Polygon p = polygons.get(i);
            List<WB_Coord> wb_coords = p.getPoints().toList();
            for (WB_Coord c : wb_coords) {
                if (set.contains(c)) {
                    set.remove(c);
                } else {
                    set.add(c);
                    System.out.println(c);
                }
            }
        }

        System.out.println("set" + set.size());
        return new WB_Polygon(new ArrayList<>(set));
    }

    // TODO: 2023/2/27 有时会存在点序问题

    /**
     * 合并相邻的空间几何图形
     * 仅适用于顶点相邻的情况
     *
     * @param polygons
     * @return
     */
    private WB_Polygon union(List<WB_Polygon> polygons) {
        double threshold = 0.1;
        List<WB_Coord> allCoords = new LinkedList<>();

        for (WB_Polygon polygon : polygons) {
            int size = polygon.getPoints().size();
            List<WB_Coord> wb_coords = polygon.getPoints().subList(0, size - 1);
            for (var coord : wb_coords) {
                List<WB_Coord> sameCoords = selectSameCoord(coord, allCoords, threshold);
                if (allCoords.size() == 0 || sameCoords.size() == 0) {
                    allCoords.add(coord);
                } else {
                    allCoords.removeAll(sameCoords);
                }
            }
        }

        return new WB_Polygon(allCoords);
    }

    private WB_Polygon jtsUnion(List<WB_Polygon> origin) {
        WB_Polygon result = origin.get(0);

        for (int i = 1; i < origin.size(); i++) {
            result = jtsTwoUnion(result, origin.get(i));
        }

        return result;
    }

    private WB_Polygon jtsTwoUnion(WB_Polygon p0, WB_Polygon p1) {
        Polygon g0 = GeoTools.WB_PolygonToJtsPolygon(p0);
        Polygon g1 = GeoTools.WB_PolygonToJtsPolygon(p1);

        double h0 = p0.getPoint(0).zd();
        double h1 = p1.getPoint(0).zd();
        //判断是否高度一样，不一样给出提示
        if (Math.abs(h0 - h1) > 1) {
            System.out.println("h " + Math.abs(h0 - h1));
            System.out.println("Faces not in same height");
        }

        Geometry buffer0 = g0.buffer(1, 0, BufferOp.CAP_BUTT);
        Geometry buffer1 = g1.buffer(1, 0, BufferOp.CAP_BUTT);

        Geometry union = buffer0.union(buffer1);
        Geometry boundary = union.getBoundary();
        Coordinate[] coordinates = boundary.getCoordinates();

        GeometryFactory gf = new GeometryFactory();
        Polygon polygon = gf.createPolygon(coordinates);

        WB_Polygon unionPoly = GeoTools.jtsPolygonToWB_Polygon(polygon);
        unionPoly = GeoTools.movePolygon3D(unionPoly, new WB_Point(0, 0, h0));

        return unionPoly;
    }

//    private List<WB_Coord> sortCoord(List<WB_Coord> origin) {
//        List<WB_Coord> result = new LinkedList<>();
//
//
//    }

    private WB_Polygon unionByMesh(List<WB_Polygon> polygons) {
        List<WB_Point> pts = new LinkedList<>();

        HEC_FromPolygons creator = new HEC_FromPolygons().setPolygons(polygons);
        HE_Mesh mesh = new HE_Mesh(creator);

        List<HE_Halfedge> allBoundaryHalfedges = mesh.getAllBoundaryHalfedges();

        Set<WB_Point> set = new HashSet<>();
        for (var edge : allBoundaryHalfedges) {
            WB_Point startPosition = edge.getStartPosition();
            WB_Point endPosition = edge.getEndPosition();

            set.add(startPosition);
            set.add(endPosition);
        }
        System.out.println(set);
        return new WB_Polygon(set);
//        return GeoTools.multiAlphaShape(polygons,1000,2000);
    }

    private List<WB_Coord> selectSameCoord(WB_Coord target, List<WB_Coord> coords, double threshold) {
        List<WB_Coord> result = new LinkedList<>();

        for (var coord : coords) {
            if (ifCoordEquals(target, coord, threshold)) {
                result.add(coord);
            }
        }

        return result;
    }

    private boolean ifCoordEquals(WB_Coord c1, WB_Coord c2, double threshold) {
        WB_Point p1 = (WB_Point) c1;
        WB_Point p2 = (WB_Point) c2;

        return Math.abs(p1.getDistance3D(p2)) < threshold;
    }


    /**
     * 初始化基础信息
     */
    @Override
    public void initInfo() {
        faceUnitMap = new HashBiMap<>();

        for (Face face : faceList) {
            faceUnitMap.put(face, face.getUnit());
        }

        building = faceList.get(0).getUnit().getBuilding();
    }

    public List<Face> getFaceList() {
        return faceList;
    }

    public Map<Face, Unit> getFaceUnitMap() {
        return faceUnitMap;
    }
}
