package unit2Vol.panelBase;

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

        initWidth();
        initUnit();
    }

    public SimplePanelBase(Face face, WB_Polygon polygon) {
        this.face = face;
        shape = polygon;

        initInfo();
        initDir();

        initWidth();
        initUnit();
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

        /**
         * 一定程度上解决浮点误差问题
         * 权宜之计
         */
        if (Math.abs(dir.xd() - 0) < 0.01) {
            dir.setX(0);
        }

        if (Math.abs(dir.yd() - 0) < 0.01) {
            dir.setY(0);
        }

        if (Math.abs(dir.zd() - 0) < 0.01) {
            dir.setZ(0);
        }
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

    public void initUnit(){
        this.unit = face.getUnit();
    }
}
