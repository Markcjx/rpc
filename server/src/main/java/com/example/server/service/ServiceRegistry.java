package com.example.server.service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceRegistry {

  private CountDownLatch latch = new CountDownLatch(1);
  @Value("netty.registyAddress")
  private String registryAddress;

  public void register(String data) {
    if (data != null) {
      ZooKeeper zk = connect();
      if (zk != null) {
        createNode(zk,data);
      }
    }
  }

  public ZooKeeper connect() {
    ZooKeeper zk = null;
    try {
      zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
          if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
          }
        }
      });
      latch.await();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return zk;
  }

  private void createNode(ZooKeeper zk, String data) {
    try {
      byte[] bytes = data.getBytes();
      String path = zk
          .create(Constant.ZK_DATA_PATH, bytes, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
      System.out.println("create zookeeper node " + path + " =>" + data);
    } catch (InterruptedException | KeeperException e) {
      e.printStackTrace();
    }
  }
}
