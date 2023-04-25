package buildingControl.dataControl.calculator;

import buildingControl.dataControl.Statistics;
import buildingControl.dataControl.parameters.PricePara;

import java.math.BigDecimal;

/**
 * @auther Alessio
 * @date 2023/4/1
 **/
public class PriceCalculator {
    private double priceNoTax;

    private double priceInTax;

    private Statistics statistics;

    private double outPrice;

    private double innerPrice;

    private double floorPrice;

    private double roofPrice;

    private double beamPrice;

    private double columnPrice;

    private double glassPrice;


    public PriceCalculator(Statistics statistics) {
        this.statistics = statistics;

        calculator();
    }

    private void calculator() {

        outPrice = statistics.getOutConVol() * PricePara.OUTPANEL;

        innerPrice = statistics.getInnerConVol() * PricePara.INNERPANEL;

        floorPrice = statistics.getFloorConVol() * PricePara.FLOORPANEL;

        roofPrice = statistics.getRoofConVol() * PricePara.ROOFPANEL;

        beamPrice = statistics.getBeamConVol() * PricePara.BEAMPANEL;

        columnPrice = statistics.getColumnConVol() * PricePara.COLUMNPANEL;

        glassPrice = statistics.getGlassArea() * PricePara.WINDOW;

        priceNoTax = outPrice + innerPrice + floorPrice + roofPrice + glassPrice + beamPrice + columnPrice;

        priceInTax = priceNoTax * (1 + PricePara.TAXRATE);

    }

    public void showPrice() {
        System.out.println("\033[33m");
        System.out.println("=====================PRICE CALCULATION=======================");
        System.out.println("ITEM           VOL             PRICE          SUM ");

        //数据部分
        System.out.println(String.format("%s | %s --> %s | %s", "OUT   ", statistics.getOutConVol(), PricePara.OUTPANEL, outPrice));
        System.out.println(String.format("%s | %s --> %s | %s", "INNER ", statistics.getInnerConVol(), PricePara.INNERPANEL, innerPrice));
        System.out.println(String.format("%s | %s --> %s | %s", "FLOOR ", statistics.getFloorConVol(), PricePara.FLOORPANEL, floorPrice));
        System.out.println(String.format("%s | %s --> %s | %s", "ROOF  ", statistics.getRoofConVol(), PricePara.ROOFPANEL, roofPrice));
        System.out.println(String.format("%s | %s --> %s | %s", "BEAM  ", statistics.getBeamConVol(), PricePara.BEAMPANEL, beamPrice));
        System.out.println(String.format("%s | %s --> %s | %s", "COLUMN", statistics.getColumnConVol(), PricePara.COLUMNPANEL, columnPrice));
        System.out.println(String.format("%s | %s --> %s | %s", "GLASS ", statistics.getGlassArea(), PricePara.WINDOW, glassPrice));
        System.out.println("----------------------------------------------------------");

        System.out.println(String.format("%s | %s", "SUM(NO TEX) ", new BigDecimal(priceNoTax).toPlainString()));
        System.out.println(String.format("%s | %s", "SUM(IN TEX) ",new BigDecimal(priceInTax).toPlainString()));
        System.out.println(String.format("%s | %s", "TEX RATE    ", PricePara.TAXRATE * 100 + "%"));

        System.out.println("=============================================================");
        System.out.println("\033[0m");
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

    public double getOutPrice() {
        return outPrice;
    }

    public double getInnerPrice() {
        return innerPrice;
    }

    public double getFloorPrice() {
        return floorPrice;
    }

    public double getRoofPrice() {
        return roofPrice;
    }

    public double getBeamPrice() {
        return beamPrice;
    }

    public double getColumnPrice() {
        return columnPrice;
    }

    public double getGlassPrice() {
        return glassPrice;
    }
}
