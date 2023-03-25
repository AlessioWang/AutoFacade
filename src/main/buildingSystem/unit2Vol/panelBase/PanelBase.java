package unit2Vol.panelBase;

import Tools.GeoTools;
import function.Function;
import unit2Vol.Building;
import unit2Vol.face.Face;
import wblut.geom.*;

import java.util.List;

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
    protected Face face;

    protected WB_Polygon shape;

    protected Building building;

    protected WB_Vector dir;

    protected Function function;

    protected double widthLength;

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

    public void initWidth() {
        List<WB_Segment> segments = shape.toSegments();
        WB_Vector vz = new WB_Vector(0, 0, 1);

        for (WB_Segment segment : segments) {
            WB_Vector dir = (WB_Vector) segment.getDirection();
            dir.normalizeSelf();

            double area = Math.abs(vz.cross(dir).normalizeSelf());
            if (area > 0.01) {
                widthLength = segment.getLength();
                return;
            }
        }
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

    public Face getFace() {
        return face;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public double getWidthLength() {
        return widthLength;
    }
}
