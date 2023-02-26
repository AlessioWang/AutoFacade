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
@Deprecated
public class Building {

    //房屋的编号(唯一互斥)
    private int id;

    //房屋平面图的基准线
    private final WB_Polygon baseline;
    //房屋的高度
    private final double height;

    //每个立面的平面基准线集合
    private List<WB_Segment> lineList;

    private List<WB_Polygon> facadeGeos;

    //基准线的方向，楼的方向（一般是（0,0,1））
    private WB_Vector vec;

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
        vec = new WB_Vector(0, 0, 1);

        //获取每个立面的几何形态
        for (WB_Segment segment : lineList) {
            WB_Polygon geo = GeoTools.getRecBySegAndWidth(segment, height, vec);
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<WB_Segment> getLineList() {
        return lineList;
    }

    public List<WB_Polygon> getFacadeGeos() {
        return facadeGeos;
    }

    public List<Facade> getFacadeList() {
        return facadeList;
    }
}
