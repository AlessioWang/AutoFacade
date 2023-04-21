package buildingControl.designControl;

/**
 * @auther Alessio
 * @date 2023/4/21
 **/
public enum PanelStyle {
    SimplePanel("SimplePanel"),

    F_Example("F_Example"),

    F_OneHole("F_OneHole"),

    F_TwoWindow("F_TwoWindow"),

    F_OneWindow("F_OneWindow"),

    F_WindowArray("F_WindowArray"),

    Handrail("Handrail"),

    S_ExtrudeIn("S_ExtrudeIn"),

    RoofSimple("RoofSimple");

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
