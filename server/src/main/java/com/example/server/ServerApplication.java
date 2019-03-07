package com.example.server;

import com.example.server.service.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class    ServerApplication implements CommandLineRunner {
  @Autowired
  private NettyServer nettyServer;
  public static void main(String[] args) {

    SpringApplication.run(ServerApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
   nettyServer.run();
  }
}
