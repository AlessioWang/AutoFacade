package client.dao;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @auther Alessio
 * @date 2022/4/22
 **/
@Configuration
@ComponentScan(basePackages = {"client.*"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AopConfig {

}
