package Facade.facade.unit;


import wblut.geom.WB_Point;
import wblut.geom.WB_Quad;
import wblut.hemesh.HEC_FromQuads;
import wblut.hemesh.HE_Mesh;

import Facade.facade.basic.BasicObject;
import Facade.facade.basic.Material;
import Facade.facade.basic.StyledMesh;
import Facade.facade.unit.styles.F_Example;
import Facade.facade.unit.styles.F_WindowArray;
import Facade.facade.unit.styles.S_ExtrudeIn;
import java.util.Arrays;


public class FacadeUnit {
    WB_Point[] rectPts;
    String userId;
    String uuid;
    StyledMesh base;
    UnitStyles style;
    BasicObject obj;

    public FacadeUnit(WB_Point[] rectPts) {
        this.rectPts = rectPts;
//        userId = "" + System.currentTimeMillis();
        userId = ""+this.hashCode();

        System.out.println("userId: "+userId);
        HE_Mesh mesh = new HEC_FromQuads(new WB_Quad[]{new WB_Quad(rectPts[0], rectPts[1], rectPts[2], rectPts[3])}).create();
        base = new StyledMesh(Material.White);
        base.add(mesh);
    }


    public String getUserId() {
        return userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public StyledMesh getBase() {
        return base;
    }

    public void initObj(UnitStyles style) {
        switch (style){
            case WIN:
                this.obj = new F_WindowArray(rectPts);
                break;
            case LONG:
                this.obj = new F_Example(rectPts);
                break;
            case EXTRUDE:
                this.obj = new S_ExtrudeIn(rectPts);
                break;
        }
    }

    public BasicObject getObj() {
        return obj;
    }

    public WB_Point[] getRectPts() {
        return rectPts;
    }

    @Override
    public String toString() {
        return "FacadeUnit{" +
                "rectPts=" + Arrays.toString(rectPts) +
                ", userId='" + userId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", base=" + base +
                '}';
    }


}
