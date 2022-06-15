package Convertor;

import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 记录一组panel-window-beams的基本几何索引关系
 *
 * @auther Alessio
 * @date 2022/6/13
 **/
public class InputGeoGroup {
    private WB_Polygon panelBounds;

    private List<WB_Polygon> windowsBounds;

    private Map<WB_Polygon, List<WB_PolyLine>> winBeamMap;

    private final WB_Point pos;

    public InputGeoGroup(WB_Polygon panelBounds, List<WB_Polygon> windowsBounds, Map<WB_Polygon, List<WB_PolyLine>> winBeamMap) {
        this.panelBounds = panelBounds;
        this.windowsBounds = windowsBounds;
        this.winBeamMap = winBeamMap;
        pos = panelBounds.getPoint(0);
    }

    public InputGeoGroup(WB_Polygon panelBounds) {
        this.panelBounds = panelBounds;
        windowsBounds = new LinkedList<>();
        winBeamMap = new HashMap<>();
        pos = panelBounds.getPoint(0);
    }

    public void addWindowBounds(WB_Polygon... polygons) {
        windowsBounds.addAll(List.of(polygons));
    }

    public WB_Polygon getPanelBounds() {
        return panelBounds;
    }

    public void setPanelBounds(WB_Polygon panelBounds) {
        this.panelBounds = panelBounds;
    }

    public List<WB_Polygon> getWindowsBounds() {
        return windowsBounds;
    }

    public void setWindowsBounds(List<WB_Polygon> windowsBounds) {
        this.windowsBounds = windowsBounds;
    }
}
