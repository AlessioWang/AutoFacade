package Test;

import FacadeGen.Panel.Panel;
import FacadeGen.Panel.PanelBase.BasicBase;
import FacadeGen.Panel.PanelGeos;
import FacadeGen.Panel.PanelRender;
import FacadeGen.Panel.panelStyle.Style01Panel;
import Tools.GeoTools;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

import java.util.Arrays;

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
    PanelRender panelRender;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);
        iniPanel();
        panelRender = new PanelRender(this, render, Arrays.asList(panelGeos));
    }

    private void iniPanel() {
        WB_Polygon basePolygon = GeoTools.createRecPolygon(6000, 3000);
        panel = new Style01Panel(new BasicBase(basePolygon));
        panelGeos = new PanelGeos(panel, new WB_Point(0, 0, 0), new WB_Vector(0, 1, 0));
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);
        panelRender.renderAll();
    }



}
