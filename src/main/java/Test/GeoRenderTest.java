package Test;

import facadeGen.Panel.Panel;
import facadeGen.Panel.PanelBase.BasicBase;
import facadeGen.Panel.PanelGeos;
import facadeGen.Panel.PanelRender;
import facadeGen.Panel.PanelStyle.Style01Panel;
import facadeGen.Panel.PanelStyle.StyleA;
import facadeGen.Panel.PanelStyle.StyleB;
import facadeGen.Panel.PanelStyle.StyleC;
import Tools.GeoTools;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

import java.util.LinkedList;
import java.util.List;

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
    Panel panel01;
    Panel panel02;
    Panel panel03;
    Panel panel04;

    List<PanelGeos> geos;

    PanelRender panelRender;

    WB_Polygon basePolygon;

    WB_Polygon testPolygon;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);
        geos = new LinkedList<>();
        iniPanel();

        panelRender = new PanelRender(this, render, geos);

        initPoly();
    }

    private void initPoly() {
        WB_Point p0 = new WB_Point(4500, 1000, 5000);
        WB_Point p1 = new WB_Point(9000, 1000, 5000);
        WB_Point p2 = new WB_Point(9000, 1000, 8900);
        WB_Point p3 = new WB_Point(4500, 1000, 8900);

        testPolygon = new WB_Polygon(p0, p1, p2, p3);


    }

    private void iniPanel() {
        basePolygon = GeoTools.createRecPolygon(6000, 3900);
        panel01 = new Style01Panel(new BasicBase(basePolygon));

        WB_Polygon schoolBase = GeoTools.createRecPolygon(4500, 3900);
        panel02 = new StyleA(new BasicBase(schoolBase));
        panel03 = new StyleB(new BasicBase(schoolBase));
        panel04 = new StyleC(new BasicBase(schoolBase));

//        geos.add(new PanelGeos(panel01, new WB_Point(0, 0, 0), new WB_Vector(0, 1, 0)));
        geos.add(new PanelGeos(panel02, new WB_Point(4500,1000, 5000), new WB_Vector(0, 1, 0)));
//        geos.add(new PanelGeos(panel03, new WB_Point(9000, 0, 0), new WB_Vector(0, 1, 0)));
//        geos.add(new PanelGeos(panel04, new WB_Point(13500, 0, 0), new WB_Vector(0, 1, 0)));
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);
        panelRender.renderAll();

        render.drawPolygonEdges(testPolygon);
    }


}
