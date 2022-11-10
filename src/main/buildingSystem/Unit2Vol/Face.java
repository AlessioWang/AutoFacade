package Unit2Vol;

import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;

/**
 * @auther Alessio
 * @date 2022/11/9
 **/
public class Face {
    private Unit unit;

    private WB_Polygon shape;

    private WB_Vector dir;

    private WB_Point midPos;

    public Face(Unit unit) {
        this.unit = unit;
    }


}
