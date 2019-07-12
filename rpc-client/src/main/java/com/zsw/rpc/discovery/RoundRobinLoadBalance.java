package com.zsw.rpc.discovery;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZhangShaowei on 2019/7/12 13:38
 **/
@Slf4j
public class RoundRobinLoadBalance implements ILoadBalance {

    List<String> services;

    private AtomicInteger nextIndex;

    public RoundRobinLoadBalance(List<String> services) {
        this.services = services;
        this.nextIndex = new AtomicInteger(0);
    }

    @Override
    public String chooseServer() {
        int index = this.nextIndex.getAndIncrement() % services.size();
        return this.services.get(index);
    }
}
