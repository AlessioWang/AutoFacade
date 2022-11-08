package construction;

import Tools.GeoTools;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/11/8
 **/
public class Building {

    //房屋的编号(唯一互斥)
    private int id;

    //房屋平面图的基准线
    private WB_Polygon baseline;
    //房屋的高度
    private double height;

    //每个立面的平面基准线集合
    private List<WB_Segment> lineList;

    private List<WB_Polygon> facadeGeos;

    //存储所有的立面以及屋顶的List
    private List<Facade> facadeList;

    public Building(WB_Polygon baseline, double height) {
        this.baseline = baseline;
        this.height = height;

        init();
    }

    /**
     * 初始化基础数据
     */
    private void init() {
        lineList = baseline.toSegments();
        facadeList = new LinkedList<>();
        facadeGeos = new LinkedList<>();

        //获取每个立面的几何形态
        for (WB_Segment segment : lineList) {
            WB_Polygon geo = GeoTools.getRecBySegAndWidth(segment, height, new WB_Vector(0, 0, 1));
            facadeGeos.add(geo);
        }
    }

    /**
     * 从外部添加facade到Building中
     *
     * @param facade
     */
    public void addFacade(Facade facade) {
        facadeList.add(facade);
    }


}
