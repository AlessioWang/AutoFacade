package input;

import buildingControl.BuildingCreator;
import buildingControl.FacadeMatcher;
import facade.basic.BasicObject;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render3D;

import java.util.List;

/**
 * @auther Alessio
 * @date 2023/3/23
 **/
public class CreatorTest extends PApplet {

//    private String file = "src\\main\\resources\\dxf\\school01.dxf";
    private String file = "src\\main\\resources\\dxf\\schoolsmall.dxf";
//    private String file = "src\\main\\resources\\dxf\\school02.dxf";

    BuildingCreator bc;

    private CameraController cameraController;

    private WB_Render3D render;

    private List<BasicObject> panels;

    private FacadeMatcher facadeMatcher;

    private WB_Polygon ground;

    public static void main(String[] args) {
        PApplet.main(CreatorTest.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
    }

    public void setup() {
        bc = new BuildingCreator(file, 4000);

        facadeMatcher = new FacadeMatcher(bc);

        cameraController = new CameraController(this, 5000);

        render = new WB_Render3D(this);

        init();

        initGround();
    }

    /**
     * 地面
     */
    private void initGround() {
        WB_Point[] pts = new WB_Point[]{
                new WB_Point(0, 0, 0),
                new WB_Point(-0, 100000, 0),
                new WB_Point(100000, 100000, 0),
                new WB_Point(100000, 0, 0),
        };

        ground = new WB_Polygon(pts);
    }

    private void init() {
        panels = facadeMatcher.getPanels();
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(10000);

        for (var panel : panels) {
            panel.draw(render);
        }

        pushStyle();
        fill(71, 158, 128);
        render.drawPolygonEdges(ground);
        popStyle();
    }

}
