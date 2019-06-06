package com;

import com.zsw.rpc.api.IServiceHello;
import com.zsw.rpc.autoconfigure.RpcClienteAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author ZhangShaowei on 2019/6/6 14:52
 **/
public class ClientTests {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcClienteAutoConfiguration.class);
        IServiceHello iServiceHello = applicationContext.getBean(IServiceHello.class);


        System.out.println(iServiceHello.sayHello("Shaowei Zhang"));

    }


}
