package FacadeGen;

import FacadeGen.Vol;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/2/16
 **/
public class Building {

    //楼的名字
    public final String name;
    //存储所有的体块
    public List<Vol> volList;

    public Building(String name) {
        this.name = name;
        volList = new ArrayList<>();
    }

    public void addVol(Vol vol) {
        volList.add(vol);
    }

    public void remove(Vol vol) {
        if (volList != null) {
            for (Vol v : volList) {
                if (v.equals(vol)) {
                    volList.remove(v);
                } else {
                    System.out.println("建筑中不存在指定体块");
                }
            }
        }
    }


    public void setVolList(List<Vol> volList) {
        this.volList = volList;
    }

    public List<Vol> getVolList() {
        return volList;
    }

    @Override
    public String toString() {
        return "Building{" +
                "楼名 ：'" + name + '\'' +
                ", 形体数：" + volList.size() +
                '}';
    }

}
