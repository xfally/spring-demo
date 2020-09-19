# 如何基于docker搭建mongodb集群（复制集）

1. 参考docker-compose.yml，配置3节点复制集；

2. 创建keyfile，以便mongo程序能够在3个节点之间通信；

在宿主机上执行（比如本工程根目录）：

```
openssl rand -base64 756 > db/mongo/keyfile
chmod 400 mongodb.key
```

然后将生成的keyfile文件`cp`到3个节点的data目录，参考docker-compose.yml文件中的配置。

3. 执行`docker-compose up -d`启动3个节点；

4. 登录其中一个节点，比如primary节点，初始化集群；

```
### 宿主机执行（容器名参考docker-compose.yml中的配置）
docker exec -it mongo-primary-27017 /bin/bash

### 登录容器中，执行（用户名和密码参考docker-compose.yml中的配置）
mongo -u root -p 123456

### 打开mongo控制台，执行以下命令：
# 创建配置变量
var cfg = {
  _id: "rs0",
  members: [
    { _id : 0, host : "mongo-primary-server:27017", "priority" : 2 },
    { _id : 1, host : "mongo-arbiter-server:27017", "arbiterOnly": true },
    { _id : 2, host : "mongo-secondary-server:27017", "priority" : 1 }
  ]
};
# 如果想从宿主机访问，且没有为docker配置宿主机的DNS，则这里最好用ip
var cfg = {
  _id: "rs0",
  members: [
    { _id : 0, host : "192.168.101.150:27017", "priority" : 2 },
    { _id : 1, host : "192.168.101.150:27018", "arbiterOnly": true },
    { _id : 2, host : "192.168.101.150:27019", "priority" : 1 }
  ]
};
# 初始化
rs.initiate(cfg);
```

> 注意：
> 1. 如果想从宿主机访问mongo服务，如果上面的cfg不配置`priority`，则会自动选举主节点。

执行后，提示`{ "ok" : 1 }`，则初始化成功。可以进一步执行`rs.status()`查看配置。

5. 使用mongo客户端或第三方工具（如NoSQLBooster）连接集群测试。
