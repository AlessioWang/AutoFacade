package input;

import facade.basic.BasicObject;
import facade.unit.styles.F_TwoWindow;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import unit2Vol.face.Face;
import unit2Vol.panelBase.MergedPanelBase;
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

        render = new WB_Render3D(this);

        buildingInputer = new BuildingInputer(file, 3500);

        building = buildingInputer.getBuilding();

        panels = new LinkedList<>();

        buildingRender = new BuildingRender(this, building);

        init();
    }

    public void init() {
        List<Face> wallAbleFaces = building.getWallAbleFaces();
        List<Face> unionFaces = new LinkedList<>();

        wallAbleFaces.stream().filter(e -> Objects.equals(e.getDir(), new WB_Vector(1, 0, 0))).forEach(unionFaces::add);

        MergedPanelBase mergedPanelBase = new MergedPanelBase(unionFaces);

//        panels.add(new F_OneHole(mergedPanelBase.getShape()));
        panels.add(new F_TwoWindow(mergedPanelBase.getShape()));

        System.out.println("sss " + panels.size());
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(10000);

        for (var panel : panels) {
            panel.draw(render);
        }

//        buildingRender.renderAll();
    }


}
