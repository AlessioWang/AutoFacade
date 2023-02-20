package Facade.facade.divid;

import wblut.geom.WB_Polygon;
import wblut.geom.WB_Quad;

import java.util.List;

public abstract class D_Divide {
    public abstract List<WB_Quad>divideToQuad(List<WB_Polygon>polys);
}
