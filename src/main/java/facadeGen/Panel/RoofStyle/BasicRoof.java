package facadeGen.Panel.RoofStyle;

import Tools.GeoTools;
import facadeGen.Panel.PanelBase.Base;
import facadeGen.Panel.PanelStyle.Panel;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/12/30
 **/
public class BasicRoof extends Panel {

    public Base base;

    // the default height of parapet wall
    private double wallHeight = 900;

    public BasicRoof(Base base) {
        this.base = base;
        styleSetting();
    }

    @Override
    public void styleSetting() {
        checkBaseDir();
        initParapetWall();
    }

    /**
     * 保证base的polygon的点序方向
     */
    private void checkBaseDir() {
        WB_Polygon basicShape = base.basicShape;

        List<WB_Segment> segments = basicShape.toSegments();
        WB_Vector v0 = segments.get(0).getNormal();
        WB_Vector v1 = segments.get(1).getNormal();

        WB_Vector dir = v0.cross(v1);
        if (dir.zd() > 0) {
            base.setBasicShape(GeoTools.reversePolygon(basicShape));
        }
    }

    /**
     * 创建一圈女儿墙
     */
    private void initParapetWall() {
        List<WB_Polygon> polygons = new LinkedList<>();
        List<WB_Segment> segments = base.basicShape.toSegments();

        for (WB_Segment seg : segments) {
            WB_Polygon shape = GeoTools.getRecBySegAndWidth(seg, wallHeight, new WB_Vector(0, 0, 1));
            polygons.add(shape);
        }

        super.setParapetWalls(polygons);
    }

    public double getWallHeight() {
        return wallHeight;
    }

    public void setWallHeight(double wallHeight) {
        this.wallHeight = wallHeight;
    }

    @Override
    public Base getBase() {
        return base;
    }
}
