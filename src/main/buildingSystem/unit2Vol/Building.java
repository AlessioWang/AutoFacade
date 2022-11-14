package unit2Vol;

import Tools.GeoTools;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.LinkedList;
import java.util.List;

/**
 * 记录Unit之间的关系
 *
 * @auther Alessio
 * @date 2022/11/10
 **/
public class Building {

    private List<Unit> unitList;

    //层数
    private int layerNumber;

    //总的建筑高度（各层层高相加）
    private double height;

    //各层平面的组合（即合并每一层的unit的底面单元）
    private List<WB_Polygon> planList;

    public Building() {
        unitList = new LinkedList<>();

        initUnitIndex();
        initUnitsPosNeighbor();
    }

    public Building(List<Unit> unitList) {
        this.unitList = unitList;

        initUnitIndex();
        initUnitsPosNeighbor();
    }

    /**
     * 根据List中的顺序来赋值unit的id
     */
    private void initUnitIndex() {
        for (int i = 0; i < unitList.size(); i++) {
            unitList.get(i).setId(i);
        }
    }

    /**
     * 以单元的信息确定距离的阈值
     *
     * @param unit
     * @param time
     * @return
     */
    private double calculateDisThreshold(Unit unit, double time) {
        double width = unit.getOriBase().getAABB().getWidth();
        double height = unit.getOriBase().getAABB().getHeight();
        double larger = Math.max(width, height);
        return Math.max(unit.getHeight(), larger) * time;
    }

    /**
     * 获取一个unit周边阈值范围内的units
     *
     * @param target
     * @param unitList
     * @return
     */
    private List<Unit> getNeighborUnits(Unit target, List<Unit> unitList) {
        List<Unit> neighbors = new LinkedList<>();
        double threshold = calculateDisThreshold(target, 1);

        for (Unit other : unitList) {
            if (other.getId() != target.getId()) {
                double dis = GeoTools.getDistance3D(target.getMidPt(), other.getMidPt());
                if (dis < threshold) {
                    neighbors.add(other);
                }
            }
        }

        return neighbors;
    }

    // TODO: 2022/11/13 需要研究更加合理地定义阈值方式
    private double threshold = 10;

    /**
     * 检索并设置Upper Unit
     *
     * @param target
     * @param neighbors
     */
    private void setUnitUpper(Unit target, List<Unit> neighbors) {
        WB_Point topPt = target.getTopFace().getMidPos();
        for (Unit unit : neighbors) {
            WB_Point btmPts = unit.getBottomFace().getMidPos();
            if (GeoTools.getDistance3D(topPt, btmPts) <= threshold) {
                target.setUpper(unit);
                break;
            }
        }
    }

    /**
     * 检索并设置right Unit
     *
     * @param target
     * @param neighbors
     */
    private void setUnitRight(Unit target, List<Unit> neighbors) {
        WB_Point oriPt = target.getRndFaces().get(0).getMidPos();
        for (Unit unit : neighbors) {
            WB_Point otherPt = unit.getRndFaces().get(2).getMidPos();
            if (GeoTools.getDistance3D(oriPt, otherPt) <= threshold) {
                target.setRight(unit);
                break;
            }
        }
    }

    /**
     * 检索并设置left Unit
     *
     * @param target
     * @param neighbors
     */
    private void setUnitLeft(Unit target, List<Unit> neighbors) {
        WB_Point oriPt = target.getRndFaces().get(2).getMidPos();
        for (Unit unit : neighbors) {
            WB_Point otherPt = unit.getRndFaces().get(0).getMidPos();
            if (GeoTools.getDistance3D(oriPt, otherPt) <= threshold) {
                target.setLeft(unit);
                break;
            }
        }
    }

    /**
     * 检索并设置Lower Unit
     *
     * @param target
     * @param neighbors
     */
    private void setUnitLower(Unit target, List<Unit> neighbors) {
        WB_Point btmPt = target.getBottomFace().getMidPos();
        for (Unit unit : neighbors) {
            WB_Point topPt = unit.getTopFace().getMidPos();
            if (GeoTools.getDistance3D(btmPt, topPt) <= threshold) {
                target.setLower(unit);
                break;
            }
        }
    }

    /**
     * 调用设置的方法
     *
     * @param target
     * @param neighbors
     */
    private void setNeighborDetail(Unit target, List<Unit> neighbors) {
        setUnitUpper(target, neighbors);
        setUnitLower(target, neighbors);
        setUnitRight(target, neighbors);
        setUnitLeft(target, neighbors);
    }

    /**
     * 建立unit之间的单元索引关系
     */
    private void initUnitsPosNeighbor() {
        for (Unit target : unitList) {
            List<Unit> neighbors = getNeighborUnits(target, unitList);
            setNeighborDetail(target, neighbors);
        }
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    public void setLayerNumber(int layerNumber) {
        this.layerNumber = layerNumber;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
