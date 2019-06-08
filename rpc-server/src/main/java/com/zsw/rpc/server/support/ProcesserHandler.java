package com.zsw.rpc.server.support;

import com.zsw.rpc.support.dto.RpcRequest;
import com.zsw.rpc.support.dto.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

/**
 * @author ZhangShaowei on 2019/6/6 14:14
 **/
@AllArgsConstructor
public class ProcesserHandler implements Runnable {

    Socket socket;

    Map<String, Object> handlerMappings;

    @SneakyThrows
    @Override
    public void run() {
        this.invoke();
    }


    private void invoke() {
        RpcResponse<Object> response = new RpcResponse<>();
        try (ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream())) {

            RpcRequest request = (RpcRequest) ois.readObject();

            Object bean = this.getBean(request.getClazz() + "#" + request.getVersion());
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
            oos.writeObject(response);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
