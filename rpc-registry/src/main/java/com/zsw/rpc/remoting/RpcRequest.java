package com.zsw.rpc.remoting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZhangShaowei on 2019/6/6 13:36
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -6573162402446252584L;
    private String clazz;

    private String method;

    private Object[] params;

    private String version;

}
