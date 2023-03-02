package convertor;

import Tools.DxfReader.DXFImporter;
import Tools.GeoTools;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import wblut.geom.*;

import java.util.*;

/**
 * @auther Alessio
 * @date 2022/6/13
 **/
public class DxfInput {
    private final String path;
    private final DXFImporter importer;

    private List<WB_Polygon> oriPanelBounds;
    private List<WB_Polygon> oriWindowsBounds;
    private List<WB_PolyLine> oriBeamsBounds;

    private List<WB_Polygon> panelBounds;
    private List<WB_Polygon> windowsBounds;
    private List<WB_PolyLine> beamBounds;

    private Map<WB_Polygon, WB_Polygon> panelTrans2Origin;

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
        panelGeoInput = new HashMap<>();
        panelTrans2Origin = new HashMap<>();

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

        transAbsToRelative();
    }

    /**
     * 把dxf的绝对坐标图元转为相对坐标图元
     */
    private void transAbsToRelative() {
        WB_Point v = oriPanelBounds.get(0).getPoint(0);
        initPanels(v);
        windowsBounds = (List<WB_Polygon>) GeoTools.moveMultiPolys(oriWindowsBounds, v);
        beamBounds = (List<WB_PolyLine>) GeoTools.moveMultiPolys(oriBeamsBounds, v);
    }

    private void initPanels(WB_Point v) {
        List<WB_Polygon> result = new LinkedList<>();
        for (WB_Polygon p : oriPanelBounds) {
            WB_Transform2D transform2D = new WB_Transform2D();
            transform2D.addTranslate2D(v.mul(-1));
            var poly = p.apply2D(transform2D);
            panelTrans2Origin.put((WB_Polygon) poly, p);
            result.add((WB_Polygon) poly);
        }
        //给全局变量赋值
        panelBounds = result;
    }

    private void initGeoMap() {
        for (WB_Polygon panel : panelBounds) {
            //beams与窗框window的索引关系
            HashMap<WB_Polygon, List<WB_PolyLine>> beamsMap = new HashMap<>();

            //包含在panel内部的win边界线
            List<WB_Polygon> windowInPanel = new LinkedList<>();
            for (WB_Polygon win : windowsBounds) {
                if (GeoTools.ifPolyCoverPoly2D(panel, win)) {
                    windowInPanel.add(win);
                }
            }

            for (WB_Polygon winBound : windowInPanel) {
                Polygon winJts = GeoTools.wb_PolygonToJtsPolygon(winBound);

                List<WB_PolyLine> beamsList = new LinkedList<>();
                for (WB_PolyLine l : beamBounds) {
                    l.getNumberSegments();
                    LineString beamLine = GeoTools.WB_polylineToJtsLinestring(l);
                    if (winJts.covers(beamLine)) {
                        beamsList.add(l);
                    }
                }
                beamsMap.put(winBound, beamsList);
            }

            InputGeoGroup inputGeoGroup = new InputGeoGroup(panel, windowInPanel, beamsMap);
            panelGeoInput.put(panel, inputGeoGroup);

        }
    }

    public List<WB_Polygon> getWindowByIndex(int index) {
        List<InputGeoGroup> geoGroups = new ArrayList<>(panelGeoInput.values());
        InputGeoGroup geos = geoGroups.get(index);
        return geos.getWindowsBounds();
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

    public Map<WB_Polygon, InputGeoGroup> getPanelGeoInput() {
        return panelGeoInput;
    }

}
