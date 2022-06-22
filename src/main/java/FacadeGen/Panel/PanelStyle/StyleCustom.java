package FacadeGen.Panel.PanelStyle;

import Convertor.DxfInput;
import FacadeGen.Panel.Component.Window;
import FacadeGen.Panel.Component.WindowStyle.CustomWindow;
import FacadeGen.Panel.Panel;
import FacadeGen.Panel.PanelBase.Base;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.List;

/**
 * @auther Alessio
 * @date 2022/6/13
 **/
public class StyleCustom extends Panel {
    public Base base;

    private final DxfInput dxfInput;

    public StyleCustom(Base base, DxfInput dxfInput) {
        this.base = base;
        this.dxfInput = dxfInput;
        styleFromDxfByIndex(0);
    }

    private void styleFromDxfByIndex(int index) {
        List<WB_Polygon> windowPolygons = dxfInput.getWindowByIndex(index);
        System.out.println(windowPolygons.size());
        for (WB_Polygon p : windowPolygons) {
            Window w = new CustomWindow(p, base, dxfInput);
//            Window w = new VerDuoWindow(p, base);
            // TODO: 2022/6/13 留有相对位置转化问题
            WB_Point pt = new WB_Point(0, 0, 0);
            addComponents(w, pt);
        }
    }

    @Override
    public Base getBase() {
        return base;
    }
}
