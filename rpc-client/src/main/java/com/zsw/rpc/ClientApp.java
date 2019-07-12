package com.zsw.rpc;

import com.zsw.rpc.api.ServiceHello;
import com.zsw.rpc.autoconfigure.RpcClientAutoConfiguration;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author ZhangShaowei on 2019/6/6 13:35
 **/
public class ClientApp {

    @SneakyThrows
    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RpcClientAutoConfiguration.class);
        ServiceHello serviceHello = applicationContext.getBean(ServiceHello.class);


        @Cleanup BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (true) {
            line = bufferReader.readLine();

            if ("exit".endsWith(line)) {
                break;
            }
            String str = serviceHello.sayHello(line);
            System.err.println(str);
        }

    }

}
