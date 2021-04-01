package com.zsw.rpc;

import com.zsw.rpc.annotation.EnableDiscoverClient;
import com.zsw.rpc.api.ServiceHello;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhangShaowei on 2019/6/6 13:35
 **/
@Slf4j
@Configuration
@EnableDiscoverClient
public class ClientApp {

    @SneakyThrows
    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientApp.class);
        ServiceHello serviceHello = applicationContext.getBean(ServiceHello.class);

        for (int i = 0; i < 100; i++) {
            try {
                log.info(serviceHello.sayHello("Shaowei Zhang " + i++));
            } catch (Exception e) {
                e.printStackTrace();
            }
            TimeUnit.SECONDS.sleep(1);
        }

    }

}
