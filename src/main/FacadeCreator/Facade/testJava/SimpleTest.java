package Facade.testJava;

import Facade.facade.unit.styles.S_ExtrudeIn;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.processing.WB_Render3D;

/**
 * @auther Alessio
 * @date 2023/2/21
 **/
public class SimpleTest extends PApplet {
    CameraController cam;
    WB_Render3D render;
    S_ExtrudeIn example;

    public static void main(String[] args) {
        PApplet.main(SimpleTest.class.getName());
    }

    public void settings() {
        size(1280, 660, P3D);
    }

    public void setup() {
        cam = new CameraController(this, 1000);
        render = new WB_Render3D(this);

        WB_Point[] pts = new WB_Point[]{
                new WB_Point(0, 0, 0),
                new WB_Point(0, 8000, 0),
                new WB_Point(0, 8000, 3000),
                new WB_Point(0, 0, 3000)
        };

        example = new S_ExtrudeIn(pts);
    }

    public void draw() {
        background(255);
        example.draw(render);
        camera();
    }

}
