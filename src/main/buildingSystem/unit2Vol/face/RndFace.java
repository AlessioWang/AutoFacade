package unit2Vol.face;

import function.Function;
import unit2Vol.Unit;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/11/10
 **/
public class RndFace extends Face {

    private Function function;

    public RndFace(Unit unit, WB_Polygon shape) {
        super(unit, shape);

        function = unit.getFunction();
    }

    public Function getFunction() {
        return function;
    }
}
