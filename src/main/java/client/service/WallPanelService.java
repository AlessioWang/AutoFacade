package client.service;

import client.entity.WallPanelEntity;
import client.dao.WallPanelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * propagation 事务传播行为
 * isolation 隔离级别
 * rollbackFor 回滚条件
 *
 * @auther Alessio
 * @date 2022/4/21
 **/


@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
public class WallPanelService {

    @Autowired
    private WallPanelDao wallPanelDao;

    public void addPanel(WallPanelEntity entity) {
        wallPanelDao.addPanel(entity);
    }

    public void deletePanel(int index) {
        wallPanelDao.deleteByIndex(index);
    }

    public void transTestAdd(WallPanelEntity entity) {
        wallPanelDao.addPanel(entity);
//        int i = 10 / 0;
        wallPanelDao.addPanel(entity);
    }

    public void batchAdd(List<Object[]> args) {
        wallPanelDao.batchAdd(args);
    }

    public List<WallPanelEntity> selectAll() {
        return wallPanelDao.selectAll();
    }

    public List<WallPanelEntity> selectByStyle(String type) {
        return wallPanelDao.selectByStyle(type);
    }

    public WallPanelEntity selectByIndex(int index) {
        return wallPanelDao.selectByIndex(index);
    }
}
