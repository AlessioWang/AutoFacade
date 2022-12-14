package Test;

import convertor.DxfConvertor;
import facadeGen.Panel.PanelStyle.Panel;
import facadeGen.Panel.PanelBase.Base;
import facadeGen.Panel.PanelBase.BasicBase;
import facadeGen.Panel.PanelGeos;
import facadeGen.Panel.PanelRender;
import facadeGen.Panel.PanelStyle.StyleCustom;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

/**
 * 测试一组使用代码动态地初始化一面墙的方法
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
    //    DxfInput dxfInput;
    DxfConvertor dxfConvertor;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);

        dxfConvertor = new DxfConvertor("E:\\INST.AAA\\SchoolProject\\dxf\\panelTest.dxf");
        initPanelGeo();
    }

    private void initPanelGeo() {
        //panel样式
        Panel panel_01 = createPanelFromDxfByIndex(0);
        Panel panel_02 = createPanelFromDxfByIndex(1);
        Panel panel_03 = createPanelFromDxfByIndex(2);
        Panel panel_04 = createPanelFromDxfByIndex(3);
        Panel panel_05 = createPanelFromDxfByIndex(4);

        //实际物理空间中的面板panel
        PanelGeos panelGeos_01 = new PanelGeos(panel_01, new WB_Point(0, 0, 0), new WB_Vector(0, 1, 0));
        PanelGeos panelGeos_02 = new PanelGeos(panel_02, new WB_Point(-6000, 0, 0), new WB_Vector(0, 1, 0));
        PanelGeos panelGeos_03 = new PanelGeos(panel_03, new WB_Point(0, -4000, 0), new WB_Vector(0, 1, 0));
        PanelGeos panelGeos_04 = new PanelGeos(panel_04, new WB_Point(-6000, -4000, 0), new WB_Vector(0, 1, 0));
        PanelGeos panelGeos_05 = new PanelGeos(panel_04, new WB_Point(-12000, 0, 0), new WB_Vector(0, 1, 0));
        PanelGeos panelGeos_06 = new PanelGeos(panel_01, new WB_Point(-12000, -4000, 0), new WB_Vector(0, 1, 0));
        PanelGeos panelGeos_07 = new PanelGeos(panel_05, new WB_Point(-30000, -4000, 0), new WB_Vector(0, 1, 0));

        panelRender = new PanelRender(this, render, panelGeos_01, panelGeos_02, panelGeos_03, panelGeos_04, panelGeos_05,panelGeos_06,panelGeos_07);
    }

    private Panel createPanelFromDxfByIndex(int index) {
        WB_Polygon inputPolygon = dxfConvertor.getOriPanelBounds().get(index);
        // TODO: 2022/6/24 hash比较方法需要复写
        WB_Polygon basePolygon = dxfConvertor.getMapOfInputGeoGroup().get(inputPolygon).getPanelBounds();
        System.out.println("base " + basePolygon);
        Base base = new BasicBase(basePolygon);

        return new StyleCustom(base, dxfConvertor);
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);
        panelRender.renderAll();
    }

}
