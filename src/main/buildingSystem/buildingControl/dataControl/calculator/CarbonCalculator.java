package buildingControl.dataControl.calculator;

import buildingControl.dataControl.Statistics;
import buildingControl.dataControl.parameters.CarbonPara;
import buildingControl.dataControl.parameters.DensityPara;

/**
 * @auther Alessio
 * @date 2023/3/31
 **/
public class CarbonCalculator {
    private double carbon;

    private Statistics statistics;

    //vol or weight paras
    private double c30Vol;

    private double c50Vol;

    private double steelWeight;

    private double alWeight;

    private double glassWeight;

    //carbon paras

    private double c30Carbon;

    private double c50Carbon;

    private double glassCarbon;

    private double alCarbon;

    private double steelCarbon;

    public CarbonCalculator(Statistics statistics) {
        this.statistics = statistics;

        calculate();
    }

    private void calculate() {
        c30Vol = statistics.getOutConVol() + statistics.getInnerConVol();
        c30Carbon = c30Vol * CarbonPara.C30;

        c50Vol = statistics.getRoofConVol() + statistics.getFloorConVol() + statistics.getBeamConVol() + statistics.getColumnConVol();
        c50Carbon = c50Vol * CarbonPara.C50;

        steelWeight = statistics.getAllSteelWeight();
        steelCarbon = steelWeight * CarbonPara.STEEL;

        glassWeight = statistics.getGlassVol() * DensityPara.GLASS;
        glassCarbon = glassWeight * CarbonPara.FLATBGLASS;

        alWeight = statistics.getAlVol() * DensityPara.ALUMINUM;
        alCarbon = alWeight * CarbonPara.AL;

        carbon = c30Carbon + c50Carbon + glassCarbon + alCarbon + steelCarbon;
    }

    public void showCarbon() {
        System.out.println("\033[33m");
        System.out.println("=====================CARBON CALCULATION=======================");
        System.out.println("ITEM           NUM            CARBON          SUM ");

        System.out.println(String.format("%s | %s --> %s | %s", "C30   ", c30Vol, CarbonPara.C30, c30Carbon));
        System.out.println(String.format("%s | %s --> %s | %s", "C50   ", c50Vol, CarbonPara.C50, c50Carbon));
        System.out.println(String.format("%s | %s --> %s | %s", "STEEL ", steelWeight, CarbonPara.STEEL, steelCarbon));
        System.out.println(String.format("%s | %s --> %s | %s", "AL    ", alWeight, CarbonPara.AL, alCarbon));
        System.out.println(String.format("%s | %s --> %s | %s", "GLASS ", glassWeight, CarbonPara.FLATBGLASS, glassCarbon));
        System.out.println("----------------------------------------------------------");

        System.out.println(String.format("%s | %s", "All CARBON ", carbon));

        System.out.println("=============================================================");
        System.out.println("\033[0m");
    }


    public double getCarbon() {
        return carbon;
    }

    @Override
    public String toString() {
        return "carbon=" + String.format("%.1f", carbon) + "kg CO2e";
    }

}
