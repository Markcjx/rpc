package com.example.myclient.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import com.example.server.proto.UserInfo;
import java.util.concurrent.TimeUnit;

public class clientHandler extends ChannelInitializer<SocketChannel> {

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline ch = socketChannel.pipeline();
    ch.addLast(new IdleStateHandler(4,0,0, TimeUnit.SECONDS));
    ch.addLast(new ProtobufVarint32FrameDecoder());
    ch.addLast(new ProtobufDecoder(UserInfo.UserMsg.getDefaultInstance()));
    ch.addLast(new ProtobufVarint32LengthFieldPrepender());
    ch.addLast(new ProtobufEncoder());
    ch.addLast(new serviceHandler());

  }
}
