package Test;

import facadeGen.Panel.PanelGeos;
import facadeGen.Panel.PanelRender;
import facadeGen.PanelFac;
import client.model.WallPanelEntity;
import client.service.WallPanelService;
import guo_cam.CameraController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import processing.core.PApplet;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/4/22
 **/
public class SQLRenderTest extends PApplet {
    public static void main(String[] args) {
        PApplet.main("Test.SQLRenderTest");
    }

    //渲染器必要对象
    CameraController cameraController;
    WB_Render render;

    //bean对象
    WallPanelService service;
    ApplicationContext JDBCcontext;
    ApplicationContext beanContext;
    PanelFac fac;
    //panel相关对象
    List<PanelGeos> geos;
    PanelRender panelRender;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        iniBeans();
        cameraController = new CameraController(this, 1000);
        render = new WB_Render(this);
        geos = new LinkedList<>();

        //从数据库初始化面板
        iniPanel();

        panelRender = new PanelRender(this, render, geos);
    }

    private void iniBeans() {
        JDBCcontext = new ClassPathXmlApplicationContext("JDBCBean.xml");
        beanContext = new ClassPathXmlApplicationContext("BeanRender.xml");
        service = JDBCcontext.getBean(WallPanelService.class);
        fac = beanContext.getBean(PanelFac.class);
        fac.init();
    }

    //渲染所有的panel
    private void iniPanel() {
        List<WallPanelEntity> entities = service.selectAll();
        for (WallPanelEntity e : entities) {
            geos.add(transEntity2Geos(e));
        }
    }

    //按照类型渲染
//    private void iniPanel() {
//        List<WallPanelEntity> entities = service.selectByStyle("StyleA");
//        for (WallPanelEntity e : entities) {
//            geos.add(transEntity2Geos(e));
//        }
//    }

//    //渲染指定编号的panel
//    private void iniPanel() {
//        WallPanelEntity entity = service.selectByIndex(1);
//        geos.add(transEntity2Geos(entity));
//    }

    public PanelGeos transEntity2Geos(WallPanelEntity entity) {
        PanelGeos geo = new PanelGeos();

        switch (entity.getPanel_style()) {
            case "StyleA":
                geo.setPanel(fac.panelA);
                break;
            case "StyleB":
                geo.setPanel(fac.panelB);
                break;
            case "StyleC":
                geo.setPanel(fac.panelC);
                break;
        }

        geo.setPos(new WB_Point(entity.getPosX(), entity.getPosY(), entity.getPosZ()));
        geo.setDirection(new WB_Vector(entity.getDirX(), entity.getDirY(), entity.getDirZ()));
        geo.iniPanel();
        return geo;
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(1000);
        panelRender.renderAll();
    }

}
