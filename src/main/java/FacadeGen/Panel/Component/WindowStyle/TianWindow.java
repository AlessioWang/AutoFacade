package FacadeGen.Panel.Component.WindowStyle;

import FacadeGen.Panel.Component.*;
import FacadeGen.Panel.PanelBase.Base;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public class TianWindow extends Window {

    public TianWindow(WB_Polygon shape, Base base) {
        super(shape, base);
        iniComponent();

        WindowGeos g = new WindowGeos(this);
        setWindowGeos(g);
    }

    @Override
    public void iniBasicParas() {
        //边框宽度
        frameWidth = 30;
        //边框深度
        frameDepth = 70;

        //水平向分隔中心线位置(横向)
        horiBeamsPos = new double[]{0.2, 0.5, 0.8};
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

    @Override
    public void createBeams() {
        horiBeam = new WindowBeam(this, 1, horiBeamsPos, horiBeamsWidth, horiBeamsDepth);
        vertiBeam = new WindowBeam(this, 0, vertiBeamsPos, vertiBeamWidth, vertiBeamsDepth);
    }

    @Override
    public void iniFrame() {
        frame = new Frame(this, frameWidth, frameDepth);
    }

    @Override
    public void iniGlass() {
        glass = new Glass(this);
    }

    @Override
    public void iniComponent() {
        iniBasicParas();
        iniFrame();
        iniGlass();
        createBeams();
    }


}
