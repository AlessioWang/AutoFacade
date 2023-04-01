package buildingControl.dataControl.calculator;

import buildingControl.dataControl.Statistics;
import buildingControl.dataControl.parameters.PricePara;

/**
 * @auther Alessio
 * @date 2023/4/1
 **/
public class PriceCalculator {
    private double priceNoTax;

    private double priceInTax;

    private Statistics statistics;

    public PriceCalculator(Statistics statistics) {
        this.statistics = statistics;

        calculator();
    }

    private void calculator() {
        double outPrice = statistics.getOutConVol() * PricePara.OUTPANEL;

        double innerPrice = statistics.getInnerConVol() * PricePara.INNERPANEL;

        double floorPrice = statistics.getFloorConVol() * PricePara.FLOORPANEL;

        double roofPrice = statistics.getRoofConVol() * PricePara.ROOFPANEL;

        double beamPrice = statistics.getBeamConVol() * PricePara.BEAMPANEL;

        double columnPrice = statistics.getColumnConVol() * PricePara.COLUMNPANEL;

        double windowPrice = statistics.getGlassArea() * PricePara.WINDOW;

        priceNoTax = outPrice + innerPrice + floorPrice + roofPrice + windowPrice+beamPrice+columnPrice;

        priceInTax = priceNoTax * (1 + PricePara.TAXRATE);
    }

    public double getPriceNoTax() {
        return priceNoTax;
    }

    public double getPriceInTax() {
        return priceInTax;
    }

    @Override
    public String toString() {
        return "priceNoTax=" + String.format("%.1f", priceNoTax) + ", priceInTax=" + String.format("%.1f", priceInTax);
    }
}
