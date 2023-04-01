package buildingControl.designControl;

import function.Function;
import input.BuildingInputer;
import unit2Vol.Building;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import unit2Vol.panelBase.MergedPanelBase;
import unit2Vol.panelBase.PanelBase;
import unit2Vol.panelBase.SimplePanelBase;
import unit2Vol.panelBase.SplitPanelBase;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

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

    private double height = 4000;

    private Building building;

    private Map<Function, List<PanelBase>> funcBaseMap;


    public BuildingCreator(String file) {
        this.file = file;

        init();
    }

    public BuildingCreator(String file, double height) {
        this.file = file;
        this.height = height;

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

        initOutWallPanel(allPanelableFaces);

        initRoof();

        initInnerWall();

        initFloor();

        mergeByFuc(Function.Open);
    }

    /**
     * 初始化外墙信息到map
     *
     * @param allFaces
     */
    private void initOutWallPanel(List<Face> allFaces) {
        //添加trimmed face 到 all panelable face
        Map<Face, List<Face>> trimmedFaceMap = building.getTrimmedFaceMap();
        Set<Map.Entry<Face, List<Face>>> entries = trimmedFaceMap.entrySet();
        for (var e : entries) {
            allFaces.addAll(e.getValue());
        }

        for (var face : allFaces) {
            func2Base(face, funcBaseMap);
        }
    }

    private void mergeByFuc(Function func) {
        List<PanelBase> baseList = funcBaseMap.get(func);
//        List<PanelBase> stairsMerge = getMergeFacesByFunc(baseList);
        List<PanelBase> stairsMerge = getMergeByNei(baseList);
        funcBaseMap.replace(func, stairsMerge);
    }

    /**
     * 合并同向的指定功能的PanelBase
     * 问题：同方向上只能有一个合并的样式
     *
     * @param bases
     * @return
     */
    @Deprecated
    private List<PanelBase> getMergeFacesByFunc(List<PanelBase> bases) {
        List<PanelBase> result = new LinkedList<>();

        Map<WB_Vector, List<PanelBase>> map = new HashMap<>();
        WB_Epsilon.EPSILON = 1;

        for (var base : bases) {
            WB_Vector dir = base.getDir();
            if (map.containsKey(dir)) {
                map.get(dir).add(base);
            } else {
                List<PanelBase> pbs = new LinkedList<>();
                pbs.add(base);
                map.put(dir, pbs);
            }
        }

        Set<Map.Entry<WB_Vector, List<PanelBase>>> entries = map.entrySet();
        for (var entry : entries) {
            List<PanelBase> value = entry.getValue();
            result.add(new MergedPanelBase((LinkedList<PanelBase>) value));
        }

        return result;
    }

    /**
     * 从相邻关系来合并
     *
     * @return
     */
    private List<PanelBase> getMergeByNei(List<PanelBase> bases) {
        List<PanelBase> result = new LinkedList<>();

        //记录所有面的方向，便于分类
        Map<WB_Vector, List<PanelBase>> dirMap = new HashMap();
        for (var panelBase : bases) {
            WB_Vector dir = panelBase.getDir();
            if (dirMap.containsKey(dir)) {
                dirMap.get(dir).add(panelBase);
            } else {
                List<PanelBase> list = new LinkedList<>();
                list.add(panelBase);
                dirMap.put(dir, list);
            }
        }

        Set<Map.Entry<WB_Vector, List<PanelBase>>> entries = dirMap.entrySet();
        for (var en : entries) {
            List<PanelBase> panels = en.getValue();

            HashMap<PanelBase, Integer> map = new HashMap<>();
            //value -> 1 未被添加
            //value -> 0 已被添加
            panels.forEach(e -> map.put(e, 1));

            for (var base : map.keySet()) {
                if (map.get(base) == 1) {
                    Set<PanelBase> merge = new HashSet<>();
                    Unit unit = base.getUnit();
                    merge.add(base);

                    HashMap<WB_Vector, List<Unit>> unitNeiMap = unit.getUnitNeiMap();
                    //所有相邻的unit
                    Set<Unit> neighborSet = new HashSet<>();
                    unitNeiMap.values().forEach(e -> neighborSet.addAll(e));

                    for (var b : panels) {
                        if (neighborSet.contains(b.getUnit())) {
                            merge.add(b);
                            map.replace(b, 0);
//                        //添加到相邻单元
//                        Unit u = b.getUnit();
//                        u.getUnitNeiMap().values().forEach(e->neighborSet.addAll(e));
                        }
                    }
                    LinkedList<PanelBase> panelBases = new LinkedList<>(merge);

                    result.add(new MergedPanelBase(panelBases));
                }
            }
        }


        return result;
    }

    private void initRoof() {
        List<PanelBase> panelBases = funcBaseMap.get(Function.Roof);

        List<PanelBase> roofBaseList = building.getRoofBaseList();

        panelBases.addAll(roofBaseList);
    }

    private void initInnerWall() {
        List<PanelBase> panelBases = funcBaseMap.get(Function.InnerWall);

        List<PanelBase> innerWallBase = building.getInnerWallBaseList();
        panelBases.addAll(innerWallBase);
    }

    private void initFloor() {
        List<PanelBase> panelBases = funcBaseMap.get(Function.Floor);

        List<PanelBase> list = building.getFloorBaseList();
        panelBases.addAll(list);
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
                panelBases.addAll(getPanelBaseByLength(face, 7000));
                break;
            case Transport:
                panelBases.addAll(getPanelBaseByLength(face, 10000));
                break;
            case Stair:
                panelBases.add(new SimplePanelBase(face));
                break;
            case Open:
                panelBases.add(new SimplePanelBase(face));
                break;
            case Handrail:
                panelBases.add(new SimplePanelBase(face));
                break;
        }
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
