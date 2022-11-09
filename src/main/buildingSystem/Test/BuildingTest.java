package Test;

import Tools.GeoTools;
import construction.Building;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render;

import java.util.List;

/**
 * @auther Alessio
 * @date 2022/11/9
 **/
public class BuildingTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(BuildingTest.class.getName());
    }

    private Building building;

    private WB_Polygon baseline;

    private CameraController cameraController;

    private WB_Render render;

    private List<WB_Polygon> geos;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 3000);

        render = new WB_Render(this);

        baseline = GeoTools.createRecPolygon(15000, 30000);

        building = new Building(baseline, 6000);

        geos = building.getFacadeGeos();

    }

    public void draw() {
        background(255);

        for (WB_Polygon polygon : geos) {
            pushStyle();
            fill(0, 100, 0,20);
            stroke(100, 0, 0);
            render.drawPolygonEdges(polygon);
            popStyle();
        }
    }

}
