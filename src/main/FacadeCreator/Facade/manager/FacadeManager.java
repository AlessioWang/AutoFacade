package Facade.manager;


import Facade.facade.unit.FacadeUnit;
import wblut.geom.WB_Polygon;

import java.util.ArrayList;
import java.util.List;

public class FacadeManager {
    List<FacadeUnit> unitsTemp;
    List<Integer> selectedUnitsTemp;


    public FacadeManager(){
        clearUnits();
        clearSelect();
    }

    public void clearUnits(){
        unitsTemp = new ArrayList<>();
    }

    public void clearSelect(){
        selectedUnitsTemp = new ArrayList<>();
    }

    public void createUnits(List<WB_Polygon>polys){


    }
}
