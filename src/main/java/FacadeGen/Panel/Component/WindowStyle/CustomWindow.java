package FacadeGen.Panel.Component.WindowStyle;

import Convertor.DxfInput;
import FacadeGen.Panel.Component.Window;
import FacadeGen.Panel.Component.WindowBeam;
import FacadeGen.Panel.PanelBase.Base;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/6/13
 **/
public class CustomWindow extends Window {

    private final DxfInput dxfInput;

    public CustomWindow(WB_Polygon shape, Base base, DxfInput dxfInput) {
        super(shape, base);

        this.dxfInput = dxfInput;
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
//        customBeam = new WindowBeam(this,);
    }

    @Override
    public void iniFrame() {

    }

    @Override
    public void iniGlass() {

    }

}
