package FacadeGen.Facade;

import FacadeGen.Unit.Unit;
import FacadeGen.Vol;
import FacadeGen.Geo.Cell;
import FacadeGen.Geo.Grid;
import wblut.geom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2021/12/14
 **/
public abstract class Facade {
    //基准面
    protected WB_Plane plane;
    //位于vol的编号
    protected final int index;
    //所属形体
    protected final Vol vol;
    //所属建筑的名字
    protected String volName;
    //基准面
    private final WB_Polygon base;
    //立面的长宽，整体的长宽
    protected double width;
    protected double height;
    //立面的Unit数量
    protected int uCellNum;
    protected int vCellNum;
    protected int allCellNum;

    protected Grid grid;
    //初始的时候为每个Cell上都初始化上Unit
    protected Unit[][] units;

    public Facade(int index, Vol vol, WB_Polygon base, int uCellNum, int vCellNum) {
        this.index = index;
        this.vol = vol;
        this.base = base;
        this.uCellNum = uCellNum;
        this.vCellNum = vCellNum;
        initWH();
        volName = vol.getName();
        allCellNum = uCellNum * vCellNum;
        iniGrid();
    }

    public Facade(int index, Vol vol,WB_Plane plane, WB_Polygon base, int uCellNum, int vCellNum) {
        this.index = index;
        this.vol = vol;
        this.plane = plane;
        this.base = base;
        this.uCellNum = uCellNum;
        this.vCellNum = vCellNum;
        initWH();
        volName = vol.getName();
        allCellNum = uCellNum * vCellNum;
        iniGrid();
    }

    private void initWH() {
        width = base.getAABB().getWidth();
        height = base.getAABB().getHeight();
    }

    private WB_Polygon createRec(double width, double height) {
        WB_Point p0 = new WB_Point(0, 0);
        WB_Point p1 = new WB_Point(width, 0);
        WB_Point p2 = new WB_Point(width, height);
        WB_Point p3 = new WB_Point(0, height);
        return new WB_Polygon(p0, p1, p2, p3);
    }

    private void iniGrid() {
        grid = createGrid();
        //在每一个的cell上都先初始化上一个unit
        units = createOriginUnits();
    }

    private Unit[][] createOriginUnits() {
        int u = uCellNum;
        int v = vCellNum;
        Unit[][] result = new Unit[u][v];

        for (int i = 0; i < u; i++) {
            for (int j = 0; j < v; j++) {
                List<Cell> cells = new ArrayList<>();
                cells.add(grid.getCells()[i][j]);
                int index = j * v + i;
                result[i][j] = new Unit(cells, index);
            }
        }

        return result;
    }

    public Grid createGrid() {
        //存储cell信息
        Grid grid = new Grid(uCellNum, vCellNum);

        //计算每个cell的尺寸
        double uStep = width / uCellNum;
        double vStep = height / vCellNum;
        WB_Polygon basic = createRec(uStep, vStep);
        //遍历初始化
        for (int u = 0; u < uCellNum; u++) {
            for (int v = 0; v < vCellNum; v++) {
                WB_Transform3D transform3D = new WB_Transform3D();
                transform3D.addTranslate(new WB_Vector(u * uStep, v * vStep));
                WB_Polygon newShape = basic.apply(transform3D);
                Cell c = new Cell(grid, newShape, u, v);
                grid.setSingleCell(c);
            }
        }
        return grid;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setPlane(WB_Plane plane) {
        this.plane = plane;
    }

}
