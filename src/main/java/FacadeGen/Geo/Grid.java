package FacadeGen.Geo;

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

    public void changeCellAvail(int u, int v, boolean flag) {
        cells[u][v].setAvailable(flag);
    }

    public void closeCell(int u, int v) {
        cells[u][v].setAvailable(false);
    }

    public void closeCells(int startU, int startV, int endU, int endV) {
        startU = Math.min(startU, uNum);
        startV = Math.min(startV, vNum);
        endU = Math.min(endU, uNum);
        endV = Math.min(endV, vNum);

        for (int i = startU; i < endU; i++) {
            for (int j = startV; j < endV; j++) {
                cells[i][j].setAvailable(false);
            }
        }
    }

    public void setCells(Cell[][] targetCells) {
        cells = targetCells;
    }

    public void setFacade(Facade facade) {
        this.facade = facade;
    }

    public Cell[][] getCells() {
        return cells;
    }

}
