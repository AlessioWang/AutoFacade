package Test;

import Tools.GeoTools;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/3/15
 **/
public class ToolTest {
    public static void main(String[] args) {
        List<WB_Point> pts = new LinkedList<>();
        pts.add(new WB_Point(0,0,0));
        pts.add(new WB_Point(30,0,0));
        pts.add(new WB_Point(30,100,0));
        pts.add(new WB_Point(0,100,0));
        WB_Polygon p = new WB_Polygon(pts);

//        WB_Polygon polygon = GeoTools.createRecPolygon(100,200);
        System.out.println(GeoTools.getPolygonWithHoles(p, 5).getPoint(7));
    }
}
