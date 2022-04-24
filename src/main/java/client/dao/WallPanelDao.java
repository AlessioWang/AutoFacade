package client.dao;

import client.model.WallPanelEntity;

import java.util.List;

/**
 * @auther Alessio
 * @date 2022/4/21
 **/
public interface WallPanelDao {

    void addPanel(WallPanelEntity wpEnt);

    void deleteByIndex(int index);

    void batchAdd(List<Object[]> args);

    WallPanelEntity selectByIndex(int index);

    List<WallPanelEntity> selectAll();

    List<WallPanelEntity> selectByStyle(String type);

}
