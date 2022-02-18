package Client;

import FacadeGen.Building;
import Geo.Cell;
import FacadeGen.Facade.Facade;
import FacadeGen.Facade.Wall;
import FacadeGen.Vol;
import Tools.W_Tools;
import wblut.geom.WB_Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/2/16
 **/
public class TestClient {
    public static void main(String[] args) {
        Building building = new Building("AAA");
        Vol vol = new Vol(building, 0);

        WB_Polygon polygon = W_Tools.createRecPolygon(50, 40);
        Facade wall = new Wall(0, vol, polygon,5,4);

        vol.addFacade(wall);

        Cell[][] cells = wall.initCellGrid();
        Cell cell = cells[1][2];

        System.out.println(cell.getShape().getPoint(1));

        List<Cell> cellList = new ArrayList<>();
        cellList.add(cells[0][0]);
        cellList.add(cells[1][0]);
        cellList.add(cells[0][1]);
    }

}
