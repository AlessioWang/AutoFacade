package FacadeGen.Panel.PanelStyle;

import FacadeGen.Panel.Component.Window;
import FacadeGen.Panel.Component.WindowStyle.VerDuoWindow;
import FacadeGen.Panel.Panel;
import FacadeGen.Panel.PanelBase.Base;
import Tools.GeoTools;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/4/21
 **/
public class StyleC extends Panel {
    public Base base;

    public StyleC(Base base) {
        this.base = base;
        styleSetting();
    }

    private void styleSetting() {
        WB_Polygon p2 = GeoTools.createRecPolygon(700, 2700);
        Window w2 = new VerDuoWindow(p2, base);
        WB_Point pos2 = new WB_Point(3400, 500);

        addComponents(w2, pos2);
    }

    @Override
    public Base getBase() {
        return base;
    }
}
