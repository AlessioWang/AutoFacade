package FacadeGen.Panel.PanelStyle;

import Convertor.DxfConvertor;
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

//    private final DxfInput dxfInput;
    private final DxfConvertor dxfConvertor;

    public StyleCustom(Base base, DxfConvertor dxfConvertor) {
        this.base = base;
        this.dxfConvertor = dxfConvertor;
        styleFromDxf();
    }

    public StyleCustom( DxfConvertor dxfConvertor) {
        this.dxfConvertor = dxfConvertor;
        styleFromDxf();
    }

    private void styleFromDxf() {
        List<WB_Polygon> windowPolygons = dxfConvertor.getWinPolysByTransPanel(base.basicShape);

        for (WB_Polygon p : windowPolygons) {
            Window w = new CustomWindow(p, base, dxfConvertor);
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

    @Override
    public void setBase(Base base) {
        this.base = base;
    }
}
