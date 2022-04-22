package client;

import client.entity.WallPanelEntity;
import client.service.WallPanelService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Alessio
 * @date 2022/4/20
 **/
public class SpringClient {

    ApplicationContext context = new ClassPathXmlApplicationContext("JDBCBean.xml");
    WallPanelService service = context.getBean(WallPanelService.class);

    @Test
    public void addTest() {
        WallPanelEntity entity = new WallPanelEntity();
        entity.setPanel_style("StyleA");
        entity.setPosX(0);
        entity.setPosY(0);
        entity.setPosZ(0);
        entity.setDirX(0);
        entity.setDirY(1);
        entity.setDirZ(0);

        service.addPanel(entity);
        System.out.println("--------------");
    }

    @Test
    public void deleteByIndex() {
        service.deletePanel(2);
        System.out.println("--------------");
    }

    @Test
    public void transTest() {
        WallPanelEntity entity = new WallPanelEntity();
        entity.setPanel_style("StyleB");
        entity.setPosX(100);
        entity.setPosY(0);
        entity.setPosZ(0);
        entity.setDirX(0);
        entity.setDirY(1);
        entity.setDirZ(0);

        service.transTestAdd(entity);
        System.out.println("--------------");
    }

    @Test
    public void batchAdd() {
        List<Object[]> args = new ArrayList<>();
        args.add(new Object[]{11, "StyleC", 4000, 6000, 0, 1, 0, 0});
        args.add(new Object[]{12, "StyleB", 8000, 6000, 0, 1, 0, 0});
        service.batchAdd(args);
    }

    @Test
    public void selectAll() {
        service.selectAll();
    }

    @Test
    public void selectStyleA() {
        service.selectByStyle("StyleA");
    }


}
