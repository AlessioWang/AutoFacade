package unit2Vol.face;

import function.Function;
import unit2Vol.Unit;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;

/**
 * @auther Alessio
 * @date 2022/11/9
 **/
public abstract class Face {

    //所属的单元
    private final Unit unit;

    //几何形状
    private WB_Polygon shape;

    //方向
    private WB_Vector dir;

    //几何中点
    private WB_Point midPos;

    //是否可以放置面板,默认为false
    private boolean ifPanel = false;

    private Function function;


    public Face(Unit unit, WB_Polygon shape) {
        this.unit = unit;
        this.shape = shape;

        dir = shape.getNormal();
        midPos = shape.getCenter();
        function = unit.getFunction();
    }

    public WB_Polygon getShape() {
        return shape;
    }

    public Unit getUnit() {
        return unit;
    }

    public WB_Vector getDir() {
        return dir;
    }

    public WB_Point getMidPos() {
        return midPos;
    }

    public boolean isIfPanel() {
        return ifPanel;
    }

    public void setIfPanel(boolean ifPanel) {
        this.ifPanel = ifPanel;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }
}
