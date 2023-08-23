


- java (1.8 이상 필요)
yum -y install java-1.8.0-openjdk

### zookeeper
- 설치 파일 다운
yum -y install wget
wget http://apache.mirror.cdnetworks.com/zookeeper/zookeeper-3.9.0/apache-zookeeper-3.9.0-bin.tar.gz
tar -zxvf apache-zookeeper-3.9.0-bin.tar.gz


- 디렉토리 링크
ln -s apache-zookeeper-3.9.0-bin zookeeper

- 데이터 영역 구성
mkdir data
echo 1 > myid
echo 2 > myid
echo 3 > myid



- config 수정
AdminServer 포트 8080 인데 28080으로 변경 (zoo.cfg)
```
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/home/mqm/min/zookeeper/data
clientPort=2181

admin.serverPort=28080  


server.1=1-110-TEST-CENT-JYS:2888:3888  (앙상블)
```

- 기동 및 확인
zkServer.sh start
netstat -nltp | grep 2181

- kafka 접속 및 확인
zkCli.sh

zk: localhost:2181(CONNECTED) 0
ls /
[admin, brokers, cluster, config, consumers, controller, controller_epoch, feature, isr_change_notification, latest_producer_id_block, log_dir_event_notification, zookeeper]

ls /brokers/ids
ls /brokers/topics

deleteall /min-kafka-test
deleteall "/ min-kafka"

### kafka

- 설치 파일 다운
wget http://apache.mirror.cdnetworks.com/kafka/3.4.1/kafka_2.12-3.4.1.tgz
tar -zxvf kafka_2.12-3.4.1.tgz

ln -s kafka_2.12-3.4.1 kafka
 
- 기동 방법
/home/mqm/min/kafka/bin/kafka-server-start.sh /home/mqm/min/kafka/config/server.properties &

(오류) /home/mqm/min/kafka/bin/kafka-server-start.sh /home/mqm/min/kafka/config/server.properties -daemon

/home/mqm/min/kafka/bin/kafka-server-start.sh -daemon /home/mqm/min/kafka/config/server.properties


/home/mqm/min/kafka/bin/kafka-server-stop.sh
netstat -nltp | grep 9092


- 토픽 생성 
(오류) /home/mqm/min/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic min-topic --create

/home/mqm/min/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic min-topic --create

- 출력
OpenJDK 64-Bit Server VM warning: If the number of processors is expected to increase from one, then you should configure the number of parallel GC threads appropriately using -XX:ParallelGCThreads=N
[2023-08-10 13:58:44,957] INFO Creating topic min-topic with configuration {} and initial partition assignment Map(0 -> ArrayBuffer(1)) (kafka.zk.AdminZkClient)
[2023-08-10 13:58:45,502] INFO [ReplicaFetcherManager on broker 1] Removed fetcher for partitions Set(min-topic-0) (kafka.server.ReplicaFetcherManager)
[2023-08-10 13:58:45,767] INFO [LogLoader partition=min-topic-0, dir=/home/mqm/min/kafka/data1] Loading producer state till offset 0 with message format version 2 (kafka.log.UnifiedLog$)
[2023-08-10 13:58:45,845] INFO Created log for partition min-topic-0 in /home/mqm/min/kafka/data1/min-topic-0 with properties {} (kafka.log.LogManager)
[2023-08-10 13:58:45,849] INFO [Partition min-topic-0 broker=1] No checkpointed highwatermark is found for partition min-topic-0 (kafka.cluster.Partition)
[2023-08-10 13:58:45,850] INFO [Partition min-topic-0 broker=1] Log loaded for partition min-topic-0 with initial high watermark 0 (kafka.cluster.Partition)
Created topic min-topic.



- 토픽 삭제
/home/mqm/min/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic min-topic --delete

/home/mqm/min/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic rep3-topic --delete

- 토픽 확인
/home/mqm/min/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
/home/mqm/min/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe

카프카 3개 추가후 

- 퍼블리싱
/home/mqm/min/kafka/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic min-topic

> 메시지 입력 ~

/home/mqm/min/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic min-topic --from-beginning

console-consumer.sh 작업을 하면 

ls /brokers/topics/__consumer_offsets 이 생김

``(1개 브로커에서는 생겼는데 replication-factor 3 으로 설정한거에서는 안만들어짐)

##### 1) kafka broker 3개 추가

각 서버 별 /home/mqm/min/kafka/config/server.properties  수정 후 기동
broker.id=1
broker.id=2
broker.id=3

같은 kafka 브로커로 만들게 되면--bootstrap-server 설정을 replication-factor 어디서든 해도 정보 정상 처리

/home/mqm/min/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --replication-factor 3 --partitions 1 --topic rep33-topic --create

같은 kafka  브로커로 만들고 토픽 사용하려고 하면 
/etc/hosts  에 각 서버 호스트네임 넣어줘야 함
```
[mqm@149-MFT2-KJW ~]$ cat /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
10.10.1.149     149-MFT2-KJW
10.10.1.148     148-MFT1-KJW
10.10.1.110     1-110-TEST-CENT-JYS
```

안 넣어주면 아래 에러 발생
java.net.UnknownHostException: 149-MFT2-KJW: 이름 혹은 서비스를 알 수 없습니다

리더 replication-factor 를 종료하면 그다음으로 넘어가고 넘어가는 순서는 ISR 순서로 보임
Topic: rep3-topic       TopicId: HUqSTXOFRQ2XZ2uCFYGiYA PartitionCount: 1       ReplicationFactor: 3    Configs: 
        Topic: rep3-topic       Partition: 0    Leader: 2       Replicas: 3,2,1 Isr: 2,1
Topic: rep33-topic      TopicId: GLbTNuKqRoaBIBLSUGjW0g PartitionCount: 1       ReplicationFactor: 3    Configs: 
        Topic: rep33-topic      Partition: 0    Leader: 1       Replicas: 3,1,2 Isr: 1,2


다시 기동하면  ISR 가장 마지막에 붙음

Topic: rep3-topic       TopicId: HUqSTXOFRQ2XZ2uCFYGiYA PartitionCount: 1       ReplicationFactor: 3    Configs: 
        Topic: rep3-topic       Partition: 0    Leader: 2       Replicas: 3,2,1 Isr: 2,1,3
Topic: rep33-topic      TopicId: GLbTNuKqRoaBIBLSUGjW0g PartitionCount: 1       ReplicationFactor: 3    Configs: 
        Topic: rep33-topic      Partition: 0    Leader: 1       Replicas: 3,1,2 Isr: 1,2,3

- 퍼블리싱 테스트
/home/mqm/min/kafka/bin/kafka-console-producer.sh --broker-list 1-110-TEST-CENT-JYS:9092, 148-MFT1-KJW:9092, 149-MFT2-KJW:9092 --topic rep3-topic

> 메시지 입력 ~

/home/mqm/min/kafka/bin/kafka-console-consumer.sh --bootstrap-server 1-110-TEST-CENT-JYS:9092, 148-MFT1-KJW:9092, 149-MFT2-KJW:9092 --topic rep3-topic --from-beginning

- 정상 리턴 확인
rep3-topic TEST MESSAGE1
TEST2
TEST33

/home/mqm/min/kafka/bin/kafka-console-producer.sh --broker-list 149-MFT2-KJW:9092 --topic rep3-topic
(특정 brokerlist 만 추가해도 consumer.sh  로 메세지 확인하면 다른 broker 에서도 동일한 메시지 확인 가능, ISR)

/home/mqm/min/kafka/bin/kafka-console-consumer.sh --bootstrap-server 1-110-TEST-CENT-JYS:9092 --topic rep3-topic --from-beginning






### trouble shooting

1) Error: Could not find or load main class org.apache.zookeeper.server.quorum.QuorumPeerMain

> zookeeper bin .tar.gz 사용 (apache-zookeeper-3.9.0-bin)

2) ERROR [main:o.a.z.s.ZooKeeperServerMain@86] - Unable to start AdminServer, exiting abnormally org.apache.zookeeper.server.admin.AdminServer$AdminServerException: Problem starting AdminServer on address 0.0.0.0, port 8080 and command URL /commands

> /home/mqm/min/kafka/zookeeper/conf/zoo.cfg 파일에 admin.serverPort=28080 추가


3) [mqm@1-110-TEST-CENT-JYS:/home/mqm/min/kafka/config] /home/mqm/min/kafka/bin/kafka-server-start.sh /home/mqm/min/kafka/config/server.properties -daemon
OpenJDK 64-Bit Server VM warning: If the number of processors is expected to increase from one, then you should configure the number of parallel GC threads appropriately using -XX:ParallelGCThreads=N
[2023-08-09 14:16:08,356] INFO Registered kafka:type=kafka.Log4jController MBean (kafka.utils.Log4jControllerRegistration$)
[2023-08-09 14:16:09,014] ERROR Exiting Kafka due to fatal exception (kafka.Kafka$)
joptsimple.UnrecognizedOptionException: d is not a recognized option
        at joptsimple.OptionException.unrecognizedOption(OptionException.java:108)
        at joptsimple.OptionParser.validateOptionCharacters(OptionParser.java:633)
        at joptsimple.OptionParser.handleShortOptionCluster(OptionParser.java:528)
        at joptsimple.OptionParser.handleShortOptionToken(OptionParser.java:523)
        at joptsimple.OptionParserState$2.handleArgument(OptionParserState.java:59)
        at joptsimple.OptionParser.parse(OptionParser.java:396)
        at kafka.Kafka$.getPropsFromArgs(Kafka.scala:55)
        at kafka.Kafka$.main(Kafka.scala:92)
        at kafka.Kafka.main(Kafka.scala)

Exception in thread "main" joptsimple.UnrecognizedOptionException: zookeeper is not a recognized option

> Kafka의 최신 버전(2.2+)에서는 더 이상 ZooKeeper 연결 문자열이 필요하지 않습니다.

`--zookeeper localhost:2181`

주제를 생성하는 동안 다음 예외가 발생합니다.

> 스레드 "main" joptsimple.UnrecognizedOptionException의 예외: 사육사는 인식된 옵션이 아닙니다.

대신 Kafka Broker `--bootstrap-server localhost:9092`연결 문자열을 추가합니다.

