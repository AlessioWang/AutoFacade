package buildingControl;

import facade.basic.BasicObject;
import facade.unit.styles.*;
import function.Function;
import unit2Vol.panelBase.PanelBase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 根据BuildingCreator的信息，与面板样式匹配
 *
 * @auther Alessio
 * @date 2023/3/24
 **/
public class FacadeMatcher {

    private List<BasicObject> panels;

    private BuildingCreator bc;

    public FacadeMatcher(BuildingCreator bc) {
        this.bc = bc;

        init();
    }

    public List<BasicObject> getPanels() {
        return panels;
    }

    private void init() {
        panels = new LinkedList<>();

        initPanels();
    }

    private void initPanels() {
        Map<Function, List<PanelBase>> funcBaseMap = bc.getFuncBaseMap();

        Set<Map.Entry<Function, List<PanelBase>>> entries = funcBaseMap.entrySet();

        for (var en : entries) {
            Function func = en.getKey();
            List<PanelBase> bases = en.getValue();

            initPanelByBaseFunc(bases, func);
        }
    }

    private void initPanelByBaseFunc(List<PanelBase> bases, Function function) {

        switch (function) {
            case ClassRoom:
                setPanelStyleByLength(bases, 4000);
//                bases.forEach(e -> panels.add(new S_Corner_Component_Lib(e.getShape())));
                bases.forEach(e -> panels.add(new F_TwoWindow(e.getShape())));
                break;
            case Transport:
                bases.forEach(e -> panels.add(new F_WindowArray(e.getShape())));
                break;
            case Stair:
                bases.forEach(e -> panels.add(new F_Example(e.getShape())));
                break;
            case Roof:
                bases.forEach(e -> panels.add(new RoofSimple(e.getShape())));
                break;
            case InnerWall:
                bases.forEach(e -> panels.add(new SimplePanel(e.getShape(), 50)));
                break;
            case Floor:
                bases.forEach(e -> panels.add(new SimplePanel(e.getShape(), 200)));
                break;
            case Open:
                bases.forEach(e -> panels.add(new F_OneHole(e.getShape())));
                break;
        }
    }

    private void setPanelStyleByLength(List<PanelBase> bases, double widthThreshold) {
        List<PanelBase> recorder = new LinkedList<>();

        for (PanelBase base : bases) {
            double width = base.getWidthLength();
            if (width < widthThreshold) {
                panels.add(new SimplePanel(base.getShape(), 200));
                recorder.add(base);
            }
        }

        bases.removeAll(recorder);
    }

}
