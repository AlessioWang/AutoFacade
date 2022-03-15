package Client;

import FacadeGen.Building;
import Geo.Cell;
import FacadeGen.Facade.Facade;
import FacadeGen.Facade.Wall;
import FacadeGen.Vol;
import Geo.Grid;
import Tools.GeoTools;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/2/16
 **/
public class TestClient {
    public static void main(String[] args) {
        Building building = new Building("AAA");
        Vol vol = new Vol(building, 0);

        WB_Polygon polygon = GeoTools.createRecPolygon(50, 40);
        Facade wall = new Wall(0, vol, polygon, 5, 4);

        vol.addFacade(wall);

        Grid grid = wall.getGrid();
        Cell[][] cells = grid.getCells();
        Cell c = cells[3][4];
        System.out.println(c.getOriginPoint());

    }

}
