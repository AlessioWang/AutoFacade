package buildingControl.dataControl.calculator;

import buildingControl.dataControl.parameters.CarbonPara;
import buildingControl.dataControl.parameters.DensityPara;
import buildingControl.dataControl.Statistics;

/**
 * @auther Alessio
 * @date 2023/3/31
 **/
public class CarbonCalculator {
    private double carbon;

    private Statistics statistics;

    public CarbonCalculator(Statistics statistics) {
        this.statistics = statistics;

        calculate();
    }

    private void calculate() {
        double outCarbon = statistics.getOutConVol() * CarbonPara.C30;

        double innerCarbon = statistics.getInnerConVol() * CarbonPara.C30;
        System.out.println("c30 " + (outCarbon + innerCarbon));

        double roofCarbon = statistics.getRoofConVol() * CarbonPara.C50;

        double floorCarbon = statistics.getFloorConVol() * CarbonPara.C50;

        double beamCarbon = statistics.getBeamConVol() * CarbonPara.C50;

        double columnCarbon = statistics.getColumnConVol() * CarbonPara.C50;
        System.out.println("c50 " + (roofCarbon + floorCarbon + beamCarbon + columnCarbon));

        double glassCarbon = statistics.getGlassVol() * DensityPara.GLASS * CarbonPara.FLATBGLASS;

        System.out.println("glass " + glassCarbon);

        double alCarbon = statistics.getAlVol() * DensityPara.ALUMINUM + CarbonPara.ALWINDOW;
        System.out.println("al " + alCarbon);

        double steelCarbon = statistics.getAllSteelWeight() * CarbonPara.STEEL;
        System.out.println("steel " +steelCarbon );

        carbon = beamCarbon + columnCarbon + outCarbon + roofCarbon + innerCarbon + floorCarbon + glassCarbon + alCarbon + steelCarbon;
    }


    public double getCarbon() {
        return carbon;
    }

    @Override
    public String toString() {
        return "carbon=" + String.format("%.1f", carbon) + "kg CO2e";
    }

}
