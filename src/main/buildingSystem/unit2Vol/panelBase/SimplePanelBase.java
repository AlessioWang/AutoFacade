package unit2Vol.panelBase;

import org.aspectj.org.eclipse.jdt.internal.compiler.parser.TheOriginalJDTScannerClass;
import unit2Vol.face.Face;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/12/15
 **/
public class SimplePanelBase extends PanelBase {
    private Face face;

    public SimplePanelBase(Face face) {
        this.face = face;

        init();
    }

    public SimplePanelBase (Face face, WB_Polygon polygon){
        this.face = face;
        shape = polygon;

        initInfo();
        initDir();
    }


    @Override
    public void init() {
        initInfo();
        initShape();
        initDir();
    }

    @Override
    public void initDir() {
        dir = face.getDir();
    }

    @Override
    public void initShape() {
        shape = face.getShape();
    }

    @Override
    public void initInfo() {
        building = face.getUnit().getBuilding();
    }

    public Face getFace() {
        return face;
    }
}
