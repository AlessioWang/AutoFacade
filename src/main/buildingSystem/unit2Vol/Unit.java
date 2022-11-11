package unit2Vol;

import Tools.GeoTools;
import unit2Vol.face.BottomFace;
import unit2Vol.face.Face;
import unit2Vol.face.RndFace;
import unit2Vol.face.TopFace;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/11/9
 **/
public class Unit {

    //相对世界坐标的位置坐标
    private final WB_Point pos;

    //底面基准线
    private final WB_Polygon base;

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


    public Unit(WB_Point pos, WB_Polygon base, double height) {
        this.pos = pos;
        this.base = base;
        this.height = height;

        rndFaces = new LinkedList<>();

        initFaces();
    }

    private void initFaces() {
        //初始化四边的面
        List<WB_Segment> segments = base.toSegments();
        segments.remove(0);

        for (WB_Segment seg : segments) {
            WB_Polygon shape = GeoTools.getRecBySegAndWidth(seg, height, new WB_Vector(0, 0, 1));
            rndFaces.add(new RndFace(this, shape));
        }

        //初始化顶面
        WB_Polygon topShape = GeoTools.movePolygon(base, new WB_Point(0, 0, 1).mul(height));
        topFace = new TopFace(this, topShape);

        //初始化底面
        bottomFace = new BottomFace(this, topShape);
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

}
