package Convertor;

import Tools.DxfReader.DXFImporter;
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

        panelBounds = geoTrans(oriPanelBounds);
        windowsBounds = geoTrans(oriWindowsBounds);
    }

    private List<WB_Polygon> geoTrans(List<WB_Polygon> polygons) {
        List<WB_Polygon> result = new LinkedList<>();
        for (WB_Polygon p : polygons) {
            WB_Vector v = p.getPoint(0).mul(-1);
            WB_Transform2D transform2D = new WB_Transform2D();
            transform2D.addTranslate2D(v);
            result.add((WB_Polygon) p.apply2D(transform2D));
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
