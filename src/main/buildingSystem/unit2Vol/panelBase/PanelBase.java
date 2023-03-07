package unit2Vol.panelBase;

import Tools.GeoTools;
import function.Function;
import unit2Vol.Building;
import wblut.geom.*;

/**
 * 由face中的基本几何信息组合或者拆分的Class
 * 解决的是会议等大空间组合的立面关系，或者一个教室对应的立面由若干种panel组成的场景
 * PanelBase中的shape信息是panelGeo放置的信息
 * 实现建筑形体单元与panel样式的解耦
 *
 * @auther Alessio
 * @date 2022/12/12
 **/
public abstract class PanelBase {
    protected WB_Polygon shape;

    protected Building building;

    protected WB_Vector dir;

    protected Function function;

    public PanelBase() {

    }

    /**
     * 初始化总方法
     */
    public abstract void init();

    /**
     * 初始化PanelBase的方向
     */
    public abstract void initDir();

    /**
     * 初始化几何形体
     */
    public abstract void initShape();

    /**
     * 初始化信息
     */
    public abstract void initInfo();

    /**
     * 翻转基准面的点序
     */
    public void reverseShape() {
        shape = GeoTools.reversePolygon(shape);
    }

    public WB_Polygon getShape() {
        return shape;
    }

    public Building getBuilding() {
        return building;
    }

    public WB_Vector getDir() {
        return dir;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }
}
