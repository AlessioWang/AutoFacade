package facadeGen.Panel.PanelStyle;

import facadeGen.Panel.Component.Window;
import facadeGen.Panel.Component.WindowStyle.*;
import facadeGen.Panel.PanelBase.Base;
import Tools.GeoTools;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.Arrays;

/**
 * @auther Alessio
 * @date 2022/4/21
 **/
public class StyleA extends Panel {
    public Base base;

    public StyleA(Base base) {
        this.base = base;
        styleSetting();
    }

    public void styleSetting() {
        WB_Polygon p1 = GeoTools.createRecPolygon(1800, 2100);
        Window w1 = new HorDuoWindow(p1, base);
        WB_Point pos1 = new WB_Point(600, 1100);

        WB_Polygon p2 = GeoTools.createRecPolygon(700, 2700);
        Window w2 = new VerDuoWindow(p2, base);
        WB_Point pos2 = new WB_Point(3400, 500);

        addComponents(w1, pos1);
        addComponents(w2, pos2);
    }

    @Override
    public Base getBase() {
        return base;
    }
}
