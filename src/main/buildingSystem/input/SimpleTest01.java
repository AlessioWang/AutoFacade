package input;

import Tools.GeoTools;

import facade.basic.BasicObject;
import facade.unit.styles.*;
import function.Function;
import function.PosType;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Beam;
import unit2Vol.Building;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import unit2Vol.panelBase.PanelBase;
import unit2Vol.panelBase.SimplePanelBase;
import unit2Vol.panelBase.SplitPanelBase;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render3D;

import java.util.*;

/**
 * @auther Alessio
 * @date 2023/2/23
 **/
public class SimpleTest01 extends PApplet {

//    private String file = "src\\main\\resources\\dxf\\beamTest.dxf";
    private String file = "src\\main\\resources\\dxf\\input_test.dxf";
//    private String file = "src\\main\\resources\\dxf\\innerWallTest.dxf";

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
    List<PanelBase> trimmedBase;

    List<SplitPanelBase> splitPanelBases;

    List<WB_Polygon> parapetPolygons;

    List<WB_Polygon> floorPolygons;

    List<WB_Polygon> innerWallPolygons;

    public static void main(String[] args) {
        PApplet.main(SimpleTest01.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
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

        initRoofPanels();

        initBeams();

        initInnerPanel();

        initFloorPanel();

        initFuncPanel();

        initTrimmed();

        initInnerWall();

        initParapet();
    }

    private void initBuildingFromDxf() {
        building = buildingInputer.getBuilding();
        panels = new LinkedList<>();
    }

    /**
     * 测试最简单的创建panel
     */
    private void initPanelSimple() {

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

    /**
     * trim的面版
     */
    private void initTrimmed() {
        trimmedBase = new LinkedList<>();

        List<Face> faces = new LinkedList<>();

        Set<Map.Entry<Face, List<Face>>> entries = building.getTrimmedFaceMap().entrySet();
        entries.forEach(e -> faces.addAll(e.getValue()));

        faces.forEach(e -> trimmedBase.add(new SimplePanelBase(e)));

        initPanelByBaseFunc(trimmedBase, Function.Transport);
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
                panels.add(new F_Example(face.getShape()));
                break;
            case Stair:
                panels.add(new F_WindowArray(face.getShape()));
        }
    }

    private void initBeams() {
        Map<Double, List<Beam>> beamMap = building.getBeamMap();

        Set<Map.Entry<Double, List<Beam>>> entries = beamMap.entrySet();
        for (var entry : entries) {
            List<Beam> beams = entry.getValue();
            for (Beam b : beams) {
                if (b.getPosType() == PosType.Center) {
                    panels.add(new RecBeam(b.getSegment(), RecBeam.BeamType.Center));
                } else
                    panels.add(new RecBeam(b.getSegment(), RecBeam.BeamType.Side));
            }
//            beams.forEach(e -> panels.add(new RecBeam(e.getSegment(), RecBeam.BeamType.Side)));
        }
    }

    private void initRoofPanels() {
        List<PanelBase> roofBaseList = building.getRoofBaseList();
        for (var p : roofBaseList) {
            panels.add(new RoofSimple(p.getShape()));
        }
    }

    private void initInnerPanel() {
        List<PanelBase> innerBaseList = building.getInnerWallBaseList();
        for (var p : innerBaseList) {
            SimplePanel simplePanel = new SimplePanel(p.getShape(), 50);
            panels.add(simplePanel);
        }
    }

    private void initFloorPanel() {
        List<PanelBase> list = building.getFloorBaseList();
        for (var p : list) {
            SimplePanel simplePanel = new SimplePanel(p.getShape(), 200);
            panels.add(simplePanel);
        }
    }

    @Deprecated
    private void initInnerWall() {
        innerWallPolygons = new LinkedList<>();

        List<PanelBase> innerWallBase = building.getInnerWallBaseList();
        innerWallBase.forEach(e -> innerWallPolygons.add(e.getShape()));
    }

    private void initParapet() {
        parapetPolygons = new LinkedList<>();
        List<PanelBase> roofBaseList = building.getRoofBaseList();
        for (var base : roofBaseList) {
            WB_Polygon shape = base.getShape();
            List<WB_Segment> segments = shape.toSegments();
            segments.forEach(e -> parapetPolygons.add(GeoTools.getRecBySegAndWidth(e, 1000, new WB_Vector(0, 0, 1))));
        }
    }


    public void draw() {
        background(255);
        cameraController.drawSystem(10000);

        for (var panel : panels) {
            panel.draw(render);
        }

        //每层的地面
        pushStyle();
        fill(195, 195, 195);

//        屋顶
//        render.drawPolygonEdges(building.getRoofBaseList().get(0).getShape());

//        for (var p : parapetPolygons) {
//            render.drawPolygonEdges(p);
//        }

//        for (var p : floorPolygons) {
//            render.drawPolygonEdges(p);
//        }

        for (var p : innerWallPolygons) {
            render.drawPolygonEdges(p);
        }

        popStyle();

//        buildingRender.renderAll();
    }

}
