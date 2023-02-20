package Facade.manager;


import Facade.facade.basic.BasicObject;
import Facade.facade.unit.FacadeUnit;
import wblut.geom.WB_Point;

import java.util.ArrayList;
import java.util.List;

public class Facade extends BasicObject {
    List<FacadeUnit> units;

    public Facade(){
        super();
        units = new ArrayList<>();
        calculate();
//        System.out.println("facade: "+getGeometries().size());
    }

    @Override
    protected void initPara() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void calculate() {
        WB_Point[]pts = new WB_Point[]{
                new WB_Point(0, 0, 0),
                new WB_Point(0, 8000, 0),
                new WB_Point(0, 8000, 3000),
                new WB_Point(0, 0, 3000)
        };
        WB_Point[] pts2 = new WB_Point[]{
                new WB_Point(0, 8000, 0),
                new WB_Point(0, 16000, 0),
                new WB_Point(0, 16000, 3000),
                new WB_Point(0, 8000, 3000),
        };
//         this.obj = new F_Example(pts);
        FacadeUnit f1 = new FacadeUnit(pts);
        FacadeUnit f2 = new FacadeUnit(pts2);
        this.units.add(f1);
        this.units.add(f2);
    }


}
