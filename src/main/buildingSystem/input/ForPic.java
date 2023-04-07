package input;

import facade.basic.BasicObject;
import facade.unit.styles.SimplePanel;
import guo_cam.CameraController;
import processing.core.PApplet;
import renders.BuildingRender;
import unit2Vol.Building;
import unit2Vol.face.Face;
import unit2Vol.panelBase.PanelBase;
import unit2Vol.panelBase.SimplePanelBase;
import unit2Vol.panelBase.SplitPanelBase;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.processing.WB_Render3D;

import java.util.LinkedList;
import java.util.List;

/**
 * 论文出图用测试类
 *
 * @auther Alessio
 * @date 2023/4/7
 **/
public class ForPic extends PApplet {

    private String file = "src\\main\\resources\\dxf\\forPic.dxf";

    private BuildingInputer buildingInputer;

    private Building building;

    private BuildingRender buildingRender;

    private List<BasicObject> panels;

    private CameraController cameraController;

    private WB_Render3D render;

    public static void main(String[] args) {
        PApplet.main(ForPic.class.getName());
    }

    public void settings() {
        size(1500, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 10000);

        cameraController.getCamera().setPerspective(false);

        render = new WB_Render3D(this);

        buildingInputer = new BuildingInputer(file, 3500);

        building = buildingInputer.getBuilding();

        panels = new LinkedList<>();

        buildingRender = new BuildingRender(this, building);

        init();
    }

    public void init() {
        List<Face> wallAbleFaces = building.getAllPanelableFaces();

        List<PanelBase> bases = new LinkedList<>();

        bases.addAll(getPanelBaseByLength(wallAbleFaces.get(1), 4500));

//        wallAbleFaces.stream().filter(e -> Objects.equals(e.getDir(), new WB_Vector(0, -1, 0))).forEach(unionFaces::add);
//        wallAbleFaces.stream().filter(e -> Objects.equals(e.getDir(), new WB_Vector(0, -1, 0))).forEach(unionFaces::add);

//        bases.forEach(e -> panels.add(new F_OneWindow(e.getShape())));
        bases.forEach(e -> panels.add(new SimplePanel(e.getShape())));

    }

    /**
     * 根据的长度确定分段个数
     *
     * @param face
     * @return
     */
    private List<PanelBase> getPanelBaseByLength(Face face, double length) {

        WB_Polygon shape = face.getShape();
        List<WB_Segment> segments = shape.toSegments();

        double maxL = Double.MIN_VALUE;
        for (WB_Segment s : segments) {
            if (s.getLength() > maxL) maxL = s.getLength();
        }

        double[] pattern = splitPatternByLength(maxL, length);
        List<SimplePanelBase> bases = new SplitPanelBase(face, pattern).getPanelBases();

        return new LinkedList<>(bases);
    }


    /**
     * 设定face分割的阈值，获取split的数组
     *
     * @param sideLength
     * @param divideL
     * @return
     */
    private double[] splitPatternByLength(double sideLength, double divideL) {
        int num = (int) Math.ceil(sideLength / divideL);

        double[] result = new double[num - 1];
        double step = 1.0 / num;

        if (num > 1) {
            for (int i = 0; i < num - 1; i++) {
                result[i] = step * (i + 1);
            }
        }

//        System.out.println("pattern : " + Arrays.toString(result));
        return result;
    }

    public void draw() {
        background(255);
//        cameraController.drawSystem(10000);

        for (var panel : panels) {
            panel.draw(render);
        }

        buildingRender.renderAll();
//        buildingRender.renderBottom();
//        buildingRender.renderBaseBoundary();
    }


}
