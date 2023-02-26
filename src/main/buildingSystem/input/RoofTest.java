package input;

import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import wblut.processing.WB_Render3D;

/**
 * @auther Alessio
 * @date 2023/2/26
 **/
public class RoofTest extends PApplet {

    private String file = "src\\main\\resources\\dxf\\roofTest.dxf";

    private BuildingInputer buildingInputer;

    private Building building;

    private CameraController cameraController;

    private WB_Render3D render;

    private BuildingRender buildingRender;


    public static void main(String[] args) {
        PApplet.main(RoofTest.class.getName());
    }

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 10000);

        render = new WB_Render3D(this);

        buildingInputer = new BuildingInputer(file);

        building = buildingInputer.getBuilding();

        buildingRender = new BuildingRender(this, building);
    }

    public void draw() {
        background(255);

        cameraController.drawSystem(10000);

        render.drawPolygonEdges(building.getRoofBaseList().get(0).getShape());

//        buildingRender.renderPanelGeo();
    }

}
