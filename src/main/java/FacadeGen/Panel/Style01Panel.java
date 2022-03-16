package FacadeGen.Panel;

import FacadeGen.Panel.Component.Window;
import FacadeGen.Panel.Component.WindowStyle.KouWindow;
import FacadeGen.Panel.Component.WindowStyle.SimpleWindow;
import FacadeGen.Panel.Component.WindowStyle.TianWindow;
import FacadeGen.Panel.PanelBase.Base;
import Tools.GeoTools;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/3/16
 **/
public class Style01Panel extends Panel {
    private Base base;

    public Style01Panel(Base base) {
        this.base = base;
        styleSetting();
    }

    private void styleSetting() {
        WB_Polygon p1 = GeoTools.createRecPolygon(1800, 1200);
        Window w1 = new TianWindow(p1, base);
        WB_Point pos1 = new WB_Point(500, 800);

        WB_Polygon p2 = GeoTools.createRecPolygon(1000, 1500);
        Window w2 = new SimpleWindow(p2, base);
        WB_Point pos2 = new WB_Point(3000, 800);

        WB_Polygon p3 = GeoTools.createRecPolygon(800, 800);
        Window w3 = new KouWindow(p3, base);
        WB_Point pos3 = new WB_Point(5000, 2000);


        addComponents(w1, pos1);
        addComponents(w2, pos2);
        addComponents(w3, pos3);
    }

    @Override
    public Base getBase() {
        return base;
    }

}
