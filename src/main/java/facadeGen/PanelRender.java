package facadeGen;

import facadeGen.Panel.PanelStyle.Panel;
import processing.core.PApplet;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;

import java.util.LinkedList;
import java.util.List;


/**
 * @auther Alessio
 * @date 2022/3/16
 **/
public class PanelRender {

    public PApplet app;
    public List<Panel> panels;


    List<WB_Polygon> base;
    List<WB_Polygon> frames;
    List<WB_PolyLine> beams;
    List<WB_Polygon> glass;

    public PanelRender(PApplet app, List<Panel> panels) {
        this.app = app;
        this.panels = panels;

        base = new LinkedList<>();
        frames = new LinkedList<>();
        beams = new LinkedList<>();
        glass = new LinkedList<>();
    }


}
