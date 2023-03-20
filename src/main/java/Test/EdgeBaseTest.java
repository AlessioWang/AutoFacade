package Test;

import facade.basic.ControlPanel;
import facade.unit.sjStyles.S_Corner_Component_AtCorner;
import guo_cam.Camera;
import guo_cam.CameraController;
import processing.core.PApplet;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import unit2Vol.panelBase.EdgePanelBase;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render3D;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2023/3/20
 **/
public class EdgeBaseTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(EdgeBaseTest.class.getName());
    }

    Unit unit;
    List<Face> faceList;

    CameraController cam;
    WB_Render3D render;
    ControlPanel panel;

    Camera camera;
    WB_Polygon poly;
    int standardUnitDividedNum = 16;

    S_Corner_Component_AtCorner example;

    WB_Point[] basePts;

    public void settings() {
        size(1800, 600, P3D);
    }

    public void setup() {
        new EdgeBaseTest();
        System.out.println("pts     " + Arrays.toString(basePts));

        basePts[2] = new WB_Point(-basePts[2].xd(), basePts[2].yd(), basePts[2].zd());
        basePts[3] = new WB_Point(-basePts[3].xd(), basePts[3].yd(), basePts[3].zd());
        cam = new CameraController(this, 200);

        camera = cam.getCamera();
        render = new WB_Render3D(this);

        poly = new WB_Polygon(basePts);

        example = new S_Corner_Component_AtCorner(basePts, standardUnitDividedNum);
        panel = new ControlPanel(this, ControlPanel.Mode.Slider);
        panel.updatePanel(example, "S_Corner_Component_AtCorner");
    }

    public EdgeBaseTest() {
        initFaces();

        EdgePanelBase base = new EdgePanelBase(faceList);

        basePts = base.getBasePts();
        System.out.println(Arrays.toString(basePts));
    }

    private void initFaces() {
        faceList = new LinkedList<>();

        WB_Point[] pts = new WB_Point[]{
                new WB_Point(0, 0, 0),
                new WB_Point(0, 7000, 0),
                new WB_Point(5000, 7000, 0),
                new WB_Point(5000, 0, 0)
        };
        WB_Polygon polygon = new WB_Polygon(pts);

        unit = new Unit(polygon, 3000);

        List<Face> rndFaces = unit.getRndFaces();
        faceList.add(rndFaces.get(1));
        faceList.add(rndFaces.get(2));
    }

    public void draw() {
        background(255);
        cam.drawSystem(10000);
        example.draw(render, this);
        strokeWeight(3);
        render.drawPolygonEdges(poly);
        noFill();
        stroke(255, 0, 0);
        camera();

    }
}
