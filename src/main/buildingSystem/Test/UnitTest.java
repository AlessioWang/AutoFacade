package Test;

import Tools.GeoTools;
import guo_cam.CameraController;
import processing.core.PApplet;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
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

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);
        WB_Point pos = new WB_Point(0, 0, 0);
        WB_Polygon base = GeoTools.createRecPolygon(12000, 8000);

        unit = new Unit(pos, base, 3500);

        rndShapes = new LinkedList<>();

    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);

        pushStyle();

        popStyle();

    }
}
