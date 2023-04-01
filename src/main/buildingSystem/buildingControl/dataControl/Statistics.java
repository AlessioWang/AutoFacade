package buildingControl.dataControl;

import buildingControl.dataControl.parameters.PanelThickPara;
import buildingControl.dataControl.parameters.SteelPara;
import buildingControl.designControl.FacadeMatcher;
import facade.basic.BasicObject;
import facade.unit.styles.ColumnSimple;
import facade.unit.styles.RecBeam;

import java.util.List;

/**
 * @auther Alessio
 * @date 2023/3/30
 **/
public class Statistics {

    private final FacadeMatcher fm;

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

    private double floorConVol;

    private double roofConVol;

    private double glassVol;

    private double alVol;

    private double columnConVol;

    private double beamConVol;

    /**
     * 钢筋重量
     */

    private double allSteelWeight;

    private double outSteelWeight;

    private double innerSteelWeight;

    private double floorSteelWeight;

    private double roofSteelWeight;

    private double columnSteelWeight;

    private double beamSteelWeight;

    public Statistics(FacadeMatcher facadeMatcher) {
        this.fm = facadeMatcher;

        initData();
    }

    private void initMaterialArea() {
        List<BasicObject> inner = fm.getInnerPanels();

        for (BasicObject p : inner) {
            innerConArea += p.concreteArea;
            glassArea += p.glassArea;
            alArea += p.alArea;
        }

        List<BasicObject> floor = fm.getFloorPanels();
        for (BasicObject p : floor) {
            floorConArea += p.concreteArea;
            glassArea += p.glassArea;
            alArea += p.alArea;
        }

        List<BasicObject> roof = fm.getRoofPanels();
        for (BasicObject p : roof) {
            roofConArea += p.concreteArea;
            glassArea += p.glassArea;
            alArea += p.alArea;
        }

        List<BasicObject> out = fm.getOutPanels();
        for (BasicObject p : out) {
            outConArea += p.concreteArea;
            glassArea += p.glassArea;
            alArea += p.alArea;
        }

        convertParaUnit();
    }


    private void initBeamVol() {
        List<BasicObject> beams = fm.getBeams();
        for (BasicObject b : beams) {
            beamConVol += volUnitConvert(((RecBeam) b).concreteVol);
        }

    }

    private void initColumnVol() {
        List<BasicObject> columns = fm.getColumns();
        for (BasicObject b : columns) {
            columnConVol += volUnitConvert(((ColumnSimple) b).concreteVol);
        }

    }

    private void initData() {
        initNumber();
        initMaterialArea();
        initVol();
    }

    private void initNumber() {
        allPanelNumber = fm.getPanels().size();
        outPanelNumber = fm.getOutPanels().size();
        innerPanelNumber = fm.getInnerPanels().size();
        floorPanelNumber = fm.getFloorPanels().size();
        roofPanelNumber = fm.getRoofPanels().size();
    }

    /**
     * 单位换算
     */
    private void convertParaUnit() {
        outConArea = areaUnitConvert(outConArea);
        innerConArea = areaUnitConvert(innerConArea);
        floorConArea = areaUnitConvert(floorConArea);
        roofConArea = areaUnitConvert(roofConArea);
        glassArea = areaUnitConvert(glassArea);
        alArea = areaUnitConvert(alArea);
    }


    private void initVol() {
        initColumnVol();

        initBeamVol();

        outConVol = outConArea * PanelThickPara.WALLTHICK;

        innerConVol = innerConArea * PanelThickPara.WALLTHICK;

        roofConVol = innerConArea * PanelThickPara.FLOORTHICK;

        floorConVol = floorConArea * PanelThickPara.FLOORTHICK;

        glassVol = glassArea * PanelThickPara.GLASSTHICK;

        alVol = alArea * PanelThickPara.ALTHICK;

        allConcreteVol = outConVol + innerConVol + roofConVol + floorConVol + columnConVol + beamConVol;

        initSteelVol();
    }


    /**
     * 由混凝土体积算用钢量
     */
    private void initSteelVol() {
        innerSteelWeight = innerConVol * SteelPara.INNERWALL;

        outSteelWeight = outConVol * SteelPara.OUTWALL;

        floorSteelWeight = floorConVol * SteelPara.FLOOR;

        roofSteelWeight = roofConVol * SteelPara.FLOOR;

        beamSteelWeight = beamConVol * SteelPara.BEAM;

        columnSteelWeight = columnConVol * SteelPara.COLUMN;

        allSteelWeight = innerSteelWeight + outSteelWeight + floorSteelWeight + roofSteelWeight + beamSteelWeight + columnSteelWeight;
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

    private double volUnitConvert(double origin) {
        return origin / 1000000000;
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

    public double getAllSteelWeight() {
        return allSteelWeight;
    }

    public double getGlassArea() {
        return glassArea;
    }

    public double getColumnConVol() {
        return columnConVol;
    }

    public double getBeamConVol() {
        return beamConVol;
    }

    public int getAllPanelNumber() {
        return allPanelNumber;
    }

    public int getOutPanelNumber() {
        return outPanelNumber;
    }

    public int getInnerPanelNumber() {
        return innerPanelNumber;
    }

    public int getFloorPanelNumber() {
        return floorPanelNumber;
    }

    public int getRoofPanelNumber() {
        return roofPanelNumber;
    }

    @Override
    public String toString() {
        return "Statistics{" + "allPanelNumber=" + allPanelNumber + ", allConcreteVol=" + allConcreteVol + ", glassVol=" + glassVol + ", alVol=" + alVol + ", allSteelWeight=" + allSteelWeight + '}';
    }
}
