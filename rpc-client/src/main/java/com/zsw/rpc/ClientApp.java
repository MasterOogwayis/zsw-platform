package com.zsw.rpc;

import com.zsw.rpc.api.ServiceHello;
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
        ServiceHello serviceHello = applicationContext.getBean(ServiceHello.class);


        System.out.println(serviceHello.sayHello("Shaowei Zhang"));

    }

}
