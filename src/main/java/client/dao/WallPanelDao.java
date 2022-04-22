package client.dao;

import client.entity.WallPanelEntity;

import java.util.List;

/**
 * @auther Alessio
 * @date 2022/4/21
 **/
public interface WallPanelDao {

    void addPanel(WallPanelEntity wpEnt);

    void deleteByIndex(int index);

    void batchAdd(List<Object[]> args);

    List<WallPanelEntity> selectAll();

    List<WallPanelEntity> selectByStyle(String type);

}
