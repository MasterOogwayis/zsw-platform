package com.zsw.rpc.server.support;

import com.zsw.rpc.support.dto.RpcRequest;
import com.zsw.rpc.support.dto.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author ZhangShaowei on 2019/6/6 14:14
 **/
@Slf4j
@AllArgsConstructor
@ChannelHandler.Sharable
public class ProcesserHandler extends SimpleChannelInboundHandler<RpcRequest> {

    Map<String, Object> handlerMappings;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        log.debug("active a request: {}", msg);
        Object response = this.invoke(msg);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object invoke(RpcRequest request) throws InvocationTargetException, IllegalAccessException {
        RpcResponse<Object> response = new RpcResponse<>();

        String name = request.getClazz() + (StringUtils.isEmpty(request.getVersion()) ? "" : ("-" + request.getVersion()));
        Object bean = this.getBean(name);
        Method method;
        if (Objects.isNull(bean)) {
            response.setData("no service found with the given class : " + request.getClazz());
            response.setSuccess(false);
        } else if ((method = this.resolveMethod(bean, request.getMethod(), request.getParams())) == null) {
            response.setData("No method [" + request.getMethod() + "] found in service " + request.getClazz());
            response.setSuccess(false);
        } else {
            Object invoke = method.invoke(bean, request.getParams());
            response.setData(invoke);
        }
        return response;

    }


    private Object getBean(String className) {
        return this.handlerMappings.get(className);
    }

    private Method resolveMethod(Object bean, String methodName, Object[] params) {
        try {
            Class<?>[] types = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                types[i] = params[i].getClass();
            }
            return bean.getClass().getDeclaredMethod(methodName, types);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
