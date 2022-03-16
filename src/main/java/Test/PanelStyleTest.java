package Test;

import FacadeGen.Panel.Component.Window;
import FacadeGen.Panel.Component.WindowGeos;
import FacadeGen.Panel.Panel;
import FacadeGen.Panel.PanelBase.BasicBase;
import FacadeGen.Panel.Style01Panel;
import Tools.GeoTools;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @auther Alessio
 * @date 2022/3/16
 **/
public class PanelStyleTest extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Test.PanelStyleTest");
    }

    CameraController cameraController;
    WB_Render render;
    Panel panel;
    BasicBase base;

    List<WB_Polygon> frames = new LinkedList<>();
    List<WB_PolyLine> beams = new LinkedList<>();
    List<WB_Polygon> glass = new LinkedList<>();

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);
        iniPanel();
        iniGeo();
    }

    private void iniPanel() {
        WB_Polygon basePolygon = GeoTools.createRecPolygon(6000, 3000);
        base = new BasicBase(basePolygon);
        panel = new Style01Panel(base);
    }

    private void iniGeo() {
        HashMap<Window, WB_Point> comps = panel.getWindows();
        for (Map.Entry<Window, WB_Point> entry : comps.entrySet()) {
            Window win = entry.getKey();
            WindowGeos geos = win.getWindowGeos();
            frames.add(GeoTools.movePolygon(geos.getFrameBase2D(), entry.getValue()));
            glass.add(GeoTools.movePolygon(geos.getGlassShape(), entry.getValue()));

            List<WB_PolyLine> rawBeams = geos.getAll2DBeams();
            for (WB_PolyLine l : rawBeams) {
                beams.add(GeoTools.movePolyline(l, entry.getValue()));
            }
        }
    }

    public void draw() {
        background(255);
        //绘制panel面板边界

        panelRender();
        beamRender();
        frameRender();
        glassRender();
    }

    private void panelRender() {
        pushStyle();
        noFill();
        stroke(150);
        strokeWeight(2);
        render.drawPolygonEdges(panel.getBase().getBasicShape());
        popStyle();
    }

    private void beamRender() {
        pushStyle();
        stroke(80, 50, 20);
        strokeWeight(3);
        for (WB_PolyLine l : beams) {
            render.drawPolylineEdges(l);
        }
        popStyle();
    }

    private void frameRender() {
        pushStyle();
        stroke(10, 50, 130);
        strokeWeight(3);
        for (WB_Polygon p : frames) {
            render.drawPolygonEdges(p);
        }
        popStyle();
    }

    private void glassRender() {
        pushStyle();
        fill(84, 192, 235);
        for (WB_Polygon p : glass) {
            render.drawPolygonEdges(p);
        }
        popStyle();
    }

}
