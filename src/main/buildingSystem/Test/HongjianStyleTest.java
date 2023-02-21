package Test;

import Facade.facade.unit.styles.S_ExtrudeIn;
import Tools.GeoTools;
import guo_cam.CameraController;
import processing.core.PApplet;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2023/2/21
 **/
public class HongjianStyleTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main(HongjianStyleTest.class.getName());
    }

    private CameraController cameraController;

    private WB_Render render;

    private Unit unit;

    private List<S_ExtrudeIn> panelList;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);

        initUnit();

        initPanel();
    }

    private void initUnit() {
        WB_Point pos = new WB_Point(1000, 0, 2000);
        WB_Polygon base = GeoTools.createRecPolygon(12000, 8000);
        WB_Vector dir = new WB_Vector(2, 1, 0);

        unit = new Unit(pos, base, dir, 3500);
    }

    private void initPanel() {
        panelList = new LinkedList<>();

        List<Face> allFaces = unit.getRndFaces();

        allFaces.stream().forEach(e -> panelList.add(new S_ExtrudeIn(e.getShape())));
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);


        for (var panel : panelList) {
            panel.draw(render);
        }
    }

}
