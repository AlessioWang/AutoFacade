package unit2Vol.panelBase;

import unit2Vol.face.Face;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 用于处理转交的PanelBase
 * 由若干个位于转角处的Face组成
 *
 * @auther Alessio
 * @date 2022/12/28
 **/

// TODO: 2022/12/30 未完全解决转角的问题
public class EdgePanelBase extends PanelBase {
    //输入的faces
    private final List<Face> faceList;

    private List<WB_Vector> dirs;

    private WB_PolyLine edge;

    private HashMap<WB_Vector, List<Face>> dirFaceMap;

    public EdgePanelBase(List<Face> faceList) {
        this.faceList = faceList;
        init();
    }

    @Override
    public void init() {
        initMap();
        initEdge();
        initDir();
        initShape();
    }

    @Override
    public void initDir() {
        dirs = new LinkedList<>(dirFaceMap.keySet());
    }

    @Override
    public void initShape() {

    }

    @Override
    public void initInfo() {
        //初始化building信息
        building = faceList.get(0).getUnit().getBuilding();
    }

    private void initEdge() {

    }

    /**
     * 初始化dirFaceMap
     */
    private void initMap() {
        dirFaceMap = new HashMap<>();

        for (Face face : faceList) {
            WB_Vector dir = face.getDir();

            //如果vector未出现，则新建一个K,V来记录
            if (!dirFaceMap.containsKey(dir)) {
                dirFaceMap.put(dir, new LinkedList<>());
            }

            dirFaceMap.get(dir).add(face);
        }
    }

    /**
     * 获取若干个朝向的方向
     *
     * @return
     */
    public List<WB_Vector> getDirs() {
        return dirs;
    }

}
