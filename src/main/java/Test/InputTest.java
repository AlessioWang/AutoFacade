package Test;

import Convertor.DxfInput;
import FacadeGen.Panel.Panel;
import FacadeGen.Panel.PanelBase.Base;
import FacadeGen.Panel.PanelBase.BasicBase;
import FacadeGen.Panel.PanelGeos;
import FacadeGen.Panel.PanelRender;
import FacadeGen.Panel.panelStyle.StyleInput;
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
    DxfInput dxfInput;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);

        dxfInput = new DxfInput("E:\\INST.AAA\\SchoolProject\\dxf\\panelTest.dxf");
        initPanelGeo();
    }

    private void initPanelGeo() {
        WB_Polygon basePolygon = dxfInput.getPanelBounds().get(0);
        Base base = new BasicBase(basePolygon);

        Panel panel = new StyleInput(base, dxfInput);
        PanelGeos panelGeos = new PanelGeos(panel, new WB_Point(0, 0, 0), new WB_Vector(0, 1, 0));
        PanelGeos panelGeos1 = new PanelGeos(panel, new WB_Point(0, -4000, 0), new WB_Vector(0, 1, 0));
        PanelGeos panelGeos2 = new PanelGeos(panel, new WB_Point(0, -8000, 0), new WB_Vector(0, 1, 0));

        panelRender = new PanelRender(this, render, panelGeos, panelGeos1, panelGeos2);
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);
        panelRender.renderAll();
    }

}
