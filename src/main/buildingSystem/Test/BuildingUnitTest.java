package Test;

import Tools.GeoTools;
import guo_cam.CameraController;
import org.junit.Test;
import processing.core.PApplet;
import unit2Vol.Unit;
import unit2Vol.UnitRender;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;

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


        double gap = 8000;
        double height = 3500;

        units = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                WB_Vector increase = dir.div(dir.normalizeSelf());

                WB_Vector horDirNor = pos.add(new WB_Point(1, 0, 0).mul(gap).mul(i));
                double length = horDirNor.dot(dir);
                WB_Vector v = increase.mul(length);

                WB_Point p = pos.add(v
                        .add(new WB_Point(0, 0, 1).mul(height).mul(j)));

                Unit u = new Unit(p, base, dir, height);
                units.add(u);
                unitRenders.add(new UnitRender(this, u));
            }
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
