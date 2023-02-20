package Facade.manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import converter.WB_Converter;

import geometry.Mesh;
import geometry.Segments;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.hemesh.HE_Mesh;

import Facade.facade.basic.Material;
import Facade.facade.basic.StyledMesh;
import Facade.facade.basic.StyledPolyLine;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    public static final Gson gson = new Gson();

    public enum unit {
        mm(0.1), cm(1), m(100);

        double scaleToCM;

        private unit(double scaleToCM) {
            this.scaleToCM = scaleToCM;
        }

        public double getScaleToCM() {
            return scaleToCM;
        }
    }

    public static Mesh toMesh(StyledMesh styledMesh, unit from) {
        HE_Mesh hemesh = styledMesh.getMesh();
//        System.out.println("from.getScaleToCM(): "+from.getScaleToCM());
        HE_Mesh scaledMesh = hemesh.scale(from.getScaleToCM(),new WB_Point(0,0,0));
        int strokeColor = styledMesh.getStrokeColor();
        final Material material = styledMesh.getMaterial();
        Mesh mesh = WB_Converter.toMesh(scaledMesh);
        JsonObject style = new JsonObject();
        if (material==Material.Glass)
            style.addProperty("isGlass", true);
        style.addProperty("MaterialColor", material.getColor());
        style.addProperty("strokeColor", strokeColor);
        mesh.setProperties(style);
        return mesh;
    }

    public static List<Segments> toSegments(StyledPolyLine styledPolyLine) {
        List<Segments> segments = new ArrayList<>();
        List<WB_PolyLine> lines = styledPolyLine.getLines();
        int strokeColor = styledPolyLine.getStrokeColor();
        for (WB_PolyLine polyline : lines) {
            Segments p = toSegments(polyline);
            JsonObject obj = new JsonObject();
            obj.addProperty("color", strokeColor);
            p.setProperties(obj);
            segments.add(p);
        }

        return segments;
    }

    public static Segments toSegments(WB_PolyLine polyline) {
        Segments segments = new Segments();
        List<Double> positions = new ArrayList();
        int size = 3;
        WB_CoordCollection coords = polyline.getPoints();

        for (int i = 0; i < coords.size(); ++i) {
            WB_Coord c = coords.get(i);

            for (int j = 0; j < size; ++j) {
                positions.add(c.getd(j));
            }
        }

        segments.setCoordinates(positions);
        segments.setSize(size);
        segments.setClosed(false);
        return segments;
    }
}
