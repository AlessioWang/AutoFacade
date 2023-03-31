package buildingControl.DataControl;

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

        double glassCarbon = statistics.getGlassVol() * DensityPara.GLASS * CarbonPara.FLATBGLASS;

        double alCarbon = statistics.getAlVol() * DensityPara.ALUMINUM;

        carbon = outCarbon + glassCarbon + alCarbon;
    }

    public double getCarbon() {
        return carbon;
    }

    @Override
    public String toString() {
        return "carbon=" + String.format("%.1f", carbon) + "kg CO2e";
    }

}
