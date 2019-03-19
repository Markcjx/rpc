package com.example.server.service;

import com.example.server.handler.printHandler;
import com.example.server.remoteService.RpcService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class NettyServer {

  @Value("${netty.port}")
  private int port;
  private EventLoopGroup boss;
  private EventLoopGroup worker;
  private ServerBootstrap boot;
  @Value("${netty.serverAddress}")
  private String serverAddress;
  @Autowired
  private GenericApplicationContext app;
  @Autowired
  private ServiceRegistry serviceRegistry;

  private Map<String, Object> handler = new HashMap<>();

  public NettyServer() {
    boss = new NioEventLoopGroup();
    worker = new NioEventLoopGroup();
    boot = new ServerBootstrap();
  }

  private void getBeanMap() {

    Map<String, Object> serviceBeanMap = app.getBeansWithAnnotation(RpcService.class);
    if (serviceBeanMap.size() > 0) {
      for (Object serviceBean : serviceBeanMap.values()) {
        String[] interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value()
            .getName().split("\\.");
        handler.put(interfaceName[interfaceName.length-1], serviceBean);
      }
    }
  }

  public void run() {
    getBeanMap();
    try {

      boot.group(boss, worker)
          .channel(NioServerSocketChannel.class)
          .childHandler(new printHandler(handler))
          .option(ChannelOption.SO_BACKLOG, 1024)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childOption(ChannelOption.TCP_NODELAY, true);
      ChannelFuture cf = boot.bind(port).sync();
      serviceRegistry.register(serverAddress +":"+ port);
      if (cf.isSuccess()) {
        System.out.println("启动 Netty Server");
      }
    } catch (Exception e) {
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
