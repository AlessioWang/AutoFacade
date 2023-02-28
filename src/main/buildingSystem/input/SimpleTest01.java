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
import unit2Vol.panelBase.SimplePanelBase;
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

    List<WB_Polygon> floorPolygon;

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

        initBuildingFromDxf();

//        initPanel();

//        initBaseWithSplit();

        initFuncPanel();

        initFloor();
    }

    private void initBuildingFromDxf() {
        building = buildingInputer.getBuilding();
        panels = new LinkedList<>();
    }

    private void initPanel() {

        List<Face> wallAbleFaces = building.getWallAbleFaces();
        for (Face f : wallAbleFaces) {
            func2Panel(f);
        }

        List<Face> topFaces = building.getRoofAbleFaces();

        topFaces.forEach(e -> panels.add(new F_Example(e.getShape())));
    }


    //测试分割bases
    private void initBaseWithSplit() {
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
        allBases.forEach(e -> polys.add(e.getShape()));
        polys.forEach(e -> panels.add(new F_Example(e)));
    }

    /**
     * 根据功能和panelBase初始化面板
     * 增加细分
     */
    private void initFuncPanel() {
        classBase = new LinkedList<>();
        transBase = new LinkedList<>();
        stairBase = new LinkedList<>();

        List<Face> wallAbleFaces = building.getWallAbleFaces();
        for (Face f : wallAbleFaces) {
            func2Base(f);
        }

        initPanelByBaseFunc(classBase, Function.ClassRoom);
        initPanelByBaseFunc(transBase, Function.Transport);
        initPanelByBaseFunc(stairBase, Function.Stair);
    }

    private void func2Base(Face face) {
        Function function = face.getFunction();
        switch (function) {
            case ClassRoom:
                classBase.addAll(new SplitPanelBase(face, new double[]{0.5}).getPanelBases());
                break;
            case Transport:
                transBase.addAll(new SplitPanelBase(face, new double[]{0.2, 0.4, 0.6, 0.8}).getPanelBases());
                break;
            case Stair:
                stairBase.add(new SimplePanelBase(face));
        }
    }

    private void initPanelByBaseFunc(List<PanelBase> bases, Function function) {
        switch (function) {
            case ClassRoom:
                bases.forEach(e -> panels.add(new S_ExtrudeIn(e.getShape())));
                break;
            case Transport:
                bases.forEach(e -> panels.add(new F_Example(e.getShape())));
                break;
            case Stair:
                bases.forEach(e -> panels.add(new F_WindowArray(e.getShape())));
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

    private void initFloor() {
        floorPolygon = new LinkedList<>();

        List<PanelBase> planBaseList = building.getFloorBaseList();
        planBaseList.forEach(e -> floorPolygon.add(e.getShape()));
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(10000);

        for (var panel : panels) {
            panel.draw(render);
        }

        //屋顶
        render.drawPolygonEdges(building.getRoofBaseList().get(0).getShape());

        //每层的地面
        for (var p : floorPolygon) {
            render.drawPolygonEdges(p);
        }

//        buildingRender.renderAll();
    }

}
