package com.example.myclient.handler;

import com.example.server.proto.Response;
import com.example.server.proto.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class serviceHandler extends SimpleChannelInboundHandler {
  private Response response;
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("连接服务端");
    super.channelActive(ctx);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o)
      throws Exception {
    try {
     this.response = (Response) o ;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      ReferenceCountUtil.release(o);
    }


  }
}
