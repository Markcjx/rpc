package com.example.myclient.handler;

import com.example.server.proto.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class serviceHandler extends SimpleChannelInboundHandler {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("连接服务端");
    UserInfo.UserMsg userMsg = UserInfo.UserMsg.newBuilder().setAge(1).setId(1).setName("lili").setState(1).build();
    ctx.writeAndFlush(userMsg);
    super.channelActive(ctx);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o)
      throws Exception {
    try {

      // 得到protobuf的数据
      UserInfo.UserMsg userMsg = (UserInfo.UserMsg) o;
      // 进行相应的业务处理。。。
      // 这里就从简了，只是打印而已
      System.out.println(
          "客户端接受到的用户信息。编号:" + userMsg.getId() + ",姓名:" + userMsg.getName() + ",年龄:" + userMsg.getAge());

      // 这里返回一个已经接受到数据的状态
      UserInfo.UserMsg.Builder userState =  UserInfo.UserMsg.newBuilder().setState(1);
      channelHandlerContext.writeAndFlush(userState);
      System.out.println("成功发送给服务端!");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      ReferenceCountUtil.release(o);
    }


  }
}
