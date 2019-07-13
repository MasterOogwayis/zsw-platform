package com.zsw.rpc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZhangShaowei on 2019/6/6 13:37
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse<T> implements Serializable {

    private static final long serialVersionUID = -7004286160936478553L;

    boolean success = true;

    T data;

}
