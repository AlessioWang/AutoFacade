package Convertor;

import Tools.DxfReader.DXFImporter;
import Tools.GeoTools;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import wblut.geom.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @auther Alessio
 * @date 2022/6/13
 **/
public class DxfInput {
    private final String path;
    private DXFImporter importer;

    private List<WB_Polygon> oriPanelBounds;
    private List<WB_Polygon> oriWindowsBounds;
    private List<WB_PolyLine> oriBeamsBounds;

    private List<WB_Polygon> panelBounds;
    private List<WB_Polygon> windowsBounds;
    private List<WB_PolyLine> beamBounds;

    private List<Map<WB_Polygon, List<WB_PolyLine>>> winBeamMaps;

    //记录在一个dxf文件中的若干个panel边缘线与内部的窗户等其他图元的map映射关系
    private Map<WB_Polygon, InputGeoGroup> panelGeoInput;

    /**
     * 从dxf文件中读取图元信息，进行整理
     * 读取dxf的文件路径
     *
     * @param path
     */
    public DxfInput(String path) {
        this.path = path;
        importer = new DXFImporter(path, DXFImporter.UTF_8);
        winBeamMaps = new LinkedList<>();
        panelGeoInput = new HashMap<>();

        initGeo2Zero();
        initGeoMap();
    }

    /**
     * 将dxf中的物件图元进行坐标归零
     */
    private void initGeo2Zero() {
        oriPanelBounds = importer.getPolygons("bound");
        oriWindowsBounds = importer.getPolygons("windows");
        oriBeamsBounds = importer.getPolyLines("beams");

        WB_Point v = oriPanelBounds.get(0).getPoint(0);
        panelBounds = (List<WB_Polygon>) geoTrans(oriPanelBounds, v);
        windowsBounds = (List<WB_Polygon>) geoTrans(oriWindowsBounds, v);
        beamBounds = (List<WB_PolyLine>) geoTrans(oriBeamsBounds, v);
    }

    private List<? extends WB_PolyLine> geoTrans(List<? extends WB_PolyLine> polygons, WB_Vector v) {
        List<WB_PolyLine> result = new LinkedList<>();
        for (WB_PolyLine p : polygons) {
            WB_Transform2D transform2D = new WB_Transform2D();
            transform2D.addTranslate2D(v.mul(-1));

            p = p instanceof WB_Polygon ? ((WB_Polygon) p) : p;
            var poly = p.apply2D(transform2D);
            result.add(poly);
        }
        return result;
    }

    private void initGeoMap() {
        for (WB_Polygon panel : panelBounds) {

            HashMap<WB_Polygon, List<WB_PolyLine>> map = new HashMap<>();
            for (WB_Polygon winBound : windowsBounds) {
                Polygon winJts = GeoTools.WB_PolygonToJtsPolygon(winBound);

                List<WB_PolyLine> beamsList = new LinkedList<>();
                for (WB_PolyLine l : beamBounds) {
                    l.getNumberSegments();
                    LineString beamLine = GeoTools.WB_polylineToJtsLinestring(l);
                    if (winJts.covers(beamLine)) {
                        beamsList.add(l);
                    }
                }
                map.put(winBound, beamsList);
            }

            InputGeoGroup inputGeoGroup = new InputGeoGroup(panel, windowsBounds, map);
            panelGeoInput.put(panel, inputGeoGroup);

            winBeamMaps.add(map);
        }
    }

    public String getPath() {
        return path;
    }

    public List<WB_Polygon> getOriPanelBounds() {
        return oriPanelBounds;
    }

    public List<WB_Polygon> getOriWindowsBounds() {
        return oriWindowsBounds;
    }

    public List<WB_Polygon> getPanelBounds() {
        return panelBounds;
    }

    public List<WB_Polygon> getWindowsBounds() {
        return windowsBounds;
    }

    public List<WB_PolyLine> getOriBeamsBounds() {
        return oriBeamsBounds;
    }

    public List<WB_PolyLine> getBeamBounds() {
        return beamBounds;
    }

    public List<Map<WB_Polygon, List<WB_PolyLine>>> getWinBeamMaps() {
        return winBeamMaps;
    }
}
