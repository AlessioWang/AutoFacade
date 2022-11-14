package facadeGen;

import facadeGen.Panel.Panel;
import facadeGen.Panel.PanelBase.BasicBase;
import facadeGen.Panel.PanelStyle.StyleA;
import facadeGen.Panel.PanelStyle.StyleB;
import facadeGen.Panel.PanelStyle.StyleC;
import Tools.GeoTools;

/**
 * @auther Alessio
 * @date 2022/4/22
 **/
public class PanelFac {
    public Panel panelA;
    public Panel panelB;
    public Panel panelC;
    public double width;
    public double height;
    public BasicBase base;

    public PanelFac() {
    }

    public PanelFac(double width, double height) {
        this.width = width;
        this.height = height;
        base = new BasicBase(GeoTools.createRecPolygon(width, height));
        panelA = new StyleA(base);
        panelB = new StyleB(base);
        panelC = new StyleC(base);
    }

    public void init(){
        base = new BasicBase(GeoTools.createRecPolygon(width, height));
        panelA = new StyleA(base);
        panelB = new StyleB(base);
        panelC = new StyleC(base);
    }

    public Panel getPanelA() {
        return panelA;
    }

    public void setPanelA(Panel panelA) {
        this.panelA = panelA;
    }

    public Panel getPanelB() {
        return panelB;
    }

    public void setPanelB(Panel panelB) {
        this.panelB = panelB;
    }

    public Panel getPanelC() {
        return panelC;
    }

    public void setPanelC(Panel panelC) {
        this.panelC = panelC;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
