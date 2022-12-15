package unit2Vol.panelBase;

import unit2Vol.face.Face;
import unit2Vol.face.RndFace;
import wblut.geom.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/12/15
 **/
public class SplitPanelBase extends PanelBase {

    private final Face face;

    //垂直分割位置
    private double[] verticalSplit;

    //水平分割位置
    private double[] horizonSplit;

    private List<WB_Polygon> shapes;

    private List<Face> faces;

    private List<SimplePanelBase> panelBases;

    public SplitPanelBase(Face face, double[] verticalSplit, double[] horizonSplit) {
        this.face = face;
        this.verticalSplit = verticalSplit;
        this.horizonSplit = horizonSplit;
    }

    @Override
    public void init() {

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
        faces = new LinkedList<>();
        panelBases = new LinkedList<>();

        for (WB_Polygon p : shapes) {
        }
    }

    private List<WB_Polygon> getShapes() {
        List<WB_Segment> segments = face.getShape().toSegments();

        WB_Segment segment0 = segments.get(0);
        WB_Segment segment1 = segments.get(1);

        WB_Segment vertiSeg = segment0;
        WB_Segment horiSeg = segment1;

        if (segment0.getDirection().zd() == 0) {
            vertiSeg = segment1;
            horiSeg = segment0;
        }

        WB_Point origin = face.getShape().getPoint(0);
        WB_Point end = face.getShape().getPoint(3);
        WB_Vector vertiVec = new WB_Vector(vertiSeg.getOrigin(), vertiSeg.getEndpoint());
        WB_Vector horiVec = new WB_Vector(horiSeg.getOrigin(), horiSeg.getEndpoint());

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

        return result;
    }
}
