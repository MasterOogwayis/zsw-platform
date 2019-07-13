package com.zsw.rpc.server;

import com.zsw.rpc.dto.RpcRequest;
import com.zsw.rpc.dto.RpcResponse;
import io.netty.channel.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author ZhangShaowei on 2019/6/6 14:14
 **/
@Slf4j
@AllArgsConstructor
@ChannelHandler.Sharable
public class ProcessorHandler extends SimpleChannelInboundHandler<RpcRequest> {

    Map<String, Object> handlerMappings;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        RpcResponse<?> response = this.invoke(request);
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ChannelFuture close = ctx.close();
        close.addListener(ChannelFutureListener.CLOSE);
    }

    @SneakyThrows
    private RpcResponse<?> invoke(RpcRequest request) {
        log.info("received request: {}", request);
        RpcResponse<Object> response = new RpcResponse<>();
        String name = request.getClazz();
        if (StringUtils.hasText( request.getVersion())) {
            name = name + "-" + request.getVersion();
        }
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
