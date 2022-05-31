#### 这个项目是flink的学习项目
env,source....
#### wsl运行的kafka,window10连接
将wsl的kafka的server.properties的listeners=PLAINTEXT://localhost:9092的localhost替换为wsl的ip,ip用"ip addr|grep eth0"获取,
注意consumer的bootstrap.servers也要替换为对应的ip
##### SourceTest
**五种数据源**
1. file
2. elements
3. collections
4. socket
5. kafka
##### ClickSource
自定义数据源,给了几条数据,然后每秒随机选取,SourceCustomTest是测试类