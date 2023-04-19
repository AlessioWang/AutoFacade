package Tools;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * JavaFx常用工具合集
 *
 * @auther Alessio
 * @date 2021/4/15
 **/
public class FxTools {

    /**
     * 得到gridPane指定位置的node
     * 如果没有，则返回空
     *
     * @param col  列数
     * @param row  行数
     * @param pane 所在的gridPane
     */
    public static Node getNodeInGridPane(int col, int row, GridPane pane) {
        ObservableList<Node> children = pane.getChildren();
        for (Node node : children) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if (columnIndex == null)
                columnIndex = 0;
            if (rowIndex == null)
                rowIndex = 0;

            if (columnIndex == col && rowIndex == row) {
                return node;
            }
        }
        return null;
    }


    /**
     * @param rawComboBox 原始待设置的ComboBox
     * @param promptText  初始文本
     * @param labels      可选择文本
     * @param ifEditable  是否可以编辑
     */
    public static void createComboBox(JFXComboBox<Label> rawComboBox, String promptText, ArrayList<Label> labels, boolean ifEditable) {
        rawComboBox.setPromptText(promptText);
        rawComboBox.getItems().addAll(labels);
        rawComboBox.setEditable(ifEditable);
    }

    @SafeVarargs
    public static void comboBoxInit(String promptText, ArrayList<Label> labels, boolean ifEditable, JFXComboBox<Label>... rawComboBoxList) {
        for (JFXComboBox<Label> comboBox : rawComboBoxList) {
            createComboBox(comboBox, promptText, labels, ifEditable);
        }
    }

    /**
     * 为一个pane里面所有的comboBox设置prefSize
     *
     * @param pane
     * @param width
     * @param height
     */
    public static void comboBoxPreSizeSetting(Pane pane, double width, double height) {
        List<Node> nodes = pane.getChildren();
        for (Node n : nodes) {
            if (n instanceof JFXComboBox) {
                JFXComboBox<Label> comboBox = (JFXComboBox<Label>) n;
                comboBox.setPrefWidth(width);
                comboBox.setPrefHeight(height);
            }
        }
    }

    /**
     * 为一个pane里面所有的comboBox设置minSize
     *
     * @param pane
     * @param width
     * @param height
     */
    public static void comboBoxMinSizeSetting(Pane pane, double width, double height) {
        List<Node> nodes = pane.getChildren();
        for (Node n : nodes) {
            if (n instanceof JFXComboBox) {
                JFXComboBox<Label> comboBox = (JFXComboBox<Label>) n;
                comboBox.setMinSize(width, height);
            }
        }
    }

    /**
     * 为一个pane里面所有的comboBox设置maxSize
     *
     * @param pane
     * @param width
     * @param height
     */
    public static void comboBoxMaxSizeSetting(Pane pane, double width, double height) {
        List<Node> nodes = pane.getChildren();
        for (Node n : nodes) {
            if (n instanceof JFXComboBox) {
                JFXComboBox<Label> comboBox = (JFXComboBox<Label>) n;
                comboBox.setMaxSize(width, height);
            }
        }
    }

    /**
     * 通过选取的元素来得到对应图层的string
     *
     * @param gridPane
     * @param name
     * @param col
     * @return 对应元素的图层名称
     */
    public static List<String> setLayerByCombo(GridPane gridPane, String name, int col) {
        List<String> layers = new ArrayList<>();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof JFXComboBox) {
                if (((JFXComboBox<?>) node).getSelectionModel().getSelectedItem() != null) {
                    JFXComboBox<Label> n = (JFXComboBox<Label>) node;
                    if (n.getSelectionModel().getSelectedItem().getText().equals(name)) {
                        int row = GridPane.getRowIndex(n);
                        JFXComboBox<Label> box = (JFXComboBox<Label>) FxTools.getNodeInGridPane(col, row, gridPane);
                        if (box.getSelectionModel().getSelectedItem() != null) {
                            String layer = box.getSelectionModel().getSelectedItem().getText();
                            layers.add(layer);
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
        return layers;
    }

    /**
     * 读取并返回指定位置的textField的数值
     *
     * @param gridPane
     * @param name
     * @param col
     * @return
     */
    public static double setPara(GridPane gridPane, String name, int col) {
        double para = 0;
        for (Node node : gridPane.getChildren()) {
            if (node instanceof JFXComboBox) {
                if (((JFXComboBox<?>) node).getSelectionModel().getSelectedItem() != null) {
                    JFXComboBox<Label> n = (JFXComboBox<Label>) node;
                    if (n.getSelectionModel().getSelectedItem().getText().equals(name)) {
                        int row = GridPane.getRowIndex(n);
                        TextField t = (TextField) getNodeInGridPane(col, row, gridPane);
                        para = Double.parseDouble(t.getText());
                    }
                }
            }
        }
        return para;
    }

    /**
     * 创建alert框
     *
     * @param title
     * @param header
     * @param context
     * @param alertType
     * @return
     */
    public static Alert alertCreator(String title, String header, String context, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        return alert;
    }

    /**
     * 以汉语拼音顺序来排列数组
     *
     * @param list
     * @return
     */
    public static List<String> sortListASChineseAlphabet(List<String> list) {
        Comparator comparator = Collator.getInstance(java.util.Locale.CHINA);
        Collections.sort(list, comparator);
        return list;
    }

    public static boolean checkStringInList(List<String> list, String target) {
        boolean flag = false;
        for (String s : list) {
            if (s.equals(target)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 设置comboBox<Label>选取的label
     *
     * @param label
     * @param comboBoxList
     */
    @SafeVarargs
    public static void setComboBoxByLabel(Label label, JFXComboBox<Label>... comboBoxList) {
        for (JFXComboBox<Label> comboBox : comboBoxList) {
            comboBox.getSelectionModel().select(label);
        }
    }

    /**
     * 以string来检索comboBox内部的的label名字，并返回一个label
     *
     * @param comboBox
     * @param name
     * @return
     */
    public static Label getLabelInCombobox(JFXComboBox<Label> comboBox, String name) {
        Label target = new Label();
        List<Label> labelList = comboBox.getItems();
        for (Label label : labelList) {
            if (label.getText().equals(name)) {
                target = label;
            }
        }
        return target;
    }

    /**
     * 以String来设置comboBox的默认选择
     *
     * @param comboBox
     * @param name
     */
    public static void setComboBoxByString(JFXComboBox<Label> comboBox, String name) {
        Label label = FxTools.getLabelInCombobox(comboBox, name);
        if (label != null) {
            FxTools.setComboBoxByLabel(label, comboBox);
        }
    }


    /**
     * 初始化按钮样式
     *
     * @param style
     * @param btnList
     */
    public static void iniButtonStyle(String style, Button... btnList) {
        for (Button btn : btnList) {
            btn.getStyleClass().add(style);
        }
    }

    /**
     * 保留两位小数
     *
     * @param source
     * @return
     */
    public static double remain2Deci(double source) {
        return Double.parseDouble(String.format("%.2f", source));
    }

    /**
     * 设置相匹配的两个comboBox
     *
     * @param elementCombo
     * @param layerCombo
     * @param elementLabel
     * @param layerName
     */
    public static void setLayerElement(JFXComboBox<Label> elementCombo, JFXComboBox<Label> layerCombo, Label elementLabel, String layerName, List<String> fileLayers) {
        setComboBoxByLabel(elementLabel, elementCombo);
        if (fileLayers.contains(layerName)) {
            setComboBoxByString(layerCombo, layerName);
        }
    }

    /**
     * 从sheet里面读取信息再初始化建立对应的comboBox
     *
     * @param label
     * @param layerNames
     * @param elementsLabels
     * @param layerLabels
     * @param gridPane
     */
    public static void iniComboInGridPaneFromSheet(Label label, List<String> layerNames, ArrayList<Label> elementsLabels, ArrayList<Label> layerLabels, List<String> fileLayers, GridPane gridPane) {
        if (layerNames != null) {
            for (String name : layerNames) {
                JFXComboBox<Label> newElementCombo = new JFXComboBox<>();
                JFXComboBox<Label> newLayerCombo = new JFXComboBox<>();
                createComboBox(newElementCombo, "选择图元", elementsLabels, false);
                createComboBox(newLayerCombo, "选择图层", layerLabels, false);
                setLayerElement(newElementCombo, newLayerCombo, label, name, fileLayers);
                int rowNum = gridPane.getRowCount();
                gridPane.addRow(rowNum, newElementCombo, newLayerCombo);
                comboBoxPreSizeSetting(gridPane, 150, 27);
            }
        }
    }

    /**
     * 获取文件的位置
     *
     * @param pane
     * @param title
     * @param fileData
     * @return
     */
    public static String getFileOpenPath(String title, Pane pane, String[]... fileData) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        for (String[] pair : fileData) {
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(pair[0], pair[1])
            );
        }
        File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
        if (file != null) {
            return file.getPath();
        } else
            return null;
    }

    /**
     * 将滑杆的数值同步到label
     * 默认保留一位小数
     *
     * @param slider
     * @param label
     */
    public static void synchronizeSliderText(Slider slider, Label label) {
        slider.valueProperty().addListener((observable, oldValue, newValue) ->
                label.setText(String.valueOf(String.format("%.1f", newValue.doubleValue()))));
    }

    /**
     * 将controller与fx文件进行绑定
     *
     * @param fxPath
     */
    public static void iniFxml2Controller(String fxPath, Pane pane) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(pane.getClass().getResource(fxPath));
        loader.setRoot(pane);
        loader.setController(pane);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Stage
     *
     * @param stage
     * @param scene
     * @param isAlwaysOnTop
     * @param resizeable
     * @param iconPath
     */
    public static void iniStage(Stage stage, Scene scene, boolean isAlwaysOnTop, boolean resizeable, String iconPath) {
        stage.setAlwaysOnTop(isAlwaysOnTop);
        stage.setResizable(resizeable);
        stage.setScene(scene);
        stage.getIcons().add(new Image(iconPath));
        stage.showAndWait();
    }

    /**
     * 在processing中创建swing报错框
     *
     * @param frame
     * @param message
     * @param title
     */
    public static void swingErrorAlert(Frame frame, String message, String title) {
        JOptionPane.showMessageDialog(frame,
                message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 将rgb颜色转成Paint
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static Paint getPaintColor(int r, int g, int b) {
        return new Color((double) r / 255, (double) g / 255, (double) b / 255, 1);
    }


}
