package FacadeGen.Panel;

import FacadeGen.Panel.Component.PanelComponent;
import FacadeGen.Panel.PanelBase.Base;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public class Panel {

    public Base base;
    public HashMap<PanelComponent, WB_Point> components = new HashMap<>();

    public Panel(Base base) {
        this.base = base;
    }

    public void addComponents(PanelComponent component, WB_Point p){
        components.put(component, p);
    }





}
