//package java;
//
//import guo_cam.CameraController;
//import guo_cam.Vec_Guo;
//import manager.*;
//import processing.core.PApplet;
//import wblut.processing.WB_Render3D;
//
//import java.manager.G_FacadeTest;
//
///**
// * @classname: archiweb
// * @description:
// * @author: amomorning
// * @date: 2020/11/23
// */
//public class LocalDisplay extends PApplet {
//
//    private Server server;
//    private CameraController cam;
//    WB_Render3D render3D;
//
//    public void settings() {
//        size(1280, 560, P3D);
//    }
//
//    public void setup() {
//        cam = new CameraController(this, 1000);
//        cam.getCamera().setPosition(new Vec_Guo(20000, 5000, 2000));
//        cam.getCamera().setLookAt(new Vec_Guo(0, 5000, 2000));
//        render3D = new WB_Render3D(this);
////        GeneratorDemo generator = new GeneratorDemo(this);
//        G_FacadeTest generator = new G_FacadeTest();
//        server = new Server(generator);
//    }
//
//    public void draw() {
//        background(221);
//        cam.drawSystem(1000);
////        if (Config.LOCAL_DRAW)
////            server.generator.localDraw(render3D);
//    }
//
//    public static void main(String[] args) {
////        if (args.length > 0) {
////            new Server();
////        } else {
//        PApplet.main("LocalDisplay");
////        }
//    }
//}
