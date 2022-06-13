package FacadeGen.Panel.panelStyle;

import Convertor.DxfInput;
import FacadeGen.Panel.Component.Window;
import FacadeGen.Panel.Component.WindowStyle.HorDuoWindow;
import FacadeGen.Panel.Component.WindowStyle.VerDuoWindow;
import FacadeGen.Panel.Panel;
import FacadeGen.Panel.PanelBase.Base;
import Tools.GeoTools;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/6/13
 **/
public class StyleInput extends Panel {
    public Base base;

    private final DxfInput dxfInput;

    public StyleInput(Base base, DxfInput dxfInput) {
        this.base = base;
        this.dxfInput = dxfInput;
        styleSetting();
    }

    private void styleFromDxf() {
        List<WB_Polygon> windowPolygons = dxfInput.getWindowsBounds();
        for (WB_Polygon p : windowPolygons) {

        }

    }

    private void styleSetting() {
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
