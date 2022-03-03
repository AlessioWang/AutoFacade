package FacadeGen.Panel.Component;

import Tools.W_Tools;
import wblut.geom.WB_Polygon;

import java.util.List;

/**
 * 将各种类型的窗户渲染出来
 *
 * @auther Alessio
 * @date 2022/3/3
 **/
public class WindowGeos {

    public Window window;
    private Frame frame;
    private WindowBeam vertiBeam;
    private WindowBeam horiBeam;

    public List<WB_Polygon> framesGeo;
    public List<WB_Polygon> vertiBeamsGeo;
    public List<WB_Polygon> horiBeamsGeo;

    public WindowGeos(Window window) {
        this.window = window;
        frame = window.frame;
        vertiBeam = window.vertiBeam;
        horiBeam = window.horiBeam;
    }

    public WB_Polygon calFrameBase() {
        WB_Polygon boundary = frame.getFrameBoundary();
        return W_Tools.getPolygonWithHoles(boundary, frame.getFrameWidth());
    }


}
