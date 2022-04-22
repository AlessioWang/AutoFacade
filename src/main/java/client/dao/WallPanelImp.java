package client.dao;

import client.entity.WallPanelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @auther Alessio
 * @date 2022/4/21
 **/

@Repository
public class WallPanelImp implements WallPanelDao {

    //注入JDBCTemplate
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addPanel(WallPanelEntity wpEnt) {
        String sql = "insert into wall_info values (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] args = {wpEnt.getW_index(), wpEnt.getPanel_style(), wpEnt.getPosX(), wpEnt.getPosY(), wpEnt.getPosZ(), wpEnt.getDirX(), wpEnt.getDirY(), wpEnt.getDirZ()};
        int updateNum = jdbcTemplate.update(sql, args);
        System.out.println("新建面板个数: " + updateNum);
    }

    @Override
    public void deleteByIndex(int index) {
        String sql = "delete from wall_info where w_index = ?";
        Object[] args = {index};
        int updateNum = jdbcTemplate.update(sql, args);
        System.out.println("删除面板个数: " + updateNum);
    }

    @Override
    public void batchAdd(List<Object[]> args) {
        String sql = "insert into wall_info values(?, ?, ?, ?, ?, ?, ?, ?)";
        int[] updateNum = jdbcTemplate.batchUpdate(sql, args);
        System.out.println("批量插入面板数: " + updateNum.length);
    }

    @Override
    public WallPanelEntity selectByIndex(int index) {
        String sql = "select * from wall_info where w_index = ?";
        Object[] args = {index};
        WallPanelEntity entity = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(WallPanelEntity.class), args);
        System.out.println(entity);
        return entity;
    }

    @Override
    public List<WallPanelEntity> selectAll() {
        String sql = "select * from wall_info";
        List<WallPanelEntity> entities = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WallPanelEntity.class));
        for (WallPanelEntity e : entities) {
            System.out.println(e);
        }
        System.out.println("共有面板数: " + entities.size());
        return entities;
    }

    @Override
    public List<WallPanelEntity> selectByStyle(String type) {
        String sql = "select * from wall_info where panel_style = ?";
        Object[] args = {type};
        List<WallPanelEntity> entities = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WallPanelEntity.class), args);
        for (WallPanelEntity e : entities) {
            System.out.println(e);
        }
        System.out.println("共有" + type + "类型面板数: " + entities.size());
        return entities;
    }


}
