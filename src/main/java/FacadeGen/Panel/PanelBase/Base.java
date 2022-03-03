package FacadeGen.Panel.PanelBase;

import wblut.geom.WB_Polygon;

/**
 * 所有panelBase的基类
 *
 * @auther Alessio
 * @date 2022/3/3
 **/
public abstract class Base {

    //基本形状
    public WB_Polygon basicShape;

    public WB_Polygon shapeWithHoles;

    //材质
    public int material;



    public Base(WB_Polygon shape) {
        iniShape(shape);
    }

    public Base(WB_Polygon shape, int material) {
        iniShape(shape);
        this.material = material;
    }

    /**
     * 判断shape是不是矩形
     */
    private boolean checkShape(double tol) {
        if (basicShape.toSegments().size() != 4) {
            return false;
        }

        return basicShape.getAABB().getArea() - Math.abs(basicShape.getSignedArea()) < tol;
    }

    private void iniShape(WB_Polygon shape) {
        if (checkShape(0.1)) {
            this.basicShape = shape;
        } else
            throw new IllegalArgumentException("传入的shape不是矩形");
    }

    public WB_Polygon getBasicShape() {
        return basicShape;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "Base{" +
                "shapeWidth + " + basicShape.getAABB().getWidth() +
                "shapeHeight + " + basicShape.getAABB().getHeight() +
                ", material=" + material +
                '}';
    }
}
