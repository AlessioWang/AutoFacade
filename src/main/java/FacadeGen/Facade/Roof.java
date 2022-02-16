package FacadeGen.Facade;

import FacadeGen.Vol;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2021/12/14
 **/
public class Roof extends Facade{

    public Roof(int index, Vol vol, WB_Polygon base, int UUnitNum, int VUnitNum) {
        super(index, vol, base, UUnitNum, VUnitNum);
    }

}
