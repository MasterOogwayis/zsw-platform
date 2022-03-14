package com.zsw.rpc.loadbalance;

/**
 * @author Administrator
 **/
public interface IRule {

    String select(String serverName) throws Exception;

}
