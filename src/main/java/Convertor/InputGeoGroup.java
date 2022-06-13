package Convertor;

import wblut.geom.WB_Polygon;

import java.util.Arrays;
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

    public InputGeoGroup(WB_Polygon panelBounds, List<WB_Polygon> windowsBounds) {
        this.panelBounds = panelBounds;
        this.windowsBounds = windowsBounds;
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
