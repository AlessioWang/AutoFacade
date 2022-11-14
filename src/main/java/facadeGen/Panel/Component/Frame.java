package facadeGen.Panel.Component;

import Tools.GeoTools;
import wblut.geom.WB_Polygon;

/**
 *
 * @auther Alessio
 * @date 2022/3/3
 **/
public class Frame {

    public Window window;
    //窗子外边框线
    private WB_Polygon frameBoundary;
    //窗子内边线
    private final WB_Polygon innerBoundary;

    //边框宽度
    private double frameWidth;
    //边框深度
    private double frameDepth;

    public Frame(Window window, double frameWidth, double frameDepth) {
        this.window = window;
        this.frameBoundary = window.getShape();
        this.frameWidth = frameWidth;
        this.frameDepth = frameDepth;
        innerBoundary = calInnerPoly();
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    private WB_Polygon calInnerPoly() {
        return GeoTools.getBuffer(frameBoundary, -frameWidth);
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

    public WB_Polygon getInnerBoundary() {
        return innerBoundary;
    }
}
