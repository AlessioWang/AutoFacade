package FacadeGen.Panel.Component.WindowStyle;

import Convertor.DxfInput;
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

    private final DxfInput dxfInput;

    public CustomWindow(WB_Polygon shape, Base base, DxfInput dxfInput) {
        super(shape, base);
        this.dxfInput = dxfInput;

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
        List<WB_PolyLine> lines = dxfInput.getPanelGeoInput().get(super.getBase().basicShape).getWinBeamMap().get(super.getShape());
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
