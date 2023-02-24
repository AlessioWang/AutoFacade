package input;

import Tools.DxfReader.DXFImporter;
import Tools.GeoTools;
import unit2Vol.Building;
import unit2Vol.Unit;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.*;

/**
 * 从dxf文件初始化建筑信息
 *
 * @auther Alessio
 * @date 2023/2/18
 **/
public class BuildingInputer {
    private final String path;


    private double unitHeight = 3500;

    private DXFImporter importer;

    private Building building;

    private List<WB_Polygon> polygons;

    private List<Unit> units;

    private List<String> layerNames;

    private Map<String, List<WB_Polygon>> recorder;

    public BuildingInputer(String path) {
        this.path = path;

        init();
    }

    private void init() {
        initImporter();
        initRecorder();
        initUnitsByRecorder();
        initBuilding();
    }

    private void initImporter() {
        importer = new DXFImporter(path, DXFImporter.UTF_8);
        importer.getLayers();
        layerNames = importer.getLayers();
    }

    private void initRecorder() {
        recorder = new HashMap<>();
        for (String id : layerNames) {
            List<WB_Polygon> polys = importer.getPolygons(id);
            recorder.put(id, polys);
        }
    }

    private void initUnitsByRecorder() {
        units = new LinkedList<>();

        for (String id : layerNames) {
            List<WB_Polygon> polygons = recorder.get(id);
            double height = Double.parseDouble(id);

            List<WB_Polygon> movedPolygons = new LinkedList<>();
            polygons.forEach(e -> movedPolygons.add(GeoTools.movePolygon3D(e, new WB_Point(0, 0, height))));

            for (WB_Polygon polygon : movedPolygons) {
                Unit unit = new Unit(polygon, unitHeight);
                units.add(unit);
            }
        }
    }


    private void initBuilding() {
        building = new Building(units);
    }

    public Building getBuilding() {
        return building;
    }


}
