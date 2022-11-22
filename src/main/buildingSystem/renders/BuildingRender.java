package renders;

import unit2Vol.Unit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 渲染building的类
 *
 * @auther Alessio
 * @date 2022/11/22
 **/
public class BuildingRender {
    private List<Unit> unitList;

    @SafeVarargs
    public BuildingRender(List<Unit>... units) {
        if (units.length != 0) {
            this.unitList = new LinkedList<>();

            for (List<Unit> u : units) {
                unitList.addAll(u);
            }
        } else {
            System.out.println("No units in target building");
        }

    }


}
