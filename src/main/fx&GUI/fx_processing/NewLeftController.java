package fx_processing;

import Tools.FxTools;
import javafx.scene.layout.AnchorPane;

/**
 * @auther Alessio
 * @date 2023/4/19
 **/
public class NewLeftController extends AnchorPane {

    private final NewMain applet;
    private final String fxPath = "/fxml/LeftLayout.fxml";

    public NewLeftController(NewMain applet) {
        this.applet = applet;

        this.setWidth(300);

        this.applet.setLeftController(this);
        FxTools.iniFxml2Controller(fxPath, this);
    }



}
