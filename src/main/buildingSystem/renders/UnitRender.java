package renders;

import function.Function;
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

    private List<WB_Polygon> innerFaceShapes;

    private List<WB_Polygon> unitBoundaryShapes;

    private Function function;

    //red
    private int[] classColor;

    //blue
    private int[] openColor;

    //yellow
    private int[] transColor;

    //green
    private int[] stairColor;

    public UnitRender(PApplet applet, Unit unit) {
        this.applet = applet;
        this.unit = unit;

        render = new WB_Render(applet);

        initColor();
        initFunc();
        initGeos();
    }

    private void initColor() {
        classColor = new int[]{239, 0, 23};

        openColor = new int[]{57, 130, 159};

        transColor = new int[]{255, 226, 141};

        stairColor = new int[]{71, 158, 128};
    }

    private void initFunc() {
        function = unit.getFunction();
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
        initInnerFaceGeos();
        initUnitBoundary();
    }

    /**
     * 初始化ifPanel为true的geos
     */
    private void initPanelFaceGeos() {
        panelFaceShapes = new LinkedList<>();

        unit.getAllFaces().stream().filter(Face::isIfPanel).forEach(e -> panelFaceShapes.add(e.getShape()));
    }


    /**
     * 初始化ifPanel为false的geos
     */
    private void initInnerFaceGeos() {
        innerFaceShapes = new LinkedList<>();

        unit.getAllFaces().stream().filter(e -> !e.isIfPanel()).forEach(e -> innerFaceShapes.add(e.getShape()));
    }

    /**
     * 底面轮廓
     */
    private void initUnitBoundary() {
        unitBoundaryShapes = new LinkedList<>();

        unitBoundaryShapes.add(unit.getRealBase());
    }

    public void renderInnerShape() {
        applet.pushStyle();
        applet.fill(124, 149, 234, 240);
        for (WB_Polygon p : innerFaceShapes) {
            render.drawPolygonEdges(p);
        }
        applet.popStyle();
    }

    public void renderPanelShape() {
        applet.pushStyle();
        applet.fill(89, 194, 101, 240);
        for (WB_Polygon p : panelFaceShapes) {
            render.drawPolygonEdges(p);
        }
        applet.popStyle();
    }

    /**
     * 根据功能着色
     */
    private void fillColor(int alpha) {
        switch (function) {
            case ClassRoom:
                applet.fill(classColor[0], classColor[1], classColor[2], alpha);
                break;
            case Transport:
                applet.fill(transColor[0], transColor[1], transColor[2], alpha);
                break;
            case Open:
                applet.fill(openColor[0], openColor[1], openColor[2], alpha);
                break;
            case Stair:
                applet.fill(stairColor[0], stairColor[1], stairColor[2], alpha);
                break;
            default:
                applet.fill(100, 100, 100, alpha);
        }
    }

    public void renderRndShape() {
        applet.pushStyle();
        fillColor(200);
        for (WB_Polygon p : rndShapes) {
            render.drawPolygonEdges(p);
        }
        applet.popStyle();
    }

    public void renderTopShape() {
        applet.pushStyle();
        fillColor(150);
        render.drawPolygonEdges(topShape);
        applet.popStyle();
    }

    public void renderBottomShape() {
        applet.pushStyle();
        fillColor(150);
        render.drawPolygonEdges(bottomShape);
        applet.popStyle();
    }

    public void renderBaseBoundary() {
        applet.pushStyle();
        applet.noFill();
        applet.strokeWeight(3);
        applet.stroke(150, 0, 0);
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
//        renderRndShape();
        renderRndShape();
        renderTopShape();
        renderBottomShape();
//        renderFaceMidPt();
        renderPosPt();
        renderId();
    }

}
