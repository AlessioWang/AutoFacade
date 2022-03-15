package FacadeGen.Panel.Component;

import Tools.GeoTools;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

import java.util.ArrayList;
import java.util.LinkedList;
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

    private WB_Polygon frameBase2D;
    private List<WB_PolyLine> vertiBeamLine2D;
    private List<WB_PolyLine> horiBeamLine2D;
    private List<WB_PolyLine> all2DBeams;
    private WB_Polygon glassShape;

    public WindowGeos(Window window) {
        this.window = window;
        frame = window.frame;
        vertiBeam = window.vertiBeam;
        horiBeam = window.horiBeam;

        //初始化2维基准线
        ini2DGeos();
    }

    private void ini2DGeos() {
        frameBase2D = calFrameBase();
        vertiBeamLine2D = vertiBeam.getbLines();
        horiBeamLine2D = horiBeam.getbLines();
        iniAllBeamLines();
        glassShape = window.getGlass().getShape();
    }

    private WB_Polygon calFrameBase() {
        WB_Polygon boundary = frame.getFrameBoundary();
        return GeoTools.getPolygonWithHoles(boundary, frame.getFrameWidth());
    }

    private void iniAllBeamLines() {
        all2DBeams = new LinkedList<>();
        all2DBeams.addAll(horiBeamLine2D);
        all2DBeams.addAll(vertiBeamLine2D);
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public WB_Polygon getFrameBase2D() {
        return frameBase2D;
    }


    public List<WB_PolyLine> getVertiBeamLine2D() {
        return vertiBeamLine2D;
    }


    public List<WB_PolyLine> getHoriBeamLine2D() {
        return horiBeamLine2D;
    }


    public List<WB_PolyLine> getAll2DBeams() {
        return all2DBeams;
    }


    public WB_Polygon getGlassShape() {
        return glassShape;
    }
}
