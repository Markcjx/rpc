package com.example.myclient.service;

import com.example.myclient.handler.clientHandler;
import com.example.myclient.handler.serviceHandler;
import com.example.server.proto.Request;
import com.example.server.proto.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import com.example.server.handler.Decoder;
import com.example.server.handler.Encoder;
import java.util.concurrent.ConcurrentHashMap;



public class NettyClient {

  private final EventLoopGroup eventLoopGroup;
  private final Bootstrap b;
  private ChannelFuture f;
  private String host;
  private int port;
  public static Map<String, Object> objMap = new ConcurrentHashMap<>();
  public static Map<String, Object> responseMap = new ConcurrentHashMap<>();

  public NettyClient(String host, int port) {
    this.host = host;
    this.port = port;
    eventLoopGroup = new NioEventLoopGroup();
    b = new Bootstrap();
  }

  public Response send(Request req) {
    Response res = null;
    try {
      b.group(eventLoopGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(new clientHandler())
          .remoteAddress(host, port);
      f = b.connect().sync();
      System.out.println("客户端连接");
      f.channel().pipeline().writeAndFlush(req).sync();
      if (objMap.get(req.getRequestId()) == null) {
        Object obj = new Object();
        synchronized (obj) {
          objMap.put(req.getRequestId(), obj);
          System.out.println("线程休眠");
          obj.wait(100);
        }
      }
      System.out.println("获取结果");
     if ((res = (Response) responseMap.get(req.getRequestId())) != null) {
       System.out.println("关闭连接");
       responseMap.remove(req.getRequestId());
       f.channel().close().sync();
       System.out.println("关闭通道");
     }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      eventLoopGroup.shutdownGracefully();
    }
    objMap.remove(req.getRequestId());
    System.out.println("完成关闭");
    return res;
  }
}
