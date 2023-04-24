package buildingControl.designControl;

import facade.basic.BasicObject;
import facade.basic.Material;
import facade.unit.styles.*;
import function.Function;
import function.PosType;
import unit2Vol.Beam;
import unit2Vol.Building;
import unit2Vol.Unit;
import unit2Vol.panelBase.PanelBase;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.*;

/**
 * @auther Alessio
 * @date 2023/4/19
 **/
public class F_Matcher {
    private Map<WB_Polygon, BasicObject> panelMap;

    private Map<WB_Polygon, BasicObject> outMap;

    private Map<WB_Polygon, BasicObject> innerMap;

    private Map<WB_Polygon, BasicObject> roofMap;

    private Map<WB_Polygon, BasicObject> floorMap;

    private List<BasicObject> structuresList;

    private List<BasicObject> beamsList;

    private List<BasicObject> columnList;

    private BuildingCreator bc;

    public F_Matcher(BuildingCreator bc) {
        this.bc = bc;

        init();
    }

    private void init() {
        initVar();

        initStructure();

        initPanels();

        mergeMap(panelMap, outMap, innerMap, roofMap, floorMap);
    }

    private void initVar() {
        panelMap = new HashMap<>();

        outMap = new HashMap<>();

        innerMap = new HashMap<>();

        roofMap = new HashMap<>();

        floorMap = new HashMap<>();

        structuresList = new LinkedList<>();

        beamsList = new LinkedList<>();

        columnList = new LinkedList<>();
    }

    @SafeVarargs
    private void mergeMap(Map target, Map... maps) {
        for (var m : maps) {
            try {
                target.putAll(m);
            } catch (Exception e) {
                System.out.println("ATTENTION: The elements in maps are not in same class with the target map");
                throw new RuntimeException(e);
            }
        }
    }


    private void initPanels() {
        Map<Function, List<PanelBase>> funcBaseMap = bc.getFuncBaseMap();

        Set<Map.Entry<Function, List<PanelBase>>> entries = funcBaseMap.entrySet();

        for (var en : entries) {
            Function func = en.getKey();
            List<PanelBase> bases = en.getValue();

            initOutPanel(bases, func);
            initInnerPanel(bases, func);
            initFloorPanel();
        }
    }

    /**
     * 以每个unit的底为基础创建floor
     * 权宜之计
     */
    private void initFloorPanel() {
        Building building = bc.getBuilding();

        List<Unit> unitList = building.getUnitList();

        for (Unit u : unitList) {
            WB_Polygon base = u.getRealBase();
            floorMap.put(base, new SimplePanel(base, 200));
        }
    }

    private void initInnerPanel(List<PanelBase> bases, Function function) {
        if (function == Function.InnerWall) {
            try {
                bases.forEach(e -> innerMap.put(e.getShape(), new SimplePanel(e.getShape(), 50)));
            } catch (Exception ignored) {
                System.out.println("InnerWall wrong");
            }
        }
    }

    private void initOutPanel(List<PanelBase> bases, Function function) {
        switch (function) {
            case ClassRoom:
                try {
                    bases.forEach(e -> outMap.put(e.getShape(), new F_TwoWindow(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Classroom wrong");
                }
                break;
            case Transport:
                try {
                    F_OneWindow.frameMaterial = Material.DarkGray;
                    bases.forEach(e -> outMap.put(e.getShape(), new F_OneWindow(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Transport wrong");
                }
                break;
            case Stair:
                try {
                    bases.forEach(e -> outMap.put(e.getShape(), new F_Example(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Stair wrong");
                }
                break;
            case Open:
                try {
                    F_OneHole.material = Material.Red;
                    bases.forEach(e -> outMap.put(e.getShape(), new F_OneHole(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Open wrong");
                }
                break;
            case Office:
                try {
                    bases.forEach(e -> outMap.put(e.getShape(), new F_TwoWindow(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Office wrong");
                }
                break;
            case Toilet:
                try {
                    F_OneWindow.bottom_height = 1800;
                    F_OneWindow.extended_distance = 300;
                    F_OneWindow.frameMaterial = Material.Blue;
                    bases.forEach(e -> outMap.put(e.getShape(), new F_OneWindow(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Toilet wrong");
                }
                break;
            case Roof:
                try {
                    bases.forEach(e -> roofMap.put(e.getShape(), new RoofSimple(e.getShape())));
                } catch (Exception ignored) {
                    System.out.println("Roof wrong");
                }
                break;
        }
    }

    private void initStructure() {
        initBeams();
        initColumn();

        structuresList.addAll(beamsList);
        structuresList.addAll(columnList);
    }

    private void initBeams() {
        Building building = bc.getBuilding();
        Map<Double, List<Beam>> beamMap = building.getBeamMap();

        Set<Map.Entry<Double, List<Beam>>> entries = beamMap.entrySet();
        for (var entry : entries) {
            List<Beam> bs = entry.getValue();
            for (Beam b : bs) {
                if (b.getPosType() == PosType.Center) {
                    beamsList.add(new RecBeam(b.getSegment(), RecBeam.BeamType.Center));
                } else
                    beamsList.add(new RecBeam(b.getSegment(), RecBeam.BeamType.Side));
            }
        }
    }

    private void initColumn() {
        Building building = bc.getBuilding();
        Map<Double, List<WB_Point>> columnBaseMap = building.getColumnBaseMap();

        Set<Map.Entry<Double, List<WB_Point>>> entries = columnBaseMap.entrySet();
        for (var entry : entries) {
            List<WB_Point> pts = entry.getValue();
            pts.forEach(e -> columnList.add(new ColumnSimple(e, 4000, 300)));
        }
    }

    public Map<WB_Polygon, BasicObject> getPanelMap() {
        return panelMap;
    }

    public List<BasicObject> getStructuresList() {
        return structuresList;
    }

    public Map<WB_Polygon, BasicObject> getOutMap() {
        return outMap;
    }

    public Map<WB_Polygon, BasicObject> getInnerMap() {
        return innerMap;
    }

    public Map<WB_Polygon, BasicObject> getRoofMap() {
        return roofMap;
    }

    public Map<WB_Polygon, BasicObject> getFloorMap() {
        return floorMap;
    }

    public List<BasicObject> getBeamsList() {
        return beamsList;
    }

    public List<BasicObject> getColumnList() {
        return columnList;
    }


}
