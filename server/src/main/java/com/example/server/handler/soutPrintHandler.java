package com.example.server.handler;

import com.example.server.proto.UserInfo;
import com.example.server.proto.UserInfo.UserMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class soutPrintHandler extends SimpleChannelInboundHandler<Object> {

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o)
      throws Exception {
    UserInfo.UserMsg msg =(UserMsg) o;
    System.out.println("User"+ o.toString());
  }
}
