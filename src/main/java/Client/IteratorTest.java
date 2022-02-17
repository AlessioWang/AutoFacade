package Client;

import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/2/17
 **/

public class IteratorTest extends PApplet {
    public static void main(String[] args) {
        WB_Polygon[] ps = new WB_Polygon[3];
        ps[0] = new WB_Polygon();
        ps[1] = new WB_Polygon(new WB_Point(0,0,0));
        ps[2] = new WB_Polygon();
        Iterator<WB_Polygon> it = Arrays.stream(ps).iterator();

        System.out.println(it.next().getNumberOfPoints());
        System.out.println(it.hasNext());
        System.out.println(it.next().getNumberOfPoints());
    }

}
