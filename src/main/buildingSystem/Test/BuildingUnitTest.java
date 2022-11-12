package Test;

import Tools.GeoTools;
import guo_cam.CameraController;
import org.junit.Test;
import processing.core.PApplet;
import unit2Vol.Unit;
import unit2Vol.UnitRender;
import unit2Vol.face.Face;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/11/12
 **/
public class BuildingUnitTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(BuildingUnitTest.class.getName());
    }

    private CameraController cameraController;

    private List<UnitRender> unitRenders;

    public void settings() {
        size(800, 800, P3D);
    }

    List<Unit> units;

    public void setup() {
        cameraController = new CameraController(this, 3000);
        unitRenders = new LinkedList<>();


        WB_Point pos = new WB_Point(1000, 0, 2000);
        WB_Polygon base = GeoTools.createRecPolygon(12000, 8000);
        WB_Vector dir = new WB_Vector(2, 1, 0);

        double gap = 12000;

        units = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            WB_Point p = pos.add(new WB_Point(1, 0, 0).mul(gap).mul(i));
            Unit u = new Unit(p, base, dir, 3500);
            units.add(u);
            unitRenders.add(new UnitRender(this, u));
        }

    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);

        for (UnitRender ur : unitRenders) {
            ur.renderAll();
        }

    }

}
