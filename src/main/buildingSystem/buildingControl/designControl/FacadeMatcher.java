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

    private List<BasicObject> beams;

    private List<BasicObject> columns;

    private BuildingCreator bc;

    public FacadeMatcher(BuildingCreator bc) {
        this.bc = bc;

        init();
    }

    private void init() {

        initList();
        initPanels();

        initBeams();
        initColumn();

        addAllPanels();
    }

    private void initList() {
        panels = new LinkedList<>();
        outPanels = new LinkedList<>();
        innerPanels = new LinkedList<>();
        roofPanels = new LinkedList<>();
        floorPanels = new LinkedList<>();
        beams = new LinkedList<>();
        columns = new LinkedList<>();
    }

    private void addAllPanels() {
        panels.addAll(outPanels);
        panels.addAll(innerPanels);
        panels.addAll(roofPanels);
        panels.addAll(floorPanels);
//        panels.addAll(beams);
//        panels.addAll(columns);
    }

    private void initPanels() {
        Map<Function, List<PanelBase>> funcBaseMap = bc.getFuncBaseMap();

        Set<Map.Entry<Function, List<PanelBase>>> entries = funcBaseMap.entrySet();

        for (var en : entries) {
            Function func = en.getKey();
            List<PanelBase> bases = en.getValue();

            initInnerStyle(bases, func);

//            initColorStyle(bases, func);
            initGrayStyle(bases, func);

//            initOutSimple(bases, func);
//            initTest(bases, func);

            /**
             * 加底 有bug
             */
//            addBottom();
        }
    }

    private void initInnerStyle(List<PanelBase> bases, Function function) {
        switch (function) {
            case Handrail:
                try {
                    bases.forEach(e -> outPanels.add(new Handrail(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Handrail wrong");
                }
                break;
            case Roof:
                System.out.println("Roof : " + bases.size());
                try {
                    bases.forEach(e -> roofPanels.add(new RoofSimple(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Roof wrong");
                }
                break;
            case InnerWall:
                System.out.println("innerwall : " + bases.size());
                try {
                    bases.forEach(e -> innerPanels.add(new SimplePanel(e.getShape(), 50)));
                } catch (Exception ignored) {
                    System.out.println("InnerWall wrong");
                }
                break;
            case Floor:
                System.out.println("floor : " + bases.size());
                try {
                    bases.forEach(e -> floorPanels.add(new SimplePanel(e.getShape(), 100)));
                } catch (Exception ignored) {
                    System.out.println("Floor wrong");
                }
                break;
        }
    }

    private void initColorStyle(List<PanelBase> bases, Function function) {
        switch (function) {
            case ClassRoom:
                replaceSimpleByWidth(bases, 4200);
                System.out.println("class : " + bases.size());
                try {
                    bases.forEach(e -> outPanels.add(new F_TwoWindow(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Classroom wrong");
                }
                break;
            case Transport:
                replaceSimpleByWidth(bases, 4000);
//                replaceHandRailByWidth(bases, 3000);
                System.out.println("Transport : " + bases.size());
                try {
                    F_OneWindow.frameMaterial = Material.DarkGray;
                    bases.forEach(e -> outPanels.add(new F_OneWindow(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Transport wrong");
                }
                break;
            case Stair:
                System.out.println("Stair : " + bases.size());
                try {
                    bases.forEach(e -> outPanels.add(new F_Example(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Stair wrong");
                }
                break;
            case Open:
                replaceSimpleByWidth(bases, 8500);
                System.out.println("Open : " + bases.size());
                try {
                    F_OneHole.material = Material.Red;
                    bases.forEach(e -> outPanels.add(new F_OneHole(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Open wrong");
                }
                break;
            case Office:
                try {
                    bases.forEach(e -> outPanels.add(new F_TwoWindow(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Office wrong");
                }
                break;
            case Toilet:
                try {
                    F_OneWindow.bottom_height = 1800;
                    F_OneWindow.extended_distance = 300;
                    F_OneWindow.frameMaterial = Material.Blue;
                    bases.forEach(e -> outPanels.add(new F_OneWindow(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Toilet wrong");
                }
                break;
        }
    }

    private void initGrayStyle(List<PanelBase> bases, Function function) {
        switch (function) {
            case ClassRoom:
                replaceSimpleByWidth(bases, 4200);
                try {
                    S_ExtrudeIn.divideNum = 2;
                    S_ExtrudeIn.top_height = 300;
                    S_ExtrudeIn.bottom_height = 600;
                    S_ExtrudeIn.seed = 4;
                    bases.forEach(e -> outPanels.add(new S_ExtrudeIn(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Classroom wrong");
                }
                break;
            case Transport:
                replaceHandRailByWidth(bases, 3000);
                try {
                    bases.forEach(e -> outPanels.add(new Handrail(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Transport wrong");
                }
                break;
            case Stair:
                System.out.println("Stair : " + bases.size());
                try {
                    bases.forEach(e -> outPanels.add(new F_WindowArray(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Stair wrong");
                }
                break;
            case Open:
                replaceSimpleByWidth(bases, 8500);
                System.out.println("Open : " + bases.size());
                try {
                    F_OneHole.material = Material.DarkGray;
                    bases.forEach(e -> outPanels.add(new F_OneHole(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Open wrong");
                }
                break;
            case Office:
                try {
                    S_ExtrudeIn.top_height = 300;
                    S_ExtrudeIn.bottom_height = 600;
                    S_ExtrudeIn.divideNum = 2;
                    S_ExtrudeIn.seed = 2;
                    bases.forEach(e -> outPanels.add(new S_ExtrudeIn(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Office wrong");
                }
                break;
            case Toilet:
                try {
                    S_ExtrudeIn.top_height = 300;
                    S_ExtrudeIn.bottom_height = 1500;
                    S_ExtrudeIn.divideNum = 0;
                    S_ExtrudeIn.seed = 0;
                    bases.forEach(e -> outPanels.add(new S_ExtrudeIn(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Toilet wrong");
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
                replaceSimpleByWidth(bases, 5000);
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
            units.stream().filter(e -> e.getFunction().equals(Function.Transport)).forEach(e -> bottoms.add(e.getBottomFace()));

            List<PanelBase> bases = new LinkedList<>();
            bottoms.forEach(e -> bases.add(new SimplePanelBase(e)));

            bases.forEach(e -> floorPanels.add(new SimplePanel(e.getShape(), 200)));
//            bases.forEach(e -> panels.add(new SimplePanel(e.getShape(), 200)));
        }
    }

    private void initBeams() {
        Building building = bc.getBuilding();
        Map<Double, List<Beam>> beamMap = building.getBeamMap();

        Set<Map.Entry<Double, List<Beam>>> entries = beamMap.entrySet();
        for (var entry : entries) {
            List<Beam> bs = entry.getValue();
            for (Beam b : bs) {
                if (b.getPosType() == PosType.Center) {
                    beams.add(new RecBeam(b.getSegment(), RecBeam.BeamType.Center));
                } else
                    beams.add(new RecBeam(b.getSegment(), RecBeam.BeamType.Side));
            }
        }
    }

    private void initColumn() {
        Building building = bc.getBuilding();
        Map<Double, List<WB_Point>> columnBaseMap = building.getColumnBaseMap();

        Set<Map.Entry<Double, List<WB_Point>>> entries = columnBaseMap.entrySet();
        for (var entry : entries) {
            List<WB_Point> pts = entry.getValue();
            pts.forEach(e -> columns.add(new ColumnSimple(e, 4000, 500)));
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

    public List<BasicObject> getBeams() {
        return beams;
    }

    public List<BasicObject> getColumns() {
        return columns;
    }
}
