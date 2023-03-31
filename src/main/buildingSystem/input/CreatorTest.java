package input;

import buildingControl.DataControl.CarbonCalculator;
import buildingControl.DataControl.Statistics;
import buildingControl.DesignControl.BuildingCreator;
import buildingControl.DesignControl.FacadeMatcher;
import facade.basic.BasicObject;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import unit2Vol.Unit;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render3D;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auther Alessio
 * @date 2023/3/23
 **/
public class CreatorTest extends PApplet {

    //        private String file = "src\\main\\resources\\dxf\\school01.dxf";
    private String file = "src\\main\\resources\\dxf\\schoolsmall.dxf";
//    private String file = "src\\main\\resources\\dxf\\one.dxf";
//    private String file = "src\\main\\resources\\dxf\\schoolBig.dxf";
//    private String file = "src\\main\\resources\\dxf\\schoolBigWithRail.dxf";
//    private String file = "src\\main\\resources\\dxf\\schoolRound.dxf";

    BuildingCreator bc;

    private CameraController cameraController;

    private WB_Render3D render;

    private List<BasicObject> panels;

    private FacadeMatcher facadeMatcher;

    private WB_Polygon ground;

    private BuildingRender br;

    private Building building;

    private Statistics statistics;

    private CarbonCalculator carbonCalculator;

    public static void main(String[] args) {
        PApplet.main(CreatorTest.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
        smooth(0);
    }

    public void setup() {
        bc = new BuildingCreator(file, 4000);

        building = bc.getBuilding();

        facadeMatcher = new FacadeMatcher(bc);

        statistics = new Statistics(facadeMatcher);
        System.out.println(statistics);

        carbonCalculator = new CarbonCalculator(statistics);
        System.out.println(carbonCalculator);

        cameraController = new CameraController(this, 15000);

        render = new WB_Render3D(this);

        br = new BuildingRender(this, building);

        panels = facadeMatcher.getPanels();

        initGround();

//        getNeiUnits();
    }

    /**
     * 出数据用
     */
    private void getNeiUnits() {
        List<Unit> unitList = building.getUnitList();

        for (Unit u : unitList) {
            HashMap<WB_Vector, List<Unit>> unitMap = u.getUnitNeiMap();

            int id = u.getId();
            Set<Map.Entry<WB_Vector, List<Unit>>> entries = unitMap.entrySet();
            for (var en : entries) {
                WB_Vector dir = en.getKey();
                List<Unit> units = en.getValue();

                if (units.size() != 0) {
                    for (Unit uu : units) {
                        int neiID = uu.getId();

                        double x = dir.xd();
                        x = Math.abs(x - 0) < 0.1 ? 0 : x;

                        double y = dir.yd();
                        y = Math.abs(y - 0) < 0.1 ? 0 : y;

                        double z = dir.zd();
                        z = Math.abs(y - 0) < 0.1 ? 0 : z;

                        System.out.println(id + "--> [" + x + " , " + y + " , " + z + "] --> " + neiID);
                    }
                }

            }
        }

    }

    /**
     * 地面
     */
    private void initGround() {
        WB_Point[] pts = new WB_Point[]{
//                new WB_Point(0, 0, 0),
//                new WB_Point(-0, 150000, 0),
//                new WB_Point(150000, 150000, 0),
//                new WB_Point(150000, 0, 0),

                new WB_Point(-100000, -100000, 0),
                new WB_Point(-100000, 100000, 0),
                new WB_Point(100000, 100000, 0),
                new WB_Point(100000, -100000, 0),
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