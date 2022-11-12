package unit2Vol;

import processing.core.PApplet;
import unit2Vol.face.Face;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/11/12
 **/
public class UnitRender {

    private PApplet applet;

    private WB_Render render;

    private Unit unit;

    private List<WB_Polygon> rndShapes;

    private WB_Polygon topShape;

    private WB_Polygon bottomShape;

    private List<WB_Point> midPts;


    public UnitRender(PApplet applet, Unit unit) {
        this.applet = applet;
        this.unit = unit;

        render = new WB_Render(applet);

        initGeos();
    }

    private void initGeos() {
        midPts = new LinkedList<>();
        rndShapes = new LinkedList<>();

        List<Face> rndFaces = unit.getRndFaces();
        for (Face face : rndFaces) {
            rndShapes.add(face.getShape());
            midPts.add(face.getMidPos());
        }

        topShape = unit.getTopFace().getShape();
        bottomShape = unit.getBottomFace().getShape();
    }

    /**
     * 渲染图元
     */
    public void renderAll() {
        applet.pushStyle();
        applet.fill(0, 100, 0, 30);
        for (WB_Polygon p : rndShapes) {
            render.drawPolygonEdges(p);
        }
        applet.popStyle();

        applet.pushStyle();
        applet.fill(100, 0, 0, 50);
        render.drawPolygonEdges(topShape);
        applet.popStyle();

        applet.pushStyle();
        applet.fill(0, 0, 100, 50);
        render.drawPolygonEdges(bottomShape);
        applet.popStyle();


        applet.pushStyle();
        applet.fill(255, 0, 100, 80);
        for (WB_Point p : midPts) {
            render.drawPoint(p, 50);
        }
        applet.popStyle();
    }


}
