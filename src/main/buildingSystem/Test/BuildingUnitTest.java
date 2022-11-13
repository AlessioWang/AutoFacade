package Test;

import Tools.GeoTools;
import guo_cam.CameraController;
import org.junit.Test;
import processing.core.PApplet;
import unit2Vol.Building;
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

    private Building build;

    private double height = 3500;

    private double gap = 8000;

    private List<Unit> units;


    public void settings() {
        size(800, 800, P3D);
    }


    public void setup() {
        cameraController = new CameraController(this, 50000);
        unitRenders = new LinkedList<>();

        initUnits();
        build = new Building(units);
        System.out.println("unit num : " + build.getUnitList().size());
    }

    private void initBuilding() {

    }

    private void initUnits() {

        WB_Point pos = new WB_Point(1000, 0, 2000);
        WB_Polygon base = GeoTools.createRecPolygon(12000, 8000);
        WB_Vector dir = new WB_Vector(1, 1, 0);

        units = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                dir.normalizeSelf();
                WB_Vector increase = dir;
                WB_Vector horDirNor = new WB_Point(1, 0, 0).mul(gap).mul(i);
                WB_Vector v = increase.mul(horDirNor.getLength());

                WB_Point p = pos.add(v.add(new WB_Point(0, 0, 1).mul(height).mul(j)));

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
