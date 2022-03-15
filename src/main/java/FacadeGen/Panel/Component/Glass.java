package FacadeGen.Panel.Component;

import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/3/15
 **/
public class Glass {
    private int material;

    private final WB_Polygon shape;

    private Window window;

    public Glass(Window window) {
        this.window = window;
        shape = window.frame.getInnerBoundary();
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public WB_Polygon getShape() {
        return shape;
    }
}
