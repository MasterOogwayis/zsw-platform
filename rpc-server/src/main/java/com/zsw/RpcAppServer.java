package com.zsw;

import com.zsw.rpc.server.RemoteServer;
import lombok.SneakyThrows;

/**
 * @author ZhangShaowei on 2019/6/6 13:26
 **/
public class RpcAppServer {

    @SneakyThrows
    public static void main(String[] args) {

        new RemoteServer().startup();

    }


}
