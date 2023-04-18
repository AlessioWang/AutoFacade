package Test.withPanelTest;

import facade.basic.BasicObject;
import facade.unit.sjStyles.S_Arc_Stretch;
import guo_cam.CameraController;
import input.BuildingInputer;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import unit2Vol.face.Face;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render3D;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2023/3/13
 **/
public class MergeExternalPanelTest extends PApplet {

    private String file = "src\\main\\resources\\dxf\\oneUnit.dxf";

    private BuildingInputer buildingInputer;

    private Building building;

    private CameraController cameraController;

    private WB_Render3D render;

    private BuildingRender buildingRender;

    List<Face> roofAbleFaces;

    private List<WB_Polygon> roofs;

    private List<WB_Polygon> trimmed;

    private List<BasicObject> panels;

    public static void main(String[] args) {
        PApplet.main(MergeExternalPanelTest.class.getName());
    }

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 10000);

        render = new WB_Render3D(this);

        buildingInputer = new BuildingInputer(file);

        building = buildingInputer.getBuilding();

        building.setHeight(1000);

        buildingRender = new BuildingRender(this, building);

        roofAbleFaces = building.getRoofAbleFaces();

        roofs = new LinkedList<>();

        roofAbleFaces.stream().forEach(e -> roofs.add(e.getShape()));

        initPanel();
    }

    WB_Polygon base;

    public void initPanel() {
        panels = new LinkedList<>();

        List<Face> wallAbleFaces = building.getWallAbleFaces();

        Face face = wallAbleFaces.get(3);

//        Changed_S_Corner_Component_Lib panel = new Changed_S_Corner_Component_Lib(face.getShape());
//        base = panel.getBasePolygon();
//        panels.add(panel);
//        panels.add(new S_Corner_Component_Lib(face.getShape()));
//        panels.add(new S_Corner_Component_Lib(face.getShape(), new WB_Transform3D().addTranslate(new WB_Point(0, 0, 1000))));

        wallAbleFaces.forEach(e -> panels.add(new S_Arc_Stretch(e.getShape())));
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(10000);

        for (var panel : panels) {
            panel.draw(render);
        }

        buildingRender.renderPanelGeo();

//        render.drawPolygonEdges(base);
    }

}
