package unit2Vol.panelBase;

import facadeGen.Panel.PanelBaseAble;
import org.eclipse.collections.impl.bimap.mutable.HashBiMap;
import unit2Vol.Building;
import unit2Vol.face.Face;
import wblut.geom.*;

import java.util.*;

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

    public PanelBase() {
    }


    public abstract void init();

    /**
     * 初始化PanelBase的方向
     */
    public abstract void initDir();

    /**
     * 初始化几何形体
     */
    public abstract void initShape();

    public abstract void initInfo();

    public WB_Polygon getShape() {
        return shape;
    }

    public Building getBuilding() {
        return building;
    }

    public WB_Vector getDir() {
        return dir;
    }
}
