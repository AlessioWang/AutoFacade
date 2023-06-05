package Test.withPanelTest;

import buildingControl.dataControl.Statistics;
import buildingControl.designControl.BuildingCreator;
import buildingControl.designControl.FacadeMatcher;
import facade.basic.BasicObject;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render3D;

import java.util.List;

/**
 * @auther Alessio
 * @date 2023/3/23
 **/
public class CreatorTest extends PApplet {

//    测试轴网
//    private String file = "src\\main\\resources\\dxf\\grid.dxf";

    //    private String file = "src\\main\\resources\\dxf\\school01.dxf";
//    private String file = "src\\main\\resources\\dxf\\schoolsmall.dxf";
//    private String file = "src\\main\\resources\\dxf\\schoolBig.dxf";
//    private String file = "src\\main\\resources\\dxf\\schoolBigWithRail.dxf";
//    private String file = "src\\main\\resources\\dxf\\schoolRound.dxf";
//    private String file = "src\\main\\resources\\dxf\\schoolNotRec.dxf";
//    private String file = "src\\main\\resources\\dxf\\schoolNotRec02.dxf";

    private String file = "C:\\Bingqi\\InstAAA\\SchoolDxfFFromSY\\1-4f\\SY_School_New_3D2.dxf";

    private BuildingCreator bc;

    private CameraController cameraController;

    private WB_Render3D render;

    private List<BasicObject> panels;

    private FacadeMatcher facadeMatcher;

    private WB_Polygon ground;

    private BuildingRender br;

    private Building building;

    private Statistics statistics;


    public static void main(String[] args) {
        PApplet.main(CreatorTest.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
        smooth(5);
    }

    public void setup() {
        bc = new BuildingCreator(file, 4000);

        building = bc.getBuilding();

        facadeMatcher = new FacadeMatcher(bc);

        statistics = new Statistics(facadeMatcher);


        cameraController = new CameraController(this, 15000);

        render = new WB_Render3D(this);

        br = new BuildingRender(this, building);

        panels = facadeMatcher.getPanels();

        initGround();

//        getNeiUnits();

        showData();
    }

    private void showData() {
        statistics.showPanelNum();
        statistics.showPriceAndCarbon();
    }

    /**
     * 地面
     */
    private void initGround() {
        WB_Point[] pts = new WB_Point[]{
                new WB_Point(-150000, -150000, 0),
                new WB_Point(-150000, 150000, 0),
                new WB_Point(150000, 150000, 0),
                new WB_Point(150000, -150000, 0),
        };

        ground = new WB_Polygon(pts);
    }

    boolean isPerspective = false;

    private void renderGround() {
        pushStyle();
        fill(71, 158, 128);
        render.drawPolygonEdges(ground);
        popStyle();
    }

    public void draw() {
        background(255);
//        cameraController.drawSystem(10000);
        cameraController.getCamera().setPerspective(isPerspective);

        for (var panel : panels) {
            panel.draw(render);
        }

//        renderGround();

//        br.renderAll();

//        br.renderPanelGeo();
//        br.renderInnerGeo();
//        br.renderBaseBoundary();
    }

    @Override
    public void keyPressed() {
        if (key == 'P' || key == 'p')
            isPerspective = !isPerspective;
    }
}
