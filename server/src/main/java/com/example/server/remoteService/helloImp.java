package com.example.server.remoteService;
@RpcService(hello.class)
public class helloImp implements hello {

  @Override
  public String sayHello(String name) {
    return "hello "+name;
  }
}
