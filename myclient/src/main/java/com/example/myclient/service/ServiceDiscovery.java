package com.example.myclient.service;

import com.example.server.service.Constant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceDiscovery {
  private CountDownLatch latch ;
  private volatile List<String> dataList ;

  @Value("${netty.registryAddress}")
  private String registryAddress = "127.0.0.1:2181";

  public ServiceDiscovery() {
    latch = new CountDownLatch(1);
    dataList = new ArrayList<>();
    ZooKeeper zk = connectServer();
    if (zk != null) {
        watchNode(zk);
    }
  }

  public String discover() {
    String data = null;
    int size = dataList.size();
    System.out.println("dataList is "+dataList.toString());
    if (size > 0){
      if(size == 1){
        data = dataList.get(0);

      }
      else{
        data = dataList.get(ThreadLocalRandom.current().nextInt(size));
      }
    }
    return data;
  }

  private ZooKeeper connectServer() {
    ZooKeeper zk = null;
    try {
      System.out.println("*************"+registryAddress);
      zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
        @Override
        public void process(WatchedEvent event) {
          if (event.getState() == Event.KeeperState.SyncConnected) {
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

  private void watchNode(ZooKeeper zk) {
    try {
      List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
          if (watchedEvent.getType() == EventType.NodeChildrenChanged) {
            watchNode(zk);
          }
        }
      });
      List<String> dataList = new ArrayList<>();
      for (String node : nodeList) {
        byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);
        dataList.add(new String(bytes));
      }
      this.dataList = dataList;
    } catch (KeeperException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
