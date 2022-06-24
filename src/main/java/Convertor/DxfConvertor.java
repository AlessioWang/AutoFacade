package Convertor;

import Tools.DxfReader.DXFImporter;
import Tools.GeoTools;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

import java.util.*;

/**
 * @auther Alessio
 * @date 2022/6/22
 **/
public class DxfConvertor {
    private final String path;
    private DXFImporter importer;

    //dxf文件中原本的图元信息
    private List<WB_Polygon> oriPanelBounds;
    private List<WB_Polygon> oriWindowsBounds;
    private List<WB_PolyLine> oriBeamsBounds;

    //将原本的图元绝对坐标转为每块panel左下角（0,0）的相对坐标
    private List<WB_Polygon> panelBounds;
    private List<WB_Polygon> windowsBounds;
    private List<WB_PolyLine> beamBounds;

    //转换后的panel边界与原本的panel边界的索引关系
    private Map<WB_Polygon, WB_Polygon> panelTrans2Origin;

    //记录在一个dxf文件中的若干个panel边缘线与内部的窗户等其他图元的map映射关系
    private Map<WB_Polygon, InputGeoGroup> mapOfInputGeoGroup;

    /**
     * 从dxf文件中读取图元信息，进行整理
     * 读取dxf的文件路径
     *
     * @param path
     */
    public DxfConvertor(String path) {
        this.path = path;

        //初始化基本变量
        importer = new DXFImporter(path, DXFImporter.UTF_8);
        mapOfInputGeoGroup = new HashMap<>();
        panelTrans2Origin = new HashMap<>();

        panelBounds = new LinkedList<>();
        windowsBounds = new LinkedList<>();
        beamBounds = new LinkedList<>();

        //处理输入的图元
        initGeo2Zero();
        transAbsToRelative();
    }

    /**
     * 将dxf中的物件图元进行坐标归零
     */
    private void initGeo2Zero() {
        oriPanelBounds = importer.getPolygons("bound");
        oriWindowsBounds = importer.getPolygons("windows");
        oriBeamsBounds = importer.getPolyLines("beams");

    }

    /**
     * 把dxf的绝对坐标图元转为相对坐标图元
     */
    private void transAbsToRelative() {
        for (WB_Polygon oriPanelBound : oriPanelBounds) {
            //处理panel
            WB_Point v = oriPanelBound.getPoint(0);
            WB_Polygon panelAfter = GeoTools.movePolygon(oriPanelBound, v.mul(-1));
            panelBounds.add(panelAfter);
            panelTrans2Origin.put(panelAfter, oriPanelBound);

            //处理winBounds
            List<WB_Polygon> winsInPanel = new LinkedList<>();
            List<WB_Polygon> winsAfterTrans = new LinkedList<>();
            Map<WB_Polygon, List<WB_PolyLine>> winBeamMap = new HashMap<>();

            InputGeoGroup inputGeoGroup;
            for (WB_Polygon winBound : oriWindowsBounds) {
                if (GeoTools.ifCoverWB(oriPanelBound, winBound)) {
                    winsInPanel.add(winBound);

                    //处理每一个win内部的beams
                    List<WB_PolyLine> beamList = new LinkedList<>();
                    for (WB_PolyLine beam : oriBeamsBounds) {
                        if (GeoTools.ifCoverWB(winBound, beam)) {
                            beam = GeoTools.movePolyline(beam, v.mul(-1));
                            beamList.add(beam);
                        }
                    }

                    winBound = GeoTools.movePolygon(winBound, v.mul(-1));
                    winsAfterTrans.add(winBound);

                    //加入map
                    winBeamMap.put(winBound, beamList);
                }
            }
            System.out.println("wins num of panel : " + winsAfterTrans.size());
            inputGeoGroup = new InputGeoGroup(panelAfter, winsAfterTrans, winBeamMap);
            mapOfInputGeoGroup.put(oriPanelBound, inputGeoGroup);
        }
        System.out.println("map Size = " + mapOfInputGeoGroup.entrySet().size());
    }

    public List<WB_Polygon> getWinPolysByIndex(int index) {
        List<InputGeoGroup> geoGroups = new ArrayList<>(mapOfInputGeoGroup.values());
        InputGeoGroup geos = geoGroups.get(index);
        return geos.getWindowsBounds();
    }

    public List<WB_Polygon> getWinPolysByOriPanel(WB_Polygon oriPanelBound) {
        InputGeoGroup inputGeoGroup = mapOfInputGeoGroup.get(oriPanelBound);
        return inputGeoGroup.getWindowsBounds();
    }

    public List<WB_Polygon> getWinPolysByTransPanel(WB_Polygon transPanel) {
        WB_Polygon ori = panelTrans2Origin.get(transPanel);
        return getWinPolysByOriPanel(ori);
    }

    public String getPath() {
        return path;
    }

    public List<WB_Polygon> getOriPanelBounds() {
        return oriPanelBounds;
    }

    public Map<WB_Polygon, InputGeoGroup> getMapOfInputGeoGroup() {
        return mapOfInputGeoGroup;
    }

    public Map<WB_Polygon, WB_Polygon> getPanelTrans2Origin() {
        return panelTrans2Origin;
    }

}
