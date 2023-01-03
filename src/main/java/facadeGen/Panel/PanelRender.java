package facadeGen.Panel;

import processing.core.PApplet;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/4/21
 **/
public class PanelRender {

    private PApplet applet;

    private WB_Render render;

    private List<PanelGeos> panelGeoList;

    /**
     * 渲染多个图元的构造方法
     *
     * @param applet
     * @param render
     * @param panels 需要渲染的诸多Panel图元
     */
    public PanelRender(PApplet applet, WB_Render render, List<PanelGeos> panels) {
        this.applet = applet;
        this.render = render;
        this.panelGeoList = panels;
    }

    /**
     * 渲染一组Panel的构造方法
     *
     * @param applet
     * @param render
     * @param panel
     */
    public PanelRender(PApplet applet, WB_Render render, PanelGeos... panel) {
        this.applet = applet;
        this.render = render;
        panelGeoList = new LinkedList<>();
        this.panelGeoList.addAll(List.of(panel));
    }

    /**
     * 调用渲染所有物件的方法
     */
    public void renderAll() {
        for (PanelGeos geos : panelGeoList) {
            panelRender(geos);
            frameRender(geos);
            beamRender(geos);
            glassRender(geos);
            parapetRender(geos);
        }
    }

    /**
     * 绘制面板本身
     *
     * @param panelGeos
     */
    public void panelRender(PanelGeos panelGeos) {
        applet.pushStyle();
        applet.fill(219, 223, 232);
        applet.noStroke();
        render.drawPolygon(panelGeos.wallGeo);
        applet.popStyle();
    }

    /**
     * 绘制窗户的分隔
     *
     * @param panelGeos
     */
    public void beamRender(PanelGeos panelGeos) {
        applet.pushStyle();
        applet.noFill();
        applet.stroke(80, 50, 20);
        applet.strokeWeight(3);
        for (WB_PolyLine l : panelGeos.beams) {
            render.drawPolylineEdges(l);
        }
        applet.popStyle();
    }

    /**
     * 绘制窗框
     *
     * @param panelGeos
     */
    public void frameRender(PanelGeos panelGeos) {
        applet.pushStyle();
        applet.noFill();
        applet.stroke(78, 101, 122);
        applet.strokeWeight(3);
        for (WB_Polygon p : panelGeos.frames) {
            render.drawPolygonEdges(p);
        }
        applet.popStyle();
    }

    /**
     * 绘制窗户玻璃
     *
     * @param panelGeos
     */
    public void glassRender(PanelGeos panelGeos) {
        applet.pushStyle();
        applet.fill(170, 196, 222);
        for (WB_Polygon p : panelGeos.glasses) {
            render.drawPolygonEdges(p);
        }
        applet.popStyle();
    }

    /**
     * 绘制女儿墙
     *
     * @param panelGeos
     */
    public void parapetRender(PanelGeos panelGeos) {
        applet.pushStyle();
        applet.fill(219, 223, 232);
        for (WB_Polygon p : panelGeos.parapets) {
            render.drawPolygonEdges(p);
        }
        applet.popStyle();
    }

}
