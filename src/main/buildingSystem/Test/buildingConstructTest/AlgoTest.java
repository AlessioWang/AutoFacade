package Test.buildingConstructTest;

import Tools.GeoTools;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/12/2
 **/
public class AlgoTest {

    public static void main(String[] args) {
        WB_Point p1 = new WB_Point(0, 0, 10);
        WB_Point p2 = new WB_Point(2, 1, 10);
        WB_Point p3 = new WB_Point(2, 2, 10);
        WB_Point p4 = new WB_Point(2, 4, 10);

        WB_Polygon polygon = new WB_Polygon(p1, p2, p3, p4);

        WB_Point pt0 = new WB_Point(0, 0, 100);
        WB_Point pt1 = new WB_Point(6, 5, 1);
        WB_Point pt2 = new WB_Point(3, 3, 1);

        System.out.println(GeoTools.ifPolyCoverPt3D(polygon, pt0));
        System.out.println(GeoTools.ifPolyCoverPt3D(polygon, pt1));
        System.out.println(GeoTools.ifPolyCoverPt3D(polygon, pt2));

    }

}
