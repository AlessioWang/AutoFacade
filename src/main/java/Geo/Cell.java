package Geo;

import FacadeGen.Facade.Facade;
import processing.core.PApplet;
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
    protected final WB_Polygon shape;
    //左下角脚点
    protected WB_Point originPoint;

    //所属的Grid
    protected Grid grid;

    //记录cell位于墙面的相对信息， [0]为横坐标，[1]为纵坐标
    protected int posU;
    protected int posV;
    protected int[] wallPos;

    //是否可以用做unit（是否被遮挡，或是空）
    protected boolean available = true;

    public Cell(Grid grid, WB_Polygon shape, int posU, int posV) {
        wallPos = new int[2];
        this.grid = grid;
        this.shape = shape;
        this.posU = posU;
        this.posV = posV;
        initWallPos(posU, posV);
        initShapeParas();
    }

    //初始化长宽
    private void initShapeParas() {
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

    public WB_Polygon getShape() {
        return shape;
    }

    public WB_Point getOriginPoint() {
        return originPoint;
    }

    public int getPosU() {
        return posU;
    }

    public int getPosV() {
        return posV;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

}
