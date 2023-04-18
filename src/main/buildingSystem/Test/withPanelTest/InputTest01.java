package Test.withPanelTest;

import Tools.DxfReader.DXFImporter;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import unit2Vol.Unit;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2023/2/19
 **/
public class InputTest01 extends PApplet {
    public static void main(String[] args) {
        PApplet.main(InputTest01.class.getName());
    }

    private String file = "src\\main\\resources\\dxf\\input_test.dxf";

    private CameraController cameraController;

    private BuildingRender buildingRender;

    private Building building;

    private DXFImporter importer;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 50000);

        buildingRender = new BuildingRender(this, building);

        importer = new DXFImporter(file, DXFImporter.UTF_8);
    }

    private void initBuilding() {
        List<Unit> units = new LinkedList<>();

        building = new Building(units);
    }

    public void draw() {
        background(255);

        cameraController.drawSystem(1000);

        buildingRender.renderAll();
    }

}
