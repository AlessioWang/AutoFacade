package FacadeGen;



import FacadeGen.Facade.Facade;
import FacadeGen.Facade.Roof;
import FacadeGen.Facade.Wall;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2021/12/14
 **/
public class Vol {
    //所属建筑
    protected final Building building;
    //所属的建筑的名称
    protected  String name;
    protected final int index;
    private List<Wall> walls;
    private List<Roof> roofs;

    public Vol(Building building, int index){
        this.building = building;
        this.index = index;
        name = building.name;
        walls = new ArrayList<>();
        roofs = new ArrayList<>();
    }

    public void addFacade(Facade facade) {
        if (facade instanceof Wall) {
            walls.add((Wall) facade);
        } else if (facade instanceof Roof) {
            roofs.add((Roof) facade);
        }
    }

    public int getNewFacadeIndex(){
        return walls.size();
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public String getName() {
        return name;
    }

    public void setWalls(List<Wall> walls) {
        this.walls = walls;
    }

    public List<Roof> getRoofs() {
        return roofs;
    }

    public void setRoofs(List<Roof> roofs) {
        this.roofs = roofs;
    }

    public Wall getWallByIndex(int index) {
        return walls.get(index);
    }

    public Roof getRoofByIndex(int index) {
        return roofs.get(index);
    }

    @Override
    public String toString() {
        return "vol{" +
                "name='" + name + '\'' +
                '}';
    }
}
