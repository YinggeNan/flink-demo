#### 这个项目是flink的学习项目
env,source....
#### wsl运行的kafka,window10连接
将wsl的kafka的server.properties的listeners=PLAINTEXT://localhost:9092的localhost替换为wsl的ip,ip用"ip addr|grep eth0"获取,
注意consumer的bootstrap.serverse也要替换为对应的ip