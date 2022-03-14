package FacadeGen.Panel.Component;

import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Transform2D;
import wblut.geom.WB_Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public class WindowBeam {

    public static final int VERTICAL = 0, HORIZON = 1;
    private int type;
    private double[] pos;
    private double width;
    private double depth;
    private Window window;

    private List<WB_PolyLine> bLines;

    public WindowBeam(int type, double[] pos, double width, double depth) {
        this.type = type;
        this.pos = pos;
        this.width = width;
        this.depth = depth;
//        bLines = createBLines();
    }

    private List<WB_PolyLine> createBLines() {
        WB_Segment baseLine = new WB_Segment();
        WB_Segment dirLine = new WB_Segment();
        WB_Vector dir = null;
        //判断beam的方向
        //纵向分隔
        if (type == 0) {
            baseLine = window.getShape().toSegments().get(0);
            dirLine = window.getShape().toSegments().get(1);
            dir = dirLine.getNormal();
        }

        List<WB_PolyLine> res = new LinkedList<>();
        for (double d : pos) {
            WB_Transform2D transform2D = new WB_Transform2D();
            assert dir != null;
            transform2D.addTranslate2D(dir.mul(d * dirLine.getLength()));
            WB_Segment segment = baseLine.apply2D(transform2D);

            WB_PolyLine line = new WB_PolyLine(segment.getOrigin(), segment.getEndpoint());
            res.add(line);
        }

        return res;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double[] getPos() {
        return pos;
    }

    public void setPos(double[] pos) {
        this.pos = pos;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public List<WB_PolyLine> getbLines() {
        return bLines;
    }

    public void setbLines(List<WB_PolyLine> bLines) {
        this.bLines = bLines;
    }
}
