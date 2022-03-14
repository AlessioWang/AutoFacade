package Client;

import FacadeGen.Panel.Component.PanelComponent;
import FacadeGen.Panel.Component.TianWindow;
import FacadeGen.Panel.Panel;
import FacadeGen.Panel.PanelBase.BasicBase;
import FacadeGen.RenderMgr;
import Tools.W_Tools;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public class PanelTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main("Client.PanelTest");
    }

    Panel panel = new Panel();
    CameraController cameraController;
    WB_Render render;
    HashMap<PanelComponent, WB_Point> comps;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 100);
        render = new WB_Render(this);

        WB_Polygon basePolygon = W_Tools.createRecPolygon(300, 200);
        BasicBase base = new BasicBase(basePolygon);

        WB_Polygon winPolygon1 = W_Tools.createRecPolygon(30, 100);
        WB_Polygon winPolygon2 = W_Tools.createRecPolygon(50, 80);
        WB_Polygon winPolygon3 = W_Tools.createRecPolygon(80,100);
        TianWindow window1 = new TianWindow(winPolygon1, base);
        TianWindow window2 = new TianWindow(winPolygon2, base);
        TianWindow window3= new TianWindow(winPolygon3, base);
        WB_Point pos1 = new WB_Point(50, 30);
        WB_Point pos2 = new WB_Point(150, 30);
        WB_Point pos3 = new WB_Point(200, 80);


        panel.setBase(base);
        panel.addComponents(window1, pos1);
        panel.addComponents(window2, pos2);
        panel.addComponents(window3, pos3);

        comps = panel.getComponents();

    }

    public void draw() {
        background(255);
        //绘制panel面板边界
        render.drawPolygonEdges(panel.getBase().getBasicShape());

        pushStyle();
        stroke(100,20,20);
        strokeWeight(3);
        for (Map.Entry<PanelComponent, WB_Point> entry : comps.entrySet()) {
            PanelComponent win = entry.getKey();
            WB_Polygon comFrame = win.getShape();

            WB_Polygon p = W_Tools.movePolygon(comFrame, entry.getValue());
            render.drawPolygonEdges(p);

        }
        popStyle();


    }


}
