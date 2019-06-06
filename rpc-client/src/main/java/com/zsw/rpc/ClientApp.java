package com.zsw.rpc;

import com.zsw.rpc.api.IServiceHello;
import com.zsw.rpc.autoconfigure.RpcClienteAutoConfiguration;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author ZhangShaowei on 2019/6/6 13:35
 **/
public class ClientApp {

    @SneakyThrows
    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcClienteAutoConfiguration.class);
        IServiceHello iServiceHello = applicationContext.getBean(IServiceHello.class);


        System.out.println(iServiceHello.sayHello("Shaowei Zhang"));

    }

}
