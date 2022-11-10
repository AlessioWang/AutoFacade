package unit2Vol.face;

import unit2Vol.Unit;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;

/**
 * @auther Alessio
 * @date 2022/11/9
 **/
public abstract class Face {

    //所属的单元
    private Unit unit;

    //几何形状
    private WB_Polygon shape;

    //方向
    private WB_Vector dir;

    //几何中点
    private WB_Point midPos;

    public Face(Unit unit, WB_Polygon shape) {
        this.unit = unit;
        this.shape = shape;
    }

}
