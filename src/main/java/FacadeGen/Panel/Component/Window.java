package FacadeGen.Panel.Component;

import FacadeGen.Panel.PanelBase.Base;
import wblut.geom.WB_Polygon;

import java.awt.*;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public abstract class Window extends PanelComponent {

    //窗子外边框线
    public WB_Polygon frameBoundary;
    //边框宽度
    public double frameWidth;
    //边框深度
    public double frameDepth;

    //水平向分隔中心线位置(横向)
    public double[] horiBeamsPos;
    //水平向分隔宽度（在buffer的时候需要距离减半，双向buffer）
    public double horiBeamsWidth;
    //水平向分隔深度
    public double horiBeamsDepth;

    //竖直向分隔中心线位置（纵向）
    public double[] vertiBeamsPos;
    //竖直向分隔宽度（在buffer的时候需要距离减半，双向buffer）
    public double vertiBeamWidth;
    //竖直向分隔深度
    public double vertiBeamsDepth;

    //窗框
    public Frame frame;
    //横向分隔
    public WindowBeam horiBeam;
    //纵向分隔
    public WindowBeam vertiBeam;

    private WindowGeos windowGeos;


    public Window(WB_Polygon shape) {
        super(shape);
    }

    public Window(WB_Polygon shape, Base base) {
        super(shape, base);
    }

    public Window(WB_Polygon shape, Base base, int material) {
        super(shape, base, material);
    }

    public WindowBeam getHoriBeam() {
        return horiBeam;
    }

    public void setHoriBeam(WindowBeam horiBeam) {
        this.horiBeam = horiBeam;
    }

    public WindowBeam getVertiBeam() {
        return vertiBeam;
    }

    public void setVertiBeam(WindowBeam vertiBeam) {
        this.vertiBeam = vertiBeam;
    }

    public WindowGeos getWindowGeos() {
        return windowGeos;
    }

    public void setWindowGeos(WindowGeos windowGeos) {
        this.windowGeos = windowGeos;
    }


}
