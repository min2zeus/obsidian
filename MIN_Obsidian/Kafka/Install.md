


- 기본 java (1.8 이상 필요)
yum -y install java-1.8.0-openjdk

### zookeeper 설치
- 설치 파일 다운
yum -y install wget
wget http://apache.mirror.cdnetworks.com/zookeeper/zookeeper-3.9.0/apache-zookeeper-3.9.0-bin.tar.gz
tar -zxvf apache-zookeeper-3.9.0-bin.tar.gz

- 디렉토리 링크
ln -s apache-zookeeper-3.9.0-bin zookeeper

- 데이터 영역 구성 ( 2. 3 번은 다른 서버에 구성)
mkdir data
echo 1 > myid
echo 2 > myid
echo 3 > myid


- config 수정
AdminServer 포트 8080 인데 이미 사용 중이라 28080으로 변경 (zoo.cfg)
```
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/home/mqm/min/zookeeper/data
clientPort=2181

admin.serverPort=28080  


server.1=1-110-TEST-CENT-JYS:2888:3888  (앙상블 포트)


#server.2=148-MFT1-KJW:2888:3888    2번 추가
#server.3=149-MFT2-KJW:2888:3888    3번 추가

추가하면 연결오류 계속 발생
2023-08-28 14:48:18,377 [myid:localhost:2181] - WARN  [main-SendThread(localhost:2181):o.a.z.ClientCnxn$SendThread@1285] - Session 0x0 for server localhost/0:0:0:0:0:0:0:1:2181, Closing socket connection. Attempting reconnect except it is a SessionExpiredException.
org.apache.zookeeper.ClientCnxn$EndOfStreamException: Unable to read additional data from server sessionid 0x0, likely server has closed socket
        at org.apache.zookeeper.ClientCnxnSocketNIO.doIO(ClientCnxnSocketNIO.java:77)
        at org.apache.zookeeper.ClientCnxnSocketNIO.doTransport(ClientCnxnSocketNIO.java:350)
        at org.apache.zookeeper.ClientCnxn$SendThread.run(ClientCnxn.java:1274)

자기 server.1 만 오픈 하면 zkCli.sh 시 정상 연결
새로 만들어진 건 구조가 다름 (하위에 zookeeper 만 있음)
data 폴더를 싱크 맞춰줘야하나 ? 주키퍼 3개 만들어서 하는건 뭔가 맵핑이 필요할 수도 ..
앙상블 포트 config는 2888, 3888 로 해야 오류 발생하지 않응
앙상블 포트 올라가는 기준이 무엇인가 ? 148번에 3888 포트만 올라가있음
1번 서버에서 148번에 올라간 3888 포트로 접속하면 정상연결되며, 148번 서버에서도
ls /  로 확인 시 구성 모두 가져옴 (3번도 갑자기 생김)

** 정리
위의 연결 에러가 config 에 server 추가해서 발생한게 아니라 server 앙상블 정보를 추가했는데 3개중에 2개가 죽어있으면 발생
주키퍼는 앙상블 구조시 3개중에 2개가 살아있어야 정상 처리 가능

2888 포트가 3개의 주키퍼 앙상블중 리더로 계속 옮겨가는것같이 보임 (3번에 있던 주키퍼를 내리니까 2888 포트가 2번으로 옮겨감)

```

- 기동 및 확인
/home/mqm/min/zookeeper/bin/zkServer.sh start
netstat -nltp | grep 2181

- kafka 접속 및 확인
/home/mqm/min/zookeeper/bin/zkCli.sh

zk: localhost:2181(CONNECTED) 0
ls /
[admin, brokers, cluster, config, consumers, controller, controller_epoch, feature, isr_change_notification, latest_producer_id_block, log_dir_event_notification, zookeeper]

ls /brokers/ids
ls /brokers/topics

deleteall /min-kafka-test
deleteall "/ min-kafka"

### kafka 설치

- 설치 파일 다운
wget http://apache.mirror.cdnetworks.com/kafka/3.4.1/kafka_2.12-3.4.1.tgz
tar -zxvf kafka_2.12-3.4.1.tgz

- 디렉토리 링크
ln -s kafka_2.12-3.4.1 kafka
 
- 기동 / 중지 방법
/home/mqm/min/kafka/bin/kafka-server-start.sh /home/mqm/min/kafka/config/server.properties &

(오류) /home/mqm/min/kafka/bin/kafka-server-start.sh /home/mqm/min/kafka/config/server.properties -daemon

/home/mqm/min/kafka/bin/kafka-server-start.sh -daemon /home/mqm/min/kafka/config/server.properties

``데몬 옵션을 앞에 설정 해야 하며, & 방법 보다 -daemon 으로 해야 죽지 않고 계속 프로세스 기동됨

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

/home/mqm/min/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --replication-factor 3 --partitions 1 --topic rep3-topic --create

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


##### 2) java 를 통한 producer

https://github.com/onlybooks/kafka/blob/master/chapter4/producer.java
파일 다운로드

eclipse 에서 소스 import 후 수정
추가로 kafka 라이브러리 java build path 에 추가
![[Pasted image 20230823180532.png]]
```
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaBookProducer1 {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "1-110-TEST-CENT-JYS:9092,148-MFT1-KJW:9092,149-MFT2-KJW:9092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    Producer<String, String> producer = new KafkaProducer<>(props);
    producer.send(new ProducerRecord<String, String>("rep3-topic", "Apache Kafka is a distributed streaming platform"));
    producer.close();
  }
}

1. bootstrap.servers 정보 수정
2. bootstrap.servers 토픽 / 메시지 수정 테스트

```

eclipse 에서 실행시, class 명 맞게 변경 후 실행 (KafkaBookProducer1)

추가로 eclipse 실행하는 windows 환경에서 동일하게 
C:\Windows\System32\drivers\etc\hosts 파일에 호스트네임 등록

rep3-topic 토픽에 "Apache Kafka is a distributed streaming platform" 메시지 put

- consumer 를 통해 메시지 확인
[mqm@148-MFT1-KJW kafka]$ /home/mqm/min/kafka/bin/kafka-console-consumer.sh --bootstrap-server 1-110-TEST-CENT-JYS:9092 --topic rep3-topic --from-beginning
OpenJDK 64-Bit Server VM warning: If the number of processors is expected to increase from one, then you should configure the number of parallel GC threads appropriately using -XX:ParallelGCThreads=N
rep3-topic TEST MESSAGE1
TEST2
TEST33
broker-list 149-MFT2-KJW:9092 TESTMESSAGE11
**Apache Kafka is a distributed streaming platform**


##### 3) python 를 통한 producer

https://pypi.org/project/kafka-python/#files
kafka-python-2.0.2.tar.gz  파일 다운로드

setup.py 파일  IDLE 에서 실행
인자로 install 넣어서 실행 (SHIFT+F5)

```
파이썬-카프카 라이브러리 설치
	pip install kafka-python

이런 방식으로도 사용할수 있는듯 
```

다되면 producer.py 파일 다운로드
https://github.com/onlybooks/kafka/blob/master/chapter4/producer.py

```
from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers='1-110-TEST-CENT-JYS:9092,148-MFT1-KJW:9092,149-MFT2-KJW:9092')

producer.send('rep3-topic', 'Apache Kafka is a distributed streaming platform - Python TEST'.encode('utf-8'))

1. bootstrap_servers 정보 변경
2. 토픽 / 메시지 변경 
3. 메시지에 .encode('utf-8') 추가 후 실행
   (추가하지 않으면, 아래와 같은 AssertionError 발생)
   (해당 내용은 버전 이슈이며 python 3 에서 encode 추가 필요)

Traceback (most recent call last): File "D:/phantom/source-python/main.py", line 4, in <module> producer.send('test', 'Hello World') File "C:\Users\user\anaconda3\lib\site-packages\kafka\producer\kafka.py", line 585, in send assert type(value_bytes) in (bytes, bytearray, memoryview, type(None)) AssertionError

```

- consumer 를 통해 메시지 확인
[mqm@148-MFT1-KJW kafka]$ /home/mqm/min/kafka/bin/kafka-console-consumer.sh --bootstrap-server 1-110-TEST-CENT-JYS:9092 --topic rep3-topic --from-beginning
OpenJDK 64-Bit Server VM warning: If the number of processors is expected to increase from one, then you should configure the number of parallel GC threads appropriately using -XX:ParallelGCThreads=N
rep3-topic TEST MESSAGE1
TEST2
TEST33
broker-list 149-MFT2-KJW:9092 TESTMESSAGE11
Apache Kafka is a distributed streaming platform
Apache Kafka is a distributed streaming platform - JAVA TEST MESSAGE
**Apache Kafka is a distributed streaming platform - Python TEST**


##### 4) 메시지 전송 방식
1) 메시지 보내고 확인하지 않음
2) 동기 전송
3) 비동기 전송

그에 따른 옵션 추가 후 전송
https://github.com/onlybooks/kafka/blob/master/chapter4/ 에서 아래 파일 다운

producer-option.java
```
import org.apache.kafka.clients.producer.KafkaProducer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerOption {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "1-110-TEST-CENT-JYS:9092,148-MFT1-KJW:9092,149-MFT2-KJW:9092");
    props.put("acks", "1");
    props.put("compression.type", "gzip");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    Producer<String, String> producer = new KafkaProducer<>(props);
    producer.send(new ProducerRecord<String, String>("rep3-topic", "Apache Kafka is a distributed streaming platform - JAVA OPTION"));
    producer.close();
  }
}
```

producer-option.py
```
from kafka import KafkaProducer

producer = KafkaProducer(acks=1, compression_type='gzip', bootstrap_servers='1-110-TEST-CENT-JYS:9092,148-MFT1-KJW:9092,149-MFT2-KJW:9092')

producer.send('rep3-topic', 'Apache Kafka is a distributed streaming platform - Python OPTION'.encode('utf-8'))

```

##### producer 에서 자주 사용하는 옵션

1, acks
###### ACK=0
- Ack=0으로 설정하면 메세지를 보내기만 한다.
- replica를 아무리 적용한다고 해도 Producer에서는 확인 할 수 없다.
- 빠르게 데이터를 전송해야할 때만 사용하는 옵션이다
- 메시지 손실 가능성 높음
###### ACK=1
- 리더가 받았는지 확인하는 옵션이다
- replica가 적용되었는지 확인 할 수 없다
- 메시지 손실 가능성 낮으나 메시지를 리더 브로커가 받고 프로듀서에게 ack 를 보낸 다음 바로 종료가 되면 다른 팔로우 브로커에서 해당 메시지를 가져가지 못하는 경우가 발생할 수 있어 메시지 손실 가능성이 있음
###### ACK=-1 or ACK=all
- 리더에 메세지가 전송되고 replica가 다 적용된 후에 돌려 받는 형식의 옵션이다.
- 리더가 장애가 발생하였어도 복제가 끝난 다음에 ack를 돌려주기 때문에 메세지가 보장이 된다.
- 이 경우 브로커에서도 min.insync.replicas 옵션 설정이 필요
- 브로커 3대의 경우 메시지가 손실 되지 않는 가장 이상적인 설정
1) ACK=all
2) min.insync.replicas=2
3) 토픽의 리플리케이션 팩터=3
`` min.insync.replicas=3 으로 하게 되면 팔로워 브로커가 하나라도 죽으면 replicas 3 을 만족시키지 못해 전체 브로커에 장애로 연결 될 수 있음



5. compression
###### Messge Compression(메세지 압축)
- 메세지를 batch로 처리 했을 때 사용하게 된다.
- batch처리 할때 꼭 사용하는 것이 아니라 처리량(메세지 보내는 횟수)이 낮은데 메세지의 용량이 클 때 주로 사용한다.
- 활성화 하는 법은 compression.type이 default값이 none인데 이 부분을 수정해 주면 활성화 된다. compression.type의 값은 Gzip, snappy, Lz4, Zstd가 있다.



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

