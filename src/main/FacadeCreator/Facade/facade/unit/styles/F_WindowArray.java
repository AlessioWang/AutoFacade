package Facade.facade.unit.styles;

import Facade.facade.basic.BasicObject;
import Tools.GeoTools;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Quad;
import wblut.geom.WB_Vector;
import wblut.hemesh.HEC_Box;
import wblut.hemesh.HEC_FromQuads;
import wblut.hemesh.HEM_Lattice;
import wblut.hemesh.HE_Mesh;

import Facade.facade.basic.Material;
import Facade.facade.basic.StyledMesh;

public class F_WindowArray extends BasicObject {

    WB_Point[] rectPts;

    public F_WindowArray(WB_Point[] rectPts) {
        this.rectPts = rectPts;
        initPara();
//        initData();
        calculate();
    }

    public F_WindowArray(WB_Polygon polygon) {
        WB_Point[] pts = GeoTools.polygon2Pts(polygon);

        this.rectPts = pts;
        initPara();
        calculate();
    }

    /**
     * ------------- parameters ------------
     */
    double collum_width, win_width, win_height, bottom_height, col_thickness, mid_thickness;
    double minTopHeight = 200, frameWidth = 50, glassThickness = 20;

    @Override
    protected void initPara() {
        collum_width = putPara(200, 200, 1200, "collum_width").getValue();
        win_width = putPara(800, 400, 1200, "win_width").getValue();
        win_height = putPara(1500, 800, 2000, "win_height").getValue();
        bottom_height = putPara(1000, 100, 1200, "bottom_height").getValue();
        col_thickness = putPara(300, 100, 300, "col_thickness").getValue();
        mid_thickness = putPara(200, 100, 300, "mid_thickness").getValue();
    }

    /**
     * ------------- data for display ------------
     */
    String winData;
    double paintArea;
    double glassArea;

    @Override
    protected void initData() {
        putData("winData", "");
        putData("paintArea", "");
        putData("glassArea", "");
    }

    @Override
    protected void calculate() {
        WB_Vector v1 = rectPts[1].subToVector3D(rectPts[0]);
        WB_Vector v2 = rectPts[3].subToVector3D(rectPts[0]);

        double height = v2.getLength();
        double width = v1.getLength();

        int winNum = (int) ((width - collum_width) / (win_width + collum_width));
        double endWidth = (width - winNum * win_width - (winNum - 1) * collum_width) / 2;
        v1.normalizeSelf();
        v2.normalizeSelf();
        WB_Vector n = v1.cross(v2);
        n.normalizeSelf();

        StyledMesh midMesh = new StyledMesh(Material.LightGray);
        StyledMesh collumMesh = new StyledMesh(Material.MidGray);
        StyledMesh glassMesh = new StyledMesh(Material.Glass);
        StyledMesh frame = new StyledMesh(Material.DarkGray);

        WB_Point p1 = rectPts[0];
        WB_Point p2 = rectPts[3].add(v1.mul(endWidth)).add(n.mul(col_thickness));
        HE_Mesh mesh = new HEC_Box().setFromCorners(p1, p2).create();
        collumMesh.add(mesh);
        for (int i = 0; i < winNum; i++) {
            p1 = rectPts[0].add(v1.mul(i * (win_width + collum_width) + endWidth));
            p2 = rectPts[0].add(v1.mul(i * (win_width + collum_width) + endWidth + win_width)).add(v2.mul(bottom_height)).add(n.mul(mid_thickness));
            mesh = new HEC_Box().setFromCorners(p1, p2).create();
            midMesh.add(mesh);

            double topHeight = height - bottom_height - win_height;
            if (topHeight < minTopHeight) {
                topHeight = minTopHeight;
                win_height = height - topHeight - bottom_height;
                getPara("win_height").setValueString("" + win_height);
            }

            p1 = rectPts[3].add(v1.mul(i * (win_width + collum_width) + endWidth).sub(v2.mul(topHeight)));
            p2 = rectPts[3].add(v1.mul(i * (win_width + collum_width) + endWidth + win_width)).add(n.mul(mid_thickness));
            mesh = new HEC_Box().setFromCorners(p1, p2).create();
            midMesh.add(mesh);

            mesh = new HEC_FromQuads(
                    new WB_Quad[]{
                            new WB_Quad(
                                    rectPts[0].add(v1.mul(i * (win_width + collum_width) + endWidth)).add(v2.mul(bottom_height)),
                                    rectPts[0].add(v1.mul(i * (win_width + collum_width) + endWidth + win_width)).add(v2.mul(bottom_height)),
                                    rectPts[0].add(v1.mul(i * (win_width + collum_width) + endWidth + win_width)).add(v2.mul(bottom_height + win_height)),
                                    rectPts[0].add(v1.mul(i * (win_width + collum_width) + endWidth)).add(v2.mul(bottom_height + win_height))
                            )
                    }
            ).create();
            mesh = new HEM_Lattice().setWidth(frameWidth).setDepth(-frameWidth).apply(mesh);
            frame.add(mesh);

            p1 = rectPts[0].add(v1.mul(i * (win_width + collum_width) + endWidth + frameWidth)).add(v2.mul(bottom_height + frameWidth));
            p2 = rectPts[3].add(v1.mul(i * (win_width + collum_width) + endWidth + win_width - frameWidth)).sub(v2.mul(frameWidth + topHeight)).add(n.mul(glassThickness));
            mesh = new HEC_Box().setFromCorners(p1, p2).create();
            glassMesh.add(mesh);


            if (i < winNum - 1) {
                p1 = rectPts[0].add(v1.mul(i * (win_width + collum_width) + endWidth + win_width));
                p2 = p1.add(v1.mul(collum_width).add(v2.mul(height))).add(n.mul(col_thickness));
                mesh = new HEC_Box().setFromCorners(p1, p2).create();
                collumMesh.add(mesh);
            }
        }
        p1 = rectPts[1].sub(v1.mul(endWidth));
        p2 = rectPts[2].add(n.mul(col_thickness));
        mesh = new HEC_Box().setFromCorners(p1, p2).create();
        collumMesh.add(mesh);

        addGeometry(frame);
        addGeometry(glassMesh);
        addGeometry(midMesh);
        addGeometry(collumMesh);
    }
}
