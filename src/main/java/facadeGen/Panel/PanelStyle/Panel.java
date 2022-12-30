package facadeGen.Panel.PanelStyle;

import facadeGen.Panel.Component.PanelComponent;
import facadeGen.Panel.Component.Window;
import facadeGen.Panel.PanelBase.Base;
import facadeGen.Panel.PanelGeos;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public abstract class Panel {

    private int index;

    private Base base;

    /**
     * 记录构件的信息
     */
    private HashMap<PanelComponent, WB_Point> components = new HashMap<>();

    /**
     * 记录窗户的信息
     */
    private HashMap<Window, WB_Point> windowsComps = new HashMap<>();

    /**
     * 记录女儿墙的信息
     * 只要roof的panel有这个属性
     */
    private List<WB_Polygon> parapetWalls = new LinkedList<>();

    private PanelGeos panelGeos;

    public Panel() {

    }

    public Panel(Base base) {
        this.base = base;
    }

    public void addComponents(PanelComponent component, WB_Point p) {
        // TODO: 2022/3/16 更多种类的component
        if (component instanceof Window) {
            windowsComps.put((Window) component, p);
        }
    }

    public abstract void styleSetting();

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

    public HashMap<Window, WB_Point> getWindowsComps() {
        return windowsComps;
    }

    public void setComponents(HashMap<PanelComponent, WB_Point> components) {
        this.components = components;
    }

    public List<WB_Polygon> getParapetWalls() {
        return parapetWalls;
    }

    public void setParapetWalls(List<WB_Polygon> parapetWalls) {
        this.parapetWalls = parapetWalls;
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
