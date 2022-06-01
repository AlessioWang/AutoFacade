package FacadeGen.Panel.PanelBase;

import wblut.geom.WB_Polygon;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * 基本的墙体类，
 * 混凝土墙面、石材墙面、砖墙等
 * @auther Alessio
 * @date 2022/3/3
 **/
public class BasicBase extends Base{

    public BasicBase(WB_Polygon shape) {
        super(shape);
        material = 0;
    }

    public BasicBase(WB_Polygon shape, int material) {
        super(shape, material);
    }


}
