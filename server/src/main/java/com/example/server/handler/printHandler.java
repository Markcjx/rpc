package com.example.server.handler;

import com.example.server.proto.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;

public class printHandler extends ChannelInitializer<SocketChannel> {

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline cp = socketChannel.pipeline();
    cp.addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
    cp.addLast(new ProtobufVarint32FrameDecoder());
    cp.addLast(new ProtobufDecoder(UserInfo.UserMsg.getDefaultInstance()));
    cp.addLast(new ProtobufVarint32LengthFieldPrepender());
    cp.addLast(new ProtobufEncoder());
    cp.addLast(new soutPrintHandler());
  }
}
