package renders;

import processing.core.PApplet;
import unit2Vol.Unit;
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

    private final PApplet applet;

    private final WB_Render render;

    private final Unit unit;

    private List<WB_Polygon> rndShapes;

    private WB_Polygon topShape;

    private WB_Polygon bottomShape;

    private List<WB_Point> midPts;

    private WB_Point posPt;

    private WB_Point topPt;

    private List<WB_Polygon> panelFaceShapes;

    public UnitRender(PApplet applet, Unit unit) {
        this.applet = applet;
        this.unit = unit;

        render = new WB_Render(applet);

        initGeos();
    }

    /**
     * 初始化需要渲染的物件
     */
    private void initGeos() {
        midPts = new LinkedList<>();
        rndShapes = new LinkedList<>();

        List<Face> rndFaces = unit.getAllFaces();
        for (Face face : rndFaces) {
            rndShapes.add(face.getShape());
            midPts.add(face.getMidPos());
        }

        topShape = unit.getTopFace().getShape();
        bottomShape = unit.getBottomFace().getShape();
        posPt = unit.getMidPt();
        topPt = unit.getTopFace().getMidPos();

        initPanelFaceGeos();
    }

    /**
     * 初始化ifPanel为true的geos
     */
    private void initPanelFaceGeos() {
        panelFaceShapes = new LinkedList<>();

        unit.getAllFaces().stream().filter(Face::isIfPanel).forEach(e -> panelFaceShapes.add(e.getShape()));
    }

    public void renderPanelShape() {
        applet.pushStyle();
        applet.fill(100, 100, 0, 90);
        for (WB_Polygon p : panelFaceShapes) {
            render.drawPolygonEdges(p);
        }
        applet.popStyle();
    }

    public void renderRndShape() {
        applet.pushStyle();
        applet.fill(0, 100, 0, 30);
        for (WB_Polygon p : rndShapes) {
            render.drawPolygonEdges(p);
        }
        applet.popStyle();
    }

    public void renderTopShape() {
        applet.pushStyle();
        applet.fill(100, 0, 0, 50);
        render.drawPolygonEdges(topShape);
        applet.popStyle();
    }

    public void renderBottomShape() {
        applet.pushStyle();
        applet.fill(0, 0, 100, 50);
        render.drawPolygonEdges(bottomShape);
        applet.popStyle();
    }

    public void renderFaceMidPt() {
        applet.pushStyle();
        applet.fill(255, 0, 100, 80);
        for (WB_Point p : midPts) {
            render.drawPoint(p, 50);
        }
        applet.popStyle();
    }

    public void renderPosPt() {
        applet.pushStyle();
        applet.fill(0, 0, 200, 50);
        render.drawPoint(posPt, 100);
        applet.popStyle();
    }

    public void renderId() {
        applet.pushStyle();
        String context = String.valueOf(unit.getId());
        applet.fill(0, 0, 0);
        applet.strokeWeight(3);
        applet.textSize(1300);
        applet.textMode(PApplet.SHAPE);
        applet.text(context, topPt.xf(), topPt.yf(), topPt.zf());
        applet.popStyle();
    }


    /**
     * 渲染所有图元
     * 除了panel geo信息
     */
    public void renderAll() {
        renderRndShape();
        renderRndShape();
        renderTopShape();
        renderBottomShape();
        renderFaceMidPt();
        renderPosPt();
        renderId();
//        rendPanelShape();
    }

}
