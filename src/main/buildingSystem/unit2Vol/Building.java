package unit2Vol;

import Tools.GeoTools;
import function.Function;
import function.PosType;
import unit2Vol.face.Face;
import unit2Vol.panelBase.MergedPanelBase;
import unit2Vol.panelBase.PanelBase;
import unit2Vol.panelBase.SimplePanelBase;
import wblut.geom.*;

import java.util.*;

/**
 * 记录Unit之间的关系
 *
 * @auther Alessio
 * @date 2022/11/10
 **/
public class Building {

    /**
     * 存储的Unit
     */
    private List<Unit> unitList;

    /**
     * 存储每层的单元信息
     */
    private Map<Double, List<Unit>> eachFloorUnits;

    /**
     * 存储每层梁的信息
     */
    private Map<Double, List<Beam>> beamMap;

    /**
     * 存储每层柱子的信息
     */
    private Map<Double, List<WB_Point>> columnBaseMap;

    /**
     * 层数
     */
    private int layerNumber;

    /**
     * 总的建筑高度（各层层高相加）
     */
    private double height;

    /**
     * 记录每一层的楼板信息
     */
    private Map<Double, List<Face>> floorMap;

    // TODO: 2022/11/13 需要研究更加合理地定义阈值方式
    /**
     * 判断面是否相邻的阈值精度
     */
    private double threshold = 100;

    /**
     * 所有可以初始化为panel的面
     */
    private List<Face> allPanelableFaces;

    private List<Face> wallAbleFaces;

    private List<Face> roofAbleFaces;

    private List<Face> unPanelAbleRndFaces;

    private Map<Face, List<Face>> trimmedFaceMap;

    private List<PanelBase> roofBaseList;

    private List<PanelBase> floorBaseList;

    /**
     * 记录内墙
     */
    private List<PanelBase> innerWallBaseList;

    public Building(List<Unit> unitList) {
        this.unitList = unitList;

        init();
    }

    /**
     * 初始化各种基本变量信息
     */
    private void init() {
        initUnitBuildingInfo();
        initUnitIndex();
        initUnitsPosNeighbor();
        initHeight();

        //初始化每层的unit信息
        initEachFloorUnits();

        //初始化每层的beam信息
        initBeam();

        //初始化每层column信息
        initColumn();

        //给Unit的rndUnitMap赋值
        setNeiUnitMap();

        //初始化unit中每个face的ifPanel信息
        initIfPanelStatus();

        //初始化所有的panelable面板
        initPanelAbleList();

        //获取屋顶的PanelBase
        initRoofBase();
//        initRoofBaseSY();

        //获取每层楼板信息
        initFloorList();

        //被遮挡切割的面的信息
        initTrimmedFace();

        //获取内墙信息
        initInnerWallList();

    }

    /**
     * key = 0 -> side  边缘的 Segment
     * key = 1 -> center 中间的 Segment
     *
     * @param origin
     * @return
     */
    private Map<Integer, List<WB_Segment>> dispatchSegment(List<WB_Segment> origin) {
        List<WB_Segment> inner = new LinkedList<>();
        List<WB_Segment> side = new LinkedList<>();

        for (WB_Segment seg : origin) {
            WB_Point start = (WB_Point) seg.getOrigin();
            WB_Point end = (WB_Point) seg.getEndpoint();

            for (WB_Segment s : origin) {
                if (Math.abs(start.getDistance3D(s.getEndpoint())) < 1 || Math.abs(end.getDistance3D(s.getOrigin())) < 1) {
                    inner.add(seg);
                } else {
                    side.add(seg);
                }
                break;
            }
        }
        Map<Integer, List<WB_Segment>> result = new HashMap<>();

        result.put(0, side);
        result.put(1, inner);

        return result;
    }

    /**
     * 初始化梁的信息
     */
    private void initBeam() {
        beamMap = new HashMap<>();

        for (var set : eachFloorUnits.entrySet()) {
            double h = set.getValue().get(0).getHeight();

            List<WB_Segment> allSeg = new LinkedList<>();
            set.getValue().forEach(e -> allSeg.addAll(e.getRealBase().toSegments()));

            List<WB_Segment> moved = new LinkedList<>();
            allSeg.forEach(e -> moved.add((WB_Segment) GeoTools.moveWb_Line3D(e, new WB_Point(0, 0, h))));

            Map<Integer, List<WB_Segment>> integerListMap = dispatchSegment(moved);

            List<Beam> beams = new LinkedList<>();
            Set<Map.Entry<Integer, List<WB_Segment>>> entries = integerListMap.entrySet();
            for (var entry : entries) {
                Integer type = entry.getKey();
                if (type == 0) {
                    entry.getValue().forEach(e -> beams.add(new Beam(e, PosType.Side)));
                } else {
                    entry.getValue().forEach(e -> beams.add(new Beam(e, PosType.Center)));
                }
            }

            beamMap.put(set.getKey(), beams);
        }
    }

    /**
     * 初始化柱子信息
     */
    private void initColumn() {
        columnBaseMap = new HashMap<>();

        for (var set : eachFloorUnits.entrySet()) {
            double h = set.getKey();

            List<Unit> units = set.getValue();

            List<WB_Coord> coords = new LinkedList<>();
            units.forEach(e -> coords.addAll(e.getRealBase().getPoints().toList()));
            /**
             * 临时出图
             */
//            units.stream().filter(e -> e.getFunction() == Function.Transport).forEach(e -> coords.addAll(e.getRealBase().getPoints().toList()));

            List<WB_Point> pts = new LinkedList<>();
            coords.forEach(e -> pts.add(new WB_Point(e.xd(), e.yd(), e.zd())));

            columnBaseMap.put(h, pts);
        }

    }

    /**
     * 初始化每一层包含的unit信息
     */
    private void initEachFloorUnits() {
        eachFloorUnits = new HashMap<>();

        for (Unit u : unitList) {
            WB_Point pos = u.getPos();
            double h = pos.zd();
            if (eachFloorUnits.containsKey(h)) {
                eachFloorUnits.get(h).add(u);
            } else {
                List<Unit> units = new LinkedList<>();
                units.add(u);
                eachFloorUnits.put(h, units);
            }
        }
    }

    /**
     * 初始化trimmed face的信息
     */
    private void initTrimmedFace() {
        trimmedFaceMap = new HashMap<>();
        for (Unit unit : unitList) {
            unit.initTrimmedFace();

            trimmedFaceMap.putAll(unit.getTrimmedFaceMap());
        }

    }

    /**
     * 初始化unit中每个face的ifPanel信息
     */
    private void initIfPanelStatus() {
        for (Unit unit : unitList) {
            unit.initFacePanelStatus();
        }
    }

    /**
     * 给building中所有的unit赋值building信息
     */
    private void initUnitBuildingInfo() {
        for (Unit u : unitList) {
            u.setBuilding(this);
        }
    }

    /**
     * 初始化高度数据
     * 最高的unit的TopFace - 最低的unit的bottomFace
     */
    private void initHeight() {
        double top = Integer.MIN_VALUE;
        double bottom = Integer.MAX_VALUE;

        for (Unit unit : unitList) {
            double tempTop = unit.getTopFace().getMidPos().zd();
            double tempBot = unit.getBottomFace().getMidPos().zd();

            if (top < tempTop) top = tempTop;

            if (bottom > tempBot) bottom = tempBot;
        }

        height = Math.abs(top - bottom);
    }

    /**
     * 根据List中的顺序来赋值unit的id
     */
    private void initUnitIndex() {
        for (int i = 0; i < unitList.size(); i++) {
            unitList.get(i).setId(i);
        }
    }

    /**
     * 记录屋顶face的map
     *
     * @return
     */
    private Map<Double, List<Face>> initRoofFaceMap() {
        Map<Double, List<Face>> map = new HashMap<>();

        for (var face : roofAbleFaces) {
            double h = face.getMidPos().zd();
            if (!map.containsKey(h)) {
                List<Face> faces = new LinkedList<>();
                faces.add(face);
                map.put(h, faces);
            } else {
                map.get(h).add(face);
            }
        }
        return map;
    }

    private void initRoofBaseSY() {
        roofBaseList = new LinkedList<>();

        roofAbleFaces.forEach(e -> roofBaseList.add(new SimplePanelBase(e)));

        for (PanelBase roof : roofBaseList) {
            //如果roof的面是朝下的，则翻转
            if (roof.getShape().getNormal().zd() < 0) {
                roof.reverseShape();
            }
        }
    }


    /**
     * 初始化屋顶的几何形状
     * 暂定标高一致的就默认是一个屋顶
     */
    private void initRoofBase() {
        roofBaseList = new LinkedList<>();

        Map<Double, List<Face>> map = initRoofFaceMap();

        Arrays.stream(map.values().toArray()).forEach(e -> roofBaseList.add(new MergedPanelBase((List<Face>) e)));

        for (PanelBase roof : roofBaseList) {
            //如果roof的面是朝下的，则翻转
            if (roof.getShape().getNormal().zd() < 0) {
                roof.reverseShape();
            }
        }

    }


    /**
     * 以单元的信息确定距离的阈值
     *
     * @param unit
     * @param time
     * @return
     */
    private double calculateDisThreshold(Unit unit, double time) {
        double width = unit.getOriBase().getAABB().getWidth();
        double height = unit.getOriBase().getAABB().getHeight();
        double larger = Math.max(width, height);
        return Math.max(unit.getHeight(), larger) * time;
    }

    /**
     * 获取一个unit周边阈值范围内的units
     *
     * @param target
     * @param unitList
     * @return
     */
    private List<Unit> getNeighborUnits(Unit target, List<Unit> unitList) {
        Set<Unit> neighbors = new HashSet<>();
        double threshold = calculateDisThreshold(target, 50);
        for (Unit other : unitList) {
            if (other.getId() != target.getId()) {
                double dis = GeoTools.getDistance3D(target.getMidPt(), other.getMidPt());
                if (dis < threshold) {
                    neighbors.add(other);
                } else {
                    List<Face> faces = new LinkedList<>(other.getAllFaces());
                    for (Face face : faces) {
                        double d = GeoTools.getDistance3D(target.getMidPt(), face.getMidPos());
                        if (d < threshold) {
                            neighbors.add(other);
                        }
                    }
                }
            }


        }
        List<Unit> result = new LinkedList<>();
        result.addAll(neighbors);

        return result;
    }

    /**
     * 初始化每一个unit的rndUnitMap信息
     */
    private void setNeiUnitMap() {
        for (Unit target : unitList) {
            HashMap map = target.getUnitNeiMap();
            List<Unit> neighbors = getNeighborUnits(target, unitList);

            List<WB_Vector> vectors = new LinkedList<>();
            target.getAllFaces().forEach(face -> vectors.add(face.getDir()));

            for (WB_Vector vector : vectors) {
                List<Unit> neiList = (List<Unit>) map.get(vector);
                neiList.addAll(checkNeiUnitByVec(target, vector, neighbors));
            }
        }
    }

    /**
     * 通过指定的unit和vec在List中寻找相邻的units
     *
     * @param target
     * @param vector
     * @param neighbors
     * @return
     */
    private List<Unit> checkNeiUnitByVec(Unit target, WB_Vector vector, List<Unit> neighbors) {
        List<Unit> result = new LinkedList<>();
        Face face = target.getFaceDirMap().get(vector);

        for (Unit unit : neighbors) {
            HashMap<WB_Vector, List<Unit>> rndUnitMap = unit.getUnitNeiMap();
            HashMap<WB_Vector, Face> dirFaceMap = unit.getFaceDirMap();
            Set<WB_Vector> vectors = rndUnitMap.keySet();
            for (WB_Vector v : vectors) {
                //找到与目标dir相反的unit face
                if (v.equals(vector.mul(-1))) {
                    Face f = dirFaceMap.get(v);
                    //判断面是否相互包含
                    if (checkFaceNei(face, f)) {
                        result.add(unit);
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 调用判断遍历point在polygon内部来判断
     *
     * @param f1
     * @param f2
     * @return
     */
    private boolean checkFaceNei(Face f1, Face f2) {
        WB_Polygon p1 = f1.getShape();
        WB_Polygon p2 = f2.getShape();

        return GeoTools.ifPolyCoverPoly3D(p1, p2) || GeoTools.ifPolyCoverPoly3D(p2, p1);
    }

    /**
     * 检测两个face是否相邻
     * 通过判断两个face的中心点的向量到水平与数值方向的投影长度与两边长和的关系
     *
     * @param face1
     * @param face2
     * @return
     */
    @Deprecated
    private boolean checkFaceNeighbor(Face face1, Face face2) {
        WB_Point p1 = face1.getMidPos();
        WB_Point p2 = face2.getMidPos();

        WB_Vector vp = new WB_Vector(p1, p2);

        List<WB_Segment> segmentList1 = face1.getShape().toSegments();
        List<WB_Segment> segmentList2 = face2.getShape().toSegments();

        //向量单位化
        WB_Vector vHor = (WB_Vector) segmentList1.get(0).getDirection();
        WB_Vector vVer = (WB_Vector) segmentList1.get(1).getDirection();

        double horDistance = (segmentList1.get(0).getLength() + segmentList2.get(0).getLength()) * 0.5;
        double verDistance = (segmentList1.get(1).getLength() + segmentList2.get(1).getLength()) * 0.5;

        System.out.println("hor dis " + horDistance + "---> " + vp.dot(vHor));
        System.out.println("ver dis " + verDistance + "---> " + vp.dot(vVer));

        System.out.println(Math.abs(vp.dot(vHor)) < horDistance && Math.abs(vp.dot(vVer)) < verDistance);


        return vp.dot(vHor) < horDistance && vp.dot(vVer) < verDistance;
    }


    /**
     * 检索并设置Upper Unit
     *
     * @param target
     * @param neighbors
     */
    private void setUnitUpper(Unit target, List<Unit> neighbors) {
        WB_Point topPt = target.getTopFace().getMidPos();
        for (Unit unit : neighbors) {
            WB_Point btmPts = unit.getBottomFace().getMidPos();
            //如果该unit一定距离内存在其他的unit底面的中点
            if (GeoTools.getDistance3D(topPt, btmPts) <= threshold) {
                target.setUpper(unit);
                break;
            }
        }
    }

    /**
     * 检索并设置Lower Unit
     *
     * @param target
     * @param neighbors
     */
    private void setUnitLower(Unit target, List<Unit> neighbors) {
        WB_Point btmPt = target.getBottomFace().getMidPos();
        for (Unit unit : neighbors) {
            WB_Point topPt = unit.getTopFace().getMidPos();
            if (GeoTools.getDistance3D(btmPt, topPt) <= threshold) {
                target.setLower(unit);
                break;
            }
        }
    }

    /**
     * 检索并设置right Unit
     *
     * @param target
     * @param neighbors
     */
    private void setUnitRight(Unit target, List<Unit> neighbors) {
        WB_Point oriPt = target.getAllFaces().get(0).getMidPos();
        for (Unit unit : neighbors) {
            WB_Point otherPt = unit.getAllFaces().get(2).getMidPos();
            if (GeoTools.getDistance3D(oriPt, otherPt) <= threshold) {
                target.setRight(unit);
                break;
            }
        }
    }

    /**
     * 检索并设置left Unit
     *
     * @param target
     * @param neighbors
     */
    private void setUnitLeft(Unit target, List<Unit> neighbors) {
        WB_Point oriPt = target.getAllFaces().get(2).getMidPos();
        for (Unit unit : neighbors) {
            WB_Point otherPt = unit.getAllFaces().get(0).getMidPos();
            if (GeoTools.getDistance3D(oriPt, otherPt) <= threshold) {
                target.setLeft(unit);
                break;
            }
        }
    }

    /**
     * 调用设置的方法
     *
     * @param target
     * @param neighbors
     */
    private void setNeighborDetail(Unit target, List<Unit> neighbors) {
        setUnitUpper(target, neighbors);
        setUnitLower(target, neighbors);
        setUnitRight(target, neighbors);
        setUnitLeft(target, neighbors);
    }

    /**
     * 建立unit之间的单元索引关系
     * 遍历每个unit，循环调用set方法
     */
    private void initUnitsPosNeighbor() {
        for (Unit target : unitList) {
            List<Unit> neighbors = getNeighborUnits(target, unitList);
            setNeighborDetail(target, neighbors);
        }
    }

    /**
     * 初始化去除了trimmed face的内墙
     */
    private void initInnerWallList() {
        innerWallBaseList = new LinkedList<>();
        List<Face> innerFaces = new LinkedList<>();

        List<Face> allFaces = unPanelAbleRndFaces;

        allFaces = removeSamePos(allFaces);
        Set<Face> faces = trimmedFaceMap.keySet();

        allFaces.stream().filter(e -> !faces.contains(e)).forEach(innerFaces::add);

        for (var face : innerFaces) {
            if (!face.isIfPanel() && (!face.getDir().equals(new WB_Vector(0, 0, 1)))) {
                innerWallBaseList.add(new SimplePanelBase(face));
            }
        }
    }

    /**
     * 去除重复位置的face
     *
     * @param faces
     * @return
     */
    private List<Face> removeSamePos(List<Face> faces) {
        Map<Face, Integer> map = new HashMap<>();
        faces.forEach(e -> map.put(e, 1));

        for (Face f : faces) {
            if (map.get(f) == 1) {
                WB_Point mid = f.getMidPos();
                for (Face other : faces) {
                    if (other != f) {
                        WB_Point otherMid = other.getMidPos();
                        if (GeoTools.getDistance3D(otherMid, mid) < 1) {
                            map.replace(other, 0);
                        }
                    }
                }
            }
        }

        List<Face> result = new LinkedList<>();
        Set<Map.Entry<Face, Integer>> entries = map.entrySet();
        for (var en : entries) {
            if (en.getValue() == 1) {
                result.add(en.getKey());
            }
        }

        return result;
    }


    /**
     * 初始化每一层的楼板的面板信息
     */
    private void initFloorList() {
        floorMap = new HashMap<>();

        // 所有类型为stair的单元都不拥有楼板
        List<Unit> floorUnits = new LinkedList<>();
        unitList.stream().filter(e -> e.getFunction() != Function.Stair).forEach(floorUnits::add);

        for (var u : floorUnits) {
            List<Face> allFaces = u.getAllFaces();
            for (var face : allFaces) {
                if (Math.abs(face.getMidPos().zd() - 0) < 10
                        || (!face.isIfPanel() && (face.getDir().equals(new WB_Vector(0, 0, 1))))) {

                    double zd = face.getMidPos().zd();
                    if (floorMap.containsKey(zd)) {
                        floorMap.get(zd).add(face);
                    } else {
                        LinkedList<Face> faces = new LinkedList<>();
                        faces.add(face);
                        floorMap.put(zd, faces);
                    }
                }
            }
        }

        initFloorBase();
    }


    private void initFloorBase() {
        floorBaseList = new LinkedList<>();

//        /**
//         * 出图修改
//         */
//        Arrays.stream(floorMap.values().toArray()).forEach(e -> floorBaseList.add(new MergedPanelBase((List<Face>) e)));

        floorMap.values().forEach(e -> e.forEach(f -> floorBaseList.add(new SimplePanelBase(f))));
    }

    /**
     * 建立所有的可以建立面板的face列表
     */
    private void initPanelAbleList() {
        allPanelableFaces = new LinkedList<>();
        wallAbleFaces = new LinkedList<>();
        roofAbleFaces = new LinkedList<>();
        unPanelAbleRndFaces = new LinkedList<>();

        for (Unit unit : unitList) {
            List<Face> allFaces = unit.getAllFaces();
            for (Face face : allFaces) {
                if (face.isIfPanel()) {
                    allPanelableFaces.add(face);
                } else if ((face.getDir().zd() != 1) && (face.getDir().zd() != -1)) {
                    unPanelAbleRndFaces.add(face);
                }
            }
        }

        for (var face : allPanelableFaces) {
            if (face.getDir().equals(new WB_Vector(0, 0, 1))) {
                roofAbleFaces.add(face);
            } else if (!face.getDir().equals(new WB_Vector(0, 0, -1))) {
                wallAbleFaces.add(face);
            }
        }
    }


    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    public void setLayerNumber(int layerNumber) {
        this.layerNumber = layerNumber;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public List<Face> getAllPanelableFaces() {
        return allPanelableFaces;
    }

    public List<Face> getWallAbleFaces() {
        return wallAbleFaces;
    }

    public List<Face> getRoofAbleFaces() {
        return roofAbleFaces;
    }

    public List<PanelBase> getRoofBaseList() {
        return roofBaseList;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public List<PanelBase> getFloorBaseList() {
        return floorBaseList;
    }

    public List<PanelBase> getInnerWallBaseList() {
        return innerWallBaseList;
    }

    public Map<Face, List<Face>> getTrimmedFaceMap() {
        return trimmedFaceMap;
    }

    public Map<Double, List<Unit>> getEachFloorUnits() {
        return eachFloorUnits;
    }

    public Map<Double, List<Beam>> getBeamMap() {
        return beamMap;
    }

    public Map<Double, List<WB_Point>> getColumnBaseMap() {
        return columnBaseMap;
    }
}
