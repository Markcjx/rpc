package com.example.server.handler;

import com.example.server.proto.Request;
import com.example.server.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;
public class Encoder extends MessageToByteEncoder {
  public Class<?> genericClass ;
  public Encoder(Class<?> genericClass) {
    this.genericClass = genericClass;
  }

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf)
      throws Exception {
    System.out.println("进入encoder");
    if (genericClass.isInstance(o)) {
      System.out.println("encode成功");
      byte[] data = SerializationUtil.serialize(o);
      byteBuf.writeInt(data.length);
      byteBuf.writeBytes(data);
    }
  }
}
