package Convertor;

import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 记录一组panel的基本几何关系
 *
 * @auther Alessio
 * @date 2022/6/13
 **/
public class InputGeoGroup {
    private WB_Polygon panelBounds;

    private List<WB_Polygon> windowsBounds;

    private final WB_Point pos;

    public InputGeoGroup(WB_Polygon panelBounds, List<WB_Polygon> windowsBounds) {
        this.panelBounds = panelBounds;
        this.windowsBounds = windowsBounds;
        pos = panelBounds.getPoint(0);
    }

    public InputGeoGroup(WB_Polygon panelBounds) {
        this.panelBounds = panelBounds;
        windowsBounds = new LinkedList<>();
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
