package com.zsw.rpc.loadbalance;

/**
 * @author Administrator on 2019/7/13 14:58
 **/
public interface IRule {

    String select(String serverName);

}
