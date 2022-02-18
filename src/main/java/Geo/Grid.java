package Geo;

import FacadeGen.Facade.Facade;

/**
 * @auther Alessio
 * @date 2022/2/18
 * =========================
 * 管理cell的图形类
 * =========================
 **/
public class Grid {

    protected int uNum;
    protected int vNum;
    protected Cell[][] cells;
    protected Facade facade;

    public Grid(Facade facade, int uNum, int vNum) {
        this.facade = facade;
        this.uNum = uNum;
        this.vNum = vNum;
        cells = new Cell[uNum][vNum];
    }

    public Grid(Facade facade, Cell[][] cells) {
        this.facade = facade;
        this.cells = cells;
        uNum = cells.length;
        vNum = cells[0].length;
    }

    public Grid(Cell[][] cells) {
        this.cells = cells;
        uNum = cells.length;
        vNum = cells[0].length;
    }

    public Grid(int uNum, int vNum) {
        this.uNum = uNum;
        this.vNum = vNum;
        cells = new Cell[uNum][vNum];
    }

    public void setSingleCell(Cell cell) {
        int u = cell.posU;
        int v = cell.posV;
        cells[u][v] = cell;
    }

    public void setCells(Cell[][] targetCells) {
        cells = targetCells;
    }

    public void setFacade(Facade facade) {
        this.facade = facade;
    }


}
