package Test.GUITest;

import buildingControl.designControl.BuildingCreator;
import core.Container;
import core.J_Selector;
import facade.basic.BasicObject;
import facade.unit.styles.SimplePanel;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import unit2Vol.panelBase.PanelBase;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render3D;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @auther Alessio
 * @date 2023/4/18
 **/
public class PickDemo extends PApplet {

    private String file = "src\\main\\resources\\dxf\\schoolsmall.dxf";

    private BuildingCreator bc;

    private CameraController cameraController;

    private WB_Render3D render;

    private List<BasicObject> panels;

//    private FacadeMatcher facadeMatcher;

    private BuildingRender br;

    private Building building;

    //-------------selector-------

    private Container container;

    private J_Selector selector;

    private Map<WB_Polygon, PanelBase> polyPanelMap;


    public static void main(String[] args) {
        PApplet.main(PickDemo.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
        smooth(5);
    }

    public void setup() {
        bc = new BuildingCreator(file, 4000);

        building = bc.getBuilding();

//        facadeMatcher = new FacadeMatcher(bc);

        cameraController = new CameraController(this, 15000);

        render = new WB_Render3D(this);

        br = new BuildingRender(this, building);

//        panels = facadeMatcher.getPanels();

        panels = new LinkedList<>();

        initSelector();
    }

    private void initSelector() {
        polyPanelMap = bc.getPolyPanelMap();

        container = new Container();

        container.addPolygons(new LinkedList<>(polyPanelMap.keySet()));

        selector = new J_Selector(container, cameraController);
    }

    private void panelCreate() {

    }


    public void draw() {
        background(255);

        if (panels.size() != 0) {
            for (var panel : panels) {
                panel.draw(render);
            }
        }

        if (showBuilding)
            br.renderAll();

        pushStyle();
        stroke(0, 0, 200);
        noFill();
        strokeWeight(5);
        if (selectedShape != null) {
            render.drawPolygonEdges(selectedShape);
        }
        popStyle();
    }

    //选取的目标polygon
    private WB_Polygon selectedShape;

    private boolean showBuilding = true;

    @Override
    public void keyPressed() {
        if (key == 'q' || key == 'Q')
            showBuilding = !showBuilding;
    }

    @Override
    public void mousePressed() {
        if (mousePressed && mouseButton == LEFT) {
            selectedShape = selector.getSelectedPolygon(this);

            PanelBase panelBase = polyPanelMap.get(selectedShape);

            if (panelBase != null)
                panels.add(new SimplePanel(panelBase.getShape()));
        }
    }
}
