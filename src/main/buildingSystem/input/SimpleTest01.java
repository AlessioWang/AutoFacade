package input;

import facade.basic.BasicObject;
import facade.unit.styles.S_ExtrudeIn;
import guo_cam.CameraController;
import processing.core.PApplet;
import unit2Vol.Building;
import unit2Vol.face.Face;
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

    private List<BasicObject> objects;

    private CameraController cameraController;

    private WB_Render3D render;

    public static void main(String[] args) {
        PApplet.main(SimpleTest01.class.getName());
    }

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);

        render = new WB_Render3D(this);

        new SimpleTest01();
    }

    public SimpleTest01() {
        buildingInputer = new BuildingInputer(file);

        test();
    }

    private void test() {
        building = buildingInputer.getBuilding();
        objects = new LinkedList<>();

        List<Face> wallAbleFaces = building.getWallAbleFaces();

        wallAbleFaces.forEach(e -> objects.add(new S_ExtrudeIn(e.getShape())));
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(10000);

        for (var panel : objects) {
            panel.draw(render);
        }

    }
}
