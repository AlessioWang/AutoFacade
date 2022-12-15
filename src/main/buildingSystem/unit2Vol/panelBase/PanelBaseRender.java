package unit2Vol.panelBase;

import processing.core.PApplet;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render;

import java.util.LinkedList;
import java.util.List;

/**
 * 渲染panelBase的图元类
 *
 * @auther Alessio
 * @date 2022/12/15
 **/
public class PanelBaseRender {
    private final PApplet applet;

    private final WB_Render render;

    private List<PanelBase> panelBaseList;

    private List<WB_Polygon> polygons;

    public PanelBaseRender(PApplet applet, List<PanelBase> panelBaseList) {
        this.applet = applet;
        this.panelBaseList = panelBaseList;
        render = new WB_Render(applet);

        initGeos();
    }

    private void initGeos() {
        polygons = new LinkedList<>();
        for (PanelBase panelBase : panelBaseList) {
            polygons.add(panelBase.getShape());
        }
    }

    public void renderAll() {
        applet.pushStyle();
        applet.noFill();
        applet.strokeWeight(2);
        applet.stroke(10,120,100);
        for (WB_Polygon p : polygons) {
            render.drawPolygonEdges(p);
        }
        applet.popStyle();
    }


}
