package FacadeGen;

import FacadeGen.Facade.Facade;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/2/16
 **/
public class Cell {
    //Cell的几何尺寸
    private double width;
    private double height;

    //几何图形
    private final WB_Polygon shape;
    //左下角脚点
    private WB_Point originPoint;

    //所属Facade
    private final Facade facade;

    //记录cell位于墙面的相对信息， [0]为横坐标，[1]为纵坐标
    protected int posU;
    protected int posV;
    protected int[] wallPos;

    //是否可以用做unit（是否被遮挡）
    public boolean available = true;

    public Cell(Facade facade, WB_Polygon shape, int posU, int posV) {
        this.shape = shape;
        this.facade = facade;
        wallPos = new int[2];
        initWallPos(posU, posV);
        initShapeParas();
    }

    //初始化长宽
    private void initShapeParas(){
        width = shape.getAABB().getWidth();
        height = shape.getAABB().getHeight();
        originPoint = shape.getPoint(0);
    }

    private void initWallPos(int posU, int posV) {
        wallPos[0] = posU;
        wallPos[1] = posV;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Facade getFacade() {
        return facade;
    }

    public WB_Polygon getShape() {
        return shape;
    }

    public WB_Point getOriginPoint() {
        return originPoint;
    }
}
