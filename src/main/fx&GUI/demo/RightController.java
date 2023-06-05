package demo;

import Tools.FxTools;
import buildingControl.dataControl.Comparator;
import buildingControl.dataControl.calculator.CarbonCalculator;
import buildingControl.dataControl.calculator.PriceCalculator;
import buildingControl.dataControl.parameters.CarbonPara;
import buildingControl.dataControl.parameters.PricePara;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * @auther Alessio
 * @date 2023/4/25
 **/
public class RightController extends AnchorPane {

    private final PickFx applet;
//    private final PickSY applet;
    private final String fxPath = "/fxml/RightLayout.fxml";

    private boolean showCompare = false;

    /**
     * Price
     */
    //-----------------VOL---------------------
    @FXML
    private Label outVol;
    @FXML
    private Label innerVol;
    @FXML
    private Label floorVol;
    @FXML
    private Label roofVol;
    @FXML
    private Label beamVol;
    @FXML
    private Label columnVol;
    @FXML
    private Label glassArea;

    //---------------PRICE--------------------
    @FXML
    private Label outPrice;
    @FXML
    private Label innerPrice;
    @FXML
    private Label floorPrice;
    @FXML
    private Label roofPrice;
    @FXML
    private Label beamPrice;
    @FXML
    private Label columnPrice;
    @FXML
    private Label glassPrice;

    //---------------SUM---------------
    @FXML
    private Label outSum;
    @FXML
    private Label innerSum;
    @FXML
    private Label floorSum;
    @FXML
    private Label roofSum;
    @FXML
    private Label beamSum;
    @FXML
    private Label columnSum;
    @FXML
    private Label glassSum;
    @FXML
    private Label inTexSum;
    @FXML
    private Label noTexSum;


    /**
     * Carbon
     */
    //--------------NUM----------------
    @FXML
    private Label c30Num;
    @FXML
    private Label c50Num;
    @FXML
    private Label steelNum;
    @FXML
    private Label alNum;
    @FXML
    private Label glassNum;

    //--------------CARBON----------------
    @FXML
    private Label c30Carbon;
    @FXML
    private Label c50Carbon;
    @FXML
    private Label steelCarbon;
    @FXML
    private Label alCarbon;
    @FXML
    private Label glassCarbon;

    //--------------CARBON----------------
    @FXML
    private Label c30CarbonSum;
    @FXML
    private Label c50CarbonSum;
    @FXML
    private Label steelCarbonSum;
    @FXML
    private Label alCarbonSum;
    @FXML
    private Label glassCarbonSum;
    @FXML
    private Label allCarbonSum;

    //--------------Compare----------------
    @FXML
    private Label targetPrice;
    @FXML
    private Label currentPrice;
    @FXML
    private Label comparePrice;
    @FXML
    private Label targetCarbon;
    @FXML
    private Label currentCarbon;
    @FXML
    private Label compareCarbon;

    @FXML
    private JFXButton btnSetDesign;

    public RightController(PickFx applet) {
        this.applet = applet;
        FxTools.iniFxml2Controller(fxPath, this);

        applet.setRightController(this);
        initStyle();
    }

//    public RightController(PickSY applet) {
//        this.applet = applet;
//        FxTools.iniFxml2Controller(fxPath, this);
//
//        applet.setRightController(this);
//        initStyle();
//    }

    /**
     * 初始化页面上的控件信息
     */
    private void initStyle() {

    }

    public void setOnAction() {
        btnAction();
    }

    private void btnAction() {
        btnSetDesign.setOnAction(event -> {
            showCompare = true;
            setCompareTarget();
        });
    }

    public void updateInfo() {
        //price
        updateVol();
        updatePrice();
        updateSum();

        //carbon
        updateNum();
        updateCarbonPara();
        updateAllCarbon();

        //compare
//        updateCompare();
    }

    private void updateVol() {
        outVol.setText(String.format("%.1f", applet.statistics.getOutConVol()));
        innerVol.setText(String.format("%.1f", applet.statistics.getInnerConVol()));
        floorVol.setText(String.format("%.1f", applet.statistics.getFloorConVol()));
        roofVol.setText(String.format("%.1f", applet.statistics.getRoofConVol()));
        beamVol.setText(String.format("%.1f", applet.statistics.getBeamConVol()));
        columnVol.setText(String.format("%.1f", applet.statistics.getColumnConVol()));
        glassArea.setText(String.format("%.1f", applet.statistics.getGlassArea()));
    }

    private void updatePrice() {
        outPrice.setText(String.format("%.1f", PricePara.OUTPANEL));
        innerPrice.setText(String.format("%.1f", PricePara.INNERPANEL));
        floorPrice.setText(String.format("%.1f", PricePara.FLOORPANEL));
        roofPrice.setText(String.format("%.1f", PricePara.ROOFPANEL));
        beamPrice.setText(String.format("%.1f", PricePara.BEAMPANEL));
        columnPrice.setText(String.format("%.1f", PricePara.COLUMNPANEL));
        glassPrice.setText(String.format("%.1f", PricePara.WINDOW));
    }

    private void updateSum() {
        PriceCalculator pc = applet.getStatistics().getPriceCalculator();

        outSum.setText(String.format("%.1f", pc.getOutPrice()));
        innerSum.setText(String.format("%.1f", pc.getInnerPrice()));
        floorSum.setText(String.format("%.1f", pc.getFloorPrice()));
        roofSum.setText(String.format("%.1f", pc.getRoofPrice()));
        beamSum.setText(String.format("%.1f", pc.getBeamPrice()));
        columnSum.setText(String.format("%.1f", pc.getColumnPrice()));
        glassSum.setText(String.format("%.1f", pc.getGlassPrice()));
        noTexSum.setText(String.format("%.1f", pc.getPriceNoTax()));
        inTexSum.setText(String.format("%.1f", pc.getPriceInTax()));
    }

    public void updateNum() {
        CarbonCalculator cc = applet.getStatistics().getCarbonCalculator();

        c30Num.setText(String.format("%.1f", cc.getC30Vol()));
        c50Num.setText(String.format("%.1f", cc.getC50Vol()));
        steelNum.setText(String.format("%.1f", cc.getSteelWeight()));
        alNum.setText(String.format("%.1f", cc.getAlWeight()));
        glassNum.setText(String.format("%.1f", cc.getGlassWeight()));
    }

    private void updateCarbonPara() {
        c30Carbon.setText(String.format("%.1f", CarbonPara.C30));
        c50Carbon.setText(String.format("%.1f", CarbonPara.C50));
        steelCarbon.setText(String.format("%.1f", CarbonPara.STEEL));
        alCarbon.setText(String.format("%.1f", CarbonPara.AL));
        glassCarbon.setText(String.format("%.1f", CarbonPara.FLATBGLASS));
    }

    private void updateAllCarbon() {
        CarbonCalculator cc = applet.getStatistics().getCarbonCalculator();

        c30CarbonSum.setText(String.format("%.1f", cc.getC30Carbon()));
        c50CarbonSum.setText(String.format("%.1f", cc.getC50Carbon()));
        steelCarbonSum.setText(String.format("%.1f", cc.getSteelCarbon()));
        alCarbonSum.setText(String.format("%.1f", cc.getAlCarbon()));
        glassCarbonSum.setText(String.format("%.1f", cc.getGlassCarbon()));
        allCarbonSum.setText(String.format("%.1f", cc.getCarbon()));
    }

    private void setCompareTarget() {
        Comparator comparator = applet.comparator;
        comparator.updateTarget();

        targetCarbon.setText(String.format("%.1f", comparator.getTargetCarbon()));
        targetPrice.setText(String.format("%.1f", comparator.getTargetPrice()));
    }

    public void setCompareCurrent() {
        Comparator comparator = applet.comparator;
        comparator.updateCurrent();

        if (showCompare) {
            currentPrice.setText(String.format("%.1f", comparator.getCurrentPrice()));
            currentCarbon.setText(String.format("%.1f", comparator.getCurrentCarbon()));

            comparePrice.setText(String.format("%.1f", comparator.getComparePrice()));
            compareCarbon.setText(String.format("%.1f", comparator.getCompareCarbon()));
        }

        updateLabelColor(comparator.getComparePrice(), comparator.getCompareCarbon());
    }

    private void updateLabelColor(double price, double carbon) {
        Color red = Color.rgb(0, 145, 131);
        Color green = Color.rgb(227, 77, 36);

        if (price < 0) {
            comparePrice.setTextFill(red);
        } else {
            comparePrice.setTextFill(green);
        }

        if (carbon < 0) {
            compareCarbon.setTextFill(red);
        } else {
            compareCarbon.setTextFill(green);
        }

    }

}
