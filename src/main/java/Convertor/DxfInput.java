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

    public DxfInput(String path) {
        this.path = path;
        importer = new DXFImporter(path, DXFImporter.UTF_8);
        winBeamMaps = new LinkedList<>();

        initGeo2Zero();
        initWinMap();
        System.out.println(winBeamMaps.get(0));
        System.out.println(winBeamMaps.size());
    }

    private void initGeo2Zero() {
        oriPanelBounds = importer.getPolygons("bound");
        oriWindowsBounds = importer.getPolygons("windows");
        oriBeamsBounds = importer.getPolyLines("beams");
        System.out.println("beam num : " + oriBeamsBounds.size());

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
            var poly = p.apply2D(transform2D);
            result.add(poly);
        }
        return result;
    }

    private void initWinMap() {
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

            winBeamMaps.add(map);
        }
    }

    private void initWinBeamMap() {
        for (WB_Polygon winBound : windowsBounds) {
            WB_Polygon temp = GeoTools.getBuffer(winBound, -50);
            for (WB_PolyLine l : beamBounds) {
                List<WB_Segment> boundSegs = temp.toSegments();
                int num = l.getNumberSegments();

                HashMap<WB_Polygon, List<WB_PolyLine>> map = new HashMap<>();
                List<WB_PolyLine> beamLines = new LinkedList<>();

                for (WB_Segment bs : boundSegs) {
                    boolean breakFlag = false;

                    for (int i = 0; i < num; i++) {
                        WB_Segment beamSeg = l.getSegment(i);
                        System.out.println(WB_GeometryOp2D.getIntersection2D(bs, beamSeg).intersection);
                        if (WB_GeometryOp2D.getIntersection2D(bs, beamSeg).intersection) {
                            beamLines.add(l);
                            breakFlag = true;
                            break;
                        }
                    }

                    map.put(winBound, beamLines);
                    winBeamMaps.add(map);
                    if (breakFlag)
                        break;
                }
            }
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
