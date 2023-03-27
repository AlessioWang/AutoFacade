package buildingControl;

import facade.basic.BasicObject;
import facade.unit.sjStyles.S_Corner_Component_Lib;
import facade.unit.styles.*;
import function.Function;
import function.PosType;
import unit2Vol.Beam;
import unit2Vol.Building;
import unit2Vol.panelBase.PanelBase;
import wblut.geom.WB_Point;

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

    private void init() {
        panels = new LinkedList<>();

        initPanels();

//        initBeams();
//        initColumn();
    }

    private void initPanels() {
        Map<Function, List<PanelBase>> funcBaseMap = bc.getFuncBaseMap();

        Set<Map.Entry<Function, List<PanelBase>>> entries = funcBaseMap.entrySet();

        for (var en : entries) {
            Function func = en.getKey();
            List<PanelBase> bases = en.getValue();

            initPanelByBaseFunc(bases, func);
//            initOutSimple(bases, func);
        }
    }

    private void initBeams() {
        Building building = bc.getBuilding();
        Map<Double, List<Beam>> beamMap = building.getBeamMap();

        Set<Map.Entry<Double, List<Beam>>> entries = beamMap.entrySet();
        for (var entry : entries) {
            List<Beam> beams = entry.getValue();
            for (Beam b : beams) {
                if (b.getPosType() == PosType.Center) {
                    panels.add(new RecBeam(b.getSegment(), RecBeam.BeamType.Center));
                } else
                    panels.add(new RecBeam(b.getSegment(), RecBeam.BeamType.Side));
            }
        }
    }

    private void initColumn() {
        Building building = bc.getBuilding();
        Map<Double, List<WB_Point>> columnBaseMap = building.getColumnBaseMap();

        Set<Map.Entry<Double, List<WB_Point>>> entries = columnBaseMap.entrySet();
        for (var entry : entries) {
            List<WB_Point> pts = entry.getValue();

            pts.forEach(e->panels.add(new ColumnSimple(e, 4000, 500)) );
        }
    }

    private void initPanelByBaseFunc(List<PanelBase> bases, Function function) {
        switch (function) {
            case ClassRoom:
                setPanelStyleByLength(bases, 4200);
                bases.forEach(e -> panels.add(new F_TwoWindow(e.getShape())));
                break;
            case Transport:
                setPanelStyleByLength(bases, 4000);
                bases.forEach(e -> panels.add(new S_Corner_Component_Lib(e.getShape())));
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
                setPanelStyleByLength(bases, 8500);
                bases.forEach(e -> panels.add(new F_OneHole(e.getShape())));
                break;
        }
    }

    private void initOutSimple(List<PanelBase> bases, Function function) {
        switch (function) {
            case ClassRoom:
                setPanelStyleByLength(bases, 4200);
                bases.forEach(e -> panels.add(new SimplePanel(e.getShape())));
                break;
            case Transport:
                setPanelStyleByLength(bases, 4000);
                bases.forEach(e -> panels.add(new SimplePanel(e.getShape())));
                break;
            case Stair:
                bases.forEach(e -> panels.add(new SimplePanel(e.getShape())));
                break;
            case Open:
                setPanelStyleByLength(bases, 8500);
                bases.forEach(e -> panels.add(new SimplePanel(e.getShape())));
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

    public List<BasicObject> getPanels() {
        return panels;
    }
}
