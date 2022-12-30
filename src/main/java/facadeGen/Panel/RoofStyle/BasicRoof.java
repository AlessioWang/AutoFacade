package facadeGen.Panel.RoofStyle;

import facadeGen.Panel.PanelBase.Base;
import facadeGen.Panel.PanelStyle.Panel;

import java.util.List;

/**
 * @auther Alessio
 * @date 2022/12/30
 **/
public class BasicRoof extends Panel {

    private Base base;



    public BasicRoof(Base base) {
        this.base = base;
        styleSetting();
    }

    @Override
    public void styleSetting() {

    }
}
