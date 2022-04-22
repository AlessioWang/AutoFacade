package FacadeGen;

import FacadeGen.Geo.Cell;
import FacadeGen.Geo.Grid;
import processing.core.PApplet;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/2/17
 **/
public class RenderMgr {

    public PApplet applet;
    WB_Render render;

    public RenderMgr(PApplet applet) {
        this.applet = applet;
        render = new WB_Render(applet);
    }

    public void displayGrid(Grid grid) {
        List<WB_Polygon> polygons = new LinkedList<>();

        Cell[][] cells = grid.getCells();
        for (Cell[] cellList : cells) {
            for (Cell cell : cellList) {
                cellDrawing(cell);
            }
        }

    }

    private void cellDrawing(Cell cell) {
        applet.pushStyle();
        applet.stroke(100, 0, 0);
        if (cell.isAvailable()) {
            applet.fill(143, 157, 106);
        }else{
            applet.fill(0);
        }
        render.drawPolygonEdges(cell.getShape());
        applet.popStyle();
    }


}
