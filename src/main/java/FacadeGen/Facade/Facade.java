package FacadeGen.Facade;

import FacadeGen.Vol;
import FacadeGen.Cell;
import wblut.geom.*;

/**
 * @auther Alessio
 * @date 2021/12/14
 **/
public abstract class Facade {
    //位于vol的编号
    protected final int index;
    //所属形体
    protected final Vol vol;
    //所属建筑的名字
    protected String volName;
    //基准面
    private final WB_Polygon base;
    //立面的长宽
    protected double width;
    protected double height;
    //立面的Unit数量
    protected int uCellNum;
    protected int vCellNum;
    protected int allCellNum;
    //记录Facade上的每个基本格子单元
    protected Cell[][] cells;

    public Facade(int index, Vol vol, WB_Polygon base, int uUnitNum, int vUnitNum) {
        this.index = index;
        this.vol = vol;
        this.base = base;
        this.uCellNum = uUnitNum;
        this.vCellNum = vUnitNum;
        initWH();
        volName = vol.getName();
        allCellNum = uUnitNum * vUnitNum;
        cells = new Cell[uUnitNum][vUnitNum];
    }

    private void initWH(){
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

    //以横纵分隔数来基本初始化Grid
    public Cell[][] initGridByNum() {
        //存储cell信息
        Cell[][] cells = new Cell[uCellNum][vCellNum];

        //计算每个cell的尺寸
        double uStep = width / uCellNum;
        double vStep = height / vCellNum;
        WB_Polygon basic = createRec(uStep, vStep);
        System.out.println(basic.getCenter());
        //遍历初始化
        for (int u = 0; u < uCellNum; u++) {
            for (int v = 0; v < vCellNum; v++) {
                WB_Transform3D transform3D = new WB_Transform3D();
                transform3D.addTranslate(new WB_Vector(u * uStep, v * vStep));
                WB_Polygon newShape = basic.apply(transform3D);
                cells[u][v] = new Cell(this, newShape, u, v);
            }
        }

        return cells;
    }

}
