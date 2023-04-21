package buildingControl.designControl;

/**
 * @auther Alessio
 * @date 2023/4/21
 **/
public enum PanelStyle {
    Simple("Simple"),

    F_Example("F_Example"),

    F_OneHole("F_OneHole"),

    F_TwoWindow("F_TwoWindow");

    private String style;

    PanelStyle(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

}
