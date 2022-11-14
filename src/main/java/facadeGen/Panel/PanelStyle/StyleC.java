package facadeGen.Panel.PanelStyle;

import facadeGen.Panel.Component.Window;
import facadeGen.Panel.Component.WindowStyle.VerDuoWindow;
import facadeGen.Panel.Panel;
import facadeGen.Panel.PanelBase.Base;
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
