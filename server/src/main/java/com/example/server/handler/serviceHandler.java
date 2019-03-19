package com.example.server.handler;

import com.example.server.proto.Request;
import com.example.server.proto.Response;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

public class serviceHandler extends ChannelInboundHandlerAdapter {
  private AtomicInteger nConnection ;
  private final Map<String, Object> handlerMap;

  public serviceHandler(Map<String, Object> handlerMap,AtomicInteger num) {
    this.handlerMap = handlerMap;
    this.nConnection = num;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println("接收到服务调用！");
    Request req = (Request) msg;
    Response response = new Response();
    response.setRequestId(req.getRequestId());
    try {
      Object result = handle(req);
      System.out.println("result is "+ result);
      response.setResult(result);
    } catch (Throwable t) {
      System.out.println("error!");
      t.printStackTrace();
      response.setError(t);
    }
    ctx.writeAndFlush(response).sync();
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    nConnection.incrementAndGet();
    System.out.println("connection "+nConnection.get());
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    nConnection.decrementAndGet();
    System.out.println("de connection "+nConnection.get());
    super.channelInactive(ctx);
  }

  private Object handle(Request req) throws InvocationTargetException {
    System.out.println("开始处理请求");
    String ClassName = req.getClassName();
    Object serviceBean = handlerMap.get(ClassName);
    if (serviceBean != null) {
      Class<?> serviceClass = serviceBean.getClass();
      String methodName = req.getMethodName();
      Class<?>[] paramTypes = req.getParameterTypes();
      Object[] args = req.getParameters();
      FastClass serviceFastClass = FastClass.create(serviceClass);
      FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, paramTypes);
      System.out.println("args is "+args);
      Object obj = serviceFastMethod.invoke(serviceBean, args);

      System.out.println("处理完成");
      System.out.println("********"+obj);
      return obj;
    } else {
      return null;
    }
  }
}
