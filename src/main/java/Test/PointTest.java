package Test;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;

/**
 * @auther Alessio
 * @date 2022/6/22
 **/
public class PointTest {
    public static void main(String[] args) {
        WB_PolyLine polyLine = new WB_PolyLine(new WB_Point(0, 0), new WB_Point(10, 0),new WB_Point(20,0));
        System.out.println(polyLine.getNumberOfPoints());
    }

}
