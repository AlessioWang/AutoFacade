package Output;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @auther Alessio
 * @date 2023/5/16
 **/
public class JsonPanelEntity {
    private String name;

    private Map<String, String> wall;

    private List<Map<String, String>> windows;

    private transient PanelEntity pe;

    public JsonPanelEntity(String name) {
        this.name = name;

        initTest();
    }

    public JsonPanelEntity(PanelEntity pe) {
        this.pe = pe;

        initFromEntity();
    }

    private void initFromEntity() {
        name = "wall_entity";
        wall = pe.getWallJson();
        windows = pe.getWindowsJson();
    }

    private void initTest() {
        wall = new HashMap<>();
        wall.put("start", "6000,0,0");
        wall.put("end", "12000,0,0");

        windows = new LinkedList<>();
        windows.add(new HashMap<>() {
            {
                put("style", "1000 x 1200mm");
                put("pos", "8500,0,600");
            }
        });
        windows.add(new HashMap<>() {
            {
                put("style", "1000 x 1200mm");
                put("pos", "8500,0,600");
            }
        });
    }


}
