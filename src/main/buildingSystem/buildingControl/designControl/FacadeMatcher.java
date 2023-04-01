package buildingControl.designControl;

import facade.basic.BasicObject;
import facade.basic.Material;
import facade.unit.sjStyles.S_Corner_Component_Lib;
import facade.unit.styles.*;
import function.Function;
import function.PosType;
import unit2Vol.Beam;
import unit2Vol.Building;
import unit2Vol.Unit;
import unit2Vol.face.Face;
import unit2Vol.panelBase.PanelBase;
import unit2Vol.panelBase.SimplePanelBase;
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

    private List<BasicObject> outPanels;

    private List<BasicObject> innerPanels;

    private List<BasicObject> roofPanels;

    private List<BasicObject> floorPanels;

    private BuildingCreator bc;

    public FacadeMatcher(BuildingCreator bc) {
        this.bc = bc;

        init();
    }

    private void init() {

        initList();
        initPanels();

//        initBeams();
//        initColumn();

        addAllPanels();
    }

    private void initList() {
        panels = new LinkedList<>();
        outPanels = new LinkedList<>();
        innerPanels = new LinkedList<>();
        roofPanels = new LinkedList<>();
        floorPanels = new LinkedList<>();
    }

    private void addAllPanels() {
        panels.addAll(outPanels);
        panels.addAll(innerPanels);
        panels.addAll(roofPanels);
        panels.addAll(floorPanels);
    }

    private void initPanels() {
        Map<Function, List<PanelBase>> funcBaseMap = bc.getFuncBaseMap();

        Set<Map.Entry<Function, List<PanelBase>>> entries = funcBaseMap.entrySet();

        for (var en : entries) {
            Function func = en.getKey();
            List<PanelBase> bases = en.getValue();

            initPanelByBaseFunc(bases, func);
//            initOutSimple(bases, func);
//            initTest(bases, func);

            /**
             * 加底 有bug
             */
//            addBottom();
        }
    }

    private void initPanelByBaseFunc(List<PanelBase> bases, Function function) {
        switch (function) {
            case ClassRoom:
                replaceSimpleByWidth(bases, 4200);
                try {
                    bases.forEach(e -> outPanels.add(new F_TwoWindow(e.getShape())));
//                    bases.forEach(e -> panels.add(new S_ExtrudeIn(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Classroom wrong");
                }
                break;
            case Transport:
//                replaceSimpleByWidth(bases, 3000);
//                replaceHandRailByWidth(bases, 3000);
                try {
//                    bases.forEach(e -> panels.add(new S_Corner_Component_Lib(e.getShape())));
                    F_OneWindow.extended_distance = 300;
                    bases.forEach(e -> outPanels.add(new F_OneWindow(e.getShape())));
//                    bases.forEach(e -> panels.add(new Handrail(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Transport wrong");
                }
                break;
            case Stair:
                try {
                    bases.forEach(e -> outPanels.add(new F_Example(e.getShape())));
//                    bases.forEach(e -> panels.add(new F_WindowArray(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Stair wrong");
                }
                break;
            case Open:
                replaceSimpleByWidth(bases, 8500);
                try {
                    F_OneHole.material = Material.DarkGray;
                    bases.forEach(e -> outPanels.add(new F_OneHole(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Open wrong");
                }
                break;
            case Handrail:
                try {
                    bases.forEach(e -> outPanels.add(new Handrail(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Handrail wrong");
                }
                break;
            case Roof:
                try {
                    bases.forEach(e -> roofPanels.add(new RoofSimple(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Roof wrong");
                }
                break;
            case InnerWall:
                try {
                    bases.forEach(e -> innerPanels.add(new SimplePanel(e.getShape(), 50)));
                } catch (Exception ignored) {
                    System.out.println("InnerWall wrong");
                }
                break;
            case Floor:
                try {
                    bases.forEach(e -> floorPanels.add(new SimplePanel(e.getShape(), 100)));
                } catch (Exception ignored) {
                    System.out.println("Floor wrong");
                }
                break;
        }
    }

    private void initOutSimple(List<PanelBase> bases, Function function) {
        switch (function) {
            case ClassRoom:
                replaceSimpleByWidth(bases, 4200);
                bases.forEach(e -> panels.add(new SimplePanel(e.getShape())));
                break;
            case Transport:
                replaceSimpleByWidth(bases, 4000);
                bases.forEach(e -> panels.add(new SimplePanel(e.getShape())));
                break;
            case Stair:
                bases.forEach(e -> panels.add(new SimplePanel(e.getShape())));
                break;
            case Open:
                replaceSimpleByWidth(bases, 8500);
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

    private void initTest(List<PanelBase> bases, Function function) {
        switch (function) {
            case Stair:
                PanelBase panelBase = bases.get(0);
                panels.add(new S_Corner_Component_Lib(panelBase.getShape()));
                break;
        }
    }


    private void replaceSimpleByWidth(List<PanelBase> bases, double widthThreshold) {
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

    private void replaceHandRailByWidth(List<PanelBase> bases, double widthThreshold) {
        List<PanelBase> recorder = new LinkedList<>();

        for (PanelBase base : bases) {
            double width = base.getWidthLength();
            if (width < widthThreshold) {
                panels.add(new Handrail(base.getShape()));
                recorder.add(base);
            }
        }

        bases.removeAll(recorder);
    }

    public List<BasicObject> getPanels() {
        return panels;
    }

    /**
     * 为整个建筑加底
     * 权宜之计
     */
    private void addBottom() {
        Building building = bc.getBuilding();
        Map<Double, List<Unit>> eachFloorUnits = building.getEachFloorUnits();
        Set<Map.Entry<Double, List<Unit>>> entries = eachFloorUnits.entrySet();

        for (var en : entries) {
            List<Unit> units = en.getValue();

            List<Face> bottoms = new LinkedList<>();
            units.forEach(e -> bottoms.add(e.getBottomFace()));

            List<PanelBase> bases = new LinkedList<>();
            bottoms.forEach(e -> bases.add(new SimplePanelBase(e)));

            bases.forEach(e -> panels.add(new SimplePanel(e.getShape(), 200)));
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

            pts.forEach(e -> panels.add(new ColumnSimple(e, 4000, 400)));
        }
    }

    public BuildingCreator getBc() {
        return bc;
    }

    public List<BasicObject> getOutPanels() {
        return outPanels;
    }

    public List<BasicObject> getInnerPanels() {
        return innerPanels;
    }

    public List<BasicObject> getRoofPanels() {
        return roofPanels;
    }

    public List<BasicObject> getFloorPanels() {
        return floorPanels;
    }
}
