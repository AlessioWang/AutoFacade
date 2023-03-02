package renders;

import processing.core.PApplet;
import unit2Vol.Building;
import unit2Vol.Unit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 渲染building的类
 *
 * @auther Alessio
 * @date 2022/11/22
 **/
public class BuildingRender {
    private final PApplet applet;

    private List<Unit> unitList;

    private List<UnitRender> unitRenders;

    private List<Building> buildings;

    public BuildingRender(PApplet applet, Building... building) {
        this.applet = applet;
        this.buildings = new ArrayList<>();
        this.unitList = new ArrayList<>();
        this.unitRenders = new ArrayList<>();

        if (building.length != 0) {
            for (Building b : building) {
                List<Unit> units = b.getUnitList();
                unitList.addAll(units);
            }

            for (Unit unit : unitList) {
                unitRenders.add(new UnitRender(applet, unit));
            }
        } else {
            System.out.println("No building");
        }
    }

    @SafeVarargs
    public BuildingRender(PApplet applet, List<Unit>... units) {
        this.applet = applet;
        this.unitRenders = new LinkedList<>();

        if (units.length != 0) {
            this.unitList = new LinkedList<>();
            for (List<Unit> u : units) {
                unitList.addAll(u);
                for (Unit unit : u) {
                    unitRenders.add(new UnitRender(applet, unit));
                }
            }
        } else {
            System.out.println("No units in target building");
        }
    }

    /**
     * 渲染building所有的unit图元
     */
    public void renderAll() {
        for (UnitRender ur : unitRenders) {
            ur.renderAll();
        }
    }

    public void renderPanelGeo() {
        for (UnitRender ur : unitRenders) {
            ur.renderPanelShape();
        }
    }
}
