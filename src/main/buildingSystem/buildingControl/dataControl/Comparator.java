package buildingControl.dataControl;

/**
 * @auther Alessio
 * @date 2023/4/25
 **/
public class Comparator {

    private double targetPrice;

    private double targetCarbon;

    private double currentPrice;

    private double currentCarbon;

    private double comparePrice;

    private double compareCarbon;

    private Statistics statistics;

    public Comparator(Statistics statistics) {
        this.statistics = statistics;

        setTarget();
        setCurrent();
        updateCompare();
    }

    public void updateTarget() {
        setTarget();
    }

    public void updateCurrent() {
        setCurrent();
        updateCompare();
    }

    /**
     * 以当前状态的statistics设置target参数
     */
    public void setTarget() {
        targetCarbon = statistics.getCarbonCalculator().getCarbon();

        targetPrice = statistics.getPriceCalculator().getPriceInTax();
    }

    /**
     * 以当前状态的statistics设置current参数
     */
    public void setCurrent() {
        currentCarbon = statistics.getCarbonCalculator().getCarbon();

        currentPrice = statistics.getPriceCalculator().getPriceInTax();
    }

    public void updateCompare() {
        compareCarbon = currentCarbon - targetCarbon;

        comparePrice = currentPrice - targetPrice;
    }

    public double getComparePrice() {
        return comparePrice;
    }

    public double getCompareCarbon() {
        return compareCarbon;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public double getTargetCarbon() {
        return targetCarbon;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getCurrentCarbon() {
        return currentCarbon;
    }
}

