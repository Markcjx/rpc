package com.example.server.handler;

import com.example.server.proto.Request;
import com.example.server.proto.Response;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

public class serviceHandler extends SimpleChannelInboundHandler<Object> {

  private final Map<String, Object> handlerMap;

  public serviceHandler(Map<String, Object> handlerMap) {
    this.handlerMap = handlerMap;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o)
      throws Exception {
    if (!(o instanceof Request)) {
      return;
    }
    Request req = (Request) o;
    Response response = new Response();
    response.setRequestId(req.getRequestId());
    try {
      Object result = handle(req);
      response.setResult(result);
    } catch (Throwable t) {
      response.setError(t);
    }
    channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
  }

  private Object handle(Request req) throws InvocationTargetException {
    String ClassName = req.getClassName();
    Object serviceBean = handlerMap.get(ClassName);
    if (serviceBean != null) {
      Class<?> serviceClass = serviceBean.getClass();
      String methodName = req.getMethodName();
      Class<?>[] paramTypes = req.getParameterTypes();
      Object[] args = req.getParameters();
      FastClass serviceFastClass = FastClass.create(serviceClass);
      FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, paramTypes);
      return serviceFastMethod.invoke(serviceBean, args);
    } else {
      return null;
    }
  }
}
