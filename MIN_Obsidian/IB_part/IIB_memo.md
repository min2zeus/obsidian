---
sticker: emoji//1f921
---
---
### AIX 브로커 시간 변경
AIX 6.1부터는 타임존 형식이 바뀌어서 발생하는 문제입니다.
저도 같은 문제를 겪은 적이 있어서, 
사실 전 루트 계정으로 /etc/environment 파일의 TZ 변수를 KORST-9로 바꾸어 해결했습니다만,

[aix61:root@/]vi /etc/environment
#TZ=Asia/Seoul
TZ=KORST-9

IBM 사이트에서는 아래와 같이 해결방법을 말하고 있습니다.

1. 브로커를 정지한 후, 다음 명령어를 실행합니다.
mqsichangebroker broker_name -c /opt/IBM/mqsi/7.0/xml4c/data

2, mqsi 프로파일에서 ICU_DATA 부분을 unset 해줍니다.
[mqm:mqsi@/home/mqsi]vi /opt/IBM/mqsi/7.0/bin/mqsiprofile
 
#ICU_DATA=${MQSI_FILEPATH}/xml4c/data${ICU_DATA:+":"${ICU_DATA}}
#export ICU_DATA

3. 브로커를 시작합니다.

자세한 내용은 아래를 참고해주세요

AIX 6.1 부터는 Olson timezone 형식(Country/City 형식)을 지원하고 있습니다.
AIX 6.1 환경에서 WMB의 Olson timezone 형식 지원을 위해서는 몇가지 설정이 필요합니다.

자세한 방법은 Technote 21441863에 설명되어 있습니다.

Technote 21441863: Olson timezone support for WebSphere Message Broker -https://www-304.ibm.com/support/docview.wss?uid=swg21441863
정리해보면,

WMB의 XML4C에서 사용되는 Olson timezone관련 환경변수(ICU_DATA)와 AIX 6.1 환경사이의 문제로 다음과 같이 설정하시면 됩니다.

1. ICU convert path를 각각의 Broker에 설정해줍니다.
mqsichangebroker broker_name -c /opt/IBM/mqsi/7.0/xml4c/data
2. mqsiprofile 스크립트에서 ICU_DATA 부분을 unset 해줍니다.
또는
Technote에 설명된 대로 $MQSI_FILEPATH\xml4c\lib 하위이 다음 Sym Link를 제거하셔도 됩니다.

libicudata.a
libicudata.so
libicui18n.a
libicui18n.so
libicuuc.a
libicuuc.so
libxml4c.a
libXML4CMessages.a
또는
Olson timezone 대신 기존의 tz timezone 포맷으로 KORST-9 로 설정하는 것도 방법이 될 수 있습니다.
위의 방법들을 테스트해보시고 환경에 맞는 방법을 선택하시면 되겠습니다.
현재 Broker에 설정된 Timezone 설정은 'mqsiservice {brk name} -t' 로 확인 가능합니다.

### Broker 실행그룹에 동일이름의 MessageFlow 가 여러개 Deploy 됐을때 삭제하는 방법
1) DataFlowEngine 의 uuid 값 확인
   ps -ef | grpe Da

2) MessageFlow 의 uuid 값 확인
   mqsireportproperties {Broker_Name} -e {DataFlowEngine_Name} -o AllMessageFlows -a
   ex) mqsireportproperties EAIBK -e TEST -o AllMessageFlows -a
       → 조회된 내용의 Label, uuid 값 확인

3) Broker DB 에서 MessageFlow 정보 확인
   db2 connect to {Broker_DB_Name}
   db2 select count(*) from BROKERRESOURCES where resourcename='{MessageFlow_uuid}' and 
       execgroupuuid='{DataFlowEngine_uuid}'
   ex) db2 select count(*) from BROKERRESOURCES where resourcename=' 9f1b79ef-2301-0000-0080-a40aa09ebd88'
       and execgroupuuid= x'2d7607591001000000809b8168cf6533'
   → 조회된 레코드 존재하는지 확인

   → 주의) '{DataFlowEngine_uuid}' 값은 다음과 같이 설정
      ps -ef | grep Da 확인한 TEST DataFlowEngine uuid 
      DataFlowEngine EAIBK 2d760759-1001-0000-0080-9b8168cf6533 TEST
     db2 where 절 : execgroupuuid=x'2d7607591001000000809b8168cf6533'

4) Broker DB 에서 MessageFlow 삭제
   db2 delete from BROKERRESOURCES where resourcename='{MessageFlow_uuid}'
       and execgroupuuid='{DataFlowEngine_uuid}'

### IIB 트레이스 수집

######### IIB TRACE 수집 전 준비 사항

/var/mqsi/common/log 경로에 여유 공간 확인

#1. USER LEVEL TRACE 수집 (log에 기록된 내용보다 많은 정보를 제공)
1. IIB TRACE 수집 시작
mqsichangetrace brokername -u -e egroup -l debug -r -c 50000  

2. 이슈 상황을 재연
3. IIB TRACE 수집 정지
mqsichangetrace brokername -u -e egroup -l none

4. 특정 구성 요소의 trace log 수집
mqsireadlog brokername -u -e egroup -f -o flowtrace.xml 

5. XML trace 파일 포맷 변경
mqsiformatlog -i flowtrace.xml -o userflowtrace.txt

6. 최종 산출물 수집
userflowtrace.txt

#2. SERVICE LEVEL TRACE 수집 (user level trace 보다 많은 정보를 제공)

1. IIB TRACE 수집 시작
mqsichangetrace brokername -t -e egroup -f flowname -l debug -r -c 100000 
mqsichangetrace HANAMBK -t -e HFG1000_HFG9000 -f HFG1000_HFG9000_SL_MAIN -l debug -r -c 1000000
mqsichangetrace HANAMBK -t -e PI -l debug -r -c 1000000

2. 이슈 상황을 재연
3. IIB TRACE 수집 정지
mqsichangetrace brokername -t -e egroup -l none 
mqsichangetrace HANAMBK -t -e HFG1000_HFG9000 -f HFG1000_HFG9000_SL_MAIN -l none 
mqsichangetrace HANAMBK -t -e PI -l none 

4. 특정 구성 요소의 trace log 수집
mqsireadlog brokername -t -e egroup -f -o flowtrace.xml 
mqsireadlog HANAMBK -t -e HFG1000_HFG9000 -f -o flowtrace.xml
mqsireadlog HANAMBK -t -e PI -f -o flowtrace6.xml

5. XML trace 파일 포맷 변경
mqsiformatlog -i flowtrace6.xml -o serviceflowtrace3.txt

6. 최종 산출물 수집
serviceflowtrace.txt

### MB AIX 설치
./setupaix -options sample-scripts/install.opt -silent
./setupaix -options sample-scripts/install.opt -console

samp 밑에있는 install.opt 복사해서 상위로가져간다음 
cp install.opt ..
vi install.opt  installLocation 위치 수정

./setupaix -G licenseAccepted=true -options "install.opt" -silent

product bean brokerFeature does not exist
License not accepted


log
user.debug      /var/mqsi/wmblog/wmb.log    rotate size 10240k files 10

### MB 솔라리스 설치

gunzip WMB_v6.1_SUN_SPARC.tar.gz
tar -xvf WMB_v6.1_SUN_SPARC.tar

cd /var/mqm/setup/mb/messagebroker_runtime1
vi /var/mqm/setup/mb/messagebroker_runtime1/sample-scripts/install.opt
 -P installLocation=/opt/IBM/mqsi/6.1  요기 주석해제하고 설치 디렉토리 수정

./setupsolaris -options sample-scripts/install.opt -console

-G licenseAccepted=true

SILENT로 하면
아마 로그파일로 써질거구
console로 하면
그게 설치과정이 터미널에 보일껄요

-silent로 하고
루트 /에 보면
mqsi~~~.log
하고 설치 로그 쓰이니까
그거 tail로 걸어서 봐두 되요


### 패스워드 변경시 영향도


1. MQ
   - 계정 : mqm
   - 영향도 : 쉘스크립트 관련 패스워드 등록시 영향(Shell Script 검토 필요)
2. MB
   - 계정 : mqsi
   - 영향도 : db2 인스턴스 업데이트 및 브로커패스워드 변경 필요, 쉘스크립트 관련 패스워드 등록시 영향
   - 작업 절차
     2.1 mqsi passwd 변경 후
     2.2 mqsistop, db2stop (기동절차에 따른 스크립트를 통한 mb 서비스 정지) 
     2.3 db2iupdt mqsi(인스턴스 계정)  (root 계정으로 실행)
     2.4 db2start
     2.5 mqsichangebroker BrokerName -i 계정(mqsi) -a 변경된 비밀번호 -p 변경된 비밀번호
           (-a 비밀번호, -p 비밀번호 똑같은 걸로 바꾼다)
     2.6 mqsistart 브로커명


3. MTE Client 
   - 계정 : mtesysadm
   - 영향도 : 없음(로그인시만 필요)
     DB Table (MTE_USER_INFO 테이블) 에서 mtesysadm 관련 Passwd 컬럼 update 후 commit

4. Adapter
   - 계정 : mqm
   - 영향도 : 없음 (패스워드 없음)
                  단, DB 계정 패스워드 변경이 된 경우 xml에서 파일 수정 필요(database tag의 password="XXXXXX" 부분 )


### TCPIP WMB 명령어 
mqsicreateconfigurableservice IBMBK -c TCPIPClient -o TCPIPClient -n Hostname,Port -v 10.10.10.37,7001

mqsireportproperties IBMBK -c TCPIPClient -o AllReportableEntityNames -r
mqsireportproperties IBMBK -c TCPIPServer -o AllReportableEntityNames -r
mqsichangeproperties IBMBK -c TCPIPClient -o TCPIPClient -n MinimumConnections -v 4


### windows 2008 툴킷실행

"C:\Program Files (x86)\IBM\MBToolkit\6.0\eclipse\eclipse.exe" -product com.ibm.etools.msgbroker.tooling.ide



### 메세지셋 반복 설정

데이터 왔을 때 설정한 부분이 안 올 수도 있으면  0 쓰고 꼭 들어오면 1쓰고 반복해서 들어올수도 있으면 -1로 써요
보통fixed는 들어온다고 보고 1 1로 설정하고
가변부분이 있으면 0 -1로 설정해요



ㅋㅋ 예를 들어
복권 주문 전문이면
100원짜리 두개 요청하면
헤더필수로 들어오고
그뒤에 각 복권에 대한 내용이 반복으로 2건 들어오는 경우가 생길수잇ㄱ어요

출석부 요청하면 헤더 달리고 뒤에 학생 100명것이 각각 100개 붙어올수더 있고
그럴 때 설정해요
네 근데 반복 부분이 적어도 한건은 온다 치면
1 -1


반복되는 부분만 유형으로 묶으면 돼요
1유형 아래에 날짜. 시간
2유형 아래에 이름.주소

메시지 아래에 
1유형(1,1)
2유형(0,-1)
이렇게 설정하면
날짜시간만 오는 메시지도 처리되고
날짜시간이름주소이름주소이름주소
이렇게 와도 처리돼요


보통 반복되는 애가 있으면 헤더쪽에  datacnt 이런 필드가 있어서 몇갠지 알려주게 설계해요


### 브로커 6.1 툴킷 접속 도메인 설정

도메인 설정
mqsicreateaclentry CONF -u Jaewon -a -x F -p
mqsicreateaclentry CONF -u Administrator -a -x F -p
mqsicreateaclentry CONF -u Min -a -x F -p

도메인 리스트 확인
mqsilistaclentry CONF

도메인 제거
mqsideleteaclentry CONF -u Jaewon -a -p

### 브로커 CAST MRM
MRM 으로 하면 데이터가 깨져도 들어간다

근데 CAST로 하면 에러 이럴떈 asbitstream 으로 하자 


그리고 뭘로 오든 다 받을수있는 1200  UTF-16   UCS-2
이걸로 가지고 온담에 properties 보고 지정한 코드셋으로 바뀌는거다

### 브로커 로그 refresh

4.Restart the syslog daemon. 

On AIX®, enter the command: refresh -s syslogd
On HP-UX and Solaris, enter the command: kill -HUP 'cat /etc/syslog.pid' (거의 HP)
On Linux, enter the command:/etc/init.d/syslogd restart
   or /etc/rc.d/init.d/syslogd restart for systems where rc.d is not a soft link 


For other syslog options, see the documentation for your operating system.


svcadm refresh system-log


2.Restart the Syslog daemon.

On Solaris 5.8 and 5.9, at the command prompt, enter /etc/init.d/syslog stop, followed by /etc/init.d/syslog start.
On Solaris 5.10, at the command prompt, enter svcadm refresh svc:/system/system-log.
On Solaris 5.10, at the command prompt, enter svcadm disable svc:/system/system-log && svcadm enable svc:/system/system-log.


AIX

lssrc -a  상태 확인

active 면 refresh -s syslogd
안되있으면 startsrc -s syslogd

### 브로커 트레이스 명령어
mqsichangetrace BK01 -t -e TCPIP_TEST -l debug -c 10000


mqsichangetrace BK01-t -e TCPIP_TEST -l none
mqsireadlog BK01 -t -e TCPIP_TEST -f -o TCPIP_TEST1.xml
mqsiformatlog -i TCPIP_TEST.xml -o TCPIP_TEST1.txt

mqsichangetrace HANAMBK -t -e HANAM -l debug -c 1000000

mqsichangetrace HANAMBK -t -e HANAM -l none
mqsireadlog HANAMBK -t -e HANAM -f -o HANAM_CLOSEWAIT.xml
mqsiformatlog -i HANAM_CLOSEWAIT.xml -o HANAM_CLOSEWAIT.txt


### 브로커 상태 확인
mqsilist IBMBK



### 브로커 실행그룹 자원 확인
mqsilist IBMBK -e SOAP_P



### 브로커 DSN 생성 정보 확인
파일을 통해 확인
계정: /var/mqsi/registry/IBMBK/CurrentVersion/DSN/TESTDB/UserID
암호: /var/mqsi/registry/IBMBK/CurrentVersion/DSN/TESTDB/Password


### 브로커 툴킷 언어 변경
 mb.ini 파일 변경
C:\IBM\WMBT700\mb.ini

eclipse
-showlocation
-product com.ibm.etools.msgbroker.tooling.ide
-nl en_US     //내용 추가, 툴킷 재실행


### 브로커 실행그룹 시작, 정지 명령
시작 명령: mqsistartmsgflow IBMBK -e TEST
정지 명령: mqsistopmsgflow IBMBK -e TEST -f normal


### 브로커 메시지 플로우 시작, 정지 명령
시작 명령: mqsistartmsgflow IBMBK -e TEST -m TEST_MFLOW
정지 명령: mqsistopmsgflow IBMBK -e TEST -m TEST_MFLOW -f normal

ACE12
시작 명령: mqsistartmsgflow MINBK -e MIN_EG -k MIN_EG_DefaultApplication -m TEST
정지 명령: mqsistopmsgflow MINBK -e MIN_EG -k MIN_EG_DefaultApplication -m TEST


### 브로커 자원배포 명령어
명령어로 bar 생성 - mqsicreatebar 명령어로 가능
  mqsicreatebar -data [Workspace위치] -b [bar파일] -o [소스파일 위치]
예제: mqsicreatebar -data C:\Workspace -b mySet.bar -o TestFlowProject\TestFlow\Test.msgflow TestSetProject\TestSet\messageSet.mset
명령어로 deploy - mqsideploy 명령어로 가능
  mqsideploy -n [brokerfile] -e [실행그룹] -a [bar파일] -w 600
예제: mqsideploy -n b1.broker -e default -a mybar.bar -w 600


### 브로커 설정 값 확인 명령어
모든 설정 값 확인: mqsireportproperties IBMBK -c AllTypes -o AllReportableEntityNames -r


### 브로커 Operation Mode 변경
고객 사이트에서 구매한 제품 라인세스에 맞게 Operation Mode를 변경할 수 있습니다.

Operation Mode 변경 시에는 브로커가 정지상태여야 함
mqsimode BROKER1 –o advanced
mqsimode BROKER1 –o standard
mqsimode BROKER1 –o express



### 실행그룹 JVM 사이즈 조정 명령어
mqsichangeproperties MB7BROKER -e DYP -o ComIbmJVMManager -n jvmMinHeapSize -v 4194304
mqsichangeproperties MB7BROKER -e DYP -o ComIbmJVMManager -n jvmMaxHeapSize -v 33554432


### biphttplistener 프로세스 기동 안되게 

~~ 통합 노드 리스너 사용 하지 않는 방법
mqsichangeproperties EAIBK01 -o HTTPListener -b httplistener -n startListener -v false
하고 재시작 

mqsichangeproperties EAIBK01 -o HTTPListener -b httplistener -n startListener -v true

--브로커가 시작될 때 리스너가 시작되는 것을 원하지 않는 사용자의 경우 환경 변수 "MQSI_DONT_RUN_LISTENER=y"를 사용
확정 아님

통합노드 리스너 -> 임베디드 리스너 사용으로 변경
mqsichangeproperties CJBK -e TCP -o ExecutionGroup -n httpNodesUseEmbeddedListener -v true

mqsireportproperties CJBK -e TCP -o ExecutionGroup -r
mqsichangeproperties CJBK -e TCP -o HTTPConnector -n explicitlySetPortNumber -v 7800   


### 브로커 툴킷 접속 포트 변경 / 확인 명령어 정리


mqsichangeproperties IIBBK10 -b webadmin -o HTTPConnector -n port -v 4416
find ./ -name "*" |xargs grep "4416"
./IIBBK10/CurrentVersion/WebAdmin/HTTPConnector/port:4416

/var/mqsi/registry/IIBBK10/CurrentVersion/WebAdmin/HTTPConnector/port

~~파일 위치에서도 확인 가능

/var/mqsi/registry/EAIBK01/CurrentVersion/WebAdmin/HTTPConnector/port       4414
/var/mqsi/registry/EAIBK01/CurrentVersion/HTTPListener/HTTPConnector/port   7080



mqsireportproperties EAIBK01 -e MINTEST -o AllReportableEntityNames -r  
mqsireportproperties EAIBK01 -b httplistener -o AllReportableEntityNames -r
mqsireportproperties EAIBK01 -b webadmin -o AllReportableEntityNames -r

-o 의 전체는 AllReportableEntityNames
-c 의 전체는 AllTypes
-c 는 구성가능서비스라서 잘 안봄
-o 는 오브젝트들을 봐서 자주 봄 (HTTPConnector / ExecutionGroup / HTTPListener)
-o 는 전체를 보고 -b 나 -e 변경하면서 보는게 좋음



mqsireportproperties EAIBK01 -o HTTPConnector -b webadmin -r
HTTPConnector
  uuid='HTTPConnector'
  address=''
  port='4414'
  maxPostSize=''
  acceptCount=''
  compressableMimeTypes='text/html,text/css,application/javascript,image/gif,image/png,application/json'
  compression='on'
  connectionLinger=''
  connectionTimeout=''
  maxHttpHeaderSize=''
  maxKeepAliveRequests=''
  maxThreads=''
  minSpareThreads=''
  noCompressionUserAgents=''
  restrictedUserAgents=''
  socketBuffer=''
  tcpNoDelay=''
  enableLookups='false'
  serverName=''
  accessLog=''
  accessLogPattern=''



mqsireportproperties EAIBK01 -e MINTEST -o HTTPConnector -a  
mqsireportproperties EAIBK01 -o HTTPListener -b httplistener -r
mqsireportproperties EAIBK01 -o HTTPConnector -b httplistener -r
HTTPConnector
  uuid='HTTPConnector'
  address=''
  port='7080'
  maxPostSize=''
  acceptCount=''
  compressableMimeTypes=''
  compression=''
  connectionLinger=''
  connectionTimeout=''
  maxHttpHeaderSize=''
  maxKeepAliveRequests='0'
  maxThreads=''
  minSpareThreads=''
  noCompressionUserAgents=''
  restrictedUserAgents=''
  socketBuffer=''
  tcpNoDelay='true'
  enableLookups='false'
  serverName=''
  accessLog=''
  accessLogPattern=''
  corsEnabled='false'
  corsAllowOrigins='*'
  corsAllowCredentials='false'
  corsExposeHeaders='Content-Type'
  corsMaxAge='-1'
  corsAllowMethods='GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS'
  corsAllowHeaders='Accept,Accept-Language,Content-Language,Content-Type'
  disableConnector=''
  disableHttpMethods=''
  relaxedPathChars=''
  relaxedQueryChars=''  


ACE 11버전 이상 
/var/mqsi/components/UPBK/overrides/node.conf.yaml 파일을 통해 확인 가능

RestAdminListener:
  authorizationEnabled: false
  authorizationMode: 'mq'
  port: 4415

  
  
### bipMQTT 프로세스 기동 시키지 않는 명령어 

mqsichangeproperties nodename -b pubsub -o MQTTServer -n enabled -v false



### ACE11 실행그룹 디버그 포트 설정
/var/mqsi/components/ACEV11NODE/servers/실행그룹명/server.conf.yaml 파일 수정
#jvmDebugPort: 0
주석 풀고 원하는 포트 번호 사용 (실행그룹 재기동 필요할듯 ?)



### webadmin 포트 비활성화


mqsichangeproperties EAIBK01 -b webadmin -o server -n enabled -v true
mqsichangeproperties EAIBK01 -b webadmin -o server -n enabled -v false

mqsireportproperties EAIBK01 -b webadmin -o server -a

server=''
  uuid='server'
  enabled='true'
  ldapAuthenticationUri=''
  ldapYamlPath=''
  sessionMaxInactiveAgeSecs=''
  enableSSL=''

false로 놓으면 접속 불가
추가로 mqsilist 확인시  아래 내용 출력 안됨

 
and administration URI 'http://hcm-173:4414'



### ACE에서 HTTPS 연결
실행그룹 - https listener 포트
변경
mqsichangeproperties BK1 -e EG01 \
-o HTTPSConnector -n explicitlySetPortNumber -v 7788
확인
mqsireportproperties BK1 -e EG01 -o HTTPSConnector -r

실행그룹 - http/https listener 를 사용하도록 설정
변경
mqsichangeproperties BK1 -f -e EG01 -o ExecutionGroup \
-n httpNodesUseEmbeddedListener -v true
확인
mqsireportproperties BK1 -f -e EG01 -o ExecutionGroup \
-n httpNodesUseEmbeddedListener

실행그룹 - https 속성 설정
확인
mqsireportproperties BK1 -e EG01 -o HTTPSConnector -r

상호 인증 사용 : 기본 값 ReqClientAuth='false’
-  클라이언트에서도 별도의 인증서를 사용해서 인증하는 기능.
- 그대로 false 로 놔둔다.

프로토콜 : 기본 값 : TLSProtocols='' : none 값. 사용불가.
- 변경 : TLSv1.2 / TLSv1.3 / all (대소문자 구분X) 셋 중 하나로 변경한다.
mqsichangeproperties BK1 -e EG01 \
-o HTTPSConnector -n TLSProtocols -v all


ACE 와 인증서에 대하여
https 서버
- (툴킷) http input 노드 ⇒ 속성 Basic ⇒ Use HTTPS 체크박스 클릭
- (ACE runtime) ‘키저장소’에 인증서가 있어야 한다.
https 클라이언트
- (툴킷) http request 노드 ⇒ 속성 Basic ⇒ Web service URL ⇒ 주소를 넣을 때  ‘https://~’ 로 시작하게 입력한다.
- (툴킷) 또는 호출 전 esql 에서 
  ‘SET OutputLocalEnvironment.Destination.HTTP.RequestURL =’ 로 지정한다.
- (ACE runtime) ‘신뢰저장소’에 인증서가 있어야 한다.

실행그룹의 ‘키저장소’ 설정
mqsichangeproperties BK1 -e EG01 -o ComIbmJVMManager \
-n keystoreFile -v '/var/mqsi/keystore/eai_keystore.jks'
mqsichangeproperties BK1 -e EG01 -o ComIbmJVMManager \
-n keystorePass -v brokerKeystore::password
mqsichangeproperties BK1 -e EG01 -o ComIbmJVMManager \
-n keystoreType -v 'jks'
mqsisetdbparms BK1 -n brokerKeystore::password \
-u temp -p mocomsys1!

실행그룹의 ‘신뢰저장소’ 설정
mqsichangeproperties BK1 -e EG01 -o ComIbmJVMManager \
-n truststoreFile \
-v '/var/mqsi/truststore/eai_truststore.jks'
mqsichangeproperties BK1 -e EG01 -o ComIbmJVMManager \
-n truststorePass -v brokerTruststore::password
mqsichangeproperties BK1 -e EG01 -o ComIbmJVMManager \
-n truststoreType -v 'jks'
mqsisetdbparms BK1 -n brokerTruststore::password \
-u temp -p mocomsys1!

확인
mqsireportproperties BK1 -e EG01 -o ComIbmJVMManager -r

ACE 설정은 완료되었고, 인증서 및 저장소는 아래 3가지 방법 중 1가지로 설정할 수 있다.

인증서 설정하는 방법
1. 자체 서명 인증서를 사용하는 방법
**서버**
mkdir /var/mqsi/keystore
cd /var/mqsi/keystore
- 키저장소 생성
runmqckm -keydb -create -db eai_keystore.jks \
-pw mocomsys1! -type jks

- 자체 서명 인증서 생성
runmqckm -cert -create -db eai_keystore.jks \
-label eai_cert \
-dn "CN=BK1.EG01,O=IBM,OU=ISSW,L=Hursley,C=GB" \
-pw mocomsys1! -expire 7300
- 생성된 인증서 내용 보기
runmqckm -cert -details -db eai_keystore.jks \
-pw mocomsys1! -label eai_cert
- 인증서 추출
runmqckm -cert -extract -db eai_keystore.jks \
-pw mocomsys1! -label eai_cert \
-target eai_cert.arm -format ascii
- 키 저장소의 모든 인증서 확인
runmqckm -cert -list -db eai_keystore.jks -pw mocomsys1!

**클라이언트**
mkdir /var/mqsi/truststore
cd /var/mqsi/truststore
cp /var/mqsi/keystore/eai_cert.arm .
runmqckm -keydb -create -db eai_truststore.jks \
-pw mocomsys1! -type jks
runmqckm -cert -add -db eai_truststore.jks \
-label EAICert -file eai_cert.arm -format ascii

2. 공인인증서의 pfx 파일을 저장소에 보관하고 사용하는 방법
**서버**
cd /var/mqsi/keystore
- 기존 .jks 파일 지워도 됨.
- 공인인증서에서 .pfx 파일을 여기에 복사해넣는다.
- 해당 파일의 비밀번호도 미리 알고 있어야 한다.
runmqckm -keydb -create -db eai_keystore.jks \
-pw mocomsys1! -type jks

runmqckm -cert -import -db _wildcard_MOCOMSYS_COM.pfx \
-type pkcs12 -target eai_keystore.jks \
-target_pw mocomsys1!
- 확인
runmqckm -cert -list -db eai_keystore.jks -pw mocomsys1!

**클라이언트**
cd /var/mqsi/truststore
- 기존 .jks 파일 지워도 됨.
- 공인인증서에서 .pfx 파일을 여기에 복사해넣는다.
- 해당 파일의 비밀번호도 미리 알고 있어야 한다.
runmqckm -keydb -create -db eai_truststore.jks \
-pw mocomsys1! -type jks
runmqckm -cert -import -db _wildcard_MOCOMSYS_COM.pfx \
-type pkcs12 -target eai_truststore.jks \
-target_pw mocomsys1!
- 확인
runmqckm -cert -list -db eai_truststore.jks -pw mocomsys1!

3. 공인인증서 및 체인인증서를 p12 파일로 만들고 저장소에
보관하여 사용하는 방법

**서버**
- p12 파일 만들기.
export LD_LIBRARY_PATH=/usr/local/lib64:$LD_LIBRARY_PATH
- 인증서 및 체인인증서 순서는 인증서를 다 열어보고 알아내야 한다.
cat _wildcard_MOCOMSYS_COM.crt \
ChainCA/SectigoRSADomainValidationSecureServerCA.crt \
ChainCA/SectigoRSAAddTrustCA.crt \
ChainCA/AAACertificateServices.crt > mocomsys.crt.pem
openssl pkcs12 -in mocomsys.crt.pem -export \
-inkey _wildcard_MOCOMSYS_COM_SHA256WITHRSA.key \
-out mocomsys.p12
cd /var/mqsi/keystore
- 기존 .jks 파일 지워도 됨.
- 생성한 .p12 파일을 여기에 복사해넣는다.
- 해당 파일의 비밀번호도 미리 알고 있어야 한다.
runmqckm -keydb -create -db eai_keystore.jks \
-pw mocomsys1! -type jks
runmqckm -cert -import -db mocomsys.p12 -type pkcs12 \
-target eai_keystore.jks -target_pw mocomsys1!
- 확인
runmqckm -cert -list -db eai_keystore.jks -pw mocomsys1!

**클라이언트**
cd /var/mqsi/truststore
- 기존 .jks 파일 지워도 됨.
- 생성한 .p12 파일을 여기에 복사해넣는다.
- 해당 파일의 비밀번호도 미리 알고 있어야 한다.
runmqckm -keydb -create -db eai_truststore.jks \
-pw mocomsys1! -type jks
runmqckm -cert -import -db mocomsys.p12 -type pkcs12 \
-target eai_truststore.jks -target_pw mocomsys1!
- 확인
runmqckm -cert -list -db eai_truststore.jks -pw mocomsys1!


### IIB https 설정 (아시아나 ESB(AS-IS))

- **인증서 관리**

Java Security 설정 파일 변경

 파일 : /opt/IBM/iib-10.0.0.13/common/jre/lib/security/java.security

 백업 파일 위치  : Z:\2.시스템 구축\3.D(설계)\설치가이드\JAVA SECURITY\

Integration Node에서 사용하는 CA저장소 (인증서 저장소) 

 파일 : /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts

Integration Node 에 CA 저장소 등록 : CA 저장소를 Integration Node에 등록 후에는 필히 재기동 한다.

 1.  CA 저장소 패스워드 등록

예 ) mqsisetdbparms <Integration Node 명> -n  <식별자 명> -u <사용자아이디> -p <패스워드>

mqsisetdbparms ESB1DBK -n brokerTruststore::password -u ignore -p changeit

mqsisetdbparms ESB1DBK -n brokerKeystore::password -u ignore -p changeit

2. Integration Node의 Registry 에 등록

예) mqsichangeproperties <Integraion Node명> -o BrokerRegistry -n <속성1,속성2..> -v”<속성1값,속정2값>”

mqsichangeproperties ESB1DBK -o BrokerRegistry -n brokerTruststoreFile,brokerTruststorePass,brokerKeystoreFile,brokerKeystorePass,brokerKeystoreType,brokerKeystoreType -v "/opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts,brokerTruststore::password,/opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts,brokerTruststore::password,JKS,JKS“

3. Integration Node 에 등록한 Registry 확인

에) mqsireportproperties <Integration Node명>  -o BrokerRegistry -r

mqsireportproperties ESB1DBK -o BrokerRegistry -r

- **인증서 관리 ( keytool 유틸리티로 인증서 추가,삭제,변경)**
JDK로 제공 되는 keytool 유틸리티를 이용해 인증서 추가, 삭제, 변경을 한다.

동일한 alias 명으로 인증서를 변경 하려면, 삭제 후 재 등록 한다.

아시아나 항공 ESB는 대외계 롤스로이스(DACS), 에어버스(FHS), 루프트한자(TCS) 연결을 위해 인증서를 사용한다.

0. CA인증서가 있는 디렉토리로 이동

예 ) cd /opt/IBM/iib-10.0.0.13/common/jre/lib/security

1.인증서 유무 확인

예) keytool -list -v -keystore <CA저장소 파일명>  -alias <인증서alias 명 >   -storepass <패스워드> 

keytool -list -v -keystore cacerts -alias tcs_dev  -storepass changeit

èkeytool error: java.lang.Exception: Alias <tcs_dev> does not exist ( tcs_dev 명으로 등록된 인증서가 없는 경우 )

èkeytool -list -v -keystore cacerts -alias tcs  -storepass changeit      ( tcs 명으로 등록된 인증서가 있는 경우 )

  Alias name: tcs

  Creation date: Dec 15, 2018

  Entry type: trustedCertEntry

  …..

2. 인증서 등록

예 ) keytool -importcert -keystore <CA저장소 파일명>  -trustcacerts -alias <인증서 alias 명>  -file <인증서파일명 >  -storepass <패스워드>

keytool -importcert -keystore cacerts -trustcacerts -alias aex -file /esbapp/cert/AEX_PROD_PUB_05072018.cer  -storepass changeit

3 인증서 삭제

예) keytool -delete -keystore <CA인증서파일명> -alias <인증서 alias명>  -storepass <패스워드>

keytool -delete -keystore /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts -alias aex  -storepass changeit

- **인증서  Verify**
인증서 등록 후, 연결 테스트를 위해 java클래스 작성 후, 운영/개발에 배포.

디렉토리 이동 :

cd /user/mqm/verify_https

연결 테스트 :

 java -cp . HttpsClient [https://aeroxchangeb2bserver.com:9003](https://aeroxchangeb2bserver.com:9003/)

============ ==========결과 =========================

Response Code : 401

Cipher Suite : SSL_ECDHE_RSA_WITH_AES_256_CBC_SHA384

Cert Type : X.509

Cert Hash Code : 27180433

Cert Public Key Algorithm : RSA

Cert Public Key Format : X.509

============ ==========결과 =========================

위와 같이 display 되면 정상적으로 인증서 등록 완료


- 아시아나 ESB 운영#1 설정


-- CA 저장소 등록

mqsireportproperties ESB1PBK -o BrokerRegistry -r
mqsisetdbparms ESB1PBK -n brokerTruststore::password -u ignore -p changeit
mqsisetdbparms ESB1PBK -n brokerKeystore::password -u ignore -p changeit

mqsichangeproperties ESB1PBK -o BrokerRegistry -n brokerTruststoreFile,brokerTruststorePass,brokerKeystoreFile,brokerKeystorePass,brokerKeystoreType,brokerKeystoreType -v /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts,brokerTruststore::password,/opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts,brokerTruststore::password,JKS,JKS"


-- 인증서 backup

cp /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts.20181217 

-- 인증서 import

-- 운영용 인증서는 /esbapp/install/cert 디렉토리에 보관

-- 백업 :z:\2.시스템 구축\5.T(통합)\5.3 이행계획\이행관련파일\인증서

keytool -importcert -keystore cacerts -trustcacerts -alias aex_verisignroot  -file /esbapp/cert/DigiCert_RootCA.cer    -storepass changeit

keytool -importcert -keystore cacerts -trustcacerts -alias aex_sematic -file /esbapp/cert/DigiCert_IntermediateCA.cer  -storepass changeit    

keytool -importcert -keystore cacerts -trustcacerts -alias aex -file /esbapp/cert/AEX_PROD_PUB_05072018.cer  -storepass changeit

keytool -importcert -keystore cacerts -trustcacerts -alias dacs_trusted -file /esbapp/cert/production_dacs_trusted.cer  -storepass changeit

keytool -importcert -keystore cacerts -trustcacerts -alias dacs_addtrust -file /esbapp/cert/production_dacs_addtrust.cer  -storepass changeit

keytool -importcert -keystore cacerts -trustcacerts -alias dacs_usertrust -file /esbapp/cert/production_dacs_usertrust.cer  -storepass changeit

keytool -importcert -keystore cacerts -trustcacerts -alias dacs -file /esbapp/cert/production_dacs.cer  -storepass changeit

keytool -importcert -keystore cacerts -trustcacerts -alias tcs_dev -file /esbapp/cert/tcs_dev_prod.cer  -storepass changeit


-- 인증서 확인

keytool -list -v -keystore cacerts -alias tcs_dev  -storepass changeit
keytool -list -v -keystore cacerts -alias aex  -storepass changeit
keytool -list -v -keystore cacerts -alias dacs  -storepass changeit



-- 인증서 delete

keytool -delete -keystore /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts -alias aex  -storepass changeit

keytool -delete -keystore /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts -alias aex_sematic  -storepass changeit

keytool -delete -keystore /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts -alias aex_verisignroot  -storepass changeit

keytool -delete -keystore /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts -alias dacs_trusted  -storepass changeit

keytool -delete -keystore /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts -alias dacs_addtrust  -storepass changeit

keytool -delete -keystore /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts -alias dacs_usertrust  -storepass changeit

keytool -delete -keystore /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts -alias dacs  -storepass changeit

keytool -delete -keystore /opt/IBM/iib-10.0.0.13/common/jre/lib/security/cacerts -alias tcs_dev  -storepass changeit

