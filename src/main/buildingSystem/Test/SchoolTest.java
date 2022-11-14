package Test;

import Tools.GeoTools;
import guo_cam.CameraController;
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
 * @date 2022/11/14
 **/
public class SchoolTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(SchoolTest.class.getName());
    }

    private CameraController cameraController;

    private List<UnitRender> unitRenders;

    private Building building01;

    private Building building02;

    private double height = 3500;

    private double gap = 8000;

    private List<Unit> units01;

    private List<Unit> units02;


    public void settings() {
        size(800, 800, P3D);
    }


    public void setup() {
        cameraController = new CameraController(this, 50000);
        unitRenders = new LinkedList<>();

        units01 = new LinkedList<>();
        units02 = new LinkedList<>();

        initUnits();
        initBuilding();
    }

    private void initBuilding() {
        building01 = new Building(units01);
        System.out.println("unit num : " + building01.getUnitList().size());

    }

    private void initUnits() {
        WB_Point pos = new WB_Point(0, 0, 0);
        WB_Polygon base = GeoTools.createRecPolygon(8000, 6000);
        WB_Vector dir = new WB_Vector(1, 0, 0);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                dir.normalizeSelf();
                WB_Vector horDirNor = new WB_Point(1, 0, 0).mul(gap).mul(i);
                WB_Vector v = dir.mul(horDirNor.getLength());

                WB_Point p = pos.add(v.add(new WB_Point(0, 0, 1).mul(height).mul(j)));

                Unit u = new Unit(p, base, dir, height);
                units01.add(u);
                unitRenders.add(new UnitRender(this, u));
            }
        }

        WB_Point cPos = new WB_Point(0, 6000);
        WB_Polygon cBase = GeoTools.createRecPolygon(32000, 2000);
        Unit corridor = new Unit(cPos, cBase, dir, height);

        units01.add(corridor);
        unitRenders.add(new UnitRender(this, corridor));
    }


    public void draw() {
        background(255);
        cameraController.drawSystem(1000);

        for (UnitRender ur : unitRenders) {
            ur.renderAll();
            ur.rendId();
        }
    }

}
