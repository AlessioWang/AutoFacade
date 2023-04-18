package Test.withPanelTest;

import facade.basic.BasicObject;
import facade.basic.Material;
import facade.unit.styles.F_OneWindow;
import guo_cam.CameraController;
import input.BuildingInputer;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import unit2Vol.face.Face;
import unit2Vol.panelBase.MergedPanelBase;
import unit2Vol.panelBase.PanelBase;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render3D;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @auther Alessio
 * @date 2023/3/16
 **/
public class UnionTest extends PApplet {

    private String file = "src\\main\\resources\\dxf\\oneUnit.dxf";

    private BuildingInputer buildingInputer;

    private Building building;

    private BuildingRender buildingRender;

    private List<BasicObject> panels;

    private CameraController cameraController;

    private WB_Render3D render;

    public static void main(String[] args) {
        PApplet.main(UnionTest.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 10000);

        cameraController.getCamera().setPerspective(false);

        render = new WB_Render3D(this);

        buildingInputer = new BuildingInputer(file, 3500);

        building = buildingInputer.getBuilding();

        panels = new LinkedList<>();

        buildingRender = new BuildingRender(this, building);

        init();
    }

    public void init() {
        List<Face> wallAbleFaces = building.getAllPanelableFaces();

        List<Face> unionFaces = new LinkedList<>();

        wallAbleFaces.stream().filter(e -> Objects.equals(e.getDir(), new WB_Vector(0, -1, 0))).forEach(unionFaces::add);

        MergedPanelBase mergedPanelBase = new MergedPanelBase(unionFaces);

        List<PanelBase> roofBaseList = building.getFloorBaseList();

        F_OneWindow.bottom_height = 1800;
        F_OneWindow.extended_distance = 300;
        F_OneWindow.frameMaterial = Material.Blue;
        panels.add(new F_OneWindow(mergedPanelBase.getShape()));

    }

    public void draw() {
        background(255);
//        cameraController.drawSystem(10000);

        for (var panel : panels) {
            panel.draw(render);
        }

//        buildingRender.renderAll();
//        buildingRender.renderBottom();
    }


}
