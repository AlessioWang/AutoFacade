package facadeGen.Facade;

import facadeGen.Vol;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2021/12/14
 **/
public class Wall extends Facade{

    public Wall(int index, Vol vol, WB_Polygon base, int UUnitNum, int VUnitNum) {
        super(index, vol, base, UUnitNum, VUnitNum);
    }

    @Override
    public String toString() {
        return "Wall{" +
                "index=" + index +
                ", volName='" + volName + '\'' +
                ", UUnitNum=" + uCellNum +
                ", VUnitNum=" + vCellNum +
                '}';
    }
}
