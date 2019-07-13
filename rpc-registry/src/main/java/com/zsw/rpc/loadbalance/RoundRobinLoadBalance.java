package com.zsw.rpc.loadbalance;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 顺序负载
 *
 * @author Administrator on 2019/7/13 14:40
 **/
@Slf4j
public class RoundRobinLoadBalance implements ILoadBanalce {

    private List<String> addresses;

    private AtomicInteger nextIndex;

    public RoundRobinLoadBalance(List<String> addresses) {
        this.addresses = addresses;
        this.nextIndex = new AtomicInteger(0);
    }

    @Override
    public String select() {
        int index = this.nextIndex.getAndIncrement();
        return this.addresses.get(index % this.addresses.size());
    }
}
