# rpc
a simple rpc framework depends on netty and zk. 

#### 使用
1. 需要在本地安装zookeeper并新建一个registry目录。
2. 安装依赖。
3. 在server端rpcService中新增需要的service
4. 启动server并在client中的test进行调用。

#### 待解决问题

1、压力测试时，采用单线程1000并发，如果每次请求不等待10ms会导致OOM,原因是进程之前打开的连接还未关闭，又继续申请新的链接，导致fd超出上限。
