package unit2Vol;

import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Segment;

/**
 * 梁的信息
 *
 * @auther Alessio
 * @date 2023/3/7
 **/
public class Beam {

    private WB_Segment segment;

    private double width = 600;

    private double height = 1000;

    public Beam(WB_Segment segment) {
        this.segment = segment;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public WB_Segment getSegment() {
        return segment;
    }
}
