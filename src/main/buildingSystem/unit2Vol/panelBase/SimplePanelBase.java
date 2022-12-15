package unit2Vol.panelBase;

import unit2Vol.face.Face;

/**
 * @auther Alessio
 * @date 2022/12/15
 **/
public class SimplePanelBase extends PanelBase {
    private final Face face;

    public SimplePanelBase(Face face) {
        this.face = face;

        init();
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
