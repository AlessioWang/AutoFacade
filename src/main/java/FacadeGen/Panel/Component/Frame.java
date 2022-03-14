package FacadeGen.Panel.Component;

import FacadeGen.Panel.PanelBase.BasicBase;
import FacadeGen.Unit.Unit;
import Tools.W_Tools;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public class Frame {

    public Window window;
    //窗子外边框线
    private WB_Polygon frameBoundary;

    //边框宽度
    private double frameWidth;
    //边框深度
    private double frameDepth;



    public Frame(Window window, double frameWidth, double frameDepth) {
        this.window = window;
        this.frameBoundary = window.getShape();
        this.frameWidth = frameWidth;
        this.frameDepth = frameDepth;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public WB_Polygon getFrameBoundary() {
        return frameBoundary;
    }

    public void setFrameBoundary(WB_Polygon frameBoundary) {
        this.frameBoundary = frameBoundary;
    }

    public double getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(double frameWidth) {
        this.frameWidth = frameWidth;
    }

    public double getFrameDepth() {
        return frameDepth;
    }

    public void setFrameDepth(double frameDepth) {
        this.frameDepth = frameDepth;
    }
}
