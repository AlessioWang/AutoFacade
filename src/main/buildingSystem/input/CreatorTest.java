package input;

import buildingControl.BuildingCreator;
import facade.basic.BasicObject;
import facade.unit.styles.F_Example;
import facade.unit.styles.S_ExtrudeIn;
import function.Function;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.panelBase.PanelBase;
import wblut.processing.WB_Render3D;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auther Alessio
 * @date 2023/3/23
 **/
public class CreatorTest extends PApplet {

    private String file = "src\\main\\resources\\dxf\\input_test_complete.dxf";

    BuildingCreator bc;

    private CameraController cameraController;

    private WB_Render3D render;

    private BuildingRender buildingRender;

    private List<BasicObject> panels;

    public static void main(String[] args) {
        PApplet.main(CreatorTest.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
    }

    public void setup() {
        bc = new BuildingCreator(file);

        panels = new LinkedList<>();

        cameraController = new CameraController(this, 10000);

        render = new WB_Render3D(this);

        buildingRender = new BuildingRender(this, bc.getBuilding());

        initPanels();
    }

    public void initPanels() {
        Map<Function, List<PanelBase>> funcBaseMap = bc.getFuncBaseMap();

        Set<Map.Entry<Function, List<PanelBase>>> entries = funcBaseMap.entrySet();

        for (var en : entries) {
            Function func = en.getKey();
            List<PanelBase> bases = en.getValue();
            System.out.println("func ------------>" +func);
            System.out.println("size _____________>" + bases.size());

            initPanelByBaseFunc(bases, func);
        }
    }

    private void initPanelByBaseFunc(List<PanelBase> bases, Function function) {
        switch (function) {
            case ClassRoom:
                bases.forEach(e -> panels.add(new S_ExtrudeIn(e.getShape())));
                break;
            case Transport:
                bases.forEach(e -> panels.add(new F_Example(e.getShape())));
                break;
            case Stair:
                bases.forEach(e -> panels.add(new F_Example(e.getShape())));
        }
    }

    public void draw() {
        background(255);
        cameraController.drawSystem(10000);

        for (var panel : panels) {
            panel.draw(render);
        }

    }

}
