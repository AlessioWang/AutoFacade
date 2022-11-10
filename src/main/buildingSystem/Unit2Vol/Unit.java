package Unit2Vol;

import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

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

    //Unit周围的面
    private List<Face> faceList;

    public Unit(WB_Point pos, WB_Polygon base, double height) {
        this.pos = pos;
        this.base = base;
        this.height = height;

        faceList = new LinkedList<>();
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
