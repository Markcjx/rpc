package com.example.server.handler;

import com.example.server.proto.Request;
import com.example.server.proto.Response;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class printHandler extends ChannelInitializer<SocketChannel> {
  Map<String,Object> handlerMap = new HashMap<>();
  AtomicInteger num ;
  public printHandler(Map<String, Object> handlerMap, AtomicInteger num) {
    this.handlerMap = handlerMap;
    this.num = num;
  }

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline cp = socketChannel.pipeline();
    cp.addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
    cp.addLast(new Decoder(Request.class));
    cp.addLast(new Encoder(Response.class));
    cp.addLast(new serviceHandler(handlerMap,num));
  }
}
