package unit2Vol;

import wblut.geom.WB_Polygon;

import java.util.LinkedList;
import java.util.List;

/**
 * 记录Unit之间的关系
 *
 * @auther Alessio
 * @date 2022/11/10
 **/
public class Building {

    private List<Unit> unitList;

    private int layerNumber;

    private double height;

    public Building() {
        unitList = new LinkedList<>();
    }

    public Building(List<Unit> unitList) {
        this.unitList = unitList;
    }

}
