package Facade.testJava;

import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.hemesh.HEC_Box;
import wblut.hemesh.HE_Mesh;
import wblut.processing.WB_Render3D;

public class TestTriangle extends PApplet {
    CameraController cam;
    WB_Render3D render;

    public void settings() {
        size(1280, 960, P3D);
    }

    public static void main(String[] args) {
        PApplet.main(TestTriangle.class.getName());
    }

    HE_Mesh mesh;

    public void setup() {
        cam = new CameraController(this, 200);
        render = new WB_Render3D(this);
        mesh = new HEC_Box().create();
        mesh.triangulate();
        System.out.println("faces: "+mesh.getNumberOfFaces());
        for (int triangle : mesh.getTriangles()) {
            System.out.println(triangle);
        }

    }

    public void draw() {
        background(255);
        cam.drawSystem(300);
        render.drawFaces(mesh);
    }

}

