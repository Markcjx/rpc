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

public class printHandler extends ChannelInitializer<SocketChannel> {
  Map<String,Object> handlerMap = new HashMap<>();

  public printHandler(Map<String, Object> handlerMap) {
    this.handlerMap = handlerMap;
  }

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline cp = socketChannel.pipeline();
    cp.addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
    cp.addLast(new Decoder(Request.class));
    cp.addLast(new Encoder(Response.class));
    cp.addLast(new serviceHandler(handlerMap));
  }
}
