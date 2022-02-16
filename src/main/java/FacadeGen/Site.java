package FacadeGen;

import wblut.geom.WB_Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/2/16
 * <p>
 * ==================说明：======================
 * Site类包含场地内若干建筑的集合
 * 存储一个场地内所有的建筑为将来的建筑之间的优化做基础
 * =============================================
 **/
public class Site {
    //存储的建筑集合
    List<Vol> volList;

    //场地的物理形态
    WB_Polygon polygon;

    public Site() {
        volList = new ArrayList<>();
    }

    public Site(WB_Polygon polygon) {
        volList = new ArrayList<>();
        this.polygon = polygon;
    }

    public void add(Vol vol) {
        this.volList.add(vol);
    }

    @Override
    public String toString() {
        return "Site{" +
                "vol nums=" + volList.size() +
                '}';
    }
}
