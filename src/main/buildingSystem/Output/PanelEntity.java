package Output;

import wblut.geom.WB_Point;

import java.util.*;

/**
 * @auther Alessio
 * @date 2023/5/20
 **/
public class PanelEntity {
    private WB_Point[] wallPos;

    private double level;

    // position -> window type
    private Map<WB_Point, String> windows;

    private Map<String, String> wallJson;

    private List<Map<String, String>> windowsJson;


    public PanelEntity(WB_Point[] wallPos, double level, Map<WB_Point, String> windows) {
        this.wallPos = wallPos;
        this.level = level;
        this.windows = windows;

        initJsonData();
    }

    public PanelEntity(WB_Point[] wallPos, Map<WB_Point, String> windows) {
        this.wallPos = wallPos;
        this.windows = windows;

        initJsonData();
    }

    private void initJsonData() {
        initWall();
        initWindows();
    }

    private void initWall() {
        wallJson = new HashMap<>();
        wallJson.put("level", String.valueOf(level));
        wallJson.put("end", pointToString(wallPos[1]));
        wallJson.put("start", pointToString(wallPos[0]));
    }

    private void initWindows() {
        windowsJson = new LinkedList<>();

        Set<WB_Point> pointSet = windows.keySet();

        for (var pos : pointSet) {
            windowsJson.add(new HashMap<>() {
                {
                    put("style", windows.get(pos));
                    put("pos", pointToString(pos));
                }
            });
        }
    }

    private String pointToString(WB_Point pt) {
        StringBuilder sb = new StringBuilder();

        sb.append(pt.xd());
        sb.append(",");
        sb.append(pt.yd());
        sb.append(",");
        sb.append(pt.zd());

        return sb.toString();
    }

    public Map<String, String> getWallJson() {
        return wallJson;
    }

    public List<Map<String, String>> getWindowsJson() {
        return windowsJson;
    }
}
