package client.dao;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @auther Alessio
 * @date 2022/4/22
 **/
@Component
@Aspect
public class WallProxy {

    //设置抽取切入点
    @Pointcut(value = "execution(* WallPanelImp.addPanel(..))")
    public void addPanelAsp() {
    }

    @Before(value = "addPanelAsp()")
    public void beforeMtd(){
        System.out.println("before");
    }

    @After(value = "addPanelAsp()")
    public void afterMtd (){
        System.out.println("after");
    }

    @Around(value = "addPanelAsp()")
    public void aroundMtd(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        System.out.println("around before");
        proceedingJoinPoint.proceed();
        System.out.println("around after");
    }

}
