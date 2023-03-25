package input;

import Tools.DxfReader.DXFImporter;
import Tools.GeoTools;
import function.Function;
import unit2Vol.Building;
import unit2Vol.Unit;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.*;

/**
 * 从dxf文件初始化建筑信息
 * <p>
 * dxf文件中：
 * 图层名表示底面标高
 * 颜色（Integer）表示功能：
 * 1 - Classroom
 * 2 - Transport
 * 3 - Stair
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

    private Map<String, Map<WB_Polygon, Integer>> layerGeoColorMap;

    public BuildingInputer(String path) {
        this.path = path;

        init();
    }

    public BuildingInputer(String path, double unitHeight) {
        this.path = path;
        this.unitHeight = unitHeight;

        init();
    }

    private void init() {
        initImporter();
        initRecorder();
        initUnitByMap();
        initBuilding();
    }

    private void initImporter() {
        importer = new DXFImporter(path, DXFImporter.UTF_8);
        importer.getLayers();
        layerNames = importer.getLayers();
    }

    private void initRecorder() {
        recorder = new HashMap<>();
        layerGeoColorMap = new HashMap<>();

        for (String id : layerNames) {
            List<WB_Polygon> polys = importer.getPolygons(id);
            recorder.put(id, polys);

            Map<WB_Polygon, Integer> polygonAndColor = importer.getPolygonAndColor(id);
            layerGeoColorMap.put(id, polygonAndColor);
        }
    }

    private void initUnitByMap() {
        units = new LinkedList<>();

        for (String id : layerNames) {
            double height = Double.parseDouble(id);
            Map<WB_Polygon, Integer> map = layerGeoColorMap.get(id);
            List<WB_Polygon> polygons = recorder.get(id);

            for (var poly : polygons) {
                int color = map.get(poly);
                WB_Polygon real = GeoTools.movePolygon3D(poly, new WB_Point(0, 0, height));
                Unit unit = new Unit(real, unitHeight);
                funcByColor(unit, color);
                units.add(unit);
            }
        }

    }

    private void funcByColor(Unit unit, Integer color) {
        switch (color) {
            case 1:
                unit.syncFunc(Function.ClassRoom);
                break;
            case 2:
                unit.syncFunc(Function.Transport);
                break;
            case 3:
                unit.syncFunc(Function.Stair);
                break;
            case 4:
                unit.syncFunc(Function.Open);
                break;
            default:
                unit.syncFunc(Function.Default);
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

    public void setUnitHeight(double unitHeight) {
        this.unitHeight = unitHeight;
    }
}
