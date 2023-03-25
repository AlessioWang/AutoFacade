package unit2Vol.panelBase;

import unit2Vol.face.Face;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * 目前仅考虑竖直分割的情况
 * 水平分隔的情况根据需求再决定是否要合并实现
 *
 * @auther Alessio
 * @date 2022/12/15
 **/
public class SplitPanelBase extends PanelBase {

    private final Face face;

    //垂直分割位置
    private double[] verticalSplit;

    private List<WB_Polygon> shapes;

    //记录一个face分隔出来的SimplePanelBase
    private List<SimplePanelBase> panelBases;

    public SplitPanelBase(Face face, double[] verticalSplit) {
        this.face = face;
        this.verticalSplit = verticalSplit;

        init();
    }

    @Override
    public void init() {
        initDir();
        initShape();
        initInfo();

        initPanelBase();
    }

    @Override
    public void initDir() {
        dir = face.getDir();
    }

    @Override
    public void initShape() {
        shapes = getShapes();
    }

    @Override
    public void initInfo() {
        building = face.getUnit().getBuilding();
    }

    private void initPanelBase() {
        panelBases = new LinkedList<>();

        for (WB_Polygon p : shapes) {
            panelBases.add(new SimplePanelBase(face, p));
        }
    }

    private List<WB_Polygon> getShapes() {
        List<WB_Segment> segments = face.getShape().toSegments();

        WB_Segment segment0 = segments.get(0);
        WB_Segment segment1 = segments.get(1);

        WB_Segment vertiSeg = segment1;
        WB_Segment horiSeg = segment0;

        //筛选水平与竖直的segment
        if (segment0.getDirection().zd() == 0) {
            vertiSeg = segment0;
            horiSeg = segment1;
        }

        WB_Point origin = face.getShape().getPoint(0);
        WB_Point end = face.getShape().getPoint(3);
        WB_Vector vertiVec = new WB_Vector(vertiSeg.getOrigin(), vertiSeg.getEndpoint());
        WB_Vector horiVec = new WB_Vector(horiSeg.getEndpoint(), horiSeg.getOrigin());

        WB_Point tempOrigin = origin;
        WB_Point tempEnd = end;
        List<WB_Polygon> result = new LinkedList<>();

        for (double pos : verticalSplit) {
            WB_Point p0 = origin.add(vertiVec.mul(pos));
            WB_Point p1 = p0.add(horiVec);
            result.add(new WB_Polygon(tempOrigin, p0, p1, tempEnd));

            tempOrigin = p0;
            tempEnd = p1;
        }

        //加入最后一个的polygon
        WB_Point p1 = face.getShape().getPoint(1);
        WB_Point p2 = face.getShape().getPoint(2);
        result.add(new WB_Polygon(tempOrigin, p1, p2, tempEnd));


        return result;
    }

    public List<SimplePanelBase> getPanelBases() {
        return panelBases;
    }
}
