package Test.GUITest;

import buildingControl.designControl.BuildingCreator;
import buildingControl.designControl.F_Matcher;
import core.Container;
import core.J_Selector;
import facade.basic.BasicObject;
import facade.unit.styles.F_TwoWindow;
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
import java.util.Set;

/**
 * @auther Alessio
 * @date 2023/4/18
 **/
public class PickDemo extends PApplet {

    private String file = "src\\main\\resources\\dxf\\schoolsmall.dxf";

    private BuildingCreator bc;

    private CameraController cameraController;

    private WB_Render3D render;

    private Map<WB_Polygon, BasicObject> panelShapeMap;

    private List<BasicObject> structureObjects;

    private F_Matcher fm;

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

        fm = new F_Matcher(bc);

        cameraController = new CameraController(this, 15000);

        render = new WB_Render3D(this);

        br = new BuildingRender(this, building);

        panelShapeMap = fm.getPanelMap();

        structureObjects = fm.getStructuresList();

        initSelector();
    }

    private void initSelector() {
        polyPanelMap = bc.getPolyPanelMap();

        container = new Container();

        container.addPolygons(new LinkedList<>(polyPanelMap.keySet()));

        selector = new J_Selector(container, cameraController);
    }

    public void draw() {
        background(255);

        if (panelShapeMap.values().size() != 0) {
            for (BasicObject panel : panelShapeMap.values()) {
                panel.draw(render);
            }
        }

        if (structureObjects.size() != 0) {
            for (BasicObject o : structureObjects) {
                o.draw(render);
            }
        }

        if (showBuilding) br.renderAll();

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

    private boolean showBuilding = false;

    @Override
    public void keyPressed() {
        if (key == 'q' || key == 'Q') showBuilding = !showBuilding;
    }

    public void value2List(Map map, List list) {
        Set<Map.Entry<WB_Polygon, BasicObject>> entries = map.entrySet();
        entries.stream().filter(e -> e.getValue() != null).forEach(e -> list.add(e.getValue()));
    }

    @Override
    public void mousePressed() {
        // 添加墙板
        if (mousePressed && mouseButton == LEFT && key == 'a') {
            selectedShape = selector.getSelectedPolygon(this);

            PanelBase panelBase = polyPanelMap.get(selectedShape);

            if (panelBase != null && !panelShapeMap.containsKey(panelBase.getShape())) {
                panelShapeMap.put(panelBase.getShape(), new F_TwoWindow(panelBase.getShape()));
            }
        }

        // 添加墙板
        if (mousePressed && mouseButton == LEFT) {
            selectedShape = selector.getSelectedPolygon(this);

            PanelBase panelBase = polyPanelMap.get(selectedShape);

            if (panelBase != null && !panelShapeMap.containsKey(panelBase.getShape())) {
                panelShapeMap.put(panelBase.getShape(), new SimplePanel(panelBase.getShape()));
            }
        }

        //删除墙板
        if (mousePressed && mouseButton == RIGHT) {
            selectedShape = selector.getSelectedPolygon(this);

            PanelBase panelBase = polyPanelMap.get(selectedShape);

            if (panelBase != null) {
                panelShapeMap.remove(panelBase.getShape());
            }
        }
    }
}
