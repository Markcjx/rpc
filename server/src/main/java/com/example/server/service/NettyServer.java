package com.example.server.service;

import com.example.server.handler.printHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class NettyServer {
  private static final int port = 8081;
  private static final EventLoopGroup boss = new NioEventLoopGroup();
  private static final EventLoopGroup worker = new NioEventLoopGroup();
  private static  final ServerBootstrap boot = new ServerBootstrap();

  public static void run(){
    try {
      boot.group(boss,worker)
          .channel(NioServerSocketChannel.class)
          .childHandler(new printHandler())
          .option(ChannelOption.SO_BACKLOG,1024)
          .childOption(ChannelOption.SO_KEEPALIVE,true)
          .childOption(ChannelOption.TCP_NODELAY,true);
      ChannelFuture cf = boot.bind(port).sync();
      if (cf.isSuccess()) {
        System.out.println("启动 Netty Server");
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }
  @PreDestroy
  public void destory() throws InterruptedException {
    boss.shutdownGracefully().sync();
    worker.shutdownGracefully().sync();
    System.out.println("关闭Netty");
  }

}
