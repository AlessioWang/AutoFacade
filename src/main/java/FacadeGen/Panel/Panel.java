package FacadeGen.Panel;

import FacadeGen.Panel.Component.PanelComponent;
import FacadeGen.Panel.Component.Window;
import FacadeGen.Panel.PanelBase.Base;
import wblut.geom.WB_Point;

import java.util.HashMap;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public class Panel {

    private int index;

    private Base base;

    private HashMap<PanelComponent, WB_Point> components = new HashMap<>();

    private HashMap<Window, WB_Point> windows = new HashMap<>();

    public Panel() {
    }

    public Panel(Base base) {
        this.base = base;
    }

    public void addComponents(PanelComponent component, WB_Point p) {

        if (component instanceof Window) {
            windows.put((Window) component, p);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public HashMap<PanelComponent, WB_Point> getComponents() {
        return components;
    }

    public HashMap<Window, WB_Point> getWindows() {
        return windows;
    }

    public void setComponents(HashMap<PanelComponent, WB_Point> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "Panel{" +
                "index=" + index +
                ", base =" + base +
                ", components=" + components +
                '}';
    }
}
