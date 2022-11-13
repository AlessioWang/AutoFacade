package unit2Vol;

import Tools.GeoTools;
import unit2Vol.face.BottomFace;
import unit2Vol.face.Face;
import unit2Vol.face.RndFace;
import unit2Vol.face.TopFace;
import wblut.geom.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/11/9
 **/

public class Unit {

    //唯一编号
    private int id;

    //相对世界坐标的位置坐标
    private final WB_Point pos;

    //底面基准线，始终起点在原点，相对位置
    private final WB_Polygon oriBase;

    //基准线的方向(指相对于x轴的向量方向，polygon中第一个segment的方向)
    private final WB_Vector dir;

    //真实物理空间的基线，由相对基线、空间位置以及方向确定
    private WB_Polygon realBase;

    //层高
    private double height;

    //上下左右的相邻Unit
    private Unit upper;
    private Unit lower;
    private Unit left;
    private Unit right;

    //Unit周围的面,不包括上下两个底面
    private List<Face> rndFaces;

    private Face topFace;

    private Face bottomFace;

    //几何体的形体中心点
    private WB_Point midPt;

    public Unit(WB_Point pos, WB_Polygon oriBase, WB_Vector dir, double height) {
        this.pos = pos;
        this.oriBase = oriBase;
        this.dir = dir;
        this.height = height;

        rndFaces = new LinkedList<>();

        init();
    }

    private void init() {
        initRealBase();
        initFaces();
        initMidPt();
    }

    /**
     * 初始化形体中心
     */
    private void initMidPt() {
        WB_Point temp = new WB_Point(0, 0, 0);

        for (Face face : rndFaces) {
            temp = temp.add(face.getMidPos());
        }

        midPt = temp.div(rndFaces.size());
    }

    /**
     * 初始化绝对位置坐标
     */
    private void initRealBase() {
        WB_Polygon p = GeoTools.transferPolygon3DByX(oriBase, new WB_Point(0, 0, 0), dir);
        realBase = GeoTools.movePolygon3D(p, pos);
    }


    private void initFaces() {
        //初始化四边的面
        List<WB_Segment> segments = realBase.toSegments();
        segments.remove(0);

        for (WB_Segment seg : segments) {
            WB_Polygon shape = GeoTools.getRecBySegAndWidth(seg, height, new WB_Vector(0, 0, 1));
            rndFaces.add(new RndFace(this, shape));
        }

        //初始化底面
        WB_Polygon reversePolygon = GeoTools.reversePolygon(realBase);
        bottomFace = new BottomFace(this, reversePolygon);

        //初始化顶面
        WB_Polygon topShape = GeoTools.movePolygon3D(realBase, new WB_Point(0, 0, 1).mul(height));
        topFace = new TopFace(this, topShape);

    }

    public WB_Point getMidPt() {
        return midPt;
    }

    public List<Face> getRndFaces() {
        return rndFaces;
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
}
