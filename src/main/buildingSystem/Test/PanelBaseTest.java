package Test;

import Tools.GeoTools;
import facadeGen.Panel.PanelStyle.Panel;
import facadeGen.Panel.PanelBase.BasicBase;
import facadeGen.Panel.PanelGeos;
import facadeGen.Panel.PanelRender;
import facadeGen.Panel.PanelStyle.StyleA;
import facadeGen.Panel.PanelStyle.StyleB;
import facadeGen.Panel.PanelStyle.StyleByBase;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import renders.UnitRender;
import unit2Vol.Building;
import unit2Vol.panelBase.MergedPanelBase;
import unit2Vol.panelBase.PanelBase;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/12/13
 **/
public class PanelBaseTest extends PApplet {
    public static void main(String[] args) {
        PApplet.main(PanelBaseTest.class.getName());
    }

    private CameraController cameraController;

    private List<UnitRender> unitRenders;

    private Building building01;

    private double height = 3500;

    private List<Unit> units;

    private BuildingRender buildingRender;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 50000);
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
        initBuildingLayer(units, unitRenders, pos, base, dir, 8000, 8, 5);
    }

    private List<PanelGeos> geos;
    private Panel panel01;
    private Panel panel02;
    private PanelRender panelRender;

    private void initPanel() {
        geos = new LinkedList<>();

        panelRender = new PanelRender(this, new WB_Render(this), geos);

        WB_Polygon basePolygon01 = GeoTools.createRecPolygon(8000, 3500 * 5);
        panel01 = new StyleByBase(new BasicBase(basePolygon01));

        WB_Polygon basePolygon02 = GeoTools.createRecPolygon(8000, 3500);
        panel02 = new StyleB(new BasicBase(basePolygon02));

        List<Face> facesMulti = new LinkedList<>();
        List<Face> facesSingle = new LinkedList<>();

        for (int i = 0; i < units.size(); i++) {
            Unit unit = units.get(i);

            if (i <= 4) {
                facesMulti.add(unit.getAllFaces().get(1));
            } else {
                facesSingle.add(unit.getAllFaces().get(1));
            }
        }


        for (Face face : facesSingle) {
            geos.add(new PanelGeos(panel02, new MergedPanelBase(Collections.singletonList(face))));
        }

        PanelBase panelBaseLarge = new MergedPanelBase(facesMulti);
        geos.add(new PanelGeos(panel01, panelBaseLarge));
    }

    public void draw() {
        background(255);

        cameraController.drawSystem(5000);

        buildingRender.renderPanelGeo();

        panelRender.renderAll();
    }


}