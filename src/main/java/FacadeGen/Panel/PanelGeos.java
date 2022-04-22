package FacadeGen.Panel;

import FacadeGen.Panel.Component.Window;
import FacadeGen.Panel.Component.WindowGeos;
import Tools.GeoTools;
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
    public Panel panel;
    //基点位置
    public final WB_Point pos;
    //朝向
    public final WB_Vector direction;

    //窗户的位置信息
    public HashMap<Window, WB_Point> windowComps;

    //需要渲染的图元
    public WB_Polygon wallGeo;
    public List<WB_Polygon> winBoundaries = new LinkedList<>();
    public List<WB_Polygon> frames = new LinkedList<>();
    public List<WB_PolyLine> beams = new LinkedList<>();
    public List<WB_Polygon> glasses = new LinkedList<>();

    public PanelGeos(Panel panel, WB_Point pos, WB_Vector direction) {
        this.panel = panel;
        this.pos = pos;
        this.direction = direction;

        //获取窗户的位置信息
        windowComps = panel.getWindowsComps();

        //初始化各种图元信息
        iniGeo();
    }

    //初始化窗户的几何图元信息
    private void iniGeo() {
        for (Map.Entry<Window, WB_Point> entry : windowComps.entrySet()) {
            Window win = entry.getKey();
            WindowGeos windowGeos = win.getWindowGeos();

            //获取图元相对于panel的相对位置（未进行三维转换）
            WB_Polygon rawFrame = GeoTools.movePolygon(windowGeos.getFrameBase2D(), entry.getValue());
            WB_Polygon rawBoundary = GeoTools.movePolygon(windowGeos.getFrameBoundary(), entry.getValue());
            WB_Polygon rawGlass = GeoTools.movePolygon(windowGeos.getGlassShape(), entry.getValue());

            frames.add(GeoTools.transferPolygon3D(rawFrame, pos, direction));
//            winBoundaries.add(GeoTools.transferPolygon3D(rawBoundary, pos, direction));
            winBoundaries.add(rawBoundary);
            glasses.add(GeoTools.transferPolygon3D(rawGlass, pos, direction));

            List<WB_PolyLine> rawBeams = windowGeos.getAll2DBeams();
            for (WB_PolyLine l : rawBeams) {
                WB_PolyLine rawL = GeoTools.movePolyline(l, entry.getValue());
                beams.add(GeoTools.transferPolyline3D(rawL, pos, direction));
            }
        }

        WB_Polygon baseShape = panel.getBase().getBasicShape();
//        wallGeo = GeoTools.getPolygonWithHoles(GeoTools.transferPolygon3D(baseShape, pos, direction), winBoundaries);
        wallGeo = GeoTools.transferPolygon3D(GeoTools.getPolygonWithHoles(baseShape, winBoundaries), pos, direction);
    }


}
