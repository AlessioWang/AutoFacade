package demo;

import Test.GUITest.PickFx;
import Tools.FxTools;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * @auther Alessio
 * @date 2023/4/19
 **/
public class NewLeftController extends AnchorPane {

    private final PickFx applet;
    private final String fxPath = "/fxml/LeftLayout.fxml";

    @FXML
    private JFXToggleButton togShowPanel;

    @FXML
    private JFXToggleButton togShowBase;

    @FXML
    private JFXToggleButton togShowStructure;


    public NewLeftController(PickFx applet) {
        this.applet = applet;

        applet.setLeftController(this);
        FxTools.iniFxml2Controller(fxPath, this);
    }

    public void setOnAction() {
        toggleAction();
    }

    private void toggleAction() {
        togShowPanel.setOnAction(event -> {
            applet.showPanel = togShowPanel.isSelected();
        });

        togShowStructure.setOnAction(event -> {
            applet.showStructure = togShowStructure.isSelected();
        });

        togShowBase.setOnAction(event -> {
            applet.showContainer = togShowBase.isSelected();
        });
    }
}
