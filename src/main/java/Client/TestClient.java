package Client;

import FacadeGen.Building;
import FacadeGen.Cell;
import FacadeGen.Facade.Facade;
import FacadeGen.Facade.Wall;
import FacadeGen.Vol;
import Tools.W_Tools;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/2/16
 **/
public class TestClient {
    public static void main(String[] args) {
        Building building = new Building("AAA");
        Vol vol = new Vol(building, 0);

        WB_Polygon polygon = W_Tools.createRecPolygon(50,20);
        Facade wall = new Wall(0,vol, polygon, 5,4);
        Cell[][] cells= wall.initGridByNum();
        Cell cell = cells[1][2];
        System.out.println(cell.getWidth());
        System.out.println(cell.getHeight());
        System.out.println(cell.getShape().getPoint(1));
    }

    public static  WB_Polygon createPolygon(double width, double height){
        WB_Point p0 = new WB_Point(0, 0);
        WB_Point p1 = new WB_Point(width, 0);
        WB_Point p2 = new WB_Point(width, height);
        WB_Point p3 = new WB_Point(0, height);
        return new WB_Polygon(p0, p1, p2, p3);
    }
}
