package FacadeGen.Panel.Component.WindowStyle;

import Convertor.DxfConvertor;
import FacadeGen.Panel.Component.*;
import FacadeGen.Panel.PanelBase.Base;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

import java.util.List;

/**
 * @auther Alessio
 * @date 2022/6/13
 **/
public class CustomWindow extends Window {

    //    private final DxfInput dxfInput;
    private final DxfConvertor dxfConvertor;

    public CustomWindow(WB_Polygon shape, Base base, DxfConvertor dxfConvertor) {
        super(shape, base);
        this.dxfConvertor = dxfConvertor;

        iniComponent();
        WindowGeos g = new WindowGeos(this);
        setWindowGeos(g);
    }

    @Override
    public void iniBasicParas() {
        frameWidth = 30;
        //边框深度
        frameDepth = 70;

        customBeamsWidth = 20;
        customBeamsDepth = 30;
    }

    @Override
    public void createBeams() {
        var p = dxfConvertor.getPanelTrans2Origin().get(super.getBase().basicShape);
        var panelGeoInput = dxfConvertor.getMapOfInputGeoGroup().get(p);
        var map = panelGeoInput.getWinBeamMap();
        List<WB_PolyLine> lines = map.get(super.getShape());
        customBeam = new WindowBeam(this, lines, customBeamsWidth, customBeamsDepth);
    }

    @Override
    public void iniFrame() {
        frame = new Frame(this, frameWidth, frameDepth);
    }

    @Override
    public void iniGlass() {
        glass = new Glass(this);
    }

}
