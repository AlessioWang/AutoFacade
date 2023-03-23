package buildingControl;

import function.Function;
import input.BuildingInputer;
import unit2Vol.Building;
import unit2Vol.face.Face;
import unit2Vol.panelBase.PanelBase;
import unit2Vol.panelBase.SimplePanelBase;
import unit2Vol.panelBase.SplitPanelBase;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;

import java.util.*;

/**
 * 从文件中获取building的信息，并获取依据功能的PanelBase Map
 *
 * @auther Alessio
 * @date 2023/3/23
 **/
public class BuildingCreator {

    /**
     * 读取的dxf文件
     */
    private String file;

    private double height = 3500;

    private Building building;

    private Map<Function, List<PanelBase>> funcBaseMap;

    public BuildingCreator(String file) {
        this.file = file;

        init();
    }

    private void init() {
        initBuilding();

        initFuncBaseMap();
    }

    /**
     * 从文件获取building实例数据
     */
    private void initBuilding() {
        BuildingInputer buildingInputer = new BuildingInputer(file, height);
        building = buildingInputer.getBuilding();
    }

    /**
     * init func -> panelBase map
     */
    private void initFuncBaseMap() {
        Function[] allFuncTypes = Function.getAllFuncTypes();
        funcBaseMap = new HashMap<>();

        List<Face> allPanelableFaces = building.getWallAbleFaces();

        //初始化map
        for (var f : allFuncTypes) {
            List<PanelBase> baseList = new LinkedList<>();

            funcBaseMap.put(f, baseList);
        }

        //添加trimmed face 到 all panelable face
        Map<Face, List<Face>> trimmedFaceMap = building.getTrimmedFaceMap();
        Set<Map.Entry<Face, List<Face>>> entries = trimmedFaceMap.entrySet();
        for (var e : entries) {
            allPanelableFaces.addAll(e.getValue());
        }

        for (var face : allPanelableFaces) {
            func2Base(face, funcBaseMap);
        }

        // TODO: 2023/3/23  roof, inner walls, floor
    }

    /**
     * 由face的func 添加到相应的list
     *
     * @param face
     * @param map
     */
    private void func2Base(Face face, Map<Function, List<PanelBase>> map) {
        Function function = face.getFunction();
        List<PanelBase> panelBases = map.get(function);
        switch (function) {
            case ClassRoom:
                panelBases.addAll(new SplitPanelBase(face, new double[]{0.5}).getPanelBases());
                break;
            case Transport:
                panelBases.addAll(getPanelBaseByLength(face));
                break;
            case Stair:
                panelBases.add(new SimplePanelBase(face));
        }
    }

    /**
     * 根据的长度确定分段个数
     *
     * @param face
     * @return
     */
    private List<PanelBase> getPanelBaseByLength(Face face) {
        List<PanelBase> result = new LinkedList<>();

        WB_Polygon shape = face.getShape();
        List<WB_Segment> segments = shape.toSegments();

        double maxL = Double.MIN_VALUE;
        for (WB_Segment s : segments) {
            if (s.getLength() > maxL)
                maxL = s.getLength();
        }

        if (maxL <= 4000) {
            result.add(new SimplePanelBase(face));
        } else if (maxL > 4000 && maxL <= 8000) {
            result.addAll(new SplitPanelBase(face, new double[]{0.5}).getPanelBases());
        } else {
            result.addAll(new SplitPanelBase(face, new double[]{0.25, 0.5, 0.75}).getPanelBases());
        }

        return result;
    }


    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Map<Function, List<PanelBase>> getFuncBaseMap() {
        return funcBaseMap;
    }

    public Building getBuilding() {
        return building;
    }
}
