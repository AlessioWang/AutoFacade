package Test.withPanelTest;

import guo_cam.CameraController;
import input.BuildingInputer;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render3D;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @auther Alessio
 * @date 2023/2/26
 **/
public class RoofTest extends PApplet {

    //    private String file = "src\\main\\resources\\dxf\\roofTest.dxf";
    private String file = "src\\main\\resources\\dxf\\debugRoof.dxf";

    private BuildingInputer buildingInputer;

    private Building building;

    private CameraController cameraController;

    private WB_Render3D render;

    private BuildingRender buildingRender;

    List<Face> roofAbleFaces;

    private List<WB_Polygon> roofs;

    private List<WB_Polygon> trimmed;

    public static void main(String[] args) {
        PApplet.main(RoofTest.class.getName());
    }

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 10000);

        render = new WB_Render3D(this);

        buildingInputer = new BuildingInputer(file);

        building = buildingInputer.getBuilding();

        building.setHeight(1000);

        buildingRender = new BuildingRender(this, building);

        roofAbleFaces = building.getRoofAbleFaces();
        roofs = new LinkedList<>();
        roofAbleFaces.stream().forEach(e -> roofs.add(e.getShape()));

        initTrimmed();
    }

    public void initTrimmed() {
        trimmed = new LinkedList<>();
        List<Unit> unitList = building.getUnitList();
        List<Face> faces = new LinkedList<>();

        for (var u : unitList) {
            Map<Face, List<Face>> trimmedFaceMap = u.getTrimmedFaceMap();
            for (var entry : trimmedFaceMap.entrySet()) {
                faces.addAll(entry.getValue());
            }
        }

        faces.forEach(e -> trimmed.add(e.getShape()));
    }

    public void draw() {
        background(255);

        cameraController.drawSystem(10000);

        render.drawPolygonEdges(building.getRoofBaseList().get(0).getShape());

        pushStyle();
        stroke(255, 0, 0);
        fill(0, 255, 0);
        for (var p : roofs) {
            render.drawPolygonEdges(p);
        }
        popStyle();

        pushStyle();
        fill(255,0,0);
        for (var p : trimmed) {
            render.drawPolygonEdges(p);
        }
        popStyle();

        buildingRender.renderPanelGeo();
    }

}
