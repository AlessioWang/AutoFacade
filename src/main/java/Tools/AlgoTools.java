package Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 关于算法的工具类
 *
 * @auther Alessio
 * @date 2022/1/6
 **/
public class AlgoTools {

    /**
     * 对比选择两个List的重复元素
     *
     * @param l1
     * @param l2
     * @return
     */
    public static List selSameObjFrom2List(List<?> l1, List<?> l2) {
        List<Object> target = new ArrayList<>();
        for (Object e : l1) {
            if (l2.contains(e)) {
                target.add(e);
            }
        }
        return target;
    }

}
