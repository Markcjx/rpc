package com.example.myclient.service;

import com.example.server.proto.Request;
import com.example.server.proto.Response;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
@SuppressWarnings("unchecked")
public class RpcProxy {
  private String serverAddress;
  @Autowired
  private ServiceDiscovery serviceDiscovery;
  public <T> T creat(Class<?> interfaceClass){
    return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Request req = new Request();
            req.setRequestId(UUID.randomUUID().toString());
            req.setClassName(method.getDeclaringClass().getName());
            req.setMethodName(method.getName());
            req.setParameterTypes(method.getParameterTypes());
            req.setParameters(method.getParameters());

            if(serviceDiscovery!=null){
              serverAddress = serviceDiscovery.discover();
            }
            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            NettyClient nettyClient = new NettyClient(host,port);
            Response res = nettyClient.send(req);
            return res;
          }
        });
  }

}
