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
        System.out.println("--------------------");

        double outPrice = statistics.getOutConVol() * PricePara.OUTPANEL;
        System.out.println("out " + statistics.getOutConVol() + "-->" + outPrice);

        double innerPrice = statistics.getInnerConVol() * PricePara.INNERPANEL;
        System.out.println("inner " + statistics.getInnerConVol() + "-->" + innerPrice);

        double floorPrice = statistics.getFloorConVol() * PricePara.FLOORPANEL;
        System.out.println("floor " + statistics.getFloorConVol() + "-->" + floorPrice);

        double roofPrice = statistics.getRoofConVol() * PricePara.ROOFPANEL;
        System.out.println("roof " + statistics.getRoofConVol() + "-->" + roofPrice);

        double beamPrice = statistics.getBeamConVol() * PricePara.BEAMPANEL;
        System.out.println("beam " + statistics.getBeamConVol() + "-->" + beamPrice);

        double columnPrice = statistics.getColumnConVol() * PricePara.COLUMNPANEL;
        System.out.println("column " + statistics.getColumnConVol() + "-->" + columnPrice);

        double windowPrice = statistics.getGlassArea() * PricePara.WINDOW;
        System.out.println("windows " + statistics.getGlassArea() + "-->" + windowPrice);

        priceNoTax = outPrice + innerPrice + floorPrice + roofPrice + windowPrice + beamPrice + columnPrice;

        priceInTax = priceNoTax * (1 + PricePara.TAXRATE);

        System.out.println("--------------------");
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
