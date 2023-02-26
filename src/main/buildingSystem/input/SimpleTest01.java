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

        initPanel();

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

        render.drawPolygonEdges(building.getRoofBaseList().get(1).getShape());

        buildingRender.renderAll();
    }
}
