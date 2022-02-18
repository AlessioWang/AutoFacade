package FacadeGen.Unit;

import Geo.Cell;

import FacadeGen.Panel.Panel;
import Tools.Douglas;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Polygon;

import java.util.List;

/**
 * @auther Alessio
 * @date 2021/12/14
 **/

public class Unit {

    //包含的cell的集合
    protected List<Cell> cells;
    //几何形态
    protected WB_Polygon shape;
    //编号
    protected final int index;
    //Unit上的面板
    protected Panel panel;
    //功能
    protected int function;
    public static final int NUll = 0, CLASS = 1, OFFICE = 2, OUTSIDE = 3, TOILET = 4;

    WB_GeometryFactory factory;


    public Unit(List<Cell> cells, int index) {
        this.cells = cells;
        this.index = index;
        factory = new WB_GeometryFactory();
        shape = initShape(cells);
    }

    /**
     * 将Unit类的list<Cell>中的shape进行合并，获得Unit的物理形态
     * 往往是一个wb_polygon
     */
    private WB_Polygon initShape(List<Cell> cells) {
        WB_Polygon result = cells.get(0).getShape();
        for (int i = 1; i < cells.size(); i++) {
            result = factory.unionPolygons2D(result, cells.get(i).getShape()).get(0);
        }
        Douglas douglas = new Douglas(result, .0001);
        return douglas.getAsPolygon();
    }

    public WB_Polygon getShape() {
        return shape;
    }

    // TODO: 2022/2/16 判断输入的cells是否都是相邻的

}