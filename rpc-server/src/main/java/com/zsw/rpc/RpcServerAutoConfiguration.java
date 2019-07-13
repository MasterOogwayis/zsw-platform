package com.zsw.rpc;

import com.zsw.rpc.stereotype.EnableDiscoverServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator on 2019/6/8 20:47
 **/
@Configuration
@EnableDiscoverServer
@ComponentScan("com.zsw.rpc")
public class RpcServerAutoConfiguration {


}
