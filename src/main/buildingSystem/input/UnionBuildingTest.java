package input;

import Tools.GeoTools;
import facade.basic.BasicObject;
import facade.unit.sjStyles.S_Corner_Component_Lib;
import facade.unit.styles.*;
import function.Function;
import function.PosType;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Beam;
import unit2Vol.Building;
import unit2Vol.face.Face;
import unit2Vol.panelBase.MergedPanelBase;
import unit2Vol.panelBase.PanelBase;
import unit2Vol.panelBase.SimplePanelBase;
import unit2Vol.panelBase.SplitPanelBase;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render3D;

import java.util.*;

/**
 * @auther Alessio
 * @date 2023/3/16
 **/
public class UnionBuildingTest extends PApplet {

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
    List<PanelBase> trimmedBase;

    List<SplitPanelBase> splitPanelBases;

    List<WB_Polygon> parapetPolygons;

    List<WB_Polygon> floorPolygons;

    List<WB_Polygon> innerWallPolygons;

    WB_Polygon ground;

    public static void main(String[] args) {
        PApplet.main(UnionBuildingTest.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 10000);

        render = new WB_Render3D(this);

        new SimpleTest01();

        buildingRender = new BuildingRender(this, building);

        initGround();
    }

    public UnionBuildingTest() {
        buildingInputer = new BuildingInputer(file, 3500);

        initBuildingFromDxf();

        initRoofPanels();

        initBeams();

        initInnerPanel();

        initFloorPanel();

        initFuncPanel();

        initTrimmed();

        initInnerWall();

        initParapet();
    }

    /**
     * 地面
     */
    private void initGround() {
        WB_Point[] pts = new WB_Point[]{
                new WB_Point(0, 0, 0),
                new WB_Point(-0, 100000, 0),
                new WB_Point(100000, 100000, 0),
                new WB_Point(100000, 0, 0),
        };

        ground = new WB_Polygon(pts);
    }

    private void initBuildingFromDxf() {
        building = buildingInputer.getBuilding();
        panels = new LinkedList<>();
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

        List<Face> stairMerges = new LinkedList<>();

        for (Face f : wallAbleFaces) {
            if (f.getFunction().equals(Function.Stair) && Objects.equals(f.getDir(), new WB_Vector(0, -1, 0))) {
                stairMerges.add(f);
            } else {
                func2Base(f);
            }
        }

        MergedPanelBase stair = new MergedPanelBase(stairMerges);
        panels.add(new F_Example(stair.getShape()));

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

        faces.forEach(this::func2Panel);

    }

    private void func2Base(Face face) {
        Function function = face.getFunction();
        switch (function) {
            case ClassRoom:
                classBase.addAll(new SplitPanelBase(face, new double[]{0.5}).getPanelBases());
//                classBase.add(new SimplePanelBase(face));
                break;
            case Transport:
                transBase.addAll(getPanelBaseByLength(face));
                break;
            case Stair:
                stairBase.add(new SimplePanelBase(face));
        }
    }

    /**
     * 根据的长度确定分段个数
     *
     * @param face
     * @return
     */
    private List<PanelBase> getPanelBaseByLength(Face face) {
        List<PanelBase> result = new LinkedList<>();

        WB_Polygon shape = face.getShape();
        List<WB_Segment> segments = shape.toSegments();

        double maxL = Double.MIN_VALUE;
        for (WB_Segment s : segments) {
            if (s.getLength() > maxL)
                maxL = s.getLength();
        }

        if (maxL <= 4000) {
            result.add(new SimplePanelBase(face));
        } else if (maxL > 4000 && maxL <= 8000) {
            result.addAll(new SplitPanelBase(face, new double[]{0.5}).getPanelBases());
        } else {
            result.addAll(new SplitPanelBase(face, new double[]{0.25, 0.5, 0.75}).getPanelBases());
        }

        return result;
    }

    private void initPanelByBaseFunc(List<PanelBase> bases, Function function) {
        switch (function) {
            case ClassRoom:
                bases.forEach(e -> panels.add(new S_ExtrudeIn(e.getShape())));
                break;
            case Transport:
                bases.forEach(e -> panels.add(new S_Corner_Component_Lib(e.getShape())));
                break;
            case Stair:
                bases.forEach(e -> panels.add(new F_Example(e.getShape())));
        }
    }

    private void func2Panel(Face face) {
        Function function = face.getFunction();
        switch (function) {
            case ClassRoom:
                panels.add(new S_ExtrudeIn(face.getShape()));
                break;
            case Transport:
                panels.add(new S_Corner_Component_Lib(face.getShape()));
                break;
            case Stair:
                panels.add(new F_Example(face.getShape()));
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


    }

}
