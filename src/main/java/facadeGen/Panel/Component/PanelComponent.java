package facadeGen.Panel.Component;

import facadeGen.Panel.PanelBase.Base;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/3/3
 **/
public abstract class PanelComponent {

    //物件的基准面
    private WB_Polygon shape;
    //物件属于的base
    private Base base;
    //材料
    private int material;

    public PanelComponent(WB_Polygon shape) {
        this.shape = shape;
    }

    public PanelComponent(WB_Polygon shape, Base base) {
        this.shape = shape;
        this.base = base;
    }

    public PanelComponent(WB_Polygon shape, Base base, int material) {
        this.shape = shape;
        this.base = base;
        this.material = material;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public WB_Polygon getShape() {
        return shape;
    }

    public void setShape(WB_Polygon shape) {
        this.shape = shape;
    }

}
