package com.example.myclient;

import com.example.myclient.service.NettyClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyclientApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(MyclientApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    NettyClient.init();
  }
}
