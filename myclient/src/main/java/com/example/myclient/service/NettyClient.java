package com.example.myclient.service;

import com.example.myclient.handler.clientHandler;
import com.example.server.proto.Request;
import com.example.server.proto.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;


public class NettyClient {
  private  final EventLoopGroup eventLoopGroup ;
  private  final Bootstrap b ;
  private  ChannelFuture f;
  private String host;
  private int port;

  public NettyClient(String host, int port) {
    this.host = host;
    this.port = port;
    eventLoopGroup = new NioEventLoopGroup();
    b = new Bootstrap();
  }

  public Response send(Request req){
    try {
      b.group(eventLoopGroup)
       .channel(NioSocketChannel.class)
       .option(ChannelOption.SO_KEEPALIVE,true)
       .handler(new clientHandler())
       .remoteAddress(host,port);
      f = b.connect();
      f.channel().writeAndFlush(req).sync();
      if (response != null) {
        f.channel().closeFuture().sync();
      }
      return response;
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      eventLoopGroup.shutdownGracefully();
    }
  }
}
