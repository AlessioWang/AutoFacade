package Test;

import Tools.GeoTools;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import wblut.geom.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2023/3/1
 **/
public class TransformTest {

    public static void main(String[] args) {
        new TransformTest();
    }

    WB_Polygon origin;

    WB_Polygon other;

    public TransformTest() {
        initPoly();

        op();
    }

    private void initPoly() {
        WB_Point p0 = new WB_Point(0, 0, 0);
        WB_Point p1 = new WB_Point(0, 10, 0);
        WB_Point p2 = new WB_Point(0, 10, 5);
        WB_Point p3 = new WB_Point(0, 0, 5);
        List<WB_Point> pts1 = new LinkedList<>();
        pts1.add(p0);
        pts1.add(p1);
        pts1.add(p2);
        pts1.add(p3);
        origin = new WB_Polygon(pts1);

        WB_Point pp0 = new WB_Point(0, 3, 0);
        WB_Point pp1 = new WB_Point(0, 5, 0);
        WB_Point pp2 = new WB_Point(0, 5, 5);
        WB_Point pp3 = new WB_Point(0, 3, 5);
        List<WB_Point> pts2 = new LinkedList<>();
        pts2.add(pp0);
        pts2.add(pp1);
        pts2.add(pp2);
        pts2.add(pp3);
        other = new WB_Polygon(pts2);
    }

    private WB_CoordinateSystem createCsByPolygon(WB_Polygon polygon) {
        WB_Point origin = polygon.getPoint(0);
        WB_Point x = polygon.getPoint(1);
        WB_Point y = polygon.getPoint(3);

        WB_Vector vx = new WB_Vector(origin, x);
        WB_Vector vy = new WB_Vector(origin, y);
        WB_Vector vz = vy.cross(vx);

        vx.normalizeSelf();
        vy.normalizeSelf();
        vz.normalizeSelf();

        return new WB_CoordinateSystem(origin, vx, vy, vz);
    }

    private WB_CoordinateSystem createWorldCS() {
        WB_Point origin = new WB_Point(0, 0, 0);
        WB_Vector vx = new WB_Vector(1, 0, 0);
        WB_Vector vy = new WB_Vector(0, 1, 0);
        WB_Vector vz = new WB_Vector(0, 0, 1);

        return new WB_CoordinateSystem(origin, vx, vy, vz);
    }

    private void op() {
        WB_CoordinateSystem world = createWorldCS();

        WB_Transform3D transform3D = new WB_Transform3D();

        transform3D.addFromCSToCS(world, createCsByPolygon(origin));
        WB_Polygon originAfter = origin.apply(transform3D);
        WB_Polygon otherAfter = other.apply(transform3D);

        System.out.println("origin " + Arrays.toString(origin.getPoints().toArray()));
        System.out.println("originAfter " + Arrays.toString(originAfter.getPoints().toArray()));
        System.out.println("otherAfter " + Arrays.toString(otherAfter.getPoints().toArray()));

        difference(originAfter, otherAfter);

        WB_Transform3D back = new WB_Transform3D();
        back.addFromCSToCS(createCsByPolygon(origin), world);
        WB_Polygon originBack = originAfter.apply(back);
        WB_Polygon otherBack = otherAfter.apply(back);
        System.out.println("originBack " + Arrays.toString(originBack.getPoints().toArray()));
        System.out.println("otherBack " + Arrays.toString(otherBack.getPoints().toArray()));
    }

    private void difference(WB_Polygon origin, WB_Polygon other) {
        Polygon originJts = GeoTools.WB_PolygonToJtsPolygon(origin);
        Polygon otherJts = GeoTools.WB_PolygonToJtsPolygon(other);

        Geometry difference = originJts.difference(otherJts);
        Coordinate[] coordinates = difference.getCoordinates();

        int numGeometries = difference.getNumGeometries();
        System.out.println(numGeometries);

        Geometry geometryN = difference.getGeometryN(0);
        System.out.println(geometryN);
        System.out.println(Arrays.toString(coordinates));
    }

}
