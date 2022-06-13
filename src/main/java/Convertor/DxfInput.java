package Convertor;

import Tools.DxfReader.DXFImporter;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Transform2D;
import wblut.geom.WB_Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/6/13
 **/
public class DxfInput {
    private final String path;
    private DXFImporter importer;

    private List<WB_Polygon> oriPanelBounds;
    private List<WB_Polygon> oriWindowsBounds;

    private List<WB_Polygon> panelBounds;
    private List<WB_Polygon> windowsBounds;


    public DxfInput(String path) {
        this.path = path;
        importer = new DXFImporter(path, DXFImporter.UTF_8);

        initGeo2Zero();
    }

    private void initGeo2Zero() {
        oriPanelBounds = importer.getPolygons("bound");
        oriWindowsBounds = importer.getPolygons("windows");

        WB_Point v = oriPanelBounds.get(0).getPoint(0);
        panelBounds = geoTrans(oriPanelBounds, v);
        windowsBounds = geoTrans(oriWindowsBounds, v);
    }

    private List<WB_Polygon> geoTrans(List<WB_Polygon> polygons, WB_Vector v) {
        List<WB_Polygon> result = new LinkedList<>();
        for (WB_Polygon p : polygons) {
            WB_Transform2D transform2D = new WB_Transform2D();
            transform2D.addTranslate2D(v.mul(-1));
            var poly = p.apply2D(transform2D);
            result.add((WB_Polygon) poly);
        }
        return result;
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
}
