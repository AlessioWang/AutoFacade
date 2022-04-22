package client;

import FacadeGen.Building;
import FacadeGen.Facade.Facade;
import FacadeGen.Facade.Wall;
import FacadeGen.RenderMgr;
import FacadeGen.Vol;
import FacadeGen.Geo.Grid;
import Tools.GeoTools;
import guo_cam.CameraController;
import processing.core.PApplet;
import wblut.geom.WB_Polygon;

/**
 * @auther Alessio
 * @date 2022/2/17
 **/
public class RenderClient extends PApplet {

    public static void main(String[] args) {
        PApplet.main("Client.RenderClient");
    }

    RenderMgr renderManger;
    CameraController cameraController;
    Building building;
    Wall wall;
    Grid grid;

    public void settings() {
        size(800, 800, P3D);
    }

    public void setup() {
        cameraController = new CameraController(this, 100);
        renderManger = new RenderMgr(this);
        init();
        wall = building.getVolList().get(0).getWalls().get(0);

        grid = wall.getGrid();
        grid.changeCellAvail(3,5,false);
        grid.closeCells(2,2,20,10);
    }

    public void draw() {
        background(255);
        renderManger.displayGrid(grid);
    }

    //初始化带有一个Facade的建筑
    private void init() {
        building = new Building("AAA");
        Vol vol = new Vol(building, 0);
        building.addVol(vol);
        WB_Polygon polygon = GeoTools.createRecPolygon(80, 40);
        Facade wall = new Wall(0, vol, polygon, 30, 20);
        vol.addFacade(wall);
    }

}
