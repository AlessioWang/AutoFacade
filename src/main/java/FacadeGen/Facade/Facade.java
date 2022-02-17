package FacadeGen.Facade;

import FacadeGen.Unit.ClassUnit;
import FacadeGen.Unit.Unit;
import FacadeGen.Vol;
import FacadeGen.Cell;
import Tools.W_Tools;
import wblut.geom.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        //根据横纵的分隔数量初始化分隔
        cells = initCellGrid();
        //在每一个的cell上都先初始化上一个unit
        units = createOriginUnits();
    }

    private Unit[][] createOriginUnits() {
        int u = uCellNum;
        int v = vCellNum;
        Unit[][] result = new Unit[u][v];

        for (int i = 0; i < u; i++) {
            for (int j = 0; j < v; j++) {
                List<Cell> cell = new ArrayList<>();
                cell.add(cells[i][j]);
                int index = j * v + u;
                result[i][j] = new ClassUnit(cell, index);
            }
        }

        return result;
    }

    //以横纵分隔数来基本初始化Grid
    public Cell[][] initCellGrid() {
        //存储cell信息
        Cell[][] cells = new Cell[uCellNum][vCellNum];

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
                cells[u][v] = new Cell(this, newShape, u, v);
            }
        }

        return cells;
    }



}
