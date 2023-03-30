package unit2Vol.panelBase;

import Tools.GeoTools;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import wblut.geom.WB_Polygon;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 由若干个基础的Face组成的PanelBase
 *
 * @auther Alessio
 * @date 2022/12/13
 **/
public class MergedPanelBase extends PanelBase {
    private List<Face> faceList;

    private Map<Face, Unit> faceUnitMap;

    public MergedPanelBase(List<Face> faceList) {
        this.faceList = faceList;
        init();
    }

    public MergedPanelBase(LinkedList<PanelBase> panelBaseList) {
        initFromPanelBases(panelBaseList);
        init();
    }

    private void initFromPanelBases(List<PanelBase> panelBaseList) {
        faceList = new LinkedList<>();

        panelBaseList.forEach(e -> faceList.add(e.getFace()));
    }

    @Override
    public void init() {
        initInfo();
        initShape();
        initDir();

        initWidth();
    }

    @Override
    public void initDir() {
        dir = faceList.get(0).getDir();
    }

    /**
     * 初始化几何形体
     */
    @Override
    public void initShape() {
        if (faceList.size() == 1) {
            shape = faceList.get(0).getShape();
        } else {
            List<WB_Polygon> polygons = new LinkedList<>();
            faceList.forEach(e -> polygons.add(e.getShape()));

//------------------------各种合并方法的尝试----------------------------------
//            shape = unionPolygon(polygons);
//            shape = unionByMesh(polygons);
//            shape = union(polygons);

            shape = jtsUnion(polygons);
        }
    }

    private WB_Polygon jtsUnion(List<WB_Polygon> origin) {
        WB_Polygon polygon = GeoTools.multiWbPolygonUnion(origin, 10);

        /**
         * 保证合并后的面方向与原本的一致
         */

        if (polygon.getNormal().getAngle(origin.get(0).getNormal()) > 0.1) {
            polygon = GeoTools.reversePolygon(polygon);
        }

        return polygon;
    }

    /**
     * 初始化基础信息
     */
    @Override
    public void initInfo() {
        faceUnitMap = new HashMap<>();

        for (Face face : faceList) {
                faceUnitMap.put(face, face.getUnit());
        }

        building = faceList.get(0).getUnit().getBuilding();
    }

    public List<Face> getFaceList() {
        return faceList;
    }

    public Map<Face, Unit> getFaceUnitMap() {
        return faceUnitMap;
    }
}
