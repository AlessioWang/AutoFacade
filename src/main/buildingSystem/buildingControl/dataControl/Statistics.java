package buildingControl.dataControl;

import buildingControl.dataControl.calculator.CarbonCalculator;
import buildingControl.dataControl.calculator.PriceCalculator;
import buildingControl.dataControl.parameters.PanelThickPara;
import buildingControl.dataControl.parameters.SteelPara;
import buildingControl.designControl.F_Matcher;
import buildingControl.designControl.FacadeMatcher;
import facade.basic.BasicObject;
import facade.unit.styles.ColumnSimple;
import facade.unit.styles.RecBeam;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2023/3/30
 **/
public class Statistics {

    private FacadeMatcher fm;

    private F_Matcher f_matcher;

    private CarbonCalculator carbonCalculator;

    private PriceCalculator priceCalculator;

    /**
     * 读取的模型数据
     */

    private List<BasicObject> all;
    private List<BasicObject> inner;
    private List<BasicObject> floor;
    private List<BasicObject> roof;
    private List<BasicObject> out;
    private List<BasicObject> column;
    private List<BasicObject> beam;

    /**
     * 数量相关参数
     */

    private int allPanelNumber;

    private int outPanelNumber;

    private int innerPanelNumber;

    private int floorPanelNumber;

    private int roofPanelNumber;

    private int columnNumber;

    private int beamNumber;

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

        initCalculator();
    }

    public Statistics(F_Matcher f_matcher) {
        this.f_matcher = f_matcher;

        initData();
        initCalculator();
    }

    private void initData() {
        initDataFromMatcher();
        initNumber();
        initMaterialArea();
        initVol();
    }

    /**
     * 初始化两个calculator
     */
    private void initCalculator() {
        carbonCalculator = new CarbonCalculator(this);
        priceCalculator = new PriceCalculator(this);
    }

    private void initDataFromMatcher() {
        if (fm != null) {
            all = fm.getPanels();
            inner = fm.getInnerPanels();
            floor = fm.getFloorPanels();
            roof = fm.getRoofPanels();
            out = fm.getOutPanels();
            column = fm.getColumns();
            beam = fm.getBeams();
        } else {
            all = new LinkedList<>(f_matcher.getPanelMap().values());
            inner = new LinkedList<>(f_matcher.getInnerMap().values());
            floor = new LinkedList<>(f_matcher.getFloorMap().values());
            roof = new LinkedList<>(f_matcher.getRoofMap().values());
            out = new LinkedList<>(f_matcher.getOutMap().values());
            column = new LinkedList<>(f_matcher.getColumnList());
            beam = new LinkedList<>(f_matcher.getBeamsList());
        }
    }

    private void initMaterialArea() {

        for (BasicObject p : inner) {
            innerConArea += p.concreteArea;
            glassArea += p.glassArea;
            alArea += p.alArea;
        }

        for (BasicObject p : floor) {
            floorConArea += p.concreteArea;
            glassArea += p.glassArea;
            alArea += p.alArea;
        }

        for (BasicObject p : roof) {
            roofConArea += p.concreteArea;
            glassArea += p.glassArea;
            alArea += p.alArea;
        }

        for (BasicObject p : out) {
            outConArea += p.concreteArea;
            glassArea += p.glassArea;
            alArea += p.alArea;
        }


        convertParaUnit();
    }


    private void initBeamVol() {
        for (BasicObject b : beam) {
            beamConVol += volUnitConvert(((RecBeam) b).concreteVol);
        }

    }

    private void initColumnVol() {
        for (BasicObject b : column) {
            //去掉位置重复
            columnConVol += volUnitConvert(((ColumnSimple) b).concreteVol) * 0.25;
        }
    }

    private void initNumber() {
        allPanelNumber = all.size();
        outPanelNumber = out.size();
        innerPanelNumber = inner.size();
        floorPanelNumber = floor.size();
        roofPanelNumber = roof.size();
        columnNumber = column.size();
        beamNumber = beam.size();
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

    public void showPanelNum() {
        System.out.println("\033[33m");
        System.out.println("=====STATISTICS INFORMATION=====");
        System.out.println("ITEM               NUM");

        System.out.println(String.format("%s  -->  %s", "OUT-PANEL  ", outPanelNumber));
        System.out.println(String.format("%s  -->  %s", "INNER-PANEL", innerPanelNumber));
//        System.out.println(String.format("%s --> %s", "FLOOR-PANEL ", floorPanelNumber));
        System.out.println(String.format("%s  -->  %s", "FLOOR-PANEL", fm.getBc().getBuilding().getUnitList().size()));
        System.out.println(String.format("%s  -->  %s", "ROOF-PANEL ", roofPanelNumber));
        System.out.println(String.format("%s  -->  %s", "COLUMN-NUM ", columnNumber / 2));
        System.out.println(String.format("%s  -->  %s", "BEAM-NUM   ", beamNumber / 2));


        System.out.println("===============================");
        System.out.println("\033[0m");
    }

    public void showPrice() {
        priceCalculator.showPrice();
    }

    public void showCarbon() {
        carbonCalculator.showCarbon();
    }

    public void showPriceAndCarbon() {
        showPrice();
        showCarbon();
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

    public CarbonCalculator getCarbonCalculator() {
        return carbonCalculator;
    }

    public PriceCalculator getPriceCalculator() {
        return priceCalculator;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "fm=" + fm +
                ", allPanelNumber=" + allPanelNumber +
                ", outPanelNumber=" + outPanelNumber +
                ", innerPanelNumber=" + innerPanelNumber +
                ", floorPanelNumber=" + floorPanelNumber +
                ", roofPanelNumber=" + roofPanelNumber +
                ", columnNumber=" + columnNumber +
                ", beamNumber=" + beamNumber +
                ", outConArea=" + outConArea +
                ", glassArea=" + glassArea +
                ", alArea=" + alArea +
                ", innerConArea=" + innerConArea +
                ", floorConArea=" + floorConArea +
                ", roofConArea=" + roofConArea +
                ", allConcreteVol=" + allConcreteVol +
                ", outConVol=" + outConVol +
                ", innerConVol=" + innerConVol +
                ", floorConVol=" + floorConVol +
                ", roofConVol=" + roofConVol +
                ", glassVol=" + glassVol +
                ", alVol=" + alVol +
                ", columnConVol=" + columnConVol +
                ", beamConVol=" + beamConVol +
                ", allSteelWeight=" + allSteelWeight +
                ", outSteelWeight=" + outSteelWeight +
                ", innerSteelWeight=" + innerSteelWeight +
                ", floorSteelWeight=" + floorSteelWeight +
                ", roofSteelWeight=" + roofSteelWeight +
                ", columnSteelWeight=" + columnSteelWeight +
                ", beamSteelWeight=" + beamSteelWeight +
                '}';
    }


}
