package Input;

import Tools.DxfReader.DXFImporter;
import unit2Vol.Building;
import unit2Vol.Unit;
import wblut.geom.WB_Polygon;

import java.util.LinkedList;
import java.util.List;

/**
 * 从dxf文件初始化建筑信息
 *
 * @auther Alessio
 * @date 2023/2/18
 **/
public class BuildingInputer {
    private final String path;

    private final String layerName;

    private double unitHeight = 3500;

    private DXFImporter importer;

    private Building building;

    private List<WB_Polygon> polygons;

    private List<Unit> units;

    public BuildingInputer(String path, String layerName) {
        this.path = path;
        this.layerName = layerName;

        init();
    }

    private void init() {
        initImporter();
        initPolygonFromDxf();
        initUnits();
        initBuilding();
    }

    private void initImporter() {
        importer = new DXFImporter(path, DXFImporter.UTF_8);
    }

    private void initPolygonFromDxf() {
        polygons = importer.getPolygons(layerName);
    }

    private void initUnits() {
        units = new LinkedList<>();

        for (WB_Polygon polygon : polygons) {
            units.add(new Unit(polygon, unitHeight));
        }
    }

    private void initBuilding() {
        building = new Building(units);
    }

    public Building getBuilding() {
        return building;
    }


}
