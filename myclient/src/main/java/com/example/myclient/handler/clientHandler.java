package com.example.myclient.handler;

import com.example.server.handler.Decoder;
import com.example.server.handler.Encoder;
import com.example.server.proto.Request;
import com.example.server.proto.Response;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import com.example.server.proto.UserInfo;
import java.util.concurrent.TimeUnit;

public class clientHandler extends ChannelInitializer<SocketChannel> {

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline ch = socketChannel.pipeline();
    ch.addLast(new IdleStateHandler(4,0,0, TimeUnit.SECONDS));
    ch.addLast(new Encoder(Request.class));
    ch.addLast(new Decoder(Response.class));
    ch.addLast(new serviceHandler());

  }
}
