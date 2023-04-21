package Test.GUITest;

import buildingControl.designControl.BuildingCreator;
import buildingControl.designControl.F_Matcher;
import core.Container;
import core.J_Selector;
import demo.NewLeftController;
import facade.basic.BasicObject;
import facade.unit.styles.*;
import fx_processing.FXPApplet;
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
public class PickFx extends FXPApplet {
    //        private String file = "src\\main\\resources\\dxf\\schoolsmall.dxf";
//    private String file = "src\\main\\resources\\dxf\\school01.dxf";
    private String file = "src\\main\\resources\\dxf\\schoolRound.dxf";

//    private String file = "src\\main\\resources\\dxf\\schoolNotRec.dxf";

    private BuildingCreator bc;

    private CameraController cameraController;

    private WB_Render3D render;

    private Map<WB_Polygon, BasicObject> panelShapeMap;

    private List<BasicObject> structureObjects;

    private F_Matcher fm;

    private BuildingRender br;

    private Building building;

    //-------------selector----------
    private J_Selector selector;

    private Map<WB_Polygon, PanelBase> polyPanelMap;

    private List<WB_Polygon> containShapes;

    //--------------GUI------------
    private NewLeftController leftController;

    private String panelStyle;

    public static void main(String[] args) {
        PApplet.main(PickFx.class.getName());
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

        Container container = new Container();

        container.addPolygons(new LinkedList<>(polyPanelMap.keySet()));

        selector = new J_Selector(container, cameraController);

        containShapes = new LinkedList<>(polyPanelMap.keySet());
    }

    public void draw() {
        background(255);

        if (showPanel) {
            if (panelShapeMap.values().size() != 0) {
                for (BasicObject panel : panelShapeMap.values()) {
                    panel.draw(render);
                }
            }
        }

        if (showStructure) {
            if (structureObjects.size() != 0) {
                for (BasicObject o : structureObjects) {
                    o.draw(render);
                }
            }
        }
        if (showBuilding) br.renderAll();

        if (showContainer) {
            if (containShapes.size() != 0) {
                containShapes.forEach(e -> render.drawPolygonEdges(e));
            }
        }

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

    //display var
    public boolean showBuilding = false;
    public boolean showContainer = false;
    public boolean showPanel = true;
    public boolean showStructure = true;

    @Override
    public void keyPressed() {
        if (key == 'q' || key == 'Q') showPanel = !showPanel;
        if (key == 'w' || key == 'W') showStructure = !showStructure;
        if (key == 'e' || key == 'E') showContainer = !showContainer;
        if (key == 'r' || key == 'R') showBuilding = !showBuilding;
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
                addPanel(panelBase);
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

    private void addPanel(PanelBase panelBase) {
        switch (panelStyle) {
            case "SimplePanel":
                panelShapeMap.put(panelBase.getShape(), new SimplePanel(panelBase.getShape()));
                break;
            case "F_Example":
                panelShapeMap.put(panelBase.getShape(), new F_Example(panelBase.getShape()));
                break;
            case "F_OneHole":
                panelShapeMap.put(panelBase.getShape(), new F_OneHole(panelBase.getShape()));
                break;
            case "F_OneWindow":
                panelShapeMap.put(panelBase.getShape(), new F_OneWindow(panelBase.getShape()));
                break;
            case "F_TwoWindow":
                panelShapeMap.put(panelBase.getShape(), new F_TwoWindow(panelBase.getShape()));
                break;
            case "F_WindowArray":
                panelShapeMap.put(panelBase.getShape(), new F_WindowArray(panelBase.getShape()));
                break;
            case "Handrail":
                panelShapeMap.put(panelBase.getShape(), new Handrail(panelBase.getShape()));
                break;
            case "S_ExtrudeIn":
                panelShapeMap.put(panelBase.getShape(), new S_ExtrudeIn(panelBase.getShape()));
                break;
            case "RoofSimple":
                panelShapeMap.put(panelBase.getShape(), new RoofSimple(panelBase.getShape()));
                break;

        }
    }

    public void setPanelStyle(String panelStyle) {
        this.panelStyle = panelStyle;
    }

    public void setLeftController(NewLeftController leftController) {
        this.leftController = leftController;
    }
}
