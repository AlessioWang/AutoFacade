package Facade.manager;

import archijson.ArchiJSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import geometry.Mesh;
import geometry.Segments;
import wblut.geom.WB_Point;

import Facade.facade.basic.BasicObject;
import Facade.facade.basic.StyledGeometry;
import Facade.facade.basic.StyledMesh;
import Facade.facade.basic.StyledPolyLine;
import Facade.facade.unit.FacadeUnit;
import Facade.facade.unit.UnitStyles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class G_FacadeTest {
    public static final Gson gson = new Gson();
    ArchiJSON tempJson;

    public G_FacadeTest() {
    }

    public void receive(ArchiJSON archiJSON) {
        tempJson = null;
        if (Config.DEBUG)
            System.out.println(archiJSON);
        JsonElement request = archiJSON.getProperties().get("request");
        if (request == null) {
            System.out.println("-------- check format! no request tag in json");
            return;
        }
        String name = request.getAsString();
        switch (name) {
            case "unit-base":
                setUnitBase();
                break;
            case "unit-styles-list":
                setUnitStyles();
                break;
            case "align-id":
                alignId(archiJSON);
                break;
            case "create-details":
                createDetails(archiJSON);
                break;
            case "unit-update":
                updateUnit(archiJSON);
                break;
            default:
                System.out.println("-------- check command: [ " + name + " ], \n should be one from {input_model,unit-base,unit-styles}");
                return;
        }
    }


    public ArchiJSON toArchiJSON() {
        return tempJson;
    }

    /**
     * --------------------- process methods --------------------
     */
    FacadeUnit[] units;

    private FacadeUnit[]createUnits(){
        FacadeUnit[]units = new FacadeUnit[12];
        for (int i = 0; i < 6; i++) {
            WB_Point[] pts = new WB_Point[]{
                    new WB_Point(0, 0, i*3000+0),
                    new WB_Point(0, 8000, i*3000+0),
                    new WB_Point(0, 8000, i*3000+3000),
                    new WB_Point(0, 0, i*3000+3000)
            };
            WB_Point[] pts2 = new WB_Point[]{
                    new WB_Point(0, 8000, i*3000+0),
                    new WB_Point(0, 16000, i*3000+0),
                    new WB_Point(0, 16000, i*3000+3000),
                    new WB_Point(0, 8000, i*3000+3000),
            };
            units[2*i] = new FacadeUnit(pts);
            units[2*i+1] = new FacadeUnit(pts2);
        }

        return units;
    }
    /**
     * 1. parse json
     * 2. prepare things to return
     * 3. build archijson
     */
    private void setUnitBase() {
        //divide by div_options
        WB_Point[] pts = new WB_Point[]{
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
        System.out.println("set unit base!!");

        FacadeUnit f1 = new FacadeUnit(pts);
        FacadeUnit f2 = new FacadeUnit(pts2);
//        units = new FacadeUnit[]{f1, f2};
        units = createUnits();

        tempJson = new ArchiJSON();
        List<JsonElement> elements = new ArrayList<>();

        for (FacadeUnit unit : units) {
            Mesh mesh = Converter.toMesh(unit.getBase(), Converter.unit.mm);
            mesh.getProperties().addProperty("userId", unit.getUserId());
            elements.add(gson.toJsonTree(mesh));
        }
        JsonObject pro = new JsonObject();
        pro.addProperty("response", "unit-base");
        pro.addProperty("layer", "unit-base");
        tempJson.setProperties(pro);
        tempJson.setGeometryElements(elements);
    }

    private void setUnitStyles() {
        tempJson = new ArchiJSON();
        JsonObject pro = new JsonObject();
        pro.addProperty("response", "unit-styles-list");
        pro.add("styles", gson.toJsonTree(UnitStyles.values()));
        tempJson.setProperties(pro);
    }

    private void alignId(ArchiJSON archiJSON) {
        JsonElement idList = archiJSON.getProperties().get("idList");
        if (idList == null) {
            System.out.println("-------- check input! no idList tag in json");
            return;
        }
        JsonArray asJsonArray = idList.getAsJsonArray();
        for (JsonElement jsonElement : asJsonArray) {
            String uuid = jsonElement.getAsJsonObject().get("uuid").getAsString();
            String userId = jsonElement.getAsJsonObject().get("userId").getAsString();
            for (FacadeUnit unit : units) {
                if (unit.getUserId().equalsIgnoreCase(userId)) {
                    unit.setUuid(uuid);
                    break;
                }
            }
        }

        for (FacadeUnit unit : units) {
            System.out.println("unit: " + unit.getUuid());
        }
    }

    private void createDetails(ArchiJSON archiJSON) {
        String uuid = archiJSON.getProperties().get("uuid").getAsString();
        FacadeUnit unit = getUnit(uuid);
        if (unit == null) {
            System.out.println("can't find unit: " + uuid);
            return;
        }
        String styleType = archiJSON.getProperties().get("styleType").getAsString();
        UnitStyles style = UnitStyles.valueOf(styleType);
        if (Config.DEBUG)
            System.out.println("styleType:" + style);

        unit.initObj(style);
        BasicObject obj = unit.getObj();
        setTempJsonByBasicObject(obj,uuid);
    }

    private void setTempJsonByBasicObject(BasicObject obj,String uuid){
        tempJson = new ArchiJSON();
        List<JsonElement> elements = new ArrayList<>();

        /** 1. set geometries */
        for (StyledGeometry geometry : obj.getGeometries()) {
            if (geometry instanceof StyledMesh){
                StyledMesh sm =  (StyledMesh)geometry;
                Mesh mesh = Converter.toMesh(sm, Converter.unit.mm);
                elements.add(gson.toJsonTree(mesh));
            }
            if (geometry instanceof StyledPolyLine){
                List<Segments> segments = Converter.toSegments((StyledPolyLine) geometry);
                for (Segments segment : segments) {
                    segment.getProperties().addProperty("layer","details");
                    elements.add(gson.toJsonTree(segment));
                }
            }
        }
        JsonObject pro = new JsonObject();

        /** 2. set paras */
        Map<String, BasicObject.Para> paras = obj.getParas();
        for (BasicObject.Para para: paras.values()) {
            JsonObject tmp = new JsonObject();
            tmp.addProperty("value", para.value);
            tmp.addProperty("isNumber", para.isNumber);
            tmp.addProperty("min", para.min);
            tmp.addProperty("max", para.max);
            tmp.addProperty("bool", para.bool);
            tmp.addProperty("name", para.name);

            pro.add(para.name, tmp);
        }

        pro.addProperty("response", "details");
        pro.addProperty("uuid", uuid);
        pro.addProperty("layer", "details");
        tempJson.setProperties(pro);
        tempJson.setGeometryElements(elements);
    }

    private void updateUnit(ArchiJSON archiJSON){
        String uuid = archiJSON.getProperties().get("uuid").getAsString();
        FacadeUnit unit = getUnit(uuid);
        if (unit == null) {
            System.out.println("can't find unit: " + uuid);
            return;
        }
        JsonArray paras = archiJSON.getProperties().get("paras").getAsJsonArray();
        for (int i = 0; i < paras.size(); i++) {
            String paraName = paras.get(i).getAsJsonObject().get("label").getAsString();
            String paraValue = paras.get(i).getAsJsonObject().get("value").getAsString();
            unit.getObj().getParas().get(paraName).setValueString(paraValue);
        }
        unit.getObj().update();
        BasicObject obj = unit.getObj();
        setTempJsonByBasicObject(obj,uuid);
    }
    private FacadeUnit getUnit(String uuid) {
        for (FacadeUnit unit : units) {
            if (unit.getUuid().equalsIgnoreCase(uuid)) {
                return unit;
            }
        }
        return null;
    }


}
