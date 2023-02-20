package Facade.manager;

import archijson.ArchiJSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import geometry.Mesh;
import geometry.Segments;
import processing.core.PApplet;

import Facade.facade.basic.BasicObject;
import Facade.facade.basic.StyledGeometry;
import Facade.facade.basic.StyledMesh;
import Facade.facade.basic.StyledPolyLine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneratorDemo extends Generator{

    public GeneratorDemo(PApplet app) {
        super(app);
    }

    @Override
    public void initObject() {
         this.obj = new Facade();
    }

    boolean initPanel = false;

    public ArchiJSON initPanel() {
        Gson gson = new Gson();
        ArchiJSON ret = new ArchiJSON();
        Map<String, BasicObject.Para> paras = this.obj.getParas();
        JsonObject pro = new JsonObject();

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

        pro.addProperty("updatePanel",true);
        ret.setProperties(pro);
        return ret;
    }

    public void receive(ArchiJSON archiJSON) {
        if(Config.DEBUG)
        System.out.println(archiJSON);
        JsonElement initPanel = archiJSON.getProperties().get("initPanel");
        if (initPanel!=null&&initPanel.getAsBoolean()) {
            this.initPanel = true;
            return;
        }
        JsonArray paras = archiJSON.getProperties().get("paras").getAsJsonArray();
        for (int i = 0; i < paras.size(); i++) {
            String paraName = paras.get(i).getAsJsonObject().get("name").getAsString();
            String paraValue = paras.get(i).getAsJsonObject().get("value").getAsString();
            this.obj.getParas().get(paraName).setValueString(paraValue);
        }
        this.obj.update();
    }

    public ArchiJSON toArchiJSON(Gson gson) {
        if (initPanel){
            initPanel = false;
            return initPanel();
        }
//        System.out.println("----------------------");
        ArchiJSON ret = new ArchiJSON();
        List<JsonElement> elements = new ArrayList<>();
        System.out.println("facade: "+obj.getGeometries().size());
        for (StyledGeometry geometry : obj.getGeometries()) {
            if (geometry instanceof StyledMesh){
                Mesh mesh = Converter.toMesh((StyledMesh) geometry, Converter.unit.mm);
                mesh.getProperties().addProperty("layer","details");
//                System.out.println("mesh: "+ ((StyledMesh) geometry).getMesh().getVertices().size());
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
        pro.addProperty("updatePanel",false);
        ret.setProperties(pro);
        ret.setGeometryElements(elements);
        return ret;
    }

}
