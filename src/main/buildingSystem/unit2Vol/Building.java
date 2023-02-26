package unit2Vol;

import Tools.GeoTools;
import unit2Vol.face.Face;
import unit2Vol.panelBase.MergedPanelBase;
import unit2Vol.panelBase.PanelBase;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;

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
     * 层数
     */
    private int layerNumber;

    /**
     * 总的建筑高度（各层层高相加）
     */
    private double height;

    /**
     * 各层平面的组合（即合并每一层的unit的底面单元）
     */
    private List<WB_Polygon> planList;

    // TODO: 2022/11/13 需要研究更加合理地定义阈值方式
    private double threshold = 10;

    /**
     * 所有可以初始化为panel的面
     */
    private List<Face> allPanelableFaces;

    private List<Face> wallAbleFaces;

    private List<Face> roofAbleFaces;

    private List<PanelBase> roofBaseList;

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

        //给Unit的rndUnitMap赋值
        setNeiUnitMap();

        //初始化unit中每个face的ifPanel信息
        initIfPanelStatus();

        //初始化所有的panelable面板
        initPanelAbleList();

        //获取屋顶的PanelBase
        initRoofPanelBase();
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
     * 初始化屋顶的几何形状
     * 暂定标高一致的就默认是一个屋顶
     */
    private void initRoofPanelBase() {
        roofBaseList = new LinkedList<>();

        System.out.println("roof face num " + roofAbleFaces.size());

        //初始化h
        double h = roofAbleFaces.get(0).getMidPos().zd();

        List<Face> oneRoof = new LinkedList<>();
        for (var face : roofAbleFaces) {
            System.out.println("hhh" + h);
            if (face.getMidPos().zd() == h) {
                oneRoof.add(face);
                System.out.println("in");
            } else {
                //添加进list
                roofBaseList.add(new MergedPanelBase(oneRoof));

                oneRoof.clear();
                oneRoof.add(face);
                h = face.getMidPos().zd();
                System.out.println("out");
                WB_Point midPos = face.getMidPos();
                System.out.println("pos " + midPos);
            }
        }

        if (oneRoof.size() != 0)
            roofBaseList.add(new MergedPanelBase(oneRoof));
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
        List<Unit> neighbors = new LinkedList<>();
        double threshold = calculateDisThreshold(target, 1.5);

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

        return neighbors;
    }

    /**
     * 初始化每一个unit的rndUnitMap信息
     */
    private void setNeiUnitMap() {
        for (Unit target : unitList) {
            HashMap map = target.getUnitMap();
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
            HashMap<WB_Vector, List<Unit>> rndUnitMap = unit.getUnitMap();
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
     * 无法使用
     * Jts的covers方法只能判断二维图形
     *
     * @param f1
     * @param f2
     * @return
     */
    @Deprecated
    private boolean checkFaceNeiTest02(Face f1, Face f2) {
        WB_Polygon polygon1 = f1.getShape();
        WB_Polygon polygon2 = f2.getShape();

        WB_Point midPosP1 = f1.getMidPos();
        System.out.println("pts1" + midPosP1);
        WB_Point midPosP2 = f2.getMidPos();
        System.out.println("pts2" + midPosP2);

        return (GeoTools.ifPolyCoverPoly2D(polygon1, polygon2) || GeoTools.ifPolyCoverPoly2D(polygon2, polygon1)) && (GeoTools.ifPolyCoverPt2D(polygon1, midPosP2) || GeoTools.ifPolyCoverPt2D(polygon2, midPosP1));

    }

    // TODO: 2022/11/30 寻找更合适的判断方法

    /**
     * 通过两个面的中心点是否在一个距离内来判断
     *
     * @param f1
     * @param f2
     * @return
     */
    @Deprecated
    private boolean checkFaceNeiTest(Face f1, Face f2) {
        return GeoTools.getDistance3D(f1.getMidPos(), f2.getMidPos()) < 1;
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
     * 建立所有的可以建立面板的face列表
     */
    private void initPanelAbleList() {
        allPanelableFaces = new LinkedList<>();
        wallAbleFaces = new LinkedList<>();
        roofAbleFaces = new LinkedList<>();

        for (Unit unit : unitList) {
//            unit.getAllFaces().stream().filter(Face::isIfPanel).forEach(e -> allPanelableFaces.add(e));
            List<Face> allFaces = unit.getAllFaces();
            for (Face face : allFaces) {
                if(face.isIfPanel()){
                    allPanelableFaces.add(face);
                }
            }
        }

        System.out.println("all panel face "+ allPanelableFaces.size());
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
}
