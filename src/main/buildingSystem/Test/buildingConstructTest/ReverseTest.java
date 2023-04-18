package Test.buildingConstructTest;

import Tools.GeoTools;
import facadeGen.Panel.PanelStyle.Panel;
import facadeGen.Panel.PanelBase.BasicBase;
import facadeGen.Panel.PanelGeos;
import facadeGen.Panel.PanelRender;
import facadeGen.Panel.PanelStyle.StyleA;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/12/11
 **/
public class ReverseTest extends PApplet {
    public static void main(String[] args) {
        PApplet.main(ReverseTest.class.getName());
    }

    CameraController cameraController;

    WB_Render render;

    Panel panel;

    List<PanelGeos> geos;

    PanelRender panelRender;

    WB_Polygon basePolygon;

    WB_Polygon testPolygon;

    WB_Polygon reversePolygon;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 5000);
        render = new WB_Render(this);
        geos = new LinkedList<>();

        panelRender = new PanelRender(this, render, geos);

        initPoly();

        iniPanel();
    }

    private void initPoly() {
        WB_Point p0 = new WB_Point(0, 0, 0);
        WB_Point p1 = new WB_Point(4500, 0, 0);
        WB_Point p2 = new WB_Point(4500, 0, 3900);
        WB_Point p3 = new WB_Point(0, 0, 3900);

        testPolygon = new WB_Polygon(p0, p1, p2, p3);

        reversePolygon = new WB_Polygon(p2, p3, p0, p1);
        reversePolygon = GeoTools.movePolygon3D(reversePolygon, new WB_Point(0, 1000, 0));
    }

    private void iniPanel() {
        basePolygon = GeoTools.createRecPolygon(6000, 3900);

        WB_Polygon schoolBase = GeoTools.createRecPolygon(4500, 3900);
        panel = new StyleA(new BasicBase(schoolBase));

        geos.add(new PanelGeos(panel, new WB_Point(0, 0, 0), testPolygon, new WB_Vector(0, 1, 0)));
        geos.add(new PanelGeos(panel, new WB_Point(0, 1000, 0), reversePolygon, new WB_Vector(0, 1, 0)));
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);
        panelRender.renderAll();

        noFill();
//        render.drawPolygonEdges(testPolygon);
        render.drawPolygonEdges(reversePolygon);
    }

}
