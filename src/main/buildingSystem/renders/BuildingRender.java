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

    public BuildingRender(List<Unit>... units) {
        this.unitList = new LinkedList<>();

        for (List<Unit> u : units) {
            unitList.addAll(u);
        }
    }


}
