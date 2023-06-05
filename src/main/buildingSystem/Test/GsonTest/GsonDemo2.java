package Test.GsonTest;

import Output.JsonPanelEntity;
import Output.PanelEntity;
import com.google.gson.Gson;
import wblut.geom.WB_Point;

import java.util.*;

/**
 * @auther Alessio
 * @date 2023/5/20
 **/
public class GsonDemo2 {

    public static void main(String[] args) {
        List<JsonPanelEntity> units  = new LinkedList<>();

        WB_Point[] wall = new WB_Point[]{new WB_Point(0,0,0), new WB_Point(5000,0,0)};
        Map<WB_Point, String> windows = new HashMap<>(){
            {
                put(new WB_Point(1000, 0, 1100), "1000 x 1200mm");
                put(new WB_Point(3000, 0, 1100), "1000 x 1200mm");
            }
        };

        WB_Point[] wall2 = new WB_Point[]{new WB_Point(5000,0,0), new WB_Point(10000,0,0)};
        Map<WB_Point, String> windows2 = new HashMap<>(){
            {
                put(new WB_Point(6000, 0, 1100), "1000 x 1200mm");
                put(new WB_Point(8000, 0, 1100), "1000 x 1200mm");
            }
        };

        PanelEntity pe1 = new PanelEntity(wall, 0,windows);
        PanelEntity pe2 = new PanelEntity(wall2,0, windows2);

        units.add(new JsonPanelEntity(pe1));
        units.add(new JsonPanelEntity(pe2));

        String json = simpleObjToJson(units);
        System.out.println(json);
    }

    public static String simpleObjToJson(Object obj) {
        if (Objects.isNull(obj)) return "";
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
