package unit2Vol.panelBase;

import org.eclipse.collections.impl.bimap.mutable.HashBiMap;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Polygon;

import java.util.*;

/**
 * 由若干个基础的Face组成的PanelBase
 * @auther Alessio
 * @date 2022/12/13
 **/
public class MergedPanelBase extends PanelBase {
    private final List<Face> faceList;

    private Map<Face, Unit> faceUnitMap;

    public MergedPanelBase(List<Face> faceList) {
        this.faceList = faceList;
        init();
    }


    @Override
    public void init() {
        initInfo();
        initShape();
        initDir();
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
            shape = unionPolygon(polygons);
        }
    }

    /**
     * 合并相邻的空间几何图形
     * 仅适用于顶点相邻的情况
     *
     * @param polygons
     * @return
     */
    private WB_Polygon unionPolygon(List<WB_Polygon> polygons) {
        WB_Polygon base = polygons.get(0);

        List<WB_Coord> basePoints = base.getPoints().toList();
        Set<WB_Coord> set = new HashSet<>(basePoints);

        polygons.remove(0);

        for (WB_Polygon p : polygons) {
            List<WB_Coord> wb_coords = p.getPoints().toList();
            for (WB_Coord c : wb_coords) {
                if (set.contains(c)) {
                    set.remove(c);
                } else {
                    set.add(c);
                }
            }
        }

        System.out.println(set);
        return new WB_Polygon(new ArrayList<>(set));
    }

    /**
     * 初始化基础信息
     */
    @Override
    public void initInfo() {
        faceUnitMap = new HashBiMap<>();

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
