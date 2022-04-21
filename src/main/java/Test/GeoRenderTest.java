package Test;

import FacadeGen.Panel.Panel;
import FacadeGen.Panel.PanelBase.BasicBase;
import FacadeGen.Panel.PanelGeos;
import FacadeGen.Panel.panelStyle.Style01Panel;
import Tools.GeoTools;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

/**
 * @auther Alessio
 * @date 2022/4/21
 **/
public class GeoRenderTest extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Test.GeoRenderTest");
    }

    CameraController cameraController;
    WB_Render render;
    Panel panel;
    PanelGeos panelGeos;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);
        iniPanel();
    }

    private void iniPanel() {
        WB_Polygon basePolygon = GeoTools.createRecPolygon(6000, 3000);
        panel = new Style01Panel(new BasicBase(basePolygon));
        panelGeos = new PanelGeos(panel, new WB_Point(0, 0, 0), new WB_Vector(0, 1, 0));
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);
//        renderAll(panelGeos);
        renderT(panelGeos);
    }

    private void renderAll(PanelGeos panelGeos) {
        panelRender(panelGeos);
        frameRender(panelGeos);
        beamRender(panelGeos);
        glassRender(panelGeos);
    }

    private void renderT(PanelGeos panelGeos) {
        pushStyle();
        stroke(80, 50, 20);
        strokeWeight(3);
        for (WB_PolyLine l : panelGeos.winBoundaries) {
            render.drawPolylineEdges(l);
        }
        popStyle();
    }

    private void panelRender(PanelGeos panelGeos) {
        pushStyle();
        noFill();
        stroke(100);
        render.drawPolylineEdges(panelGeos.wallGeo);
        popStyle();
    }

    private void beamRender(PanelGeos panelGeos) {
        pushStyle();
        stroke(80, 50, 20);
        strokeWeight(3);
        for (WB_PolyLine l : panelGeos.beams) {
            render.drawPolylineEdges(l);
        }
        popStyle();
    }

    private void frameRender(PanelGeos panelGeos) {
        pushStyle();
        stroke(10, 50, 130);
        strokeWeight(3);
        for (WB_Polygon p : panelGeos.frames) {
            render.drawPolygonEdges(p);
        }
        popStyle();
    }

    private void glassRender(PanelGeos panelGeos) {
        pushStyle();
        fill(84, 192, 235);
        for (WB_Polygon p : panelGeos.glasses) {
            render.drawPolygonEdges(p);
        }
        popStyle();
    }


}
