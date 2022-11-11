package Test;

import Tools.GeoTools;
import guo_cam.CameraController;
import processing.core.PApplet;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/11/10
 **/
public class UnitTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(UnitTest.class.getName());
    }

    private CameraController cameraController;

    private WB_Render render;

    private Unit unit;

    List<WB_Polygon> rndShapes;

    WB_Polygon topShape;

    WB_Polygon bottomShape;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);
        WB_Point pos = new WB_Point(1000, 0, 2000);
        WB_Polygon base = GeoTools.createRecPolygon(12000, 8000);
        WB_Vector dir = new WB_Vector(2, 1, 0);

        unit = new Unit(pos, base, dir, 3500);

        rndShapes = new LinkedList<>();

        List<Face> rndFaces = unit.getRndFaces();
        for (Face face : rndFaces) {
            rndShapes.add(face.getShape());
        }

        topShape = unit.getTopFace().getShape();

        bottomShape = unit.getBottomFace().getShape();

    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);

        pushStyle();
        fill(0, 100, 0, 30);
        for (WB_Polygon p : rndShapes) {
            render.drawPolygonEdges(p);
        }
        popStyle();

        pushStyle();
        fill(100,0,0,50);
        render.drawPolygonEdges(topShape);
        popStyle();

        pushStyle();
        fill(0,0,100,50);
        render.drawPolygonEdges(bottomShape);
        popStyle();

    }
}
