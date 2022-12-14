package facadeGen.Panel;

import facadeGen.Panel.Component.Window;
import facadeGen.Panel.Component.WindowGeos;
import Tools.GeoTools;
import facadeGen.Panel.PanelStyle.Panel;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;

import java.util.*;

/**
 * 存放panel的几何图元用于渲染计算等
 *
 * @auther Alessio
 * @date 2022/3/4
 **/
public class PanelGeos {
    //图元所在的panel
    public Panel panel;

    //基点位置
    public WB_Point pos;

    //panel 第一条边的方向
    public WB_Vector v1;

    //panel 第二条边的方向
    public WB_Vector v2;

    public WB_Polygon target;

    //朝向
    public WB_Vector direction;

    //窗户的位置信息
    public HashMap<Window, WB_Point> windowComps;

    //需要渲染的图元
    public WB_Polygon wallGeo;
    public List<WB_Polygon> winBoundaries = new LinkedList<>();
    public List<WB_Polygon> frames = new LinkedList<>();
    public List<WB_PolyLine> beams = new LinkedList<>();
    public List<WB_Polygon> glasses = new LinkedList<>();

    public PanelGeos() {
    }

    public PanelGeos(Panel panel, WB_Point pos, WB_Polygon target, WB_Vector direction) {
        this.panel = panel;
        this.pos = pos;
        this.direction = direction;
        this.target = target;

        //获取窗户的位置信息
        windowComps = panel.getWindowsComps();

        //初始化各种图元信息
        iniGeo();
    }


    public PanelGeos(Panel panel, WB_Point pos, WB_Vector v1, WB_Vector v2, WB_Vector direction) {
        this.panel = panel;
        this.pos = pos;
        this.direction = direction;
        this.v1 = v1;
        this.v2 = v2;

        //获取窗户的位置信息
        windowComps = panel.getWindowsComps();

        //初始化各种图元信息
        iniGeo();
    }

    public PanelGeos(Panel panel, WB_Point pos, WB_Vector direction) {
        this.panel = panel;
        this.pos = pos;
        this.direction = direction;

        //获取窗户的位置信息
        windowComps = panel.getWindowsComps();

        //初始化各种图元信息
        iniGeo();
    }

    /**
     * 初始化panel
     */
    public void iniPanel() {
        //获取窗户的位置信息
        windowComps = panel.getWindowsComps();

        //初始化各种图元信息
        iniGeo();
    }


    /**
     * 初始化panel与window的几何图元信息
     */
    private void iniGeo() {
        initWindowGeo();
        initPanelGeo();
    }

    /**
     * window的几何信息
     */
    private void initWindowGeo() {
        for (Map.Entry<Window, WB_Point> entry : windowComps.entrySet()) {
            Window win = entry.getKey();
            WindowGeos windowGeos = win.getWindowGeos();

            //获取图元相对于panel的相对位置（未进行三维转换）
            WB_Polygon rawFrame = GeoTools.movePolygon(windowGeos.getFrameBase2D(), entry.getValue());
            WB_Polygon rawBoundary = GeoTools.movePolygon(windowGeos.getFrameBoundary(), entry.getValue());
            WB_Polygon rawGlass = GeoTools.movePolygon(windowGeos.getGlassShape(), entry.getValue());

            frames.add(GeoTools.transferPolygon3dByAxis(rawFrame, pos, direction));
            winBoundaries.add(rawBoundary);
            glasses.add(GeoTools.transferPolygon3dByAxis(rawGlass, pos, direction));

            List<WB_PolyLine> rawBeams = windowGeos.getAll2DBeams();
            for (WB_PolyLine l : rawBeams) {
                WB_PolyLine rawL = GeoTools.movePolyline(l, entry.getValue());
                beams.add(GeoTools.transferPolyline3dByAxis(rawL, pos, direction));
            }
        }
    }

    /**
     * panel的几何信息
     */
    private void initPanelGeo() {
        WB_Polygon baseShape = panel.getBase().getBasicShape();
        WB_Polygon polygonWithHoles = GeoTools.getPolygonWithHoles(baseShape, winBoundaries);

        wallGeo = GeoTools.transferPolygon3dByAxis(polygonWithHoles, pos, direction);

        System.out.println("pos " + pos);
        System.out.println("------------------------------");
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public void setPos(WB_Point pos) {
        this.pos = pos;
    }

    public void setDirection(WB_Vector direction) {
        this.direction = direction;
    }
}
