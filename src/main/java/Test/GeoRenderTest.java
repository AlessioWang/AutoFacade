package Test;

import facadeGen.Panel.Panel;
import facadeGen.Panel.PanelBase.BasicBase;
import facadeGen.Panel.PanelGeos;
import facadeGen.Panel.PanelRender;
import facadeGen.Panel.PanelStyle.Style01Panel;
import facadeGen.Panel.PanelStyle.StyleA;
import Tools.GeoTools;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Transform3D;
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

    WB_Polygon movePolygon;

    WB_Polygon yPolygon;

    WB_Polygon zPolygon;

    WB_Polygon rotatePolygon;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);
        geos = new LinkedList<>();

        panelRender = new PanelRender(this, render, geos);

        initPoly();

        iniPanel();
    }

    private void initPoly() {
        WB_Point p0 = new WB_Point(4500, 3000, 5000);
        WB_Point p1 = new WB_Point(9000, 3000, 5000);
        WB_Point p2 = new WB_Point(9000, 3000, 8900);
        WB_Point p3 = new WB_Point(4500, 3000, 8900);

        testPolygon = new WB_Polygon(p0, p1, p2, p3);

        movePolygon = GeoTools.movePolygon3D(testPolygon, new WB_Point(5000, 0, 0));

        WB_Point pp0 = new WB_Point(0, 0, 0);
        WB_Point pp1 = new WB_Point(0, 4500, 0);
        WB_Point pp2 = new WB_Point(0, 4500, 3900);
        WB_Point pp3 = new WB_Point(0, 0, 3900);

        yPolygon = new WB_Polygon(pp0, pp1, pp2, pp3);

        WB_Point ppp0 = new WB_Point(0, 0, 1000);
        WB_Point ppp1 = new WB_Point(4500, 0, 1000);
        WB_Point ppp2 = new WB_Point(4500, 3900, 1000);
        WB_Point ppp3 = new WB_Point(0, 3900, 1000);

        zPolygon = new WB_Polygon(ppp0, ppp1, ppp2, ppp3);


        WB_Transform3D transform3D = new WB_Transform3D();
        transform3D.addRotateZ(Math.PI * 0.25);
        rotatePolygon = yPolygon.apply(transform3D);

    }

    private void iniPanel() {
        basePolygon = GeoTools.createRecPolygon(6000, 3900);
        panel01 = new Style01Panel(new BasicBase(basePolygon));

        WB_Polygon schoolBase = GeoTools.createRecPolygon(4500, 3900);
        panel02 = new StyleA(new BasicBase(schoolBase));
//        panel03 = new StyleB(new BasicBase(schoolBase));
//        panel04 = new StyleC(new BasicBase(schoolBase));

        geos.add(new PanelGeos(panel02, new WB_Point(4500, 3000, 5000), testPolygon, new WB_Vector(0, 1, 0)));
        geos.add(new PanelGeos(panel02, new WB_Point(9500, 3000, 5000), movePolygon, new WB_Vector(0, 1, 0)));
        geos.add(new PanelGeos(panel02, new WB_Point(0, 0, 0), yPolygon, new WB_Vector(1, 0, 0)));
//        geos.add(new PanelGeos(panel02, new WB_Point(0, 0, 1000), zPolygon, new WB_Vector(0, 0, 1)));

//        geos.add(new PanelGeos(panel02, new WB_Point(0,0,0), rotatePolygon, new WB_Vector(-1, 1, 0)));
        System.out.println("sss " + geos.size());
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);
        panelRender.renderAll();

        noFill();
        render.drawPolygonEdges(testPolygon);
        render.drawPolygonEdges(movePolygon);
        render.drawPolygonEdges(yPolygon);
        render.drawPolygonEdges(zPolygon);
//        render.drawPolygonEdges(rotatePolygon);
    }


}
