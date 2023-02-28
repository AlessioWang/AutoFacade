package input;

import facade.basic.BasicObject;
import facade.unit.styles.F_Example;
import facade.unit.styles.F_WindowArray;
import facade.unit.styles.S_ExtrudeIn;
import function.Function;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import unit2Vol.face.Face;
import unit2Vol.panelBase.PanelBase;
import unit2Vol.panelBase.SplitPanelBase;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render3D;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2023/2/23
 **/
public class SimpleTest01 extends PApplet {

    private String file = "src\\main\\resources\\dxf\\input_test.dxf";

    private BuildingInputer buildingInputer;

    private Building building;

    private BuildingRender buildingRender;

    private List<BasicObject> panels;

    private CameraController cameraController;

    private WB_Render3D render;

    List<PanelBase> allBases;

    List<PanelBase> classBase;
    List<PanelBase> transBase;
    List<PanelBase> stairBase;

    List<SplitPanelBase> splitPanelBases;

    public static void main(String[] args) {
        PApplet.main(SimpleTest01.class.getName());
    }

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 10000);

        render = new WB_Render3D(this);

        new SimpleTest01();

        buildingRender = new BuildingRender(this, building);
    }

    public SimpleTest01() {
        buildingInputer = new BuildingInputer(file);

//        initPanel();

        initBassWithSplit();

        List<PanelBase> roofBaseList = building.getRoofBaseList();
    }

    private void initPanel() {
        building = buildingInputer.getBuilding();
        panels = new LinkedList<>();

        List<Face> wallAbleFaces = building.getWallAbleFaces();
        for (Face f : wallAbleFaces) {
            func2Panel(f);
        }

        List<Face> topFaces = building.getRoofAbleFaces();

        topFaces.forEach(e -> panels.add(new F_Example(e.getShape())));
    }

    //测试分割bases
    private void initBassWithSplit() {
        building = buildingInputer.getBuilding();
        panels = new LinkedList<>();

        splitPanelBases = new LinkedList<>();
        allBases = new LinkedList<>();

        List<Face> wallAbleFaces = building.getWallAbleFaces();

        for (var face : wallAbleFaces) {
            SplitPanelBase splitPanelBase = new SplitPanelBase(face, new double[]{0.5});
            splitPanelBases.add(splitPanelBase);
        }

        for (var base : splitPanelBases) {
            allBases.addAll(base.getPanelBases());
        }

        List<WB_Polygon> polys = new LinkedList<>();
        allBases.stream().forEach(e -> polys.add(e.getShape()));
        polys.forEach(e -> panels.add(new F_Example(e)));
    }

    private void initFuncPanel() {
        classBase = new LinkedList<>();
        transBase = new LinkedList<>();
        stairBase = new LinkedList<>();

        List<Face> wallAbleFaces = building.getWallAbleFaces();
        for (Face f : wallAbleFaces){

        }

    }

    private void func2Panel(Face face) {
        Function function = face.getFunction();
        switch (function) {
            case ClassRoom:
                panels.add(new S_ExtrudeIn(face.getShape()));
                break;
            case Transport:
                panels.add(new F_WindowArray(face.getShape()));
                break;
            case Stair:
                panels.add(new F_Example(face.getShape()));
        }
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(10000);

        for (var panel : panels) {
            panel.draw(render);
        }

        render.drawPolygonEdges(building.getRoofBaseList().get(0).getShape());

        buildingRender.renderAll();
    }

}
