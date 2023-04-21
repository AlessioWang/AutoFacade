package demo;

import Test.GUITest.PickFx;
import Tools.FxTools;
import buildingControl.designControl.PanelStyle;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2023/4/19
 **/
public class NewLeftController extends AnchorPane {

    private final PickFx applet;
    private final String fxPath = "/fxml/LeftLayout.fxml";
    private final String panelPath = "C:\\Bingqi\\InstAAA\\FacadeCreator\\src\\main\\java\\facade\\unit\\styles";

    @FXML
    private JFXToggleButton togShowPanel;

    @FXML
    private JFXToggleButton togShowBase;

    @FXML
    private JFXToggleButton togShowStructure;

    @FXML
    private JFXComboBox<Label> comboPanelType;

    public NewLeftController(PickFx applet) {
        this.applet = applet;

        applet.setLeftController(this);
        FxTools.iniFxml2Controller(fxPath, this);

        initStyle();
    }

    /**
     * 初始化页面上的控件信息
     */
    private void initStyle() {
        initCombo();
    }

    /**
     * 初始化combo_box
     */
    private void initCombo() {
        ArrayList<Label> labels = new ArrayList<>();

        PanelStyle[] allTypes = PanelStyle.values();

        List<String> panelName = getPanelName(panelPath);
        for (var type : panelName) {
            Label label = new Label(type);
            labels.add(label);
        }

        FxTools.createComboBox(comboPanelType, "Panel Type", labels, false);
    }

    public void setOnAction() {
        toggleAction();

        comboAction();
    }

    private void comboAction() {
        comboPanelType.setOnAction(event -> {
            String selected = getSelectFromCombo(comboPanelType);
            System.out.println(selected);
        });
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

    /**
     * 获取combobox选取的label内容
     *
     * @param combo
     * @return
     */
    private String getSelectFromCombo(JFXComboBox<Label> combo) {
        Label selectedItem = combo.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            return selectedItem.getText();
        }
        return null;
    }

    public List<String> getPanelName(String path) {
        List<String> result = new LinkedList<>();

        File[] files = getFiles(path);
        for (File f : files) {
            String name = f.getName();
            String style = name.substring(0, name.indexOf("."));
            result.add(style);
        }

        return result;
    }

    public File[] getFiles(String path) {
        File file = new File(path);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件 文件夹
                if (files[i].isDirectory()) {
                    getFiles(files[i].getPath());
                }
            }
            return files;
        }

        return null;
    }
}
