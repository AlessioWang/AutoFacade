package Facade.testJava;



import Facade.facade.basic.ControlPanel;
import guo_cam.CameraController;
import guo_cam.Vec_Guo;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render3D;

import Facade.facade.unit.styles.F_Example;

public class TestGUI extends PApplet {
    CameraController cam;
    WB_Render3D render;
    ControlPanel panel;
//    F_WindowArray example;
    F_Example example;

    public void settings() {
        size(1280, 960, P3D);
    }

    public static void main(String[] args) {
        PApplet.main(TestGUI.class.getName());
    }

    public void setup() {
        cam = new CameraController(this, 200);
        cam.getCamera().setPosition(new Vec_Guo(20000,5000,2000));
        cam.getCamera().setLookAt(new Vec_Guo(0,5000,2000));
        render = new WB_Render3D(this);
        WB_Point[] pts = new WB_Point[]{
                new WB_Point(0, 0, 0),
                new WB_Point(0, 8000, 0),
                new WB_Point(0, 8000, 3000),
                new WB_Point(0, 0, 3000)
        };

//        example = new F_WindowArray(pts);
        example = new F_Example(pts);
        poly = new WB_Polygon(pts);
        panel = new ControlPanel(this, ControlPanel.Mode.Slider);

        panel.updatePanel(example,"F_WindowArray");
    }

    WB_Polygon poly;
    public void draw() {
        background(255);
        cam.drawSystem(10000);
        example.draw(render);
        noFill();
        stroke(255,0,0);
        render.drawPolygonEdges(poly);
        camera();
    }


}

