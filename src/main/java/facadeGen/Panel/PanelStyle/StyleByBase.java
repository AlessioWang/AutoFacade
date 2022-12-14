package facadeGen.Panel.PanelStyle;

import Tools.GeoTools;
import facadeGen.Panel.Component.Window;
import facadeGen.Panel.Component.WindowStyle.HorDuoWindow;
import facadeGen.Panel.Component.WindowStyle.KouWindow;
import facadeGen.Panel.Component.WindowStyle.SimpleWindow;
import facadeGen.Panel.Component.WindowStyle.VerDuoWindow;
import facadeGen.Panel.PanelBase.Base;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.Arrays;

/**
 * @auther Alessio
 * @date 2022/12/14
 **/
public class StyleByBase extends Panel {
    public Base base;

    public StyleByBase(Base base) {
        this.base = base;

        styleSetting();
    }

    public void styleSetting() {
        WB_Polygon basePoly = base.getBasicShape();
        WB_Polygon winPoly = GeoTools.getBuffer(basePoly, -600);
        winPoly = GeoTools.reversePolygon(winPoly);
        System.out.println("base window" + Arrays.toString(winPoly.getPoints().toArray()));

        Window window = new KouWindow(winPoly, base);
        WB_Point pos = new WB_Point(0, 0);

        addComponents(window, pos);
    }

    @Override
    public Base getBase() {
        return base;
    }
}
