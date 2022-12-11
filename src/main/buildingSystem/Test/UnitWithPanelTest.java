package Test;

import Tools.GeoTools;
import facadeGen.Panel.Panel;
import facadeGen.Panel.PanelBase.BasicBase;
import facadeGen.Panel.PanelGeos;
import facadeGen.Panel.PanelRender;
import facadeGen.Panel.PanelStyle.StyleC;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import renders.UnitRender;
import unit2Vol.Building;
import unit2Vol.Unit;
import unit2Vol.face.Face;
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
 * @date 2022/12/6
 **/
public class UnitWithPanelTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(UnitWithPanelTest.class.getName());
    }

    private CameraController cameraController;

    private List<UnitRender> unitRenders;

    private Building building01;

    private double height = 3500;

    private List<Unit> units01;


    private BuildingRender buildingRender;


    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 50000);
        unitRenders = new LinkedList<>();

        units01 = new LinkedList<>();

        initUnits();
        initBuilding();

        initPanel();

//        //测试信息方法
//        checkInfo();
    }

    private void initBuilding() {
        building01 = new Building(units01);

        System.out.println("building 01 unit num : " + building01.getUnitList().size());

        buildingRender = new BuildingRender(this, building01);
    }

    /**
     * test building information
     */
    private void checkInfo() {
        Unit unit = building01.getUnitList().get(4);

        HashMap<WB_Vector, List<Unit>> map = unit.getUnitMap();

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
        initBuildingLayer(units01, unitRenders, pos, base, dir, 8000, 1, 1);

    }

    private List<PanelGeos> geos = new LinkedList<>();
    private Panel panel01;
    private PanelRender panelRender;


    private void initPanel() {
        panelRender = new PanelRender(this, new WB_Render(this), geos);

        WB_Polygon basePolygon = GeoTools.createRecPolygon(8000, 3500);
        panel01 = new StyleC(new BasicBase(basePolygon));

        for (Unit unit : units01) {
            List<Face> faces = new LinkedList<>();
            unit.getAllFaces().stream().filter(Face::isIfPanel).forEach(faces::add);

//            faces.add(unit.getAllFaces().get(1));
//            faces.add(unit.getAllFaces().get(3));

            for (Face p : faces) {
                WB_Point pt0 = p.getShape().getPoint(0);
                WB_Point pt1 = p.getShape().getPoint(3);

                System.out.println("pt0 " + pt0.zd());
                System.out.println("pt1 " + pt1.zd());

                WB_Point pt = pt0.zd() >= pt1.zd() ? pt0 : pt1;

                geos.add(new PanelGeos(panel01, pt, p.getShape(), p.getDir()));
            }
        }
    }


    public void draw() {
        background(255);
        cameraController.drawSystem(1000);

        buildingRender.renderPanelGeo();

        panelRender.renderAll();
    }


}
