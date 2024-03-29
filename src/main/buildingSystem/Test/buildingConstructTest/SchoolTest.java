package Test.buildingConstructTest;

import Tools.GeoTools;
import facade.basic.BasicObject;
import facade.unit.styles.F_WindowArray;
import facade.unit.styles.S_ExtrudeIn;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import renders.UnitRender;
import unit2Vol.Building;
import unit2Vol.Unit;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    private List<Unit> units01;

    private List<Unit> units02;

    private BuildingRender buildingRender;

    private List<BasicObject> panelList;

    private WB_Render render;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 50000);
        unitRenders = new LinkedList<>();
        render = new WB_Render(this);


        units01 = new LinkedList<>();
        units02 = new LinkedList<>();

        initUnits();
        initBuilding();

        //测试信息方法
        checkInfo();

        initOuterPanel();
    }



    private void initBuilding() {

        building01 = new Building(units01);
        building02 = new Building(units02);

        System.out.println("building 01 unit num : " + building01.getUnitList().size());
        System.out.println("building 02 unit num : " + building02.getUnitList().size());

        buildingRender = new BuildingRender(this, building01, building02);
    }


    int unitNum = 0;

    /**
     * test building information
     */
    private void checkInfo() {
        Unit unit = building01.getUnitList().get(unitNum);

//        System.out.println("building01 h : " + building01.getHeight());
//        System.out.println("building02 h : " + building02.getHeight());

        HashMap<WB_Vector, List<Unit>> map = unit.getUnitNeiMap();

        System.out.println("-----------------------------");
        System.out.println("target unit: " + unitNum);
        for (Map.Entry<WB_Vector, List<Unit>> entry : map.entrySet()) {
            System.out.println(entry.getKey());

            entry.getValue().forEach(e -> System.out.println(e.getId()));
        }
    }

    private void initBuildingLayer(List<Unit> target, List<UnitRender> renders, WB_Point pos, WB_Polygon base, WB_Vector dir, double gap, int horNum, int layerNum) {
        for (int i = 0; i < horNum; i++) {
            for (int j = 0; j < layerNum; j++) {
                dir.normalizeSelf();

                WB_Vector horDirNor = new WB_Point(1, 0, 0).mul(gap).mul(i);
                WB_Vector v = dir.mul(horDirNor.getLength());
                WB_Point p = pos.add(v.add(new WB_Point(0, 0, 1).mul(height).mul(j)));

                Unit u = new Unit(p, base, dir, height);
                target.add(u);
                renders.add(new UnitRender(this, u));
            }
        }
    }


    private void initUnits() {
        WB_Point pos = new WB_Point(0, 0, 0);
        WB_Polygon base = GeoTools.createRecPolygon(8000, 6000);
        WB_Vector dir = new WB_Vector(1, 0, 0);
        initBuildingLayer(units01, unitRenders, pos, base, dir, 8000, 4, 3);

        WB_Point cPos = new WB_Point(0, 6000);
        WB_Polygon cBase = GeoTools.createRecPolygon(32000, 2000);
        initBuildingLayer(units01, unitRenders, cPos, cBase, dir, 0, 1, 3);

        WB_Point pos02 = new WB_Point(24000, 14000);
        WB_Polygon base02 = GeoTools.createRecPolygon(6000, 8000);
        WB_Vector dir02 = new WB_Vector(0, 1, 0);
        initBuildingLayer(units02, unitRenders, pos02, base02, dir02, 6000, 3, 5);

        WB_Point cPos02 = new WB_Point(24000, 8000);
        WB_Polygon cBase02 = GeoTools.createRecPolygon(18000, 2000);
        WB_Vector cDir02 = new WB_Vector(0, -1, 0);
        initBuildingLayer(units02, unitRenders, cPos02, cBase02, cDir02, 0, 1, 5);
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);

        if (ifAll)
            buildingRender.renderAll();

        if (ifPanel)
            buildingRender.renderPanelGeo();

        if (ifInfo) {
            checkInfo();
            ifInfo = false;
        }

        for (var panel : panelList) {
            panel.draw(render);
        }
    }

    public void initOuterPanel() {
        panelList = new LinkedList<>();
        List<WB_Polygon> panelFaceShapes = new LinkedList<>();

        building01.getWallAbleFaces().forEach(e -> panelFaceShapes.add(e.getShape()));
        building02.getWallAbleFaces().forEach(e -> panelFaceShapes.add(e.getShape()));

        panelFaceShapes.forEach(e -> panelList.add(new F_WindowArray(e)));
        panelFaceShapes.forEach(e -> panelList.add(new S_ExtrudeIn(e)));

    }

    boolean ifAll = true;
    boolean ifPanel = true;
    boolean ifInfo = false;

    @Override
    public void keyPressed() {
        if (key == 'Q' || key == 'q')
            ifAll = !ifAll;

        if (key == 'W' || key == 'w')
            ifPanel = !ifPanel;

        if (key == 'z' || key == 'Z') {
            if (unitNum <= units01.size()) {
                unitNum++;
                ifInfo = true;
            } else {
                System.out.println("Out range");
            }

        }

        if (key == 'x' || key == 'X') {
            if (unitNum >= 0) {
                unitNum--;
                ifInfo = true;
            } else {
                System.out.println("unit num must > 0");
            }
        }

    }

}
