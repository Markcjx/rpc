package com.example.myclient;

import com.example.myclient.service.RpcProxy;
import com.example.server.proto.Response;
import com.example.server.remoteService.hello;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyclientApplicationTests {
  @Autowired
  private RpcProxy rpcProxy;
  @Test
  public void contextLoads() {
  }
  @Test
  public void HelloTest(){
    hello Hello = rpcProxy.creat(hello.class);
    Object result = Hello.sayHello("mark");
    System.out.println("*******"+result);
  }


}
