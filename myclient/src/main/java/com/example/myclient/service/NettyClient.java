package com.example.myclient.service;

import com.example.myclient.handler.clientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
  private static final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
  private static final Bootstrap b = new Bootstrap();
  private static ChannelFuture f;
  public static void init(){
    try {
      b.group(eventLoopGroup)
       .channel(NioSocketChannel.class)
       .option(ChannelOption.SO_KEEPALIVE,true)
       .handler(new clientHandler())
       .remoteAddress("127.0.0.1",8081);
      f = b.connect();
      f.channel().closeFuture().sync();
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
