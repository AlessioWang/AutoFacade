package Convertor;

import Tools.DxfReader.DXFImporter;
import Tools.GeoTools;
import org.springframework.context.annotation.Bean;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Transform2D;

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
    private Map<WB_Polygon, InputGeoGroup> panelGeoInput;

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
        panelGeoInput = new HashMap<>();
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

            //处理winBounds
            List<WB_Polygon> winsInPanel = new LinkedList<>();
            Map<WB_Polygon, List<WB_PolyLine>> winBeamMap = new HashMap<>();

            InputGeoGroup inputGeoGroup;
            for (WB_Polygon winBound : oriWindowsBounds) {
                if (GeoTools.ifCoverWB(oriPanelBound, winBound)) {
                    winsInPanel.add(winBound);
                    GeoTools.movePolygon(winBound, v.mul(-1));

                    //处理每一个win内部的beams
                    List<WB_PolyLine> beamList = new LinkedList<>();
                    for (WB_PolyLine beam : oriBeamsBounds) {
                        if (GeoTools.ifCoverWB(winBound, beam)) {
                            beamList.add(beam);
                        }

                        beamList = (List<WB_PolyLine>) GeoTools.moveMultiPolys(beamList, v);
                    }
                    winBeamMap.put(winBound, beamList);
                }
            }

            inputGeoGroup = new InputGeoGroup(panelAfter, winsInPanel, winBeamMap);
            panelGeoInput.put(oriPanelBound, inputGeoGroup);
        }
    }

}
