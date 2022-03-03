package FacadeGen.Panel.Component;

import FacadeGen.Panel.PanelBase.Base;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public class TianWindow extends Window {

    public TianWindow(WB_Polygon shape) {
        super(shape);
        iniComponent();
    }

    public TianWindow(WB_Polygon shape, Base base) {
        super(shape, base);
        iniComponent();
    }

    public TianWindow(WB_Polygon shape, Base base, int material) {
        super(shape, base, material);
        iniComponent();
    }

    private void iniBasicParas() {
        //边框宽度
        frameWidth = 50;
        //边框深度
        frameDepth = 70;

        //水平向分隔中心线位置(横向)
        horiBeamsPos = new double[]{0.5};
        //水平向分隔宽度（在buffer的时候需要距离减半，双向buffer）
        horiBeamsWidth = 20;
        //水平向分隔深度
        horiBeamsDepth = 20;

        //竖直向分隔中心线位置（纵向）
        vertiBeamsPos = new double[]{0.5};
        //竖直向分隔宽度（在buffer的时候需要距离减半，双向buffer）
        vertiBeamWidth = 10;
        //竖直向分隔深度
        vertiBeamsDepth = 10;
    }


    private void createFrame() {
        frame = new Frame(this, 30, 50);
    }

    private void createBeams() {
        horiBeam = new WindowBeam(1, horiBeamsPos, horiBeamsWidth, horiBeamsDepth);
        vertiBeam = new WindowBeam(0, vertiBeamsPos, vertiBeamWidth, vertiBeamsDepth);
    }

    private void iniComponent() {
        iniBasicParas();
        createFrame();
        createBeams();
    }


}
