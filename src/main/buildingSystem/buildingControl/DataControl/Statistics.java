package buildingControl.DataControl;

import buildingControl.DesignControl.FacadeMatcher;
import facade.basic.BasicObject;
import unit2Vol.Building;

import java.util.List;

/**
 * @auther Alessio
 * @date 2023/3/30
 **/
public class Statistics {

    private Building building;

    private FacadeMatcher facadeMatcher;

    /**
     * 数量相关参数
     */

    private int allPanelNumber;

    private int outPanelNumber;

    private int innerPanelNumber;

    private int floorPanelNumber;

    private int roofPanelNumber;

    /**
     * 面积相关参数
     */

    private double allConcreteArea;

    private double outConArea;

    private double glassArea;

    private double alArea;

    private double innerConArea;

    private double floorConArea;

    private double roofConArea;

    /**
     * 体积相关参数
     */

    private double allConcreteVol;

    private double outConVol;

    private double innerConVol;

    private double glassVol;

    private double alVol;

    private double floorConVol;

    private double roofConVol;

    public Statistics(FacadeMatcher facadeMatcher) {
        this.facadeMatcher = facadeMatcher;
        building = facadeMatcher.getBc().getBuilding();

        syncData();

        initData();
        initVol();
    }

    private void initData() {
        List<BasicObject> panels = facadeMatcher.getPanels();
        for (BasicObject p : panels) {
            outConArea += p.concreteArea;
            glassArea += p.glassArea;
            alArea += p.alArea;
        }

        convertParaUnit();
    }

    public void syncData() {
        updateAllNum();
        updateVol();
    }

    /**
     * 单位换算
     */
    private void convertParaUnit() {
        outConArea = areaUnitConvert(outConArea);
        glassArea = areaUnitConvert(glassArea);
        alArea = areaUnitConvert(alArea);
    }

    private void updateAllNum() {
        allPanelNumber = outPanelNumber + innerPanelNumber + floorPanelNumber + roofPanelNumber;
    }

    private void updateVol() {
        allConcreteVol = outConVol + innerConVol + floorConVol + roofConVol;
    }

    private void initVol() {
        outConVol = outConArea * PanelPara.WALLTHICK;

        glassVol = glassArea * PanelPara.GLASSTHICK;

        alVol = alArea * PanelPara.ALTHICK;
    }

    /**
     * 面积单位换算
     * 由毫米单位换算到米单位
     *
     * @param origin
     * @return
     */
    private double areaUnitConvert(double origin) {
        return origin / 1000000;
    }

    public double getAllConcreteVol() {
        return allConcreteVol;
    }

    public double getOutConVol() {
        return outConVol;
    }

    public double getInnerConVol() {
        return innerConVol;
    }

    public double getGlassVol() {
        return glassVol;
    }

    public double getAlVol() {
        return alVol;
    }

    public double getFloorConVol() {
        return floorConVol;
    }

    public double getRoofConVol() {
        return roofConVol;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "outConArea=" + outConArea +
                ", glassArea=" + glassArea +
                ", alArea=" + alArea +
                ", outConVol=" + outConVol +
                ", glassVol=" + glassVol +
                ", alVol=" + alVol +
                '}';
    }
}
