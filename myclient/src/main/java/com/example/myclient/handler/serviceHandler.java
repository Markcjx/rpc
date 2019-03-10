package com.example.myclient.handler;

import com.example.myclient.service.NettyClient;
import com.example.server.proto.Response;
import com.example.server.proto.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import java.util.concurrent.ConcurrentHashMap;

public class serviceHandler extends SimpleChannelInboundHandler {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("连接服务端");
    super.channelActive(ctx);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o)
      throws Exception {
    try {
      System.out.println("收到调用结果");
      Response response = (Response) o ;
      if(NettyClient.objMap.get(response.getRequestId())!=null){
        NettyClient.responseMap.put(response.getRequestId(),response);
        System.out.println("唤醒线程");
        System.out.println( NettyClient.objMap.get(response.getRequestId()).toString());
        synchronized ( NettyClient.objMap.get(response.getRequestId())){
          NettyClient.objMap.get(response.getRequestId()).notifyAll();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      ReferenceCountUtil.release(o);
    }
  }
}
