package facadeGen.Panel.PanelStyle;

import facadeGen.Panel.Component.Window;
import facadeGen.Panel.Component.WindowStyle.VerDuoWindow;
import facadeGen.Panel.PanelBase.Base;
import Tools.GeoTools;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/4/21
 **/
public class StyleB extends Panel {
    public Base base;

    public StyleB(Base base) {
        this.base = base;
        styleSetting();
    }

    public void styleSetting() {
        WB_Polygon p1 = GeoTools.createRecPolygon(2000, 2700);
        Window w1 = new VerDuoWindow(p1, base);
        WB_Point pos1 = new WB_Point(600, 500);

        WB_Polygon p2 = GeoTools.createRecPolygon(2000, 2700);
        Window w2 = new VerDuoWindow(p2, base);
        WB_Point pos2 = new WB_Point(3800, 500);

        addComponents(w1, pos1);
        addComponents(w2, pos2);
    }

    @Override
    public Base getBase() {
        return base;
    }
}
