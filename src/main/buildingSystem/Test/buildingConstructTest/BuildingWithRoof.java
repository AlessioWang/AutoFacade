package Test.buildingConstructTest;

import Tools.GeoTools;

import facadeGen.Panel.PanelBase.BasicBase;
import facadeGen.Panel.PanelGeos;
import facadeGen.Panel.PanelRender;
import facadeGen.Panel.PanelStyle.Panel;
import facadeGen.Panel.RoofStyle.BasicRoof;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import renders.UnitRender;
import unit2Vol.Building;
import unit2Vol.Unit;

import unit2Vol.face.Face;
import unit2Vol.panelBase.MergedPanelBase;
import unit2Vol.panelBase.PanelBase;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;


import java.util.LinkedList;
import java.util.List;


/**
 * @auther Alessio
 * @date 2023/1/3
 **/
public class BuildingWithRoof extends PApplet {

    public static void main(String[] args) {
        PApplet.main(BuildingWithRoof.class.getName());
    }

    private CameraController cameraController;

    private List<UnitRender> unitRenders;

    private Building building01;

    private double height = 3500;

    private List<Unit> units;

    private BuildingRender buildingRender;

    private PanelRender panelRender;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 150000);
        unitRenders = new LinkedList<>();

        units = new LinkedList<>();

        initUnits();
        initBuilding();
        initPanel();
    }

    private void initBuilding() {
        building01 = new Building(units);
        buildingRender = new BuildingRender(this, building01);
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
        initBuildingLayer(units, unitRenders, pos, base, dir, 8000, 2, 1);
    }

    Panel roofPanel;

    private List<PanelGeos> geos = new LinkedList<>();

    private void initPanel() {
        WB_Polygon roofPolygon = GeoTools.createRecPolygon(8000 * 2, 6000);

        // TODO: 2023/1/3 polygon的点序需要与face的方向相同
        roofPanel = new BasicRoof(new BasicBase(roofPolygon));

        PanelBase roofBase = getRoofBase();

        // 指定基点来放置屋顶
        geos.add(new PanelGeos(roofPanel, roofBase, roofBase.getShape().getPoint(0)));

        panelRender = new PanelRender(this, new WB_Render(this), geos);
    }

    private MergedPanelBase getRoofBase() {
        List<Unit> unitList = building01.getUnitList();

        List<Face> faces = new LinkedList<>();

        for (Unit unit : unitList) {
            faces.add(unit.getTopFace());
        }

        return new MergedPanelBase(faces);
    }

    public void draw() {
        background(255);

        cameraController.drawSystem(5000);

        buildingRender.renderPanelGeo();

        buildingRender.renderPanelGeo();

        panelRender.renderAll();
    }

}
