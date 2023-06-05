package demo;

import buildingControl.dataControl.Comparator;
import buildingControl.dataControl.Statistics;
import buildingControl.designControl.BuildingCreator;
import buildingControl.designControl.F_Matcher;
import core.Container;
import core.J_Selector;
import facade.basic.BasicObject;
import facade.unit.styles.*;
import fx_processing.FXPApplet;
import guo_cam.CameraController;
import javafx.application.Platform;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import unit2Vol.panelBase.PanelBase;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.processing.WB_Render3D;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @auther Alessio
 * @date 2023/4/18
 **/
public class PickSY extends FXPApplet {
    private String file = "C:\\Bingqi\\InstAAA\\SchoolDxfFFromSY\\1-4f\\SY_School_New_3D2.dxf";

    private BuildingCreator bc;

    private CameraController cameraController;

    private WB_Render3D render;

    private Map<WB_Polygon, BasicObject> panelShapeMap;

    private List<BasicObject> structureObjects;

    private F_Matcher fm;

    private BuildingRender br;

    private Building building;

    boolean isPerspective = false;

    //-------------selector----------
    private J_Selector selector;

    private Map<WB_Polygon, PanelBase> polyPanelMap;

    private List<WB_Polygon> containShapes;

    //--------------GUI------------
    private LeftController leftController;

    private RightController rightController;

    private String panelStyle;

    //-----------Statistics----------
    public Statistics statistics;

    public Comparator comparator;

    public static void main(String[] args) {
        PApplet.main(PickSY.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
        smooth(5);
    }

    public void setup() {
        initEnv();

        br = new BuildingRender(this, building);

        panelShapeMap = fm.getPanelMap();

        structureObjects = fm.getStructuresList();

        initSelector();

        initStatistics();
    }

    private void initStatistics() {
        statistics = new Statistics(fm);

        statistics.showPriceAndCarbon();

        updateInfo();

        comparator = new Comparator(statistics);
    }

    private void initEnv() {
        bc = new BuildingCreator(file, 4000);

        building = bc.getBuilding();

        fm = new F_Matcher(bc);

        cameraController = new CameraController(this, 15000);

        render = new WB_Render3D(this);
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
        cameraController.getCamera().setPerspective(isPerspective);

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
    public WB_Polygon selectedShape;

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

    @Override
    public void mousePressed() {
        // 添加墙板
        if (mousePressed && mouseButton == LEFT) {
            selectedShape = selector.getSelectedPolygon(this);

            PanelBase panelBase = polyPanelMap.get(selectedShape);

            if (panelBase != null && !panelShapeMap.containsKey(panelBase.getShape())) {
                try {
                    addPanel(panelBase);
                } catch (Exception e) {
                    System.out.println("Add error");
                }
            }

            GUIUpdate();
        }

        //删除墙板
        if (mousePressed && mouseButton == RIGHT) {
            selectedShape = selector.getSelectedPolygon(this);

            PanelBase panelBase = polyPanelMap.get(selectedShape);

            if (panelBase != null) {
                WB_Vector dir = selectedShape.getNormal();
                dir.normalizeSelf();
                if (dir.equals(new WB_Vector(0, 0, 1))) {
                    fm.getRoofMap().remove(panelBase.getShape());
                } else {
                    fm.getOutMap().remove(panelBase.getShape());
                }

                panelShapeMap.remove(panelBase.getShape());
            }

            GUIUpdate();
        }
    }

    private void GUIUpdate() {
        statistics.updateData();

        Platform.runLater(() -> {
            comparator.updateCurrent();
            rightController.setCompareCurrent();
        });
        updateInfo();
    }

    private synchronized void updateInfo() {
        Platform.runLater(() -> {
            rightController.updateInfo();
        });
    }

    private void addPanel(PanelBase panelBase) {
        Map<WB_Polygon, BasicObject> outMap = fm.getOutMap();
        Map<WB_Polygon, BasicObject> roofMap = fm.getRoofMap();
        switch (panelStyle) {
            case "SimplePanel":
                panelShapeMap.put(panelBase.getShape(), new SimplePanel(panelBase.getShape()));
                outMap.put(panelBase.getShape(), new SimplePanel(panelBase.getShape()));
                break;
            case "F_Example":
                panelShapeMap.put(panelBase.getShape(), new F_Example(panelBase.getShape()));
                outMap.put(panelBase.getShape(), new F_Example(panelBase.getShape()));
                break;
            case "F_OneHole":
                panelShapeMap.put(panelBase.getShape(), new F_OneHole(panelBase.getShape()));
                outMap.put(panelBase.getShape(), new F_OneHole(panelBase.getShape()));
                break;
            case "F_OneWindow":
                panelShapeMap.put(panelBase.getShape(), new F_OneWindow(panelBase.getShape()));
                outMap.put(panelBase.getShape(), new F_OneWindow(panelBase.getShape()));
                break;
            case "F_TwoWindow":
                panelShapeMap.put(panelBase.getShape(), new F_TwoWindow(panelBase.getShape()));
                outMap.put(panelBase.getShape(), new F_TwoWindow(panelBase.getShape()));
                break;
            case "F_WindowArray":
                panelShapeMap.put(panelBase.getShape(), new F_WindowArray(panelBase.getShape()));
                outMap.put(panelBase.getShape(), new F_WindowArray(panelBase.getShape()));
                break;
            case "Handrail":
                panelShapeMap.put(panelBase.getShape(), new Handrail(panelBase.getShape()));
                outMap.put(panelBase.getShape(), new Handrail(panelBase.getShape()));
                break;
            case "S_ExtrudeIn":
                panelShapeMap.put(panelBase.getShape(), new S_ExtrudeIn(panelBase.getShape()));
                outMap.put(panelBase.getShape(), new S_ExtrudeIn(panelBase.getShape()));
                break;
            case "RoofSimple":
                panelShapeMap.put(panelBase.getShape(), new RoofSimple(panelBase.getShape()));
                roofMap.put(panelBase.getShape(), new RoofSimple(panelBase.getShape()));
                break;
        }
    }

    public void setPanelStyle(String panelStyle) {
        this.panelStyle = panelStyle;
    }

    public void setLeftController(LeftController leftController) {
        this.leftController = leftController;
    }

    public void setRightController(RightController rightController) {
        this.rightController = rightController;
    }

    public Statistics getStatistics() {
        return statistics;
    }

}
