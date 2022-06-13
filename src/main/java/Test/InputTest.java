package Test;

import Convertor.DxfInput;
import FacadeGen.Panel.Panel;
import FacadeGen.Panel.PanelBase.Base;
import FacadeGen.Panel.PanelBase.BasicBase;
import FacadeGen.Panel.PanelGeos;
import FacadeGen.Panel.PanelRender;
import FacadeGen.Panel.panelStyle.StyleA;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

/**
 * @auther Alessio
 * @date 2022/6/10
 **/
public class InputTest extends PApplet {

    public static void main(String[] args) {
        PApplet.main("Test.InputTest");
    }

    //渲染器必要对象
    CameraController cameraController;
    WB_Render render;

    //panel相关
    PanelRender panelRender;
    DxfInput input;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);

        input = new DxfInput("E:\\INST.AAA\\SchoolProject\\dxf\\panelTest.dxf");
        initPanelGeo();
    }

    private void initPanelGeo() {
        WB_Polygon basePolygon = input.getPanelBounds().get(0);
        Base base = new BasicBase(basePolygon);

        Panel panel = new StyleA(base);
        PanelGeos panelGeos = new PanelGeos(panel, new WB_Point(0, 0, 0), new WB_Vector(0, 0, 1));

        panelRender = new PanelRender(this, render, panelGeos);
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);
        panelRender.renderAll();
    }

}
