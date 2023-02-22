package Test;

import Facade.facade.unit.styles.S_ExtrudeIn;
import Tools.GeoTools;
import guo_cam.CameraController;
import processing.core.PApplet;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import wblut.geom.*;
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

        checkDir();
    }

    private void initUnit() {
        WB_Point pos = new WB_Point(1000, 0, 2000);
        WB_Polygon base = GeoTools.createRecPolygon(8000, 6000);
        WB_Vector dir = new WB_Vector(0, 1, 0);

        unit = new Unit(pos, base, dir, 3000);
    }

    private void initPanel() {
        panelList = new LinkedList<>();

        List<Face> allFaces = unit.getRndFaces();

//        for (int i = 0; i < allFaces.size(); i++) {
//            Face face = allFaces.get(i);
//            System.out.println(face.getDir() + "---" + face.getShape().getNormal());
//        }

        allFaces.forEach(e -> panelList.add(new S_ExtrudeIn(e.getShape())));
    }

    private void checkDir() {
        List<Face> allFaces = unit.getRndFaces();

        for (Face f : allFaces) {
            WB_Vector dir = f.getDir();
            dir.normalizeSelf();

            WB_Polygon shape = f.getShape();
            WB_Vector direction = (WB_Vector) shape.getSegment(0).getDirection();
            WB_Vector direction1 = (WB_Vector) shape.getSegment(1).getDirection();

            WB_Vector cross = direction.cross(direction1);
            cross.normalizeSelf();

            System.out.println(dir + "___" + cross);
        }

    }


    public void draw() {
        background(255);
        cameraController.drawSystem(1000);

        for (var panel : panelList) {
            panel.draw(render);
        }
    }

}
