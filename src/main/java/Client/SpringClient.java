package Client;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @auther Alessio
 * @date 2022/4/20
 **/
public class SpringClient {

    @Test
    public void test01() {
        System.out.println("001");
    }

    @Test
    public void testSQL() {
        ApplicationContext context = new ClassPathXmlApplicationContext("JDBCBean.xml");

    }


}
