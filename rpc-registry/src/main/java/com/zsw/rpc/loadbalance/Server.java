package com.zsw.rpc.loadbalance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 **/
@Getter
@Setter
@AllArgsConstructor
public class Server {

    private String serverName;

    private ILoadBanalce loadBanalce;

    public String choose() {
        return this.loadBanalce.select();
    }

}
