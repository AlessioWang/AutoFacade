package Test.GsonTest;

import Output.JsonPanelEntity;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @auther Alessio
 * @date 2023/5/16
 **/
public class GsonDemo {

    public static void main(String[] args) {
        List<JsonPanelEntity> units  = new LinkedList<>();
        units.add(new JsonPanelEntity("abc"));
        units.add(new JsonPanelEntity("123"));

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
