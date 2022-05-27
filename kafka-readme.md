#### 1.启动zk服务
```
// 如果需要切换盘符,直接d:
cd D:\software\dev_software\kafka_2.11-1.1.1\bin\windows
zookeeper-server-start.bat ..\..\config\zookeeper.properties
```
#### 2.启动kafka服务
```
cd D:\software\dev_software\kafka_2.11-1.1.1\bin\windows
kafka-server-start.bat ..\..\config\server.properties
```

#### 3.创建一个topic
topic就像一个file system里的folder,message就是folder里的file
```
cd D:\software\dev_software\kafka_2.11-1.1.1\bin\windows
// 对于低于2.2版本的kafka, --bootstrap-server会报错,使用下面命令替换
// 参考 https://stackoverflow.com/questions/55494988/why-is-kafka-not-creating-a-topic-bootstrap-server-is-not-a-recognized-option
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic quickstart-events
```

####  4.查看一个topic
```
cd D:\software\dev_software\kafka_2.11-1.1.1\bin\windows
kafka-topics.bat --describe --topic quickstart-events --zookeeper localhost:2181
```
--zookeeper localhost:2181

#### 5.列出topics
```
kafka-topics.bat --list --zookeeper localhost:2181
```
#### 6.发送event到指定topic
```
kafka-console-producer.bat  --broker-list localhost:9092 --topic quickstart-events
```

#### 7.start a consumer,会持续消费
```
kafka-console-consumer.bat --zookeeper localhost:2181 --topic quickstart-events --from-beginning
```
可以在另一个cmd终端打开producer持续写入数据, consumer终端这边会持续消费, 而且可以打开多consumer同时消费
#### 创建包含多个broker节点的集群
参考：https://kafka.apache.org/081/documentation.html#quickstart

#### 8.使用kafka connect将数据import、export为event stream
kafka connector可以使得kafka和database、file、等系统交互，互相传输数据

##### 使用kafka connector在kafka topic和file之间互传数据
(1)编辑 “config/connect-standalone.properties" 文件,添加或修改 pulgin.path项为下列
``` 
plugin.path=D:/software/dev_software/kafka_2.11-1.1.1/libs/connect-file-1.1.1.jar
```
(2)创建文件夹和source、sink文件  
目录：D:\software\dev_software\kafka_2.11-1.1.1\test  
文件：  
D:\software\dev_software\kafka_2.11-1.1.1\test\test.source.txt  
D:\software\dev_software\kafka_2.11-1.1.1\test\test.sink.txt  
在D:\software\dev_software\kafka_2.11-1.1.1\test\test.source.txt里写入"first, I love you forever"  
(3)修改source、sink配置文件
文件：D:\software\dev_software\kafka_2.11-1.1.1\config\connect-file-sink.properties内容
``` 
name=local-file-sink
connector.class=FileStreamSink
tasks.max=1
file=D:/software/dev_software/kafka_2.11-1.1.1/test/test.sink.txt
topics=quickstart-events
```
文件：D:\software\dev_software\kafka_2.11-1.1.1\config\connect-file-source.properties内容
``` 
name=local-file-source
connector.class=FileStreamSource
tasks.max=1
file=D:/software/dev_software/kafka_2.11-1.1.1/test/test.source.txt
topic=quickstart-events
```
(4)启动命令   
``` 
cd D:\software\dev_software\kafka_2.11-1.1.1\bin\windows
connect-standalone.bat ../../config/connect-standalone.properties ../../config/connect-file-source.properties ../../config/connect-file-sink.properties
```
上面的命令创建了两个connectors,第一个从文件test.source.txt读取数据并发送到kafka topic "quickstart-events",第二个从topic "quickstart-events" 读数据然后写入到file "test.sink.txt"

#### 9.创建java应用producer、consumer
