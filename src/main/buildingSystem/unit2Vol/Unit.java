package unit2Vol;

import Tools.GeoTools;
import function.Function;
import unit2Vol.face.BottomFace;
import unit2Vol.face.Face;
import unit2Vol.face.RndFace;
import unit2Vol.face.TopFace;
import wblut.geom.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @auther Alessio
 * @date 2022/11/9
 **/

public class Unit {

    //唯一编号
    private int id;

    //unit所属的building
    private Building building;

    //相对世界坐标的位置坐标
    private WB_Point pos;

    //底面基准线，始终起点在原点，相对位置
    private WB_Polygon oriBase;

    //基准线的方向(指相对于x轴的向量方向，polygon中第一个segment的方向)
    private WB_Vector dir;

    //真实物理空间的基线，由相对基线、空间位置以及方向确定
    private WB_Polygon realBase;

    //层高
    private double height;

    //上下左右的相邻Unit
    private Unit upper;
    private Unit lower;
    private Unit left;
    private Unit right;

    //this unit周边的unit信息
    private HashMap<WB_Vector, List<Unit>> unitMap;

    //this unit的方向与face的映射关系
    private HashMap<WB_Vector, Face> faceDirMap;

    private List<Face> rndFaces;

    //Unit周围上下左右的face
    private List<Face> allFaces;

    private Face topFace;

    private Face bottomFace;

    /**
     * 被其他unit相邻剪切后的Face
     */
    private Map<Face, List<Face>> trimmedFaceMap;

    /**
     * 几何体的形体中心点
     */
    private WB_Point midPt;

    /**
     * unit单元所属的功能
     */
    private Function function = Function.Default;

    public Unit(WB_Point pos, WB_Polygon oriBase, WB_Vector dir, double height) {
        this.pos = pos;
        this.oriBase = oriBase;
        this.dir = dir;
        this.height = height;

        rndFaces = new LinkedList<>();
        allFaces = new LinkedList<>();

        initRealBase();
        init();
    }

    public Unit(WB_Polygon realBase, double height) {
        this.pos = realBase.getPoint(0);
        this.oriBase = realBase;
        this.realBase = realBase;
        this.height = height;

        rndFaces = new LinkedList<>();
        allFaces = new LinkedList<>();

        init();
    }

    /**
     * 初始化基本变量信息
     */
    private void init() {
        initFaces();
        initMidPt();
        initRndUnitMap();
    }

    /**
     * 初始化RndUnitMap
     * 将周边的每个面的vector作为key，value为一个空List
     */
    private void initRndUnitMap() {
        unitMap = new HashMap<>();
        faceDirMap = new HashMap<>();

        unitMap.put(new WB_Vector(0, 0, 1), new LinkedList<>());
        faceDirMap.put(new WB_Vector(0, 0, 1), topFace);

        unitMap.put(new WB_Vector(0, 0, -1), new LinkedList<>());
        faceDirMap.put(new WB_Vector(0, 0, -1), bottomFace);

        for (Face face : allFaces) {
            WB_Vector dir = face.getDir();
            unitMap.put(dir, new LinkedList<>());
            faceDirMap.put(dir, face);
        }
    }

    /**
     * 初始化每个面的ifPanel字段的值，检测unit的每个face周边是否有相邻的单元
     * 需要Building实例在初始化unit周边信息，building外部调用
     * 调用时需要初始化this unit周边unit的信息
     */
    public void initFacePanelStatus() {
        //top face
        if (upper == null) topFace.setIfPanel(true);

        //bottom face
        if (lower == null) bottomFace.setIfPanel(true);

        //round face
        for (Face face : allFaces) {
            WB_Vector dir = face.getDir();
            //对应的list长度为0，证明无相邻的unit
            if (unitMap.get(dir).size() == 0) {
                face.setIfPanel(true);
            }
        }
    }

    /**
     * 初始化形体中心
     */
    private void initMidPt() {
        WB_Point temp = new WB_Point(0, 0, 0);

        for (Face face : allFaces) {
            temp = temp.add(face.getMidPos());
        }

        midPt = temp.div(allFaces.size());
    }

    /**
     * 初始化绝对位置坐标
     */
    private void initRealBase() {
        WB_Polygon p = GeoTools.transferPolygon3DByX(oriBase, new WB_Point(0, 0, 0), dir);
        realBase = GeoTools.movePolygon3D(p, pos);
    }

    /**
     * 初始化face信息
     */
    private void initFaces() {
        //初始化四边的面
        List<WB_Segment> segments = realBase.toSegments();

        for (WB_Segment seg : segments) {
            if (seg.getLength() > 0) {
                WB_Polygon shape = GeoTools.getRecBySegAndWidth(seg, height, new WB_Vector(0, 0, 1));
                allFaces.add(new RndFace(this, shape));
                rndFaces.add(new RndFace(this, shape));
            }
        }

        //初始化底面
        WB_Polygon reversePolygon = GeoTools.reversePolygon(realBase);
        bottomFace = new BottomFace(this, reversePolygon);
        allFaces.add(bottomFace);

        //初始化顶面
        WB_Polygon topShape = GeoTools.movePolygon3D(realBase, new WB_Point(0, 0, 1).mul(height));
        topFace = new TopFace(this, topShape);
        allFaces.add(topFace);
    }

    /**
     * 更新每个unit的功能信息属性
     *
     * @param function
     */
    public void syncFunc(Function function) {
        this.function = function;

        updateFaceFunc(function);
    }

    /**
     * 由一个指定点面获取相邻的face
     *
     * @param face
     * @return
     */
    private List<Face> getNeighborFaces(Face face) {
        WB_Vector faceDir = face.getDir();
        //获取相邻方向的unit列表
        List<Unit> units = unitMap.get(faceDir);

        List<Face> neiFaces = new LinkedList<>();
        for (var unit : units) {
            List<Face> uFaces = unit.getAllFaces();
            for (var f : uFaces) {
                double angle = GeoTools.calAngle(f.getDir(), faceDir);
                if (angle < 3.15 && angle > 3.14) {
                    neiFaces.add(f);
                }
            }
        }

        return neiFaces;
    }

    /**
     * 合并一个list中所有face的polygon
     * 默认polygon都是相邻的
     *
     * @param faces
     * @return 合并后的polygon
     */
    private WB_Polygon unionFaces(List<Face> faces) {
        if (faces.size() > 0) {
            WB_Polygon result = faces.get(0).getShape();

            if (faces.size() == 1)
                return result;
            else {
                List<WB_Polygon> polygons = new LinkedList<>();
                faces.forEach(e -> polygons.add(e.getShape()));

//                System.out.println("input num " + polygons.size());
                return GeoTools.multiWbPolygonUnion(polygons);
            }
        } else return null;

    }


    private double minArea = 1;

    /**
     * 获取trimmed face
     * 需要在初始化相邻单元之后调用
     * 需要外部调用
     */
    public void initTrimmedFace() {
        trimmedFaceMap = new HashMap<>();

        for (var face : rndFaces) {
            //去除外部直接暴露的face
            if (!face.isIfPanel()) {
                List<Face> neighborFaces = getNeighborFaces(face);
                if (neighborFaces.size() > 0) {
                    WB_Polygon unionNeighbor = unionFaces(neighborFaces);

                    List<WB_Polygon> difference = GeoTools.wb_polygonDifference(face.getShape(), unionNeighbor);
                    List<Face> diffFaces = new LinkedList<>();
                    difference.stream().filter(e -> Math.abs(e.getSignedArea()) > minArea).forEach(e -> diffFaces.add(new RndFace(this, e)));

                    if (diffFaces.size() > 0) {
                        diffFaces.forEach(e -> e.setFunction(function));
                        trimmedFaceMap.put(face, diffFaces);
                    }
                }
            }
        }

    }

    private void updateFaceFunc(Function function) {
        allFaces.forEach(e -> e.setFunction(function));
    }

    public WB_Point getMidPt() {
        return midPt;
    }

    public List<Face> getAllFaces() {
        return allFaces;
    }

    public Face getTopFace() {
        return topFace;
    }

    public Face getBottomFace() {
        return bottomFace;
    }

    public Unit getUpper() {
        return upper;
    }

    public void setUpper(Unit upper) {
        this.upper = upper;
    }

    public Unit getLower() {
        return lower;
    }

    public void setLower(Unit lower) {
        this.lower = lower;
    }

    public Unit getLeft() {
        return left;
    }

    public void setLeft(Unit left) {
        this.left = left;
    }

    public Unit getRight() {
        return right;
    }

    public void setRight(Unit right) {
        this.right = right;
    }

    public double getHeight() {
        return height;
    }

    public WB_Polygon getOriBase() {
        return oriBase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public HashMap<WB_Vector, List<Unit>> getUnitMap() {
        return unitMap;
    }

    public HashMap<WB_Vector, Face> getFaceDirMap() {
        return faceDirMap;
    }

    public WB_Vector getDir() {
        return dir;
    }

    public List<Face> getRndFaces() {
        return rndFaces;
    }

    public Function getFunction() {
        return function;
    }

    public Map<Face, List<Face>> getTrimmedFaceMap() {
        return trimmedFaceMap;
    }

    public WB_Point getPos() {
        return pos;
    }

    public WB_Polygon getRealBase() {
        return realBase;
    }
}
