package unit2Vol;

import function.PosType;
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

    /**
     * 默认轴线居中
     */
    private PosType posType = PosType.Center;

    public Beam(WB_Segment segment) {
        this.segment = segment;
    }

    public Beam(WB_Segment segment, PosType posType) {
        this.segment = segment;
        this.posType = posType;
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

    public PosType getPosType() {
        return posType;
    }

    public void setPosType(PosType posType) {
        this.posType = posType;
    }
}
