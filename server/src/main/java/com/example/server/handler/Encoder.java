package com.example.server.handler;

import com.example.server.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder {
  public Class<?> genericClass ;
  public Encoder(Class<?> genericClass) {
    this.genericClass = genericClass;
  }

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf)
      throws Exception {
    if (genericClass.isInstance(o)) {
      byte[] data = SerializationUtil.serialize(o);
      byteBuf.writeInt(data.length);
      byteBuf.writeBytes(data);
    }
  }
}
