package buildingControl;

import buildingControl.DataControl.CarbonCalculator;
import buildingControl.DataControl.Statistics;
import buildingControl.DesignControl.BuildingCreator;
import buildingControl.DesignControl.FacadeMatcher;
import facade.basic.BasicObject;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import wblut.processing.WB_Render3D;

import java.util.List;

/**
 * @auther Alessio
 * @date 2023/3/31
 **/
public class CalculateTest extends PApplet {

    private String file = "src\\main\\resources\\dxf\\one.dxf";

    BuildingCreator bc;

    private CameraController cameraController;

    private WB_Render3D render;

    private List<BasicObject> panels;

    private FacadeMatcher facadeMatcher;

    private Building building;

    private Statistics statistics;

    private CarbonCalculator carbonCalculator;

    private BuildingRender br;

    public static void main(String[] args) {
        PApplet.main(CalculateTest.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
        smooth(0);
    }

    public void setup() {
        bc = new BuildingCreator(file, 4000);

        building = bc.getBuilding();

        br = new BuildingRender(this, building);

        facadeMatcher = new FacadeMatcher(bc);

        statistics = new Statistics(facadeMatcher);
        System.out.println(statistics);

        carbonCalculator = new CarbonCalculator(statistics);
        System.out.println(carbonCalculator);

        cameraController = new CameraController(this, 15000);

        render = new WB_Render3D(this);

        panels = facadeMatcher.getPanels();
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(10000);

        for (var panel : panels) {
            panel.draw(render);
        }

        br.renderAll();
    }


}
